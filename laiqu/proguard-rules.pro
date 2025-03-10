# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android\studiosdk/tools/proguard/proguard-android.txt
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
-dontwarn com.yolanda.nohttp.**
-keep class com.yolanda.nohttp.**{*;}
-dontwarn com.netease.**
-dontwarn io.netty.**
-keep class com.netease.** {*;}
-dontwarn net.poemcode.**
-dontwarn android.support.**

-dontwarn org.apache.http.**
-dontwarn com.amap.**
-dontwarn com.alibaba.**
-dontwarn com.netease.**
-dontwarn io.netty.**
-dontwarn com.autonavi.amap.**
-keep class com.dou361.** {
*;
}
-ignorewarning
