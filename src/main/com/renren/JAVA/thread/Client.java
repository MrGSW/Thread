package renren.JAVA.thread;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import renren.JAVA.ImageUtil;
import renren.JAVA.PicInfo;
import renren.JAVA.PoiRead;

/**
 * Created by shengwen.gan on 2018/8/7. desc:
 */
public class Client {

  private static String excelName = "D:\\zhuomian\\picture\\pic1s.xls";

  public static void readSheet() {
    try {
      InputStream inputStream = new FileInputStream(new File(excelName).getAbsolutePath());
      Workbook workbook = Workbook.getWorkbook(inputStream);
      int sheetsNumber = workbook.getNumberOfSheets();
      ExecutorService executorService = Executors.newCachedThreadPool();
      CountDownLatch latch = new CountDownLatch(sheetsNumber);
      for (Sheet sheet : workbook.getSheets()) {
        System.out.println(sheet.getName() + " " + sheet.getRows());
        executorService.submit(new SheetThread(latch, sheet));
      }
      latch.await();
      executorService.shutdown();

    } catch (IOException e) {
      e.printStackTrace();
    } catch (BiffException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    long start = System.currentTimeMillis();
    readSheet();
    long end = System.currentTimeMillis();
    System.out.println("读取耗时：" + (end - start) + " ms!");
    System.out.println("读取完的结果：" + ReadSheetThread.PIC_INFO_LIST.size());
    ImageUtil imageUtil = new ImageUtil();
    imageUtil.multiThreadWriteExcel( PoiRead.picInfos);
  }

}
