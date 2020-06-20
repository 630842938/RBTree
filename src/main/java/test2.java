import com.alibaba.fastjson.JSONObject;

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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author llh
 * @Date 2020/5/21 --- 10:07
 */

/**
 * 模拟高并发场景
 */
public class test2 {

    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws Exception {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(2000, 5000, 2, TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(5000));
        //模拟100人并发请求
        CountDownLatch latch = new CountDownLatch(1);
        //模拟100个用户
        for (int i = 0; i < 150; i++) {

            AnalogUser analogUser = new AnalogUser(latch,i);
            executor.execute(analogUser);

        }
        //计数器減一  所有线程释放 并发访问。
        latch.countDown();

    }


    static class AnalogUser implements Runnable {
        CountDownLatch latch;
        Integer threadId;


        public AnalogUser(CountDownLatch latch,Integer threadId) {
            this.latch = latch;
            this.threadId = threadId;
        }


        @Override
        public void run() {
            long starTime = 0;
            try {
                starTime = System.currentTimeMillis();
                latch.await();
                System.out.println("请求"+threadId+"开始了");
                JSONObject jsonObject = httpPost("http://localhost:8080/shunsheng/BaseDataApi/test2", "threadId="+threadId);
                System.out.println("返回的参数为："+jsonObject);
                /*System.out.println("返回的状态码为："+jsonObject.get("code"));
                System.out.println("返回的状态信息："+jsonObject.get("msg"));
                System.out.println("返回的内容为："+jsonObject.get("data"));*/

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long endTime = System.currentTimeMillis();
            Long t = endTime - starTime;
            System.out.println(t / 1000F + "秒");
        }


    }

    static JSONObject httpPost(String url, String strParam) {

        // post请求返回结果
        CloseableHttpClient httpClient = HttpClients.createDefault();
        JSONObject jsonResult = null;
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
            CloseableHttpResponse result = httpClient.execute(httpPost);
            //请求发送成功，并得到响应
            if (result.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                String str;
                try {
                    //读取服务器返回过来的json字符串数据
                    str = EntityUtils.toString(result.getEntity(), "utf-8");
                    //把json字符串转换成json对象
                    jsonResult = JSONObject.parseObject(str);
                } catch (Exception e) {

                }
            }
        } catch (IOException e) {

        } finally {
            httpPost.releaseConnection();
        }
        return jsonResult;
    }
}
