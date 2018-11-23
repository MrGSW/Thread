package renren.JAVA;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

/**
 * Created by shengwen.gan on 2018/8/3. desc:
 */
public class HttpRequest {

  // 读取超时
  private final static int SOCKET_TIMEOUT = 10000;
  // 连接超时
  private final static int CONNECTION_TIMEOUT = 10000;
  // 每个HOST的最大连接数量
  private final static int MAX_CONN_PRE_HOST = 20;
  // 连接池的最大连接数
  private final static int MAX_CONN = 60;
  // 连接池
  private final static HttpConnectionManager httpConnectionManager;

  static {
    httpConnectionManager = new MultiThreadedHttpConnectionManager();
    HttpConnectionManagerParams params = httpConnectionManager.getParams();
    params.setConnectionTimeout(CONNECTION_TIMEOUT);
    params.setSoTimeout(SOCKET_TIMEOUT);
    params.setDefaultMaxConnectionsPerHost(MAX_CONN_PRE_HOST);
    params.setMaxTotalConnections(MAX_CONN);
  }

  public static HttpConnection getHttpConnection() {
    HostConfiguration configuration = new HostConfiguration();
    return httpConnectionManager.getConnection(configuration);
  }

  public static String doHttpRequest(GetMethod method) {

    return null;
  }

}
