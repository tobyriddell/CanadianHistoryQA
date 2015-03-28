package com.prevtec.canadianhistoryqa;

import android.util.Log;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.*;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by toby on 28/03/2015.
 */
public class ProcessWatsonResponse {
    static final String outputEncoding = "UTF-8";

    Document doc = null;

    ProcessWatsonResponse(String response) {
        Log.e("ProcessWatsonResponse", response);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (Exception e) {
            // Do nothing (yet)
        }
//        Document doc = db.parse(response);
        OutputStreamWriter errorWriter = null;
        try {
            errorWriter = new OutputStreamWriter(System.err, "UTF-8");
        } catch (Exception e) {
            // Do nothing (yet)
        }

        db.setErrorHandler(new MyErrorHandler (new PrintWriter(errorWriter, true)));

        try {
//            doc = db.parse(response);
            doc = db.parse(new InputSource(new ByteArrayInputStream(response.getBytes("utf-8"))));
        } catch (Exception e) {
            Log.e("ProcessWatsonResponse", e.toString() + "\n");
        }

        Log.e("ProcessWatsonResponse - doc", doc.toString());
        Log.e("ProcessWatsonResponse", doc.getDocumentElement().toString());
//        doc.getElementsByTagName("evidencelist")
        Log.e("ProcessWatsonResponse - el", doc.getElementsByTagName("evidencelist").toString());
        NodeList nl = doc.getElementsByTagName("evidencelist");
        Log.e("ProcessWatsonResponse - evidencelist node count", Integer.toString(nl.getLength()));

        // Only one <evidencelist> tag
        NodeList nlc = doc.getElementsByTagName("evidencelist").item(0).getChildNodes();

        for (int i = 0; i < nlc.getLength(); i++) {
            Node subnode = nlc.item(i);
            Log.e("ProcessWatsonResponse - nn", subnode.getNodeName());
//            Log.e("ProcessWatsonResponse - tc", subnode.getTextContent());

            if ( subnode.getNodeName().equals("evidence") ) {
                Log.e("ProcessWatsonResponse", "got evidence element... processing further");
                NodeList nlcc = subnode.getChildNodes();
                Log.e("ProcessWatsonResponse", "this evidence element has " + Integer.toString(nlcc.getLength()) + " child nodes");

                for (int j = 0; j < nlcc.getLength(); j++ ) {
//                    Log.e("ProcessWatsonResponse", "child element " + Integer.toString(j) + " has name " + nlcc.item(j).getNodeName());
                    if ( nlcc.item(j).getNodeName().equals("text") ) {
                        Log.e("ProcessWatsonResponse - text", nlcc.item(j).getTextContent());
                    } else if ( nlcc.item(j).getNodeName().equals("value"))  {
                        Log.e("ProcessWatsonResponse - value", nlcc.item(j).getTextContent());
                    }

                }
            }
        }
    }

    String getEvidenceList() {
        return doc.getElementsByTagName("evidencelist").toString();
//        return new String("");
    }

    private static class MyErrorHandler implements ErrorHandler {
        private PrintWriter out;

        MyErrorHandler(PrintWriter out) {
            this.out = out;
        }

        private String getParseExceptionInfo(SAXParseException spe) {
            String systemId = spe.getSystemId();
            if (systemId == null) {
                systemId = "null";
            }

            String info = "URI=" + systemId + " Line=" + spe.getLineNumber() +
                    ": " + spe.getMessage();
            return info;
        }

        public void warning(SAXParseException spe) throws SAXException {
            out.println("Warning: " + getParseExceptionInfo(spe));
        }

        public void error(SAXParseException spe) throws SAXException {
            String message = "Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }

        public void fatalError(SAXParseException spe) throws SAXException {
            String message = "Fatal Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }
    }
}
