package renren.JAVA;

import java.io.*;
import java.util.*;

/**
 * Created by shengwen.gan on 2018/8/3. desc:
 */
public class GetSqlAuto {

  public static String getSql(String table, List<String> fields) {
    StringBuffer sb = new StringBuffer("select '" + table + "' , id , ");
    for (int i = 0; i < fields.size(); i++) {
      sb.append(" " + fields.get(i) + " ");
      if (i < fields.size() - 1) {
        sb.append(",");
      }
    }
    sb.append(" from " + table + " where ");
    for (int i = 0; i < fields.size(); i++) {
      sb.append(" (" + fields.get(i) + " is not null " + " and " + fields.get(i) + " <> '' )");
      if (i < fields.size() - 1) {
        sb.append(" or ");
      }
    }
    sb.append(" limit 0, 2500;");
    return sb.toString();
  }

  public static List<String> getInsertIDXSql(Map<String, String> map) {
    List<String> sqls = new ArrayList<>();
    for (String key : map.keySet()) {
      StringBuffer stringBuffer = new StringBuffer("update website_deadline_info set site_type = " +
          "" + map.get(key) +
          " , dead_line = 1564588800000 where team_creator_id = " + key + ";");
      sqls.add(stringBuffer.toString());
    }
    return sqls;
  }

