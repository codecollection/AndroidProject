package com.thanone.appbuy.web;

import com.lidroid.xutils.http.RequestParams;

import java.io.File;

public class HttpUrlUtil {

	public static final String URLBASE = "http://192.168.1.119:8080/daituan/";
	
	public static final String URL_WEIBO = "http://weibo.com/daituanDT";
	public static final String URL_QQAPP = "http://sj.qq.com/myapp/detail.htm?apkName=com.thanone.appbuy";
	
	public static final String URL_LOGO_SHARE = URLBASE + "images/sharelogo.jpg";// 分享默认的LOGO图标
	public static final String URL_DOWNLOAD = URLBASE + "APP/DOWNLOAD.jsp";// APP下载页
	public static final String URL_WEB_AQ = URLBASE + "APP/AQ.jsp";// 安全 网页
	public static final String URL_WEB_GYDT = URLBASE + "APP/GYDT.jsp";// 关于代团 网页
	public static final String URL_WEB_DTFWSYXY = URLBASE + "APP/DTFWSYXY.jsp";// 代团服务使用协议 网页
	public static final String URL_WEB_YSTK = URLBASE + "APP/YSTK.jsp";// 隐私条款 网页
	
	public static final String URL_COMMENT_ADDCOMMENT = 		URLBASE + "comment/addcomment.ajax";// 用户回复消息
	public static final String URL_COMMENT_ADDGOODSCOMMENT = 	URLBASE + "comment/addgoodscomment.ajax";// 评论商品
	public static final String URL_COMMENT_ADDSUBJECTCOMMENT = 	URLBASE + "comment/addsubjectcomment.ajax";// 评论话题
	public static final String URL_COMMENT_CLEAR = 				URLBASE + "comment/clear.ajax";// 清空自己的消息
	public static final String URL_COMMENT_DEL = 				URLBASE + "comment/del.ajax";// 删除评论
	public static final String URL_COMMENT_FAVOUR = 			URLBASE + "comment/favour.ajax";// 评论点赞
	public static final String URL_COMMENT_GOODSCOMMENT = 		URLBASE + "comment/goodscomment.ajax";// 商品评论列表
	public static final String URL_COMMENT_MYDEL =			 	URLBASE + "comment/mydel.ajax";// 删除某消息
	public static final String URL_COMMENT_MYLIST =			 	URLBASE + "comment/mylist.ajax";// 我的消息列表
	public static final String URL_COMMENT_SUBJECTCOMMENT = 	URLBASE + "comment/subjectcomment.ajax";// 话题评论列表
	public static final String URL_COMMENT_READ = 				URLBASE + "comment/read.ajax";// 用户标识某消息为已读
	public static final String URL_COMMENT_REPORT = 			URLBASE + "comment/report.ajax";// 举报评论
	public static final String URL_COMMENT_UNFAVOUR = 			URLBASE + "comment/unfavour.ajax";// 评论取消点赞
	public static final String URL_COMMENT_UNREAD = 			URLBASE + "comment/unread.ajax";// 用户是否有未读消息
	
	public static final String URL_FAVORITE_ADD = 			URLBASE + "favorite/add.ajax";// 收藏商品
	public static final String URL_FAVORITE_CLEAR = 		URLBASE + "favorite/clear.ajax";// 用户清空收藏列表
	public static final String URL_FAVORITE_DEL = 			URLBASE + "favorite/del.ajax";// 取消商品收藏
	public static final String URL_FAVORITE_LIST = 			URLBASE + "favorite/list.ajax";// 用户收藏列表
	
	public static final String URL_GOODS_DETAIL = 			URLBASE + "goods/detail.ajax";// 商品详细
	public static final String URL_GOODS_SELLING = 			URLBASE + "goods/selling.ajax";// 抢购商品列表
	public static final String URL_GOODS_TOSELL = 			URLBASE + "goods/tosell.ajax";// 预告商品列表
	
	public static final String URL_SHARE_ADDGOODSSHARE = 	URLBASE + "share/addgoodsshare.ajax";// 分享商品
	public static final String URL_SHARE_ADDSUBJECTSHARE = 	URLBASE + "share/addsubjectshare.ajax";// 分享话题
	public static final String URL_SHARE_GOODSSHARE = 		URLBASE + "share/goodsshare.ajax";// 用户商品分享列表
	public static final String URL_SHARE_SUBJECTSHARE = 	URLBASE + "share/subjectshare.ajax";// 用户话题分享列表
	public static final String URL_SHARE_SUBJECTSHARELIST = URLBASE + "share/subjectsharelist.ajax";// 某话题的分享列表
	public static final String URL_SHARE_DELSUBJECT = 		URLBASE + "share/delsubject.ajax";// 用户删除某话题分享
	public static final String URL_SHARE_DELGOODS = 		URLBASE + "share/delgoods.ajax";// 用户删除某商品分享
	public static final String URL_SHARE_CLEARSUBJECT = 	URLBASE + "share/clearsubject.ajax";// 用户清空话题分享
	public static final String URL_SHARE_CLEARGOODS = 		URLBASE + "share/cleargoods.ajax";// 用户清空商品分享
	
