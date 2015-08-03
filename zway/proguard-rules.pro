# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
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
-keep class com.zandroid.zway.** { *; }

# v4 v7
-keep class android.support.v4.** { *;}
-dontwarn android.support.v4.**
-keep class android.support.v7.** { *;}
-dontwarn android.support.v7.**

# umeng
-keep public class com.umeng.fb.ui.ThreadView {
}
-keep public class * extends com.umeng.**
-keep class com.umeng.** { *; }
-keep class com.zandroid.zlibbmap.** { *; }

# baidu
-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}

# gson
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.** { *; }
-keep public interface com.google.**
-dontwarn android.google.**
