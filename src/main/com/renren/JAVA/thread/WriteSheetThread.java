package renren.JAVA.thread;

import jxl.write.WritableSheet;

import java.util.concurrent.CountDownLatch;

/**
 * Created by shengwen.gan on 2018/8/8. desc:
 */
public class WriteSheetThread implements Runnable {

  private CountDownLatch latch;
  private WritableSheet sheet;
  private int startRow;

  public WriteSheetThread(CountDownLatch latch, WritableSheet sheet, int startRow) {
    this.latch = latch;
    this.sheet = sheet;
    this.startRow = startRow;
  }

  @Override
  public void run() {

  }

}
