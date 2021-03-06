package renren.JAVA;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO 多线程开发！HttpClient的多线程 Created by shengwen.gan on 2018/8/2. desc:
 */
public class ImageUtil {

  public static final ConcurrentHashMap<String, Double> cache = new ConcurrentHashMap<>();
  //	public static final List<PicInfo> picInfos =Collections.synchronizedList(new
  // ArrayList<PicInfo>());
  private static final String WRITE_FILE_NAME = "D:\\zhuomian\\picture\\picInfo.xls";
  private static final String WRITE_FILE_TEMPLATE = "D:\\zhuomian\\picture\\pics.xls";
  private static final MultiThreadedHttpConnectionManager cm = new
      MultiThreadedHttpConnectionManager();
  private static final int THREAD_NUMBER = 8;
  private static final int WRITE_THREAD_NUMBER = 64;
  private static volatile int rowCountZero = 1;
  private static volatile int rowCountOne = 1;
  private static volatile int rowCountTwo = 1;
  private static volatile int rowCountThree = 1;
  private static volatile int rowCountFour = 1;
  private static volatile int rowCountFive = 1;
  private static volatile int rowCountSix = 1;
  private static volatile int rowCountSeven = 1;
  private static volatile int rowCountEight = 1;
  private static Workbook book;
  private static volatile int counts = 0;
  private static volatile int unse_counts = 0;

  private static List<String> result = new ArrayList<>();

  private static PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

