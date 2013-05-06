package tc.ht.http;

/**
 * Created with IntelliJ IDEA.
 * User: hqtc
 * Date: 13-5-6
 * Time: 上午9:45
 * To change this template use File | Settings | File Templates.
 */

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 自动51cto 登录，签到 领取下载豆
 */
public class DailySign51 {
    public static final String URL_LOGIN = "http://home.51cto.com/index.php?s=/Index/doLogin";
    public static final String URL_SIGN = "http://home.51cto.com/index.php?s=/Home/toSign";
    public static final String URL_FREE_CREDITS = "http://down.51cto.com/download.php?do=getfreecredits&t="
            + System.currentTimeMillis();

    private static String user = "hqtc123";
    private static String password = "1l10veone";

    public static final DefaultHttpClient client = new DefaultHttpClient();

    /**
     * generate the login form;
     */
    private static UrlEncodedFormEntity generateLoginFormEntity() throws UnsupportedEncodingException {
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        formParams.add(new BasicNameValuePair("email", user));
        formParams.add(new BasicNameValuePair("passwd", password));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formParams, "utf-8");
        return formEntity;
    }

    /**
     * get session ,token or other information from cookie
     */
    private static String getCookieInfo() {
        List<Cookie> cookies = client.getCookieStore().getCookies();
        String str = "";
        for (Cookie cookie : cookies) {
            str += "&" + cookie.getName() + "=" + cookie.getValue();
        }
        return str;
    }

    /**
     * deal the response of login request
     */
    private static void processResponse(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream inputStream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                line = new String(line.getBytes(), "utf-8");
                int begin = line.indexOf("http://");
                int end = line.indexOf("\"></script", begin);
                HttpGet get = null;
                while (begin != -1) {
                    String url = line.substring(begin, end);
                    System.out.println(url);
                    get = new HttpGet(url);
                    response = client.execute(get);
                    System.out.println(response.getStatusLine().getStatusCode());
                    get.abort();
                    line = line.substring(end);
                    begin = line.indexOf("http://");
                    end = line.indexOf("\"></script", begin);
                }
            }
        }

        EntityUtils.consume(entity);
    }

    /**
     * read and print the content
     */
    public static void showResult(HttpResponse response) throws IOException {
        int status = response.getStatusLine().getStatusCode();
        System.out.println(status);
        HttpEntity entity = response.getEntity();
        InputStream instream = null;
        if (entity != null) {
            instream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                line = new String(line.getBytes(), "utf-8");
                System.out.println(line);
            }
        }
        instream.close();
        EntityUtils.consume(entity);
    }

    public static void main(String[] args) throws IOException {
        UrlEncodedFormEntity formEntity = generateLoginFormEntity();
        HttpPost loginPost = new HttpPost(URL_LOGIN);
        loginPost.setEntity(formEntity);
        HttpResponse response = client.execute(loginPost);
        processResponse(response);

        String authStr = getCookieInfo();

        System.out.println(URL_SIGN + authStr);

        HttpGet signGet = new HttpGet(URL_SIGN + authStr);
        HttpResponse signResponse = client.execute(signGet);
        showResult(signResponse);

        System.out.println(URL_FREE_CREDITS + authStr);
        HttpGet freeCreditsGet = new HttpGet(URL_FREE_CREDITS + authStr);
        HttpResponse freeResponse = client.execute(freeCreditsGet);
        showResult(freeResponse);
    }
}
