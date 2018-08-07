package renren.JAVA.thread;

import jxl.Sheet;
import renren.JAVA.PicInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 这是一个读单个Sheet里面label的线程
 * Created by shengwen.gan on 2018/8/7.
 * desc:
 */
public class ReadSheetThread implements Runnable {

	//定义一个线程安全的List
	public static final List<PicInfo> PIC_INFO_LIST = Collections.synchronizedList(new ArrayList<PicInfo>());
	private CountDownLatch latch;
	private Sheet sheet;
	private int startRow;
	private int endRow;

	public ReadSheetThread(CountDownLatch latch, Sheet sheet, int startRow, int endRow) {
		this.latch = latch;
		this.sheet = sheet;
		this.startRow = startRow;
		this.endRow = endRow;
	}

	public void readSheet(Sheet sheet, int startRow, int endRow) {
		if (sheet == null || sheet.getRows() == 0) {
			return;
		}
		int columnCounts = sheet.getColumns();
		String tableName = sheet.getCell(0, 1).getContents();
		//TODO 加上一个id字段的辨识
		List<String> fieldsName = new ArrayList<>();
		for (int fieldsIndex = 2; fieldsIndex < columnCounts; fieldsIndex++) {
			fieldsName.add(sheet.getCell(fieldsIndex, 0).getContents());
		}
		for (int row = startRow; row < endRow; row++) {
			for (int column = 2; column < columnCounts; column++) {
				String allPicUrl = sheet.getCell(column, row).getContents();
				String fieldId = sheet.getCell(column, row).getContents();
				if (invalidString(allPicUrl)) {
					continue;
				}
				List<String> urls = splitString(allPicUrl);
				for (String url : urls) {
					PicInfo picInfo = new PicInfo(tableName, fieldId, fieldsName.get(column), url);
					PIC_INFO_LIST.add(picInfo);
					System.out.println(PIC_INFO_LIST.size());
				}
			}
		}
	}

	public List<String> splitString(String picUrl) {
		List<String> picUrls = new ArrayList<>();
		String[] urls = null;
		if (picUrl.contains("\\|")) {
			urls = picUrl.split("\\|");
		} else {
			urls = picUrl.trim().split(" ");
		}
		for (int i = 0; i < urls.length; i++) {
			if (invalidString(urls[i])) {
				continue;
			}
			picUrls.add(transferString(urls[i]));
		}
		return picUrls;
	}

	private String transferString(String target) {
		if (target.startsWith("http://") || target.startsWith("https://")) {
			return target;
		}
		return "https://" + target;
	}

	private boolean invalidString(String str) {
		return str == null || str.trim().equals("") || str.equals("-");
	}

	@Override
	public void run() {
		try {
			readSheet(this.sheet, this.startRow, this.endRow);
		} finally {
			this.latch.countDown();
		}
	}
}
