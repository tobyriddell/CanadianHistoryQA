package com.prevtec.canadianhistoryqa;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by toby on 26/03/2015.
 */
public class WatsonQuery {
    private String response;
    private String question;

//    final HashMap<String, Object> questionObject = null;

//    @SuppressWarnings("serial")
//    HashMap<String, Object> question_info = new HashMap<String, Object>() {{
//        put("questionText", "What is aperture?");
//    }};


    public String getResponse() {
           return this.response;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

//    public void setQuestion(String questionString) {
//        HashMap<String, String> questionInner;
//
//        questionInner = new HashMap<String, Object>() {
//            put("questionText", questionString);
//        };
//
//        questionObject = new HashMap<String, Object>() {
//            put("question", questionInner);
//        };
//
////        this.question = question;
//    }
}
