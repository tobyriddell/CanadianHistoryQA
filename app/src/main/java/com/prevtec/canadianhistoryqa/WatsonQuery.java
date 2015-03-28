package com.prevtec.canadianhistoryqa;

import java.util.HashMap;

/**
 * Created by toby on 26/03/2015.
 */
public class WatsonQuery {
//    private HashMap<String, HashMap> question;

    private String question;

    // Sample question:
    // {
    //     {
    //         “question” : {
    //             “questionText” : “Who was Sir Sam Steele?”
    //         }
    //     }
    // }

    public void setQuestion(String myQuestion) {
        this.question = "{ { \"question\" : { \"questionText\" : \"Who was Sir Sam Steele?\" } } }";


//        HashMap<String, String> questionGuts = new HashMap<String, String>();
//        questionGuts.put("questionText", myQuestion);
//        this.question = new HashMap<String, HashMap>();
//        this.question.put("question", questionGuts);
    }
}
