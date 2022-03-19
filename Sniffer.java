package test.test1;


import com.google.gson.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Sniffer {

    public static void main(String[] args) throws IOException {
        Sniffer.call("https://j1.pupuapi.com/client/product/storeproduct/detail/deef1dd8-65ee-46bc-9e18-8cf1478a67e9/6bc4d43c-33de-47a2-a199-6dc719b02380");
    }

    public static void call(String link)throws IOException {
        try {
            URL url = new URL(link);
            URLConnection urlConn = url.openConnection();
            HttpURLConnection conn = (HttpURLConnection)urlConn;
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36 Edg/99.0.1150.39");
            conn.setRequestProperty("Host","j1.pupuapi.com");
            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));

            StringBuilder sb = new StringBuilder();
            for (int i = 0;(i = in.read())>=0;)sb.append((char) i);
            String html = sb.toString();
            analyse(html);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private static void analyse(String json1) {

        try {
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(json1);
            //System.out.println("code=" + object.get("code").getAsInt());
            JsonArray array = object.get("msg").getAsJsonArray();
            for (int i=0;i<array.size();i++){
                System.out.println("--------------------------------------");
                JsonObject subObject = array.get(i).getAsJsonObject();
                String name = subObject.get("name").getAsString();
                String price = subObject.get("price").getAsString();
                String spec = subObject.get("spec").getAsString();
                String market_price = subObject.get("market_price").getAsString();
                String origin = subObject.get("origin").getAsString();
                String share = subObject.get("share_content").getAsString();
                System.out.println("-------------" + name + "-------------");
                System.out.println("规格:" + spec);
                System.out.println("价格:" + price);
                System.out.println("原价/折扣价:" + market_price + "/" + price);
                System.out.println("产地:" + origin);
                System.out.println("详细信息:" + share);
            }
        }catch (JsonIOException e){
            e.printStackTrace();
        }catch (JsonSyntaxException e){
            e.printStackTrace();
        }
    }

    public static String getDate() {
        LocalDateTime now = LocalDateTime.now();
        //将LocalDateTime依据格式转换成String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm", Locale.CHINESE);
        String date = LocalDateTime.parse(now.toString()).format(formatter);
        return date;
    }

    public static String regex(JsonObject data, String name) {
        String regex = "\"+";
        String result = data.get(name).toString().replaceAll(regex, "");
        return result;
    }




}
