package com.api.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Administrator on 2017-6-12.
 */
public class HttpHelper {


    public static String post(String url, String postData, String requestType,String method,String cookie) {
        URL uri = null;
        try {
            uri = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) uri.openConnection();
            httpURLConnection.setRequestMethod(method);// 提交模式
            httpURLConnection.setConnectTimeout(5000);//连接超时 单位毫秒
            httpURLConnection.setReadTimeout(5000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            if("body".equals(requestType)){
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
            }
            if(cookie!=null&& !Objects.equals(cookie, "")){
                httpURLConnection.setRequestProperty("Cookie", cookie);
            }
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
            printWriter.write(postData);//post的参数 xx=xx&yy=yy
            // flush输出流的缓冲
            printWriter.flush();
            //开始获取数据
            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            byte[] arr = new byte[1024];
            while ((len = bis.read(arr)) != -1) {
                bos.write(arr, 0, len);
                bos.flush();
            }
            bos.close();
            return bos.toString("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return
     * @Description // httpClient  get方式请求
     * @Date 2018/9/27 15:07
     * @Param url：链接 参数拼接一起
     **/
    public static String sendGet(String url,String cookie) {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        if(cookie!=null&& !Objects.equals(cookie, "")) {
            get.setHeader("Cookie", cookie);
        }
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(get);
            if(response != null && response.getStatusLine().getStatusCode() == 200)
            {
                HttpEntity entity = response.getEntity();
                result = entityToString(entity);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
                if(response != null)
                {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
    private static String entityToString(HttpEntity entity) throws IOException {
        String result = "";
        if(entity != null)
        {
            long lenth = entity.getContentLength();
            if(lenth != -1 && lenth < 2048)
            {
                result = EntityUtils.toString(entity,"UTF-8");
            }else {
                InputStreamReader reader1 = new InputStreamReader(entity.getContent(), "UTF-8");
                CharArrayBuffer buffer = new CharArrayBuffer(2048);
                char[] tmp = new char[1024];
                int l;
                while((l = reader1.read(tmp)) != -1) {
                    buffer.append(tmp, 0, l);
                }
                result = buffer.toString();
            }
        }
        return result;
    }

}

