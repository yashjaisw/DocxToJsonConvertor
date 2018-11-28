package com.extension.DocToJsonConvertor;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.ExpandedTitleContentHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class TikaImplementation implements TikaService {

    private static final Logger logger = LoggerFactory.getLogger(ChromeController.class);

    public FormResponse getFormResponse(byte[] file) throws TransformerConfigurationException, TikaException, SAXException, IOException {
        AutoDetectParser tikaParser = new AutoDetectParser();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        TransformerHandler handler;
        handler = factory.newTransformerHandler();
        handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "html");
        handler.getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");
        handler.getTransformer().setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        handler.setResult(new StreamResult(out));
        ExpandedTitleContentHandler handler1 = new ExpandedTitleContentHandler(handler);
        tikaParser.parse(new ByteArrayInputStream(file), handler1, new Metadata());
        String html = new String(out.toByteArray(), "UTF-8");
        Document doc = Jsoup.parse(html);

        String addressLine1,addressLine2,contact,county,palletCount,palletWeight,telephone,postCode,town;
        FormResponse formResponse = new FormResponse();
        try {
            Element table = doc.select("table").get(0);
            Elements rows = table.select("tr");
            Element row = rows.get(2);
            Elements cols = row.select("td");
            Element deliveryDetailsElement = cols.get(1);
            if(deliveryDetailsElement.select("p").size()>0) {
                Element addressLine1Ele = deliveryDetailsElement.select("p").get(0);
                addressLine1 = addressLine1Ele.text();
                formResponse.setAddressLine1(addressLine1);
            }
            if(deliveryDetailsElement.select("p").size()>1) {
                Element addressLine2Ele = deliveryDetailsElement.select("p").get(1);
                addressLine2 = addressLine2Ele.text();
                formResponse.setAddressLine2(addressLine2);
            }
            if(deliveryDetailsElement.select("p").size()>2) {
                Element townEle = deliveryDetailsElement.select("p").get(2);
                town = townEle.text();
                formResponse.setTown(town);
            }
            if(deliveryDetailsElement.select("p").size()>3) {
                Element countyEle = deliveryDetailsElement.select("p").get(3);
                county = countyEle.text();
                formResponse.setCounty(county);
            }
            if(deliveryDetailsElement.select("p").size()>4) {
                Element postCodeEle = deliveryDetailsElement.select("p").get(4);
                postCode = postCodeEle.text();
                formResponse.setPostCode(postCode);
            }
            row = rows.get(3);
            cols = row.select("td");
            Element contactEle = cols.get(1).select("p").get(0);
            contact = contactEle.text();
            if(contact.split(":").length > 1) {
                contact = contact.split(":")[1].replace(" ", "");
                formResponse.setContact(contact);
            }
            else{
                contact = "";
                formResponse.setContact(contact);
            }
            row = rows.get(4);
            cols = row.select("td");
            Element telephoneEle = cols.get(1).select("p").get(0);
            telephone = telephoneEle.text();
            if(telephone.split(":").length > 1) {
                telephone = telephone.split(":")[1].replace(" ", "");
                formResponse.setTelephone(telephone);
            }
            else{
                telephone = "";
                formResponse.setTelephone(telephone);
            }
            row = rows.get(7);
            cols = row.select("td");
            Element palletCountEle = cols.get(1).select("p").get(0);
            palletCount = palletCountEle.text();
            row = rows.get(8);
            cols = row.select("td");
            Element palletWeightEle = cols.get(1).select("p").get(0);
            palletWeight = palletWeightEle.text();

            formResponse.setPalletCount(palletCount);
            formResponse.setPalletWeight(palletWeight);

        }catch (Exception e){
            logger.info("Exception Occurred while parsing DOM" + e);
        }
        return formResponse;
    }
}
