package edu.ut.softlab.rate;

import com.fasterxml.jackson.databind.util.JSONPObject;
import edu.ut.softlab.rate.dao.ICurrencyDao;
import edu.ut.softlab.rate.model.Currency;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * Created by alex on 16-4-12.
 */


public class Utility {

    public static Utility utility;
    public static Utility getUtility(){
        if(utility == null){
            utility = new Utility();
        }
        return utility;
    }

    public static final String HOST = "smtp.163.com";
    public static final String PROTOCOL = "smtp";
    public static final int PORT = 25;
    public static final String FROM = "quickrateregister@163.com";//发件人的email
    public static final String PWD = "helloworld123";//发件人密码
    private static Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);//设置服务器地址
        props.put("mail.store.protocol" , PROTOCOL);//设置协议
        props.put("mail.smtp.port", PORT);//设置端口
        props.put("mail.smtp.auth" , true);

        Authenticator authenticator = new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PWD);
            }

        };
        return Session.getDefaultInstance(props , authenticator);
    }

    public static void send(String toEmail , String content) {
        Session session = getSession();
        try {
            System.out.println("--send--"+content);
            // Instantiate a message
            Message msg = new MimeMessage(session);

            //Set message attributes
            msg.setFrom(new InternetAddress(FROM));
            InternetAddress[] address = {new InternetAddress(toEmail)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject("账号激活邮件");
            msg.setSentDate(new Date());
            msg.setContent(content , "text/html;charset=utf-8");

            //Send the message
            Transport.send(msg);
        }
        catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }


    public static byte[] encode2bytes(String source) {
        byte[] result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(source.getBytes("UTF-8"));
            result = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 将源字符串使用MD5加密为32位16进制数
     * @param source
     * @return
     */
    public static String encode2hex(String source) {
        byte[] data = encode2bytes(source);

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            String hex = Integer.toHexString(0xff & data[i]);

            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }

    /**
     * 验证字符串是否匹配
     * @param unknown 待验证的字符串
     * @param okHex 使用MD5加密过的16进制字符串
     * @return  匹配返回true，不匹配返回false
     */
    public static boolean validate(String unknown , String okHex) {
        return okHex.equals(encode2hex(unknown));
    }





    public ArrayList<String> postData(Date date, String currencyCode){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://fx.sauder.ubc.ca/cgi/fxdata");
        if(date == null){
            date = new GregorianCalendar(2013,0,1).getTime();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        List<org.apache.http.NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("b","USD"));
        nvps.add(new BasicNameValuePair("c",currencyCode));
        nvps.add(new BasicNameValuePair("rd",""));
        nvps.add(new BasicNameValuePair("fd",String.valueOf(cal.get(Calendar.DATE))));
        nvps.add(new BasicNameValuePair("fm",String.valueOf(cal.get(Calendar.MONTH)+1)));
        nvps.add(new BasicNameValuePair("fy",String.valueOf(cal.get(Calendar.YEAR))));
        cal.setTime(new Date());
        nvps.add(new BasicNameValuePair("ld",String.valueOf(cal.get(Calendar.DATE))));
        nvps.add(new BasicNameValuePair("lm",String.valueOf(cal.get(Calendar.MONTH)+1)));
        nvps.add(new BasicNameValuePair("ly",String.valueOf(cal.get(Calendar.YEAR))));
        nvps.add(new BasicNameValuePair("y","daily"));
        nvps.add(new BasicNameValuePair("q","volume"));
        nvps.add(new BasicNameValuePair("f","plain"));
        nvps.add(new BasicNameValuePair("o",""));
        System.out.println(nvps.toString());
        ArrayList<String> result = new ArrayList<>();
        try{
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = reader.readLine()) != null){
                result.add(line);
            }
            EntityUtils.consume(entity);
            response.close();

        }catch (Exception ex){
            System.out.println(ex.toString());
        }

        return result;
    }

    public Map<String, Double> getRateData(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://finance.yahoo.com/webservice/v1/symbols/allcurrencies/quote?format=json");
        Map<String, Double> result = new HashMap<>();
        try{
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonArray = jsonObject.getJSONObject("list").getJSONArray("resources");
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject fields = ((JSONObject)jsonArray.get(i)).getJSONObject("resource").getJSONObject("fields");
                result.put(fields.getString("name"), fields.getDouble("price"));
            }

            EntityUtils.consume(entity);
            response.close();
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
        return result;
    }

    public void updateRate(){
        ApplicationContext context = new ClassPathXmlApplicationContext("WEB-INF/mvc-dispatcher-servlet.xml", "WEB-INF/spring-hibernate.xml");
        BeanFactory factory = (BeanFactory)context;
//        IRateDao dao = (IRateDao)factory.getBean("rateDao");
//        ArrayList<String> result = postData(null);
//        for(int i=2; i< result.size()-1; i++){
//
//        }
        ICurrencyDao dao = (ICurrencyDao)factory.getBean("currencyDao");
        for(CurrencyCode currencyCode : CurrencyCode.values()){
            Currency currency = factory.getBean(Currency.class);
            currency.setCode(currencyCode.name());
            dao.create(currency);
            System.out.println("hello");
        }
    }

    public enum CurrencyCode {
        USD, JPY, CNY,CAD, EUR, GBP, AUD, HKD, KRW, TWD
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}