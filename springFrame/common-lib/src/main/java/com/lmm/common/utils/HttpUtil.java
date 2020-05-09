package com.lmm.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {
    private final static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static final MediaType XML = MediaType.parse("application/json; charset=utf-8");

    /****==========OKHTTP========****/
    public static String sendPostWithOkHttpClient(String url, JSONObject jsonObject) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toJSONString());
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("authorization","RjhmBlLBGcBo")
            .build();

        String result = null;
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("服务器端错误: " + response);
            }
            assert response.body() != null;
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.body().close();
                //response.close();
            }
        }
//        if (StringUtils.isEmpty(result)) return null;
//        return JSONObject.parseObject(result);
        return result;
    }

    public static String sendGetWithOkHttp(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("cache-control", "no-cache")
            .build();

        String result = null;
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("服务器端错误: " + response);
            }
            assert response.body() != null;
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.body().close();
            }
        }
        return result;
    }


    /**
     * 发送post请求，根据 Content-Type 返回不同的返回值
     *
     * @param url
     * @param header
     * @param body
     * @return
     */
    public static String doPost2(String url, Map<String, String> header, String body) {
        PrintWriter out = null;
        try {
            // 设置 url
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            // 设置 header
            for (String key : header.keySet()) {
                httpURLConnection.setRequestProperty(key, header.get(key));
            }
            // 设置请求 body
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            out = new PrintWriter(httpURLConnection.getOutputStream());
            // 保存body
            out.print(body);
            // 发送body
            out.flush();
            if (HttpURLConnection.HTTP_OK != httpURLConnection.getResponseCode()) {
                System.out.println("Http 请求失败，状态码：" + httpURLConnection.getResponseCode());
                return null;
            }
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line;
                String result = "";
                while ((line = in.readLine()) != null) {
                    result += line;
                }
                return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 流转二进制数组
     *
     * @param in
     * @return
     * @throws IOException
     */
    private static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    public static byte[] getBytePost(String url, JSONObject jsonObject) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toJSONString());
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Content-Type", "application/json")
            .build();

        byte[] result = null;
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("服务器端错误: " + response);
            }
            assert response.body() != null;
            result = response.body().bytes();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.body().close();
                //response.close();
            }
        }
        return result;
    }

    public static InputStream getInputStreamPost(String url, JSONObject jsonObject) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toJSONString());
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Content-Type", "application/json")
            .build();

        InputStream result = null;
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("服务器端错误: " + response);
            }
            assert response.body() != null;
            result = response.body().byteStream();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.body().close();
                //response.close();
            }
        }
        return result;
    }
}
