package com.android.zouchongjin.j_junit;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.Log;

import com.android.zouchongjin.R;
import com.zcj.android.util.UtilAppFile;
import com.zcj.android.util.UtilContacts;
import com.zcj.android.util.UtilMessage;
import com.zcj.android.util.UtilSharedPreferences;
import com.zcj.android.util.UtilTelAddress;
import com.zcj.android.util.bean.ContactBean;
import com.zcj.util.UtilFile;

public class MyTest extends AndroidTestCase {
	// 获取所有联系人
	public void testContacts() {
		List<ContactBean> cs = UtilContacts.getContacts(getContext());
		for (ContactBean c : cs) {
			Log.v("zouchongjin", c.getName()+c.getPhone()+c.getEmail()+c.getContactid());
		}
	}

	// 根据号码获取联系人的姓名
	public void testContactNameByNumber() {
		String name = UtilContacts.getContactNameByPhone(getContext(), "13560650505");
		Log.v("zouchongjin", name);
	}

	// 添加联系人
	public void testAddContact() throws Exception {
		ContactBean c = new ContactBean("邹崇锦22", "13714324432", "fsa@sina.com");
		ContactBean c2 = new ContactBean("邹崇锦11", "13714324432", "fsa@sina.com");
		Log.v("zouchongjin", UtilContacts.addContacts(getContext(), c) + "");
		Log.v("zouchongjin", UtilContacts.addContacts(getContext(), c2) + "");
	}

	public void utilSharedPreferences() {
		Log.v("zouchongjin", UtilSharedPreferences.save(getContext(), "abc", "a", "fsagfdagf")+"");
		Log.v("zouchongjin", UtilSharedPreferences.get(getContext(), "abc", "a"));
	}
	
	public void utilFile() {
		Log.v("zouchongjin", UtilAppFile.saveToFiles(getContext(), "zou", "fsdafsagfdgfsdafsag是地方萨芬213123", Context.MODE_PRIVATE)+"");
		Log.v("zouchongjin", UtilAppFile.getByFiles(getContext(), "zou"));
		Log.v("zouchongjin", UtilAppFile.getByStaticFile(getContext(), R.raw.zouzou));
		if (UtilAppFile.sdcardExist()) {			
			Log.v("zouchongjin", UtilFile.saveByByte("fsadfsagfgaf规范的施工方的给".getBytes(), Environment.getExternalStorageDirectory()+ File.separator+ "AndroidHelp"+ File.separator + "sss.txt")+"");
		}
	}
	
	public void utilTelAddress() {
		String address = UtilTelAddress.getAddress("1378013");
		Log.v("zouchongjin", address);
	}
	
	public void utilAndroid() {
		UtilMessage.sendMessage("5554", "fsdafsafsafdsf发的事发时");
	}
}
