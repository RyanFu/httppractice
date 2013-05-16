package hq.downweb;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: hqtc
 * Date: 13-5-16
 * Time: 下午6:59
 * To change this template use File | Settings | File Templates.
 */
public class HoopUrlContainer {
    private BlockingQueue<String> toDoUrls = new LinkedBlockingQueue<String>();
    private BlockingQueue<String> doneUrls = new LinkedBlockingQueue<String>();

    public synchronized boolean addDoneUrl(String urlStr) {
        return this.doneUrls.add(urlStr);
    }

    public synchronized boolean addToDoUrl(String urlStr) {
        return this.toDoUrls.add(urlStr);
    }

    public synchronized String getOneToDoUrl() {
        return this.toDoUrls.remove();
    }
}