	public static final String URL_SUBJECT_DETAIL = 		URLBASE + "subject/detail.ajax";// 获取话题详细
	public static final String URL_SUBJECT_FAVOUR = 		URLBASE + "subject/favour.ajax";// 给话题点赞
	public static final String URL_SUBJECT_UNFAVOUR = 		URLBASE + "subject/unfavour.ajax";// 给话题取消点赞
	public static final String URL_SUBJECT_LIST = 			URLBASE + "subject/list.ajax";// 话题列表
	public static final String URL_SUBJECT_VOTE = 			URLBASE + "subject/vote.ajax";// 给投票话题投票
	
	public static final String URL_USER_LOGIN = 			URLBASE + "user/login.ajax";// 登陆
	public static final String URL_USER_REGISTER = 			URLBASE + "user/register.ajax";// 注册
	public static final String URL_USER_UPDATEPASSWORD = 	URLBASE + "user/updatepassword.ajax";// 修改密码
	public static final String URL_USER_UPLOAD = 			URLBASE + "user/upload.ajax";// 上传头像
	public static final String URL_USER_RESETPASSWORD = 	URLBASE + "user/resetpassword.ajax";// 重置密码
	public static final String URL_USER_CLOGIN = 			URLBASE + "user/clogin.ajax";// 第三方登陆
	public static final String URL_USER_CREGISTER = 		URLBASE + "user/cregister.ajax";// 第三方账号创建
	
