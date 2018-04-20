package ro.pub.cs.systems.eim.lab07.calculatorwebservice.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import ro.pub.cs.systems.eim.lab07.calculatorwebservice.general.Constants;

public class CalculatorWebServiceAsyncTask extends AsyncTask<String, Void, String> {

    private TextView resultTextView;

    public CalculatorWebServiceAsyncTask(TextView resultTextView) {
        this.resultTextView = resultTextView;
    }

    @Override
    protected String doInBackground(String... params) {
        String operator1 = params[0];
        String operator2 = params[1];
        String operation = params[2];
        int method = Integer.parseInt(params[3]);

        // TODO exercise 4
        // signal missing values through error messages
        if (operator1 == null || operator2 == null || operation == null ||
                operator1.isEmpty() || operator2.isEmpty() || operation.isEmpty()) {
            String error = "Missing fields!";
            return error;
        }

        // create an instance of a HttpClient object
        HttpClient httpClient = new DefaultHttpClient();

        // get method used for sending request from methodsSpinner

        // 1. GET
        // a) build the URL into a HttpGet object (append the operators / operations to the Internet address)
        // b) create an instance of a ResultHandler object
        // c) execute the request, thus generating the result

        String content = null;
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            switch (method) {
                case Constants.GET_OPERATION:
                    HttpGet httpGet = new HttpGet(Constants.GET_WEB_SERVICE_ADDRESS
                            + "?" + Constants.OPERATION_ATTRIBUTE + "=" + operation
                            + "&" + Constants.OPERATOR1_ATTRIBUTE + "=" + operator1
                            + "&" + Constants.OPERATOR2_ATTRIBUTE + "=" + operator2);
                    content = httpClient.execute(httpGet, responseHandler);

                case Constants.POST_OPERATION:
                    List<NameValuePair> postParams = new ArrayList<>();
                    postParams.add(new BasicNameValuePair(Constants.OPERATION_ATTRIBUTE, operation));
                    postParams.add(new BasicNameValuePair(Constants.OPERATOR1_ATTRIBUTE, operator1));
                    postParams.add(new BasicNameValuePair(Constants.OPERATOR2_ATTRIBUTE, operator2));
                    HttpPost httpPost = new HttpPost(Constants.POST_WEB_SERVICE_ADDRESS);
                    try {
                        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(postParams, HTTP.UTF_8);
                        httpPost.setEntity(urlEncodedFormEntity);
                    } catch (UnsupportedEncodingException unsupportedEncodingException) {
                        Log.e(Constants.TAG, unsupportedEncodingException.getMessage());
                        if (Constants.DEBUG) {
                            unsupportedEncodingException.printStackTrace();
                        }
                    }
                    content = httpClient.execute(httpPost, responseHandler);
                default:
                    return content;
            }
        } catch (Exception exception) {
            Log.e(Constants.TAG, exception.getMessage());
            if (Constants.DEBUG) {
                exception.printStackTrace();
            }
            return null;
        }
        // 2. POST
        // a) build the URL into a HttpPost object
        // b) create a list of NameValuePair objects containing the attributes and their values (operators, operation)
        // c) create an instance of a UrlEncodedFormEntity object using the list and UTF-8 encoding and attach it to the post request
        // d) create an instance of a ResultHandler object
        // e) execute the request, thus generating the resultHttpPost httpPost = new HttpPost(Constants.POST_WEB_SERVICE_ADDRESS);


    }

    @Override
    protected void onPostExecute(final String result) {
        // display the result in resultTextView

        resultTextView.setText(result);
    }

}
