package renren.JAVA;

import jxl.Sheet;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by shengwen.gan on 2018/8/5.
 * desc:
 */
public class PoiWrite implements Runnable {

	private HttpClient httpClient;
	private WritableWorkbook writableWorkbook;
	private List<PicInfo> picInfos;
	private final CountDownLatch latch;
	private int start;
	private int end;
	private List<WritableSheet> sheets;

	public PoiWrite(HttpClient httpClient, WritableWorkbook writableWorkbook, List<PicInfo> picInfos, CountDownLatch latch, int start, int end, List<WritableSheet> sheets) {
		this.httpClient = httpClient;
		this.writableWorkbook = writableWorkbook;
		this.picInfos = picInfos;
		this.latch = latch;
		this.start = start;
		this.end = end;
		this.sheets = sheets;
	}

	@Override
	public void run() {
		try {
			ImageUtil.writeExcel( this.writableWorkbook, this.httpClient, this.picInfos, this.start, this.end, sheets);
		} catch (Exception e) {

		} finally {
			latch.countDown();
		}
	}
}