	public static RequestParams user_upload(Long userID, File file) {
		return initParams(new String[] { "userID", "headImage" }, new Object[] { userID, file });
	}
	public static RequestParams user_clogin(String usid, Integer type) {
		return initParams(new String[] { "usid", "type" }, new Object[] { usid, type });
	}
	public static RequestParams user_cregister(String nickName, String usid, Integer type) {
		return initParams(new String[] { "nickName", "usid", "type" }, new Object[] { nickName, usid, type });
	}
	public static RequestParams user_resetpassword(String userName, String mailBox) {
		return initParams(new String[] { "userName", "mailBox" }, new Object[] { userName, mailBox });
	}
	public static RequestParams favorite_clear(Long userID) {
		return initParams(new String[] { "userID" }, new Object[] { userID });
	}
	public static RequestParams share_delsubject(Long userID, Long subjectID) {
		return initParams(new String[] { "userID", "subjectID" }, new Object[] { userID, subjectID });
	}
	public static RequestParams share_delgoods(Long userID, Long goodsID) {
		return initParams(new String[] { "userID", "goodsID" }, new Object[] { userID, goodsID });
	}
	public static RequestParams share_clearsubject(Long userID) {
		return initParams(new String[] { "userID" }, new Object[] { userID });
	}
	public static RequestParams share_cleargoods(Long userID) {
		return initParams(new String[] { "userID" }, new Object[] { userID });
	}
	public static RequestParams comment_addcomment(Long userID, Long targetUserID, String content) {
		return initParams(new String[] { "userID", "targetUserID", "content" }, new Object[] { userID, targetUserID, content });
	}
	public static RequestParams comment_unread(Long userID) {
		return initParams(new String[] { "userID" }, new Object[] { userID });
	}
	public static RequestParams favorite_add(Long userID, Long goodsID) {
		return initParams(new String[] { "userID", "goodsID" }, new Object[] { userID, goodsID });
	}
	public static RequestParams comment_unfavour(Long userID, Long commentID) {
		return initParams(new String[] { "commentID", "userID" }, new Object[] { commentID, userID });
	}
	public static RequestParams comment_favour(Long userID, Long commentID) {
		return initParams(new String[] { "commentID", "userID" }, new Object[] { commentID, userID });
	}
	public static RequestParams user_login(String username, String password) {
		return initParams(new String[] { "userName", "passWord" }, new Object[] { username, password });
	}
	public static RequestParams comment_addgoodscomment(Long userId, Long goodsId, Long targetUserID, String content) {
		return initParams(new String[]{"userID","goodsID","targetUserID","content"}, new Object[]{userId, goodsId, targetUserID, content});
	}
	public static RequestParams comment_report(Long userID, Long commentID) {
		return initParams(new String[] { "userID", "commentID" }, new Object[] { userID, commentID });
	}
	public static RequestParams favorite_del(Long userID, Long goodsID) {
		return initParams(new String[] { "userID", "goodsID" }, new Object[] { userID, goodsID });
	}
	public static RequestParams user_register(String userName, String passWord, String nickName, String mailBox, String introduce) {
		return initParams(new String[]{"userName","passWord","nickName","mailBox","introduce"}, new Object[]{userName,passWord,nickName,mailBox,introduce});
	}
	public static RequestParams user_updatepassword(String oldPassWord, String newPassWord, Long userID) {
		return initParams(new String[]{"oldPassWord","newPassWord","userID"}, new Object[]{oldPassWord,newPassWord,userID});
	}
	public static RequestParams favorite_list(Long userID, int pageSize, int offset) {
		return initParams(new String[]{"userID","pageSize","offset"}, new Object[]{userID,pageSize,offset});
	}
	public static RequestParams share_goodsshare(Long userID, int pageSize, int offset) {
		return initParams(new String[]{"userID","pageSize","offset"}, new Object[]{userID,pageSize,offset});
	}
	public static RequestParams share_subjectshare(Long userID, int pageSize, int offset) {
		return initParams(new String[]{"userID","pageSize","offset"}, new Object[]{userID,pageSize,offset});
	}
	public static RequestParams subject_vote(Long userID, Long subjectID, Integer item) {
		return initParams(new String[]{"userID","subjectID","item"}, new Object[]{userID,subjectID,item});
	}
	public static RequestParams share_addsubjectshare(Long userID, Long subjectID) {
		return initParams(new String[]{"userID","subjectID"}, new Object[]{userID,subjectID});
	}
	public static RequestParams share_addgoodsshare(Long userID, Long goodsID) {
		return initParams(new String[]{"userID","goodsID"}, new Object[]{userID,goodsID});
	}
	public static RequestParams comment_addsubjectcomment(String content, Long userID, Long subjectID, Long targetUserID) {
		return initParams(new String[]{"content","userID","subjectID","targetUserID"}, new Object[]{content,userID,subjectID,targetUserID});
	}
	public static RequestParams comment_subjectcomment(int pageSize, int offset, Long userID, Long subjectID) {
		return initParams(new String[]{"pageSize","userID","subjectID","offset"}, new Object[]{pageSize,userID,subjectID,offset});
	}
	public static RequestParams share_subjectsharelist(int pageSize, int offset, Long subjectID) {
		return initParams(new String[]{"pageSize","subjectID","offset"}, new Object[]{pageSize,subjectID,offset});
	}
	public static RequestParams subject_favour(Long userID, Long subjectID) {
		return initParams(new String[]{"userID","subjectID"}, new Object[]{userID,subjectID});
	}
	public static RequestParams subject_unfavour(Long userID, Long subjectID) {
		return initParams(new String[]{"userID","subjectID"}, new Object[]{userID,subjectID});
	}
	public static RequestParams comment_mylist(Long userID, int pageSize, int offset) {
		return initParams(new String[]{"userID","pageSize","offset"}, new Object[]{userID,pageSize,offset});
	}
	public static RequestParams comment_clear(Long userID) {
		return initParams(new String[]{"userID"}, new Object[]{userID});
	}
	public static RequestParams comment_read(Long commentID) {
		return initParams(new String[]{"commentID"}, new Object[]{commentID});
	}
	public static RequestParams comment_mydel(Long commentID) {
		return initParams(new String[]{"commentID"}, new Object[]{commentID});
	}
	public static RequestParams subject_detail(Long userID, Long subjectID) {
		return initParams(new String[]{"userID","subjectID"}, new Object[]{userID,subjectID});
	}
	public static RequestParams goods_selling(int pageSize, int offset) {
		return initParams(new String[]{"pageSize","offset"}, new Object[]{pageSize,offset});
	}
	public static RequestParams goods_tosell(int pageSize, int offset) {
		return initParams(new String[]{"pageSize","offset"}, new Object[]{pageSize,offset});
	}
	public static RequestParams subject_list(int pageSize, int offset, Long userID) {
		return initParams(new String[]{"pageSize","offset", "userID"}, new Object[]{pageSize,offset,userID});
	}
	public static RequestParams comment_goodscomment(int pageSize, int offset, Long userID, Long goodsID) {
		return initParams(new String[]{"pageSize","offset", "userID", "goodsID"}, new Object[]{pageSize,offset,userID,goodsID});
	}
	public static RequestParams goods_detail(Long userID, Long goodsID) {
		return initParams(new String[]{"userID", "goodsID"}, new Object[]{userID,goodsID});
	}
	
	public static RequestParams initParams(String[] keys, Object[] values) {
		if (keys == null || values == null || keys.length != values.length) {
			return null;
		}
		RequestParams params = new RequestParams();
		for (int i = 0; i < keys.length; i++) {
			if (values[i] != null) {				
				if (values[i] instanceof File) {
					params.addBodyParameter(keys[i], (File)values[i]);
				} else {					
					params.addBodyParameter(keys[i], String.valueOf(values[i]));
				}
			}
		}
		return params;
	}

}
