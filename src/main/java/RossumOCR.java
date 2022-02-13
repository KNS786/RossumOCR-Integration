import org.apache.http.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.FileEntity;
import org.json.*;
import java.io.File;

public class RossumOCR {

    public static String USERNAME = "navani@beezlabs.com";
    public static String PASSWORD = "navani.007";
    public static String QUEUE_ID = "171180";
    public static String API_URL = "https://api.elis.rossum.ai";
    public static String FILE_PATH = "/home/navani/Desktop/RossumOcr/Sample Invoice.pdf";
    public static String DOCUMENT_ID="12105341";

    public static String Login(String url, String username, String password){

        DefaultHttpClient httpclient = new DefaultHttpClient();

        try {

            System.out.println("Logging");

            HttpPost postRequest = new HttpPost(url);

            StringEntity params = new StringEntity(String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password));
            postRequest.addHeader("content-type", "application/json");
            postRequest.setEntity(params);

            HttpResponse httpResponse = httpclient.execute(postRequest);
            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {

                JSONObject obj = new JSONObject(EntityUtils.toString(entity));

                String key = obj.getString("key");
                return  key;
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            httpclient.getConnectionManager().shutdown();
        }

        return null;

    }

    public static void upload(String key, String url, String filePath){

        DefaultHttpClient httpclient = new DefaultHttpClient();
        try{

            HttpPost postRequest = new HttpPost(url);

            File file = new File(filePath);

            FileEntity params = new FileEntity(file);
            postRequest.setHeader(HttpHeaders.AUTHORIZATION, "token " + key);
            postRequest.setHeader("Content-Disposition", String.format("attachment; filename=%s", file.getName()));
            postRequest.setEntity(params);

            HttpResponse httpResponse = httpclient.execute(postRequest);
            HttpEntity entity = httpResponse.getEntity();

            String responseString = EntityUtils.toString(entity, "UTF-8");
            System.out.println(responseString);

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            httpclient.getConnectionManager().shutdown();
        }
    }

    public static void invoiceOCR(String key,String url){
        DefaultHttpClient httpclient = new DefaultHttpClient();

        try{
            HttpGet GetRequest=new HttpGet(url);
            GetRequest.setHeader(HttpHeaders.AUTHORIZATION,"token "+ key);

            HttpResponse httpResponse=httpclient.execute(GetRequest);
            HttpEntity entity = httpResponse.getEntity();
            String responseString = EntityUtils.toString(entity,"UTF-8");
            System.out.println(responseString);

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            httpclient.getConnectionManager().shutdown();
        }

    }

    public static void GetDocumentContent(String key,String url){
        DefaultHttpClient httpclient=new DefaultHttpClient();
        try{
            HttpGet GetRequest=new HttpGet(url);
            GetRequest.setHeader(HttpHeaders.AUTHORIZATION,"token "+ key);

            HttpResponse httpResponse=httpclient.execute(GetRequest);
            HttpEntity entity = httpResponse.getEntity();
            String responseString = EntityUtils.toString(entity,"UTF-8");
            System.out.println(responseString);


        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            httpclient.getConnectionManager().shutdown();
        }

    }

    public static void main(String[] args) {

        String key = Login(String.format(API_URL+"%s", "/v1/auth/login"), USERNAME, PASSWORD);
        upload(key, String.format(API_URL+"%s"+"%s"+"%s", "/v1/queues/", QUEUE_ID, "/upload"), FILE_PATH);
        invoiceOCR(key,String.format(API_URL+"%s"+"%s"+"%s","/v1/annotations/",DOCUMENT_ID,"/content"));
        GetDocumentContent(key,String.format(API_URL+"%s"+"%s"+"%s","/v1/documents/",DOCUMENT_ID,"/content"));
    }

}