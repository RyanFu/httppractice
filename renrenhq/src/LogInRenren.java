/**
 * Created with IntelliJ IDEA.
 * User: hqtc
 * Date: 13-6-13
 * Time: 下午8:11
 * To change this template use File | Settings | File Templates.
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

public class LogInRenren
{
    public static void main(String[] args) throws ClientProtocolException,

            IOException
    {
        // TODO Auto-generated method stub
        String loginurl="http://www.renren.com/PLogin.do";
        String username="814216440@qq.com";
        String password="l110veone";
        System.out.println(LogInRenren.posturl

                (loginurl,username,password));
    }


    public static String posturl(String loginurl,String username,String

            password) throws ClientProtocolException, IOException
    {
        HttpClient httpclient1 = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(loginurl);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("email",username));
        formparams.add(new BasicNameValuePair("password",password));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,

                "utf-8");
        httppost.setEntity(entity);
        String str="";
        HttpClient httpclient2=null;
        try {
            HttpResponse response1 = httpclient1.execute(httppost);

            String login_success=response1.getFirstHeader

                    ("Location").getValue();//获取登陆成功之后跳转链接
            HttpGet httpget = new HttpGet(login_success);
            httpclient2 = new DefaultHttpClient();
            HttpResponse response2=httpclient2.execute(httpget);
            str=EntityUtils.toString(response2.getEntity());
//            httppost.abort();
//            httpget.abort();
        } finally
        {
//            httpclient1.getConnectionManager().shutdown();
//            httpclient2.getConnectionManager().shutdown();
        }

        return str;
    }
}

