package renren.JAVA.thread;

import jxl.Sheet;

import renren.JAVA.PicInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 这是一个读单个Sheet里面label的线程 Created by shengwen.gan on 2018/8/7. desc:
 */
public class ReadSheetThread implements Runnable {

  //定义一个线程安全的List
  public static final List<PicInfo> PIC_INFO_LIST = Collections.synchronizedList(new
      ArrayList<PicInfo>());
  private CountDownLatch latch;
  private Sheet sheet;
  private int startRow;
  private int endRow;
  private volatile int counts;

  public ReadSheetThread(CountDownLatch latch, Sheet sheet, int startRow, int endRow) {
    this.latch = latch;
    this.sheet = sheet;
    this.startRow = startRow;
    this.endRow = endRow;
  }

  public void readSheet(Sheet sheet, int startRow, int endRow) {
    if (sheet == null || sheet.getRows() == 0) {
      System.out.println("返回点1！");
      return;
    }
    int columnCounts = sheet.getColumns();
    String tableName = sheet.getCell(0, 1).getContents();
    String idFieldName = sheet.getCell(1, 0).getContents().substring(6);
    List<String> fieldsName = new ArrayList<>();
    for (int fieldsIndex = 2; fieldsIndex < columnCounts; fieldsIndex++) {
      fieldsName.add(sheet.getCell(fieldsIndex, 0).getContents());
    }
    for (int row = startRow; row < endRow; row++) {
      for (int column = 2; column < columnCounts; column++) {
        String allPicUrl = sheet.getCell(column, row).getContents();
        String fieldId = sheet.getCell(1, row).getContents();
        String fieldName = fieldsName.get(column - 2);
        if (invalidString(allPicUrl)) {
          continue;
        }
        List<String> urls = splitString(allPicUrl);
        for (int i = 0; i < urls.size(); i++) {
          PicInfo picInfo = new PicInfo(tableName, idFieldName, fieldId, fieldName, urls.get(i));
          PIC_INFO_LIST.add(picInfo);
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
