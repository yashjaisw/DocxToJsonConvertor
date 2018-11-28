package com.extension.DocToJsonConvertor;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerConfigurationException;
import java.io.IOException;

public interface TikaService {

    public FormResponse getFormResponse(byte[] file) throws TransformerConfigurationException, TikaException, SAXException, IOException;
}
