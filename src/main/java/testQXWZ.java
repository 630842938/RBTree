import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author llh
 * @Date 2020/5/21 --- 10:07
 */


public class testQXWZ {

    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws Exception {

        long starTime = 0;
        starTime = System.currentTimeMillis();
        JSONObject jsonObject = httpPost("http://47.103.218.170:8080/QXWZ", "threadId=");
        System.out.println("jsonObject："+jsonObject);
        List<JSONObject> data = (List<JSONObject>)((JSONObject)jsonObject.get("data")).get("data");
        System.out.println("data:"+data);
        /*System.out.println("返回的状态码为："+jsonObject.get("code"));
        System.out.println("返回的状态信息："+jsonObject.get("msg"));
        System.out.println("返回的内容为："+jsonObject.get("data"));*/

        long endTime = System.currentTimeMillis();
        Long t = endTime - starTime;
        System.out.println(t / 1000F + "秒");

    }




    static JSONObject httpPost(String url, String strParam) {

        // post请求返回结果
        CloseableHttpClient httpClient = HttpClients.createDefault();
        JSONObject jsonResult = null;
        String s = null;
        HttpPost httpPost = new HttpPost(url);
        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(2000).setConnectTimeout(2000).build();
        httpPost.setConfig(requestConfig);
        try {
            if (null != strParam) {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(strParam, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/x-www-form-urlencoded");
                httpPost.setEntity(entity);
            }
            CloseableHttpResponse response = (CloseableHttpResponse)httpClient.execute(httpPost);
            //请求发送成功，并得到响应
            try {
                HttpEntity entity = response.getEntity();
                //JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(entity));

                if (entity != null) {
                    s = EntityUtils.toString(entity);
                    System.out.println(entity);
                    System.out.println("返回值："+s);

                    //System.out.println("JSON:"+jsonObject);

                    /*
                     * 此处针对返回结果进行不同的处理
                     */
                    EntityUtils.consume(entity);
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {

        } finally {
            httpPost.releaseConnection();
        }

        jsonResult = JSONObject.parseObject(s);
        return jsonResult;
    }
}
