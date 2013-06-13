import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hqtc
 * Date: 13-6-13
 * Time: 上午10:52
 * To change this template use File | Settings | File Templates.
 */
public class SignIn {
    public String getLOGIN_URL() {
        return LOGIN_URL;
    }

    private final String LOGIN_URL = "http://www.renren.com/Plogin.do";

    private String emailStr = "814216440@qq.com";
    private String passwordStr = "l110veone";

    /**
     * generate the login form
     */

    private UrlEncodedFormEntity getLoginFormEntity() throws UnsupportedEncodingException {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("email", emailStr));
        nvps.add(new BasicNameValuePair("password", passwordStr));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps, "utf-8");
        return entity;
    }

    /**
     * process response
     *
     * @param resp
     */
    public void processResp(HttpResponse resp) throws IOException {
        HttpEntity respEntity = resp.getEntity();
        if (respEntity != null) {
            InputStream inputs = respEntity.getContent();
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(inputs));
            String line = null;
            while ((line = bfReader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    public static void main(String[] args) {
        SignIn signIn = new SignIn();
        HttpClient httpClient1 = new DefaultHttpClient();
        HttpClient httpClient2 = null;
        try {
            UrlEncodedFormEntity entity = signIn.getLoginFormEntity();
            HttpPost post = new HttpPost(signIn.getLOGIN_URL());
            post.setEntity(entity);
            HttpResponse resp1 = httpClient1.execute(post);

            String successUrl = resp1.getFirstHeader("Location").getValue();
            HttpGet httpGet = new HttpGet(successUrl);

            httpClient2 = new DefaultHttpClient();
            HttpResponse resp2 = httpClient2.execute(httpGet);

            System.out.println(EntityUtils.toString(resp2.getEntity()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClientProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            httpClient1.getConnectionManager().shutdown();
        }
    }
}
