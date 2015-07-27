# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


#-libraryjars libs/arm64-v8a/liblocSDK5.so
#-libraryjars libs/armeabi/libBaiduMapSDK_v3_4_0_15.so
#-libraryjars libs/armeabi/libbspatch.so
#-libraryjars libs/armeabi/liblocSDK5.so
#-libraryjars libs/armeabi-v7a/libBaiduMapSDK_v3_4_0_15.so
#-libraryjars libs/armeabi-v7a/liblocSDK5.so
#-libraryjars libs/mips/liblocSDK5.so
#-libraryjars libs/mips64/liblocSDK5.so
#-libraryjars libs/x86/liblocSDK5.so
#-libraryjars libs/x86_64/liblocSDK5.so
#
#-libraryjars libs/android-support-v4.jar
#-libraryjars libs/baidumapapi_v3_4_0.jar
#-libraryjars libs/gson-2.1.jar
#-libraryjars libs/locSDK_5.2.jar
#-libraryjars libs/umeng-update-v2.4.2.jar


# base
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Fragment 
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class **.R$* {*;}
-keep class * extends java.lang.annotation.Annotation { *; }
-keep class com.google.zxing.** { *; }
-keep class com.lidroid.xutils.** { *; }
-keep class com.zcj.util.json.** { *; }
-keep class com.zcj.android.web.** { *; }
-keep class com.thanone.zgps.** { *; }

# v4
-keep class android.support.v4.** { *;}
-dontwarn android.support.v4.**

# umeng
-keep public class com.umeng.fb.ui.ThreadView {
}
-keep public class * extends com.umeng.**
-keep class com.umeng.** { *; }

# baidu
-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}

# gson
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.** { *; }
-keep public interface com.google.**
-dontwarn android.google.**

