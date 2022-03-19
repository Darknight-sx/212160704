package test.test1;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sniffer1 {

    public static void main(String[] args) {
        JsonObject object = getDataOut(call());
        wares(object);
        priceListener();
    }
    //获取url里的字符串内容
    public static String call(){
        String url = "https://j1.pupuapi.com/client/product/storeproduct/detail/deef1dd8-65ee-46bc-9e18-8cf1478a67e9/6bc4d43c-33de-47a2-a199-6dc719b02380";
        StringBuilder sb = new StringBuilder();
        try {
            URL url1 = new URL(url);
            URLConnection conn = url1.openConnection();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String input = null;
            while ((input = buffer.readLine())!=null){
                sb.append(input);
            }
            buffer.close();
        }catch (MalformedURLException m){
            m.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static JsonObject getDataOut(Object obj){

        //从字符串中取出数据
        JsonObject object = new JsonParser().parse((String) obj).getAsJsonObject().getAsJsonObject("data");
        return object;
    }

    public static String nowDateTime(){

        //将现在的时间通过LocalDateTime解析为字符串类型并返回
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
        String date1 = LocalDateTime.parse(dateTime.toString()).format(formatter);
        return date1;
    }

    public static String regex(JsonObject data,String name){
        //设置正则表达式
        String result = data.get(name).toString().replaceAll("\"+","");
        return result;
    }

    public static void wares(JsonObject data){

        //通过正则切割得到对应的字符串
        String spec = regex(data, "spec");
        Double price = Double.valueOf(regex(data, "price")) / 100;
        Double market_price = Double.valueOf(regex(data, "market_price")) / 100;
        String title = regex(data, "share_title");
        String content = regex(data, "share_content");
        System.out.println("---------------------商品：" + title + "---------------------");
        System.out.println("规格：" + spec);
        System.out.println("价格：" + price);
        System.out.println("原价/折扣价：" + market_price + "/" + price);
        System.out.println("详细内容：" + content + "\n");
        System.out.println("-------------------------\"" + title + "\"的价格波动-------------------------");
    }

    public static void priceListener(){
        //监控价格的浮动
        while (true){
            String json = call();
            JsonObject data = getDataOut(json);
            Double price = Double.valueOf(regex(data,"price"))/100;
            System.out.println("当前时间是" + nowDateTime() + "，价格为" + price);
            try {
                Thread.sleep(5*1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}
