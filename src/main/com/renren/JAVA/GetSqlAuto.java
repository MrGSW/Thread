package renren.JAVA;

import java.io.*;
import java.util.*;

/**
 * Created by shengwen.gan on 2018/8/3.
 * desc:
 */
public class GetSqlAuto {

	public static String getSql(String table, List<String> fields) {
		StringBuffer sb = new StringBuffer("select '" + table + "' , id , ");
		for (int i = 0; i < fields.size(); i++) {
			sb.append(" " +  fields.get(i) + " ");
			if (i < fields.size()-1) {
				sb.append(",");
			}
		}
		sb.append(" from " + table + " where ");
		for (int i = 0; i < fields.size(); i++) {
			sb.append(" (" +  fields.get(i) + " is not null " + " and " + fields.get(i) + " <> '' )");
			if (i < fields.size()-1) {
				sb.append(" or ");
			}
		}
		sb.append(" limit 0, 2500;");
		return sb.toString();
	}

	public static List<String> getInsertIDXSql(Map<String, String> map) {
		List<String> sqls = new ArrayList<>();
		for (String key : map.keySet()) {
			StringBuffer stringBuffer = new StringBuffer("update website_deadline_info set site_type = " + map.get(key) +
							" , dead_line = 1564588800000 where team_creator_id = " + key + ";");
			sqls.add(stringBuffer.toString());
		}
		return sqls;
	}

	public static void main(String[] args) {

		Map<String, String> tables = readTxt("D:\\zhuomian\\IDX.txt");


		List<String> names = readTxt1("D:\\zhuomian\\sql1.txt");

		System.out.println(tables.size());
		System.out.println(names.size());

		List<String> creatorId = new ArrayList<>();
		StringBuffer stringBuffer = new StringBuffer("select count (1) from website_info where uid in (");
		for (String key : tables.keySet()) {
			stringBuffer.append( key + ", ");
		}
		stringBuffer.append(");");
//		stringBuffer.append(" ) or parent_id in ( select id from website_info where uid in (");
//		for (String key : tables.keySet()) {
//			stringBuffer.append( key + ", ");
//		}
//		stringBuffer.append("))");

		System.out.println(stringBuffer.toString());


/*
		List<String> sqls = getInsertIDXSql(tables);

		for (int i = 0; i < sqls.size(); i++) {
			System.out.println(sqls.get(i));
		}
*/


	}

	public static List<String> readTxt1(String pathName) {
		File filename = new File(pathName);
		InputStreamReader reader = null;
		BufferedReader bufferedReader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(filename));
			bufferedReader = new BufferedReader(reader);
			String line = "";
			line = bufferedReader.readLine();
			List<String> strings = new ArrayList<>();
			while (line != null) {
				String[] array = line.split(" ");
				for (int i = 0; i < array.length; i++) {
					strings.add(array[i]);
				}
				line = bufferedReader.readLine();
			}
			return strings;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return Collections.emptyList();
	}






	public static Map<String, String> readTxt(String pathName) {
		File filename = new File(pathName);
		InputStreamReader reader = null;
		BufferedReader bufferedReader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(filename));
			bufferedReader = new BufferedReader(reader);
			String line = "";
			line = bufferedReader.readLine();
			Map<String, String> map = new HashMap<>();
			while (line != null) {
				String[] array = line.split(" ");
				for (int i = 0; i < array.length; i++) {

				}
/*
				List<String> strings = new ArrayList<>();
				for (int i = 1; i < array.length; i++) {
					strings.add(array[i]);
				}
				map.put(array[0], strings);
*/
				map.put(array[0], array[1]);
				line = bufferedReader.readLine();
			}
			return map;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return Collections.emptyMap();
	}

}
