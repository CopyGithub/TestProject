package com.cc.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HttpRequester {
    public class ResponseResult {
        public int statusCode = 0;
        public String responseMessage = "";
        public Map<String, List<String>> headers;
        public long responseLength = 0;
        public Object responseBody;
    }

    public ResponseResult sendGetRequest(String url, int responseType) {
        ResponseResult responseResult = new ResponseResult();
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.connect();
            setReponseResult(responseResult, urlConnection, responseType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseResult;
    }

    public ResponseResult sendPostRequest(String url, byte[] body, int responseType) {
        ResponseResult responseResult = new ResponseResult();
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.getOutputStream().write(body);
            setReponseResult(responseResult, urlConnection, responseType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseResult;
    }

    private void setReponseResult(ResponseResult responseResult, HttpURLConnection urlConnection,
            int responseType) throws IOException {
        responseResult.statusCode = urlConnection.getResponseCode();
        responseResult.responseMessage = urlConnection.getResponseMessage();
        responseResult.headers = urlConnection.getHeaderFields();
        responseResult.responseLength = urlConnection.getContentLengthLong();
        InputStream inputStream = null;
        if (responseResult.statusCode == HttpURLConnection.HTTP_OK) {
            inputStream = urlConnection.getInputStream();
        } else {
            inputStream = urlConnection.getErrorStream();
        }
        switch (responseType) {
        case 1:
            responseResult.responseBody = inputStream2String(inputStream);
            break;

        default:
            responseResult.responseBody = inputStream;
            break;
        }
    }

    private String inputStream2String(InputStream inputStream) throws IOException {
        String text = "";
        String line = "";
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = bufferedReader.readLine()) != null) {
            text += line;
        }
        return text;
    }
}
