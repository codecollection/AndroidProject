package com.thanone.palc.bean;

/**
 * 手机参数
 * 
 * @author zouchongjin@sina.com
 * @data 2015年6月7日
 */
public class PhoneInfo {

	private Long id;

	private String deviceID;// 设备ID

	private String os;// 手机操作系统
	private String model;// 手机型号
	private String macAddress;// mac地址
	private String imei;// 手机IMEI
	private String imsi;// 手机IMSI
	private String idfa;// IOS的广告标识符
	private String bluetoothMacAddress;// 蓝牙MAC地址

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getOs() {
		return os;
	}

	public String getIdfa() {
		return idfa;
	}

	public void setIdfa(String idfa) {
		this.idfa = idfa;
	}

	public String getBluetoothMacAddress() {
		return bluetoothMacAddress;
	}

	public void setBluetoothMacAddress(String bluetoothMacAddress) {
		this.bluetoothMacAddress = bluetoothMacAddress;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getModel() {
		return model;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

}
