package com.cc.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class HttpRequester {
    private String mMethod = "GET";
    private String mUrl = "";
    private Map<String, String> mHeaders = new HashMap<>();
    private Map<String, String> mParams = new HashMap<>();
    private byte[] body = null;
    private int mResponseType = 0;

    public void setMethod(String method) {
        this.mMethod = method.toUpperCase();
    }

    private String getUrl() throws UnsupportedEncodingException {
        String fullUrl = mUrl;
        if (!mParams.isEmpty()) {
            fullUrl += "?";
            Set<Entry<String, String>> entries = mParams.entrySet();
            Iterator<Entry<String, String>> lt = entries.iterator();
            while (lt.hasNext()) {
                fullUrl += URLEncoder.encode(lt.next().getKey(), "utf-8") + "=";
                fullUrl += URLEncoder.encode(lt.next().getValue(), "utf-8");
                if (lt.hasNext()) {
                    fullUrl += "&";
                }
            }
        }
        return fullUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public void addHeaders(String key, String value) {
        mHeaders.put(key, value);
    }

    public void addParams(String key, String value) {
        mParams.put(key, value);
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setResponseType(int responseType) {
        mResponseType = responseType;
    }

    public class ResponseResult {
        public int statusCode = 0;
        public String responseMessage = "";
        public Object responseBody;
    }

    public ResponseResult exec(HttpRequester request) throws Exception {
        ResponseResult responseResult = new ResponseResult();
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
            switch (request.mMethod) {
            case "GET":
                urlConnection.connect();
                break;
            case "POST":
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod(request.mMethod);
                urlConnection.getOutputStream().write(request.body);
                break;
            default:
                throw new Exception("请求方法错误");
            }
            setReponseResult(responseResult, urlConnection, request.mResponseType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseResult;
    }

    private void setReponseResult(ResponseResult responseResult, HttpURLConnection urlConnection,
            int responseType) throws IOException {
        responseResult.statusCode = urlConnection.getResponseCode();
        responseResult.responseMessage = urlConnection.getResponseMessage();
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
