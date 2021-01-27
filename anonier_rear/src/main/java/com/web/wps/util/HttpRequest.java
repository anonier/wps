package com.web.wps.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * java发送http的get和post请求
 */
public class HttpRequest {

    /**
     * 向指定URL发送GET方式的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数
     * @return URL 代表远程资源的响应
     */
    public static String sendGet(String url, String param) {
        String result = "";
        String urlName = url + "?" + param;
        try {
            URL realUrl = new URL(urlName);
            //打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            //设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //建立实际的连接
            conn.connect();
            //获取所有的响应头字段
            Map<String, List<String>> map = conn.getHeaderFields();
            //遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "-->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常" + e);
            e.printStackTrace();
        }
        return result;


    }

    /**
     * 指定URL发送POST请求 MultipartFile文件和字段
     *
     * @param mFile
     * @param url
     * @param userId
     * @return
     */
    public static String httpPostUpload(MultipartFile mFile, String url, String userId) {
        HttpPost post = null;
        String result;
        try {
            HttpClient httpClient = HttpClients.createDefault();
            post = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(StandardCharsets.UTF_8);
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            // 文件流
            builder.addBinaryBody("file", mFile.getInputStream(), ContentType.MULTIPART_FORM_DATA,
                    mFile.getOriginalFilename());
            //字节流
            ContentType contentType = ContentType.create("application/json", StandardCharsets.UTF_8);
            builder.addTextBody("_w_userid", userId, contentType);
            // 构建消息实体
            HttpEntity entity = builder.build();

            // 发送Json格式的数据请求
            post.setEntity(entity);
            HttpResponse response = httpClient.execute(post);
            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = response.getEntity();
                result = entityToString(httpEntity);
            } else if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                HttpEntity httpEntity = response.getEntity();
                result = entityToString(httpEntity);
            } else {
                result = String.valueOf(response.getStatusLine().getStatusCode());
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("post请求，上传文件异常");
        } finally {
            if (post != null) {
                try {
                    post.releaseConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String entityToString(HttpEntity entity) throws IOException {
        String result = null;
        if (entity != null) {
            long length = entity.getContentLength();
            if (length != -1 && length < 2048) {
                result = EntityUtils.toString(entity, "UTF-8");
            } else {
                InputStreamReader reader = new InputStreamReader(entity.getContent(), "UTF-8");
                CharArrayBuffer buffer = new CharArrayBuffer(2048);
                char[] tmp = new char[1024];
                int l;
                while ((l = reader.read(tmp)) != -1) {
                    buffer.append(tmp, 0, l);
                }
                result = buffer.toString();
            }
        }
        return result;
    }

    /**
     * 向指定URL发送POST方式的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数
     * @return URL 代表远程资源的响应
     */
    public static String sendPost(String url, String param) {
        String result = "";
        try {
            URL realUrl = new URL(url);
            //打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            //设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("x-weboffice-file-id",
                    "80");
            //发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //获取URLConnection对象对应的输出流
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            //发送请求参数
            out.print(param);
            //flush输出流的缓冲
            out.flush();
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常" + e);
            e.printStackTrace();
        }
        return result;
    }

    //测试发送GET和POST请求
    public static void main(String[] args) throws Exception {
        //发送GET请求
        String s = HttpRequest.sendGet("http://127.0.0.1:8080/index", null);
        System.out.println(s);
        //发送POST请求
        String s1 = HttpRequest.sendPost("http://localhost:8080/addComment", "questionId=1&content=美丽人生");
        System.out.println(s1);
    }
}