  public static void main(String[] args) {

//	  List<String> updatedId = readTxt1("D:\\zhuomian\\team_creator_id.txt");
//    System.out.println(updatedId.size());
//
//    List<String> all = readTxt1("D:\\zhuomian\\all.txt");
//    System.out.println(all.size());

//    Map<String, String> updatedMap = readTxt2("D:\\zhuomian\\team_creator_id.txt");
//    System.out.println(updatedMap.size());
    Map<String, String> allMap = readTxt2("D:\\zhuomian\\update.txt");
    System.out.println(allMap.size());

    Map<String, String> tables = readTxt("D:\\zhuomian\\IDX.txt");
    System.out.println(tables.size());


//		Map<String, String> one = readTxt1("D:\\zhuomian\\notInIDXMore.txt");
//
//		for (String key : one.keySet()) {
//			if (!tables.containsKey(key)) {
//				System.out.println(key);
//			}
//		}

    StringBuffer sb = new StringBuffer("select team_creator_id, site_type, updated_time from " +
        "website_deadline_info where team_creator_id in (");
    for (String updatedKey : tables.keySet()) {
      if (!allMap.containsKey(updatedKey)) {
//        StringBuffer stringBuffer = new StringBuffer("insert into website_deadline_info " +
//          "(team_creator_id, site_id, site_type, dead_line) values (" + updatedKey + ", 0 , " +
//          tables.get(updatedKey) + ", 1564588800000);");

//        StringBuffer stringBuffer = new StringBuffer("update website_deadline_info set
// site_type " +
//          " = " + tables.get(updatedKey)  + " where team_creator_id = " + updatedKey + ";");
        sb.append(updatedKey + ",");

      }
    }
    sb.append(");");
    System.out.println(sb.toString());


    Map<String, String> maps = new HashMap<>();
    for (String key : allMap.keySet()) {
      if (tables.containsKey(key)) {
        continue;
      }
    }


    StringBuffer stringBuffer = new StringBuffer("select dead_line from website_deadline_info " +
        "where " +
        "team_creator_id in ( ");
    for (String teamCreatorId : maps.keySet()) {
      stringBuffer.append(teamCreatorId + ", ");
    }
    stringBuffer.append(" );");
    System.out.println(stringBuffer.toString());

/*		for (String teamCreatorId : tables.keySet()) {
			StringBuffer sb = new StringBuffer("update website_deadline_info set site_type = " + tables
				.get(teamCreatorId) + ", dead_line = 1564675200000 where team_creator_id = " +
				teamCreatorId + ";");
			System.out.println(sb.toString());
		}*/


/*

		for (String tableName : tables.keySet()) {
			List<String> fields = tables.get(tableName);
			StringBuffer sb = new StringBuffer("select '" + tableName + "' ,'field:site_id', site_id,");
			for (int i = 0; i < fields.size(); i++) {
				sb.append(fields.get(i) );
				if (i < fields.size()-1) {
					sb.append(",");
				}
			}
			sb.append(" from " + tableName + " where site_id not in (select id from website_info where
			dead_line < 1533739502000) and ");
			for (int i = 0; i < fields.size(); i++) {
				sb.append("(" + fields.get(i)+ " is not null and " + fields.get(i) + " <> '' )");
				if (i < fields.size() - 1) {
					sb.append(" or ");
				}
			}
			sb.append(" limit 0, 8000;");
			System.out.println(sb.toString());
		}
*/
/*





select 'sys_arg' ,'field:site_id', site_id,value from sys_arg where site_id not in (select id
from website_info where dead_line < 1533739502000) and (value is not null and value <> '' ) limit
 0, 8000;


















































*/

//		List<String> names = readTxt1("D:\\zhuomian\\sql1.txt");
//
//		System.out.println(tables.size());
//		System.out.println(one.size());

//		List<String> creatorId = new ArrayList<>();
//		StringBuffer stringBuffer = new StringBuffer("select uid from website_info where parent_id =
// 0 and uid not in (");
//		for (String key : tables.keySet()) {
//			stringBuffer.append( key + ", ");
//		}
//		stringBuffer.append(");");
//		stringBuffer.append(" ) or parent_id in ( select id from website_info where uid in (");
//		for (String key : tables.keySet()) {
//			stringBuffer.append( key + ", ");
//		}
//		stringBuffer.append("))");

//		System.out.println(stringBuffer.toString());


    /*
     * select count(1) from website_info where uid in (437441938769789, 295892228267013,
     * 506596988165884, 416715502389863, 298391427963311, 534911503926188, 434741308200813,
     * 451359243966243, 445794372705673, 391126094138479, 327467719819809, 328874752364699,
     * 366126478323163, 414885838860962, 355161303202852, 480657116022225, 450689500828816,
     * 504646457520228, 430371423948534, 493495266821631, 544955561155729, 460894262000027,
     * 514301927751541, 433085302406093, 311207904427723, 479443212245886, 298721739150352,
     * 368297731598887, 513700341746104, 490160871866969, 326672982054866, 514297931915408,
     * 444129087260588, 367770680320425, 498802290535298, 494539606773979, 427010007319540,
     * 530375139102211, 328228691323313, 380444914489312, 432014856055457, 307970122069520,
     * 357279524052991, 357910382220547, 321622423389711, 416697906226298, 480636375971258,
     * 386865984274838, 449440166569458, 527958057499909, 486130435459307, 315721170637143,
     * 514277288184797, 522602801440851, 464882390435064, 344180389265451, 556243002981778,
     * 482125330143214, 344296542309274, 469409781468761, 332022937727602, 521468074964323,
     * 339150723988343, 300826155145155, 477225586750437, 356868168496377, 558934881854738,
     * 390521040353169, 357425022937696, 428341089508160, 549622943801934, 413911515272431,
     * 458934402529201, 330441451503038, 470026621334813, 341212507828089, 439198706297508,
     * 386863949521964, 409301681554369, 424333676376299, 441619711865402, 378041263417961,
     * 309487477758597, 336198329823419, 511948268562144, 354740697722795, 514571647062377,
     * 350080911331781, 382997859772789, 329615205529572, 313783743253759, 299306272645044,
     * 466914263071918, 492391686065549, 359003197841321, 318705063862062, 518616540303712,
     * 300458026396171, 389640936024628, 303431504740635, 287192456164694, 401166478188068,
     * 452719577424611, 366757623226832, 404599079123277, 439843668668475, 504408967505102,
     * 485932880116594, 384826736858039, 426284474298236, 289817107987502, 328290796213257,
     * 326882641082724, 297702600715252, 363763901979112, 531742229123368, 292092456637264,
     * 474340925218666, 397938294835528, 327524981385766, 500330103015402, 290997474987214,
     * 538101160474688, 457628834228513, 369568760782081, 306376319350964, 293710456344952,
     * 298439607524807, 346148599926045, 314240597204571, 468468696283257, 426086001725942,
     * 328556556031304, 514500943659799, 543111626331869, 458939138350372, 336122305612577,
     * 361283700329617, 434586961726535, 374575221987477, 343380465860506, 500899886075254,
     * 495823925234424, 347423681737816, 304415159241229, 496520364986687, 337078697886304,
     * 546185220403954, 554512440497980, 345820106442641, 383477603380141, 375034445257275,
     * 525513888139652, 456574652983486, 521416199938229, 539790064861442, 535346763878575,
     * 372281237165125, 426459551679959, 505651343961941, 386402991780561, 412857061634311,
     * 516368373804436, 386005899738474, 549599946762865, 519640237175232, 436756064888740,
     * 286467341526251, 528651923606244, 557873935097032, 429747564673771, 546070751652996,
     * 465831782071860, 442750727775417, 422128729279267, 475756811815428, 282565188278803,
     * 338438905857475, 426529509046320, 511891563723426, 358959230868467, 544892423557337,
     * 339412344996416, 531042751576670, 349396846576772, 302826466670513, 394809465213106,
     * 535204713656080, 559801172406203, 446223733714245, 547698451709023, 442433993774538,
     * 292917228923987, 413657274084664, 506877660407030, 415913555928599, 408178770257963,
     * 309143850302254, 405119814987115, 336690683629273, 322795179355418, 444416013950443,
     * 438513890735149, 499689122102344, 289422336168363, 358797246685960, 511093893886664,
     * 501214493513287, 516624290536187, 372726145447938, 477347764337039, 317025246432691,
     * 465523570074107, 470742009690262, 348731372678230, 408603980514754, 372116814590894,
     * 368532862965619, 466981538659611, 546971962671331, 545452986915214, 338729813556220,
     * 306288382660587, 329350761770206, 283476473008407, 380917740928116, 498895961537629,
     * 380333624008356, 301995571973458, 499175649882582, 478291177562202, 414532982897542,
     * 422842323195860, 550509752687426, 404292799845235, 363149179331624, 417792930093146,
     * 490933524024274, 335526410682813, 336732529227479, 526691573323591, 343448280524641,
     * 560309421781616, 290823357887248, 415005773949035, 311084581789887, 303275406128989,
     * 295485937956959, 323780870684586, 372012869111228, 331978427753760, 307746393996675,
     * 388935322131089, 426968301649015, 530848533488190, 285742948138148, 558688625968913,
     * 512102627595812, 370593095134624, 342163990430939, 477265535235707, 327089138436623,
     * 335855798273943, 352661143281346, 386791075272456, 407645921530832, 441515602520112,
     * 368645140640806, 465429098164213, 381614123190799, 503871390667034, 535005929494899,
     * 319091009588344, 514793193420954, 457093403243271, 363633533655311, 370541861727766,
     * 352346635487078, 561590786196398, 339727647459511, 532494077995052, 426272961183446,
     * 329964982211802, 559638828357597, 476436120773019, 396410558912309, 360179805701348,
     * 434296431206751, 523090591325881, 554373283643729, 548565353957304, 362639478881422,
     * 348793381295392, 316444449612319, 383238196470504, 372191638173804, 386247601248098,
     * 355213657850240, 553117257612030, 553682929262097, 419136504835135, 443309485611063,
     * 366541519473538, 521651212551228, 437700477139721, 321462671845295, 383740657325366,
     * 452934567972885, 561990855965364, 335180333563874, 500076663835798, 525847958703023,
     * 400334590032086, 288451154724883, 514493920065255, 328807485504879, 501006525019375,
     * 418887335453641, 505318323574132, 331816538643490, 312455440162229);
     * */

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
        String[] array = line.split("\t");
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


  public static Map<String, String> readTxt2(String pathName) {
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
        map.put(array[0], "1");
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
