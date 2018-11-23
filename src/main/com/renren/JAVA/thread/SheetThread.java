package renren.JAVA.thread;

import jxl.Sheet;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 读一个Sheet的线程 Created by shengwen.gan on 2018/8/7. desc:
 */
public class SheetThread implements Runnable {

  private static final int READ_SHEET_CELL_THREAD_NUMBER = 20;
  private CountDownLatch latch;
  private Sheet sheet;

  public SheetThread(CountDownLatch latch, Sheet sheet) {
    this.latch = latch;
    this.sheet = sheet;
  }

  private static int getLength(int targetNumber, int threadNumber) {
    return (targetNumber - 1) % threadNumber == 0 ? (targetNumber - 1) / threadNumber : (
        (targetNumber - 1) / threadNumber + 1);
  }

  private static int getEndIndex(int start, int length, int rows) {
    return (start + length) <= rows ? (start + length) : rows;
  }

  @Override
  public void run() {
    try {
      int rows = this.sheet.getRows();
      if (rows > 1) {
        //TODO JAVA中的四种线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch cellLath = new CountDownLatch(rows < 120 ? 1 :
            READ_SHEET_CELL_THREAD_NUMBER);
        int stepLength = getLength(rows, rows < 120 ? 1 : READ_SHEET_CELL_THREAD_NUMBER);
        for (int index = 1; index < rows; index += stepLength) {
          executorService.submit(new ReadSheetThread(cellLath, this.sheet, index, getEndIndex
              (index, stepLength, rows)));
        }
        cellLath.await();
        executorService.shutdown();
      }
    } catch (InterruptedException e) {
      System.out.println("SheetThread await error!");
    } finally {
      this.latch.countDown();
    }
  }
}
