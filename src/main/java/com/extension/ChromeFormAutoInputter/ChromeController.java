package com.extension.DocToJsonConvertor;



import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import javax.xml.transform.TransformerConfigurationException;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class ChromeController {


    @Autowired
    private TikaService tikaService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ChromeController.class);

    @RequestMapping(value="/parseDoc", method= RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity uploadFile(
            @RequestParam("file") MultipartFile uploadedInputStream) throws IOException, TikaException, TransformerConfigurationException, SAXException {


    byte[] file = uploadedInputStream.getBytes();
    FormResponse formResponse = getTikaService().getFormResponse(file);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Access-Control-Allow-Origin","*");
        return new ResponseEntity<FormResponse>(formResponse, responseHeaders, HttpStatus.CREATED);
}

    public TikaService getTikaService() {
        return tikaService;
    }
}
