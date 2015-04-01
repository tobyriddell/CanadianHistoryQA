package com.prevtec.canadianhistoryqa;

import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.util.Log;
import android.widget.TextView;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import java.util.Map;

// TODO: Make keyboard appear when focus changes to text entry field, disappear on submit
// TODO: On physical device, keyboard appears but want to submit question check checkbox is
// clicked (currently requires a further click on physical device, or two carriage returns on
// virtual device)

public class MainActivity extends ActionBarActivity {
    ProcessWatsonResponse p = null;

    private class HttpRequestTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                final String url = "https://watson-wdc01.ihost.com/instance/532/deepqa/v1/question";

                HttpAuthentication authHeader = new HttpBasicAuthentication(getResources().getString(R.string.username),
                        getResources().getString(R.string.password));
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setAuthorization(authHeader);
                requestHeaders.setContentType(new MediaType("application", "xml"));
                requestHeaders.set("X-SyncTimeout", "30");

                EditText e = (EditText) findViewById(R.id.editText);
                String watsonQuery = new String("<question>" +
                        "<questionText>\"" +
                        e.getText().toString() +
                        "\"</questionText>" +
                        "</question>");

                Log.e("MainActivity", "Question entered is " + e.getText().toString());

                HttpEntity<String> requestEntity = new HttpEntity<String>(watsonQuery, requestHeaders);
                RestTemplate restTemplate = new RestTemplate();

                // Add the Jackson and String message converters
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                // Make the HTTP POST request, marshalling the request to JSON, and the response to String
                ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                String result = responseEntity.getBody();

                // Parse XML and extract answers
                p = new ProcessWatsonResponse(result);
                return result;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            final String BULLET_SYMBOL = "&#8226";

            String allAnswers = new String("Watson's answers (best answer first):");
            for(Map.Entry<Double, String> entry : p.getAnswers().entrySet()) {
                String thisAnswer = entry.getValue();
                allAnswers = allAnswers + System.getProperty("line.separator") + Html.fromHtml(BULLET_SYMBOL + " " + thisAnswer);
            }

            ((TextView) findViewById(R.id.textView)).setText(allAnswers);
        }
    }

    public boolean onSubmitQuestion(View view) {
        new HttpRequestTask().execute();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
