-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-dontwarn android.support.v4.**
-dontwarn android.support.v7.**
-dontwarn android.support.v13.**
-dontwarn android.webkit.WebViewClient
-dontwarn com.tencent.mm.sdk.**
-dontwarn pl.droidsonroids.gif.**
-dontskipnonpubliclibraryclassmembers

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

-keep public class * extends android.app.Activity
-keep public class * extends android.app.IntentService
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.v4.** { *; }
-keep class android.support.v7.** { *; }
-keep class android.support.v13.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.v13.**
-keep public class * extends android.app.Fragment
-keep public class * extends android.os.IInterface


-keep class com.tencent.mm.sdk.**
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}

-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

 -keep public class com.dangdang.buy2.R$*{
     public static final int *;
 }
 
 -keep public class com.feedback.ui.ThreadView { }
 
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.photoview.** { *; }
-dontwarn com.photoview.**
-keep class com.ntalker.** { *; }
-dontwarn com.ntalker.**
-keep class com.capricorn.** { *; }
-dontwarn com.capricorn.**
-keep class com.example.** { *; }
-dontwarn com.example.**

-keep class pl.droidsonroids.gif.** { *; }
-keep interface pl.droidsonroids.gif.** { *; }

 
# ProGuard configurations for NetworkBench Lens
-keep class com.networkbench.** { *; }
-dontwarn com.networkbench.**
-keepattributes Exceptions, Signature, InnerClasses

# End NetworkBench Lens

#gaode location
-keep class com.amap.api.location.** { *; }
-keep class com.aps.** { *; }
-dontwarn com.amap.api.location.**
-dontwarn com.aps.**

-dontwarn edu.emory.mathcs.**
-dontwarn net.sf.retrotranslator.**
-dontwarn org.fusesource.hawtdispatch.**

-keep class edu.emory.mathcs.backport.java.util.** { *; }
-keep class net.sf.retrotranslator.** { *; }
-keep class org.fusesource.** { *; }

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
 public *;
}

-keepattributes Exceptions,InnerClasses

-keep class sun.misc.Unsafe { *; }

-keep class com.google.gson.examples.android.model.** { *; }

-keepclassmembers class * extends com.sea_monster.dao.AbstractDao {
 public static java.lang.String TABLENAME;
}
-keep class **$Properties
-dontwarn org.eclipse.jdt.annotation.**

-keep class com.ultrapower.** {*;}

-keep class de.greenrobot.event.** {*;}
-dontwarn de.greenrobot.event.**
-keepclassmembers class ** {
    public void onEvent*(**);
}


-keep class com.morgoo.** { *; }
-dontwarn com.morgoo.**

-keep class android.util.Singleton { *; }


-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

-keepclasseswithmembers public class com.morgoo.hook.NativeHelper {
      *** nativeHandleHookedMethod(...);
      *** nativeHa(...);
      *** nativeHb(...);
      *** nativeHc(...);
      *** nativeHd(...);
      *** nativeHe(...);
}