  static {
    try {
      book = Workbook.getWorkbook(new File(WRITE_FILE_TEMPLATE));
      HttpConnectionManagerParams params = new HttpConnectionManagerParams();
      params.setConnectionTimeout(1000);
      params.setSoTimeout(2000);
      params.setMaxTotalConnections(500);
      params.setDefaultMaxConnectionsPerHost(50);
      cm.setParams(params);

      connectionManager.setMaxTotal(500);
      connectionManager.setDefaultMaxPerRoute(64);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (BiffException e) {
      e.printStackTrace();
    }
  }

  public static double getImageSize(HttpClient httpClient1, String urlStr) {
    /*HttpClient httpClient = new HttpClient();
    HttpMethod method = new GetMethod(urlStr);
    try {
      httpClient.executeMethod(method);
      if (method.getStatusCode() != 200) {
        return -method.getStatusCode();
      }
      double imageSize = Double.valueOf(method.getResponseHeader("Content-Length").getValue()) /
          (1024.0 * 1024.0);
      return (double) Math.round(imageSize * 10000) / 10000;

    } catch (HttpException e) {
      System.out.println("HttpException! : ");
      e.printStackTrace();
      return -2;
    } catch (IOException e) {
      System.out.println("IOException!");
      return -3;
    } finally {
      method.releaseConnection();
    }*/
    HttpGet httpGet = new HttpGet(urlStr);
    HttpClientUtil.config(httpGet);
    return HttpClientUtil.get(urlStr);
  }

  public static List<PicInfo> readErrorSheet(Sheet sheet, int start, int end) {
    List<PicInfo> picInfos = new ArrayList<>();
    for (int i = start; i < end; i++) {
      String tableName = sheet.getCell(0, 1).getContents(); //获取表格名字
      if (tableName == null || tableName.equals("")) {
        continue;
      }
      String picUrl = sheet.getCell(3, i).getContents().replace("http:////", "https://");
      String id = sheet.getCell(1, i).getContents();
      String fieldName = sheet.getCell(2, i).getContents();
      picInfos.add(new PicInfo(id, tableName, fieldName, picUrl));
    }
    return picInfos;
  }

  public static List<PicInfo> readSheet(Sheet sheet, int start, int end) {
    List<PicInfo> picInfos = new ArrayList<>();
    String tableName = sheet.getCell(0, 1).getContents(); //获取表格名字
    List<String> fieldsName = new ArrayList<>(); //获取所有字段的名字
    for (int i = 2; i < sheet.getColumns(); i++) {
      fieldsName.add(sheet.getCell(i, 0).getContents());
    }
    for (int j = start; j < end; j++) {
      for (int k = 2; k < sheet.getColumns(); k++) {
        String fieldId = sheet.getCell(1, j).getContents();
        String picUrls = sheet.getCell(k, j).getContents();
        if (picUrls == null || picUrls.trim().equals("") || picUrls.equals("-")) {
          continue;
        }
        List<String> picUrlList = transferPicUrl(picUrls);
        for (String picUrl : picUrlList) {
          picInfos.add(new PicInfo(fieldId, tableName, fieldsName.get(k - 2), picUrl));
        }
      }
    }
    return picInfos;
  }

  public static void readOtherSheet(Sheet sheet, int start, int end) {
    List<String> updateFeaturedListing = new ArrayList<>();
    for (int i = start; i < end; i++) {
      StringBuilder stringBuilder = new StringBuilder("update feature_condition_info set agent_ids = '");
      stringBuilder.append(sheet.getCell(1, i).getContents() + "', agent_org_ids='");
      String agentOrgIds = sheet.getCell(2, i).getContents();
      String[] agentOrgIdsArray = agentOrgIds.split(",");
      for (int j = 0; j < agentOrgIdsArray.length; j++) {
        stringBuilder.append(agentOrgIdsArray[j]);
        if (j < agentOrgIdsArray.length - 1) {
          stringBuilder.append(";");
        }
      }
      stringBuilder.append("', office_names_v2='");
      stringBuilder.append(sheet.getCell(4, i).getContents() + "' ");
      stringBuilder.append(", union_groups = 'agentId;agentOrgIds;officeNamesV2' ");
      stringBuilder.append("where page_name='featured-listing' and site_id = (select id from website_info where domain_name = '");
      stringBuilder.append(sheet.getCell(0, i).getContents() + "');");
      updateFeaturedListing.add(stringBuilder.toString());
//      updateFeaturedListing.add(sheet.getCell(0, i).getContents());
    }
    result.addAll(updateFeaturedListing);
  }

  public static List<String> transferPicUrl(String picUrls) {
    String[] picUrlArray = null;
    if (picUrls.contains("|")) {
      picUrlArray = picUrls.split("\\|");
    } else {
      picUrlArray = picUrls.split(" ");
    }
    List<String> picUrlList = new ArrayList<>();
    for (int i = 0; i < picUrlArray.length; i++) {
      String picUrl = picUrlArray[i];
      if (!picUrl.startsWith("http://") && !picUrl.startsWith("https://")) {
        if (picUrl.startsWith("//")) {
          picUrlList.add("https:" + picUrl);
        } else {
          picUrlList.add("https://" + picUrl);
        }
      }
      if (picUrl.startsWith("http://")) {
        picUrlList.add(picUrl.replace("http://", "https://"));
      } else {
        picUrlList.add(picUrl);
      }
    }
    return picUrlList;
  }

  private static String transferUrl(String picUrl) {
    if (!picUrl.startsWith("http://") && !picUrl.startsWith("https://")) {
      if (picUrl.startsWith("//")) {
        picUrl = "https:" + picUrl;
      } else {
        picUrl = "https:" + picUrl;
      }
    }
    if (picUrl.startsWith("http://")) {
      picUrl = picUrl.replace("http://", "https://");
    }
    return picUrl;
  }

  private static int getLength(int targetNumber, int threadNumber) {
    return (targetNumber - 1) % threadNumber == 0 ? (targetNumber - 1) / threadNumber : (
        (targetNumber - 1) / threadNumber + 1);
  }

  private static int getEndIndex(int start, int length, int rows) {
    return (start + length) <= rows ? (start + length) : rows;
  }

  /**
   * 利用多线程读取excel表格，不返回值，直接获取PoiRead中的静态常量list，作为读取的结果集
   */
  public static void multiThreadReadExcel(File file) {

    Long startTime = System.currentTimeMillis();
    try {
      InputStream inputStream = new FileInputStream(file.getAbsolutePath());
      Workbook workbook = Workbook.getWorkbook(inputStream);
      System.out.println(workbook.getNumberOfSheets());
      for (int index = 0; index < workbook.getNumberOfSheets(); index++) {
        ExecutorService service = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(THREAD_NUMBER);
        Sheet sheet = workbook.getSheet(index);
        System.out.println("总的行数：" + sheet.getRows());
        int length = getLength(sheet.getRows(), THREAD_NUMBER);
        int start = 1;
        for (int i = 0; i < THREAD_NUMBER; i++) {
          service.submit(new PoiRead(latch, sheet, start, getEndIndex(start, length, sheet
              .getRows())));
          start += length;
        }
        latch.await();
        service.shutdown();
      }
      Long end = System.currentTimeMillis();
      System.out.println("多线程读耗时：" + (end - startTime) + "ms!");
    } catch (FileNotFoundException e) {
      System.out.println("file not find!");
    } catch (IOException e) {
      System.out.println("IO error!");
    } catch (BiffException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      System.out.println("线程阻赛失败！");
    }
  }

  public static double getPicSize(HttpClient client, String picUrl) {
    if (picUrl == null) {
      return -1;
    }
    if (cache.get(picUrl) != null) {
      return cache.get(picUrl);
    } else {
      double picSize = getImageSize(client, picUrl);
      cache.put(picUrl, picSize);
      return picSize;
    }
  }

  public static void writeExcel(WritableWorkbook writableWorkbook, HttpClient client,
      List<PicInfo> picInfos, int start, int end, List<WritableSheet> sheets) {
    try {
      for (int i = start; i < end; i++) {
        PicInfo picInfo = picInfos.get(i);
        if (picInfo.getPicUrl().equals("-") || picInfo.getPicUrl() == null || picInfo.getPicUrl()
            .trim().equals("")) {
          unse_counts++;
          continue;
        }

        double picSize = 0.0;
        if (!picInfo.getPicUrl().contains(".png") && !picInfo.getPicUrl().contains(".jpeg")
            && !picInfo.getPicUrl().contains("jpg") && !picInfo.getPicUrl().contains("ico")
            && !picInfo.getPicUrl().contains("gif")) {
          picSize = -404;
        } else {
          picSize = getPicSize(client, transferUrl(picInfo.getPicUrl()));
        }
        counts++;
        System.out.println(transferUrl(picInfo.getPicUrl()) + " size:" + picSize);
        picInfo.setPicNumber(picSize);
        if (picSize < -200.0) {
          writeExcel(picInfo, sheets.get(8), rowCountEight++);
        } else if (picSize < 0.0) {
          writeExcel(picInfo, sheets.get(7), rowCountSeven++);
        } else if (picSize < 0.5) {
          writeExcel(picInfo, sheets.get(0), rowCountZero++);
        } else if (picSize < 1.0) {
          writeExcel(picInfo, sheets.get(1), rowCountOne++);
        } else if (picSize < 1.5) {
          writeExcel(picInfo, sheets.get(2), rowCountTwo++);
        } else if (picSize < 2.0) {
          writeExcel(picInfo, sheets.get(3), rowCountThree++);
        } else if (picSize < 5.0) {
          writeExcel(picInfo, sheets.get(4), rowCountFour++);
        } else if (picSize < 10.0) {
          writeExcel(picInfo, sheets.get(5), rowCountFive++);
        } else {
          writeExcel(picInfo, sheets.get(6), rowCountSix++);
        }
      }
    } finally {

    }
  }

  public static void multiThreadWriteExcel(List<PicInfo> picInfos) {
    long startTime = System.currentTimeMillis();
    ExecutorService executorService = Executors.newCachedThreadPool();
    CountDownLatch latch = new CountDownLatch(WRITE_THREAD_NUMBER);
    try {

      File file = new File(WRITE_FILE_NAME);
      WritableWorkbook writableWorkbook = Workbook.createWorkbook(file, book);
      List<WritableSheet> sheets = Arrays.asList(writableWorkbook.getSheets());
      int length = getLength(picInfos.size(), WRITE_THREAD_NUMBER);
      HttpClient httpClient = new HttpClient(cm);
      int start = 0;
      for (int i = 0; i < WRITE_THREAD_NUMBER; i++) {
        executorService.submit(new PoiWrite(httpClient, writableWorkbook, picInfos,
            latch, start, getEndIndex(start, length, picInfos.size()), sheets));
        start += length;
      }
      latch.await();
      executorService.shutdown();
      writableWorkbook.write();
      writableWorkbook.close();
      System.out.println("进行写的总的图片数量："  + picInfos.size());
      System.out.println("各个数量分别为：" + rowCountZero + ", " + rowCountOne + ", "
          + rowCountTwo + ", " + rowCountThree + ", "
          + rowCountFour + ", " + rowCountFive + ", "
          + rowCountSix + ", " + rowCountSeven + ", " + rowCountEight);
      System.out.println(rowCountEight+rowCountSeven+rowCountSix+rowCountFive+rowCountFour+rowCountThree+rowCountTwo+rowCountOne+rowCountZero);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (WriteException e) {
      e.printStackTrace();
    }
    long endTime = System.currentTimeMillis();
    System.out.println("写入耗时：" + (endTime - startTime) + "ms!");
  }

  public synchronized static void writeExcel(PicInfo picInfo, WritableSheet sheet, int lastRow) {
    try {
      sheet.addCell(new Label(0, lastRow, picInfo.getTableName()));
      sheet.addCell(new Label(1, lastRow, picInfo.getId()));
      sheet.addCell(new Label(2, lastRow, picInfo.getFieldName()));
      sheet.addCell(new Label(3, lastRow, picInfo.getPicUrl()));
      sheet.addCell(new Label(4, lastRow, String.valueOf(picInfo.getPicNumber())));
    } catch (WriteException e) {
      System.out.println("写入异常" + picInfo);
    }
  }
/*

	public static void writeExcel(HttpClient httpClient, File file, Map<String, List<List<String>>>
	numbersMap) {
		try {
			Workbook workbook = Workbook.getWorkbook(file);
			File tempFile = new File(WRITE_FILE_NAME);
			WritableWorkbook writableWorkbook = Workbook.createWorkbook(tempFile, workbook);
			WritableSheet sheet = writableWorkbook.getSheet(0);

			System.out.println("------开始写入------");
			int row = 1;
			int oldColumnsSize = sheet.getColumns();
			for (Map.Entry<String, List<List<String>>> entry : numbersMap.entrySet()) {
				List<List<String>> values = entry.getValue();
				for (int i = 0; i < values.size(); i++) {
					String photoSize = "";
					for (int j = 0; j < values.get(i).size(); j++) {
						String url = values.get(i).get(j);
						if (cache.containsKey(url)) {
							photoSize += cache.get(url) + " ";
						} else {
							double length = getImageSize(httpClient, url);
							photoSize += length + " ";
							cache.put(url, length);
							System.out.println(url + " : " + length + "MB");
						}
					}
					sheet.addCell(new Label(oldColumnsSize+i, row++, photoSize));
				}
			}
			writableWorkbook.write();
			writableWorkbook.close();
			System.out.println("------写入结束------");

		} catch (IOException e) {
			System.out.println("出错了1");;
		} catch (BiffException e) {
			System.out.println("出错了2");;
		} catch (RowsExceededException e) {
			System.out.println("出错了3");;
		} catch (WriteException e) {
			System.out.println("出错了4");;
		}
	}
*/

  public static void main(String[] args) {
    System.out.println(cache.size());
    File file = new File("D:\\zhuomian\\picture\\giswen.xls");
    multiThreadReadExcel(file);
//
//    StringBuilder stringBuilder = new StringBuilder("select agent_ids,agent_org_ids, office_names_v2, union_groups from feature_condition_info");
//    stringBuilder.append(" where page_name='featured-listing' and site_id in (select id from website_info where domain_name in (");
//    for (String str : result) {
//     stringBuilder.append("'" + str + "', ");
//    }
//    stringBuilder.append("));");
//    System.out.println(stringBuilder.toString());
//    System.out.println("The result is :" + result.size());
    System.out.println(PoiRead.picInfos.size());
    multiThreadWriteExcel(PoiRead.picInfos);
  }

}
