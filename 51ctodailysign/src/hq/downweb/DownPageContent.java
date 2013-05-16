package hq.downweb;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: hqtc
 * Date: 13-5-15
 * Time: 下午5:34
 * To change this template use File | Settings | File Templates.
 */
public class DownPageContent {
    public void saveURLFile(String url, String fileDes) {
        File desFile = new File(fileDes);
        if (desFile.exists()) {
            System.out.println("file already exist");
            return;
        }
        try {
            desFile.createNewFile();
            FileOutputStream outImgStream = new FileOutputStream(desFile);
            outImgStream.write(getURLFileData(url));
            outImgStream.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private byte[] getURLFileData(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            httpconn.connect();
            InputStream inStream = httpconn.getInputStream();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            inStream.close();
            byte[] fileData = outStream.toByteArray();
            outStream.close();
            return fileData;
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public String getUrlDetail(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.connect();
            InputStream inStream = httpConn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "gbk"));
            StringBuffer buffer = new StringBuffer();
            String rl = null;
            while ((rl = reader.readLine()) != null) {
                buffer.append(rl).append("\n");
            }
            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static void main(String[] args) {
        DownPageContent down = new DownPageContent();
        String str = down.getUrlDetail("http://photo.hupu.com/ent/tag/美女");
        System.out.println(str);
        //down.saveURLFile("http://i.mmcdn.cn/simba/img/T1r9qBXthfXXb1upjX.jpg","c:/a.jpg");
    }
}
