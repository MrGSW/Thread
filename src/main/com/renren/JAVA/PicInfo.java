package renren.JAVA;

/**
 * Created by shengwen.gan on 2018/8/6.
 * desc:
 */
public class PicInfo {

	private String id;
	private String idFieldName;
	private String tableName;
	private String fieldName;
	private String picUrl;
	private double picNumber;

	public PicInfo() {}

	public PicInfo(String tableName, String fieldName) {
		this.tableName = tableName;
		this.fieldName = fieldName;
	}

	public PicInfo(String tableName, String idFieldName, String id, String fieldName, String picUrl) {
		this.id = id;
		this.idFieldName = idFieldName;
		this.tableName = tableName;
		this.fieldName = fieldName;
		this.picUrl = picUrl;
	}

	public PicInfo(String id, String tableName, String fieldName, String picUrl) {
		this.id = id;
		this.tableName = tableName;
		this.fieldName = fieldName;
		this.picUrl = picUrl;
	}

	public PicInfo(String id, String tableName, String fieldName, String picUrl, double picNumber) {
		this.id = id;
		this.tableName = tableName;
		this.fieldName = fieldName;
		this.picUrl = picUrl;
		this.picNumber = picNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public double getPicNumber() {
		return picNumber;
	}

	public void setPicNumber(double picNumber) {
		this.picNumber = picNumber;
	}

	@Override
	public String toString() {
		return "PicInfo{" +
						"id='" + id + '\'' +
						", tableName='" + tableName + '\'' +
						", fieldName='" + fieldName + '\'' +
						", picUrl='" + picUrl + '\'' +
						", picNumber=" + picNumber +
						'}';
	}
}
