-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature

-keepclassmembers  class com.gh.sammie.ghanawater.model.**{*; }
-keepnames  class com.gh.sammie.ghanawater.model.**{*; }
 -keepclassmembers  class com.apps.norris.paywithslydepay.models.**{*; }
 -keepnames  class com.apps.norris.paywithslydepay.models.**{*; }

-keep class com.shockwave.**
-keepclasseswithmembers public class com.flutterwave.raveandroid.** { *; }
-dontwarn com.flutterwave.raveandroid.card.CardFragment
-keep class cn.pedant.SweetAlert.** { *; }
-keep public class * extends java.lang.Exception
