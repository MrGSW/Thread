package renren.JAVA;

import jxl.Sheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by shengwen.gan on 2018/8/5.
 * desc:
 */
public class PoiRead implements Runnable {


	public static List<PicInfo> picInfos =new ArrayList<>();
	private final CountDownLatch latch;
	private Sheet sheet;
	private int start;
	private int end;

	public PoiRead(CountDownLatch latch, Sheet sheet, int start, int end) {
		this.end = end;
		this.start = start;
		this.sheet = sheet;
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
//			Long startTime = System.currentTimeMillis();
			picInfos.addAll(ImageUtil.readSheet(this.sheet, this.start, this.end));
//			Long end = System.currentTimeMillis();
//			System.out.println(Thread.currentThread().getName() + "耗时：" + (end-startTime) + " ms！ Current Time : " + System.currentTimeMillis());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("多线程读取失败！");
		} finally {
			latch.countDown();
		}
	}

}
