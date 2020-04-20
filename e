[1mdiff --git a/.idea/gradle.xml b/.idea/gradle.xml[m
[1mindex 440480e..5cd135a 100644[m
[1m--- a/.idea/gradle.xml[m
[1m+++ b/.idea/gradle.xml[m
[36m@@ -1,5 +1,6 @@[m
 <?xml version="1.0" encoding="UTF-8"?>[m
 <project version="4">[m
[32m+[m[32m  <component name="GradleMigrationSettings" migrationVersion="1" />[m
   <component name="GradleSettings">[m
     <option name="linkedExternalProjectsSettings">[m
       <GradleProjectSettings>[m
[1mdiff --git a/app/src/main/AndroidManifest.xml b/app/src/main/AndroidManifest.xml[m
[1mindex 5d42015..b9a9f69 100644[m
[1m--- a/app/src/main/AndroidManifest.xml[m
[1m+++ b/app/src/main/AndroidManifest.xml[m
[36m@@ -9,7 +9,8 @@[m
         android:roundIcon="@mipmap/ic_launcher_round"[m
         android:supportsRtl="true"[m
         android:theme="@style/AppTheme">[m
[31m-        <activity android:name=".MainActivity">[m
[32m+[m[32m        <activity android:name=".BlankActivity"></activity>[m
[32m+[m[32m        <activity android:name=".SplashScreenActivity">[m
             <intent-filter>[m
                 <action android:name="android.intent.action.MAIN" />[m
 [m
[1mdiff --git a/app/src/main/java/com/daytoday/business/dailydelivery/BlankActivity.java b/app/src/main/java/com/daytoday/business/dailydelivery/BlankActivity.java[m
[1mindex 0681744..c38b6f6 100644[m
[1m--- a/app/src/main/java/com/daytoday/business/dailydelivery/BlankActivity.java[m
[1m+++ b/app/src/main/java/com/daytoday/business/dailydelivery/BlankActivity.java[m
[36m@@ -1,6 +1,7 @@[m
 package com.daytoday.business.dailydelivery;[m
 [m
 import androidx.appcompat.app.AppCompatActivity;[m
[32m+[m
 import android.os.Bundle;[m
 [m
 public class BlankActivity extends AppCompatActivity {[m
[1mdiff --git a/app/src/main/java/com/daytoday/business/dailydelivery/SplashScreenActivity.java b/app/src/main/java/com/daytoday/business/dailydelivery/SplashScreenActivity.java[m
[1mindex 5dbe46d..78ca302 100644[m
[1m--- a/app/src/main/java/com/daytoday/business/dailydelivery/SplashScreenActivity.java[m
[1m+++ b/app/src/main/java/com/daytoday/business/dailydelivery/SplashScreenActivity.java[m
[36m@@ -7,6 +7,7 @@[m [mimport android.os.Bundle;[m
 import android.os.Handler;[m
 [m
 public class SplashScreenActivity extends AppCompatActivity {[m
[32m+[m[32m    private static int SPLASH_SCREEN_TIME = 300; /*This is the Splash screen time which is 3 seconds*/[m
 [m
     @Override[m
     protected void onCreate(Bundle savedInstanceState) {[m
[1mdiff --git a/app/src/main/res/layout/activity_splash_screen.xml b/app/src/main/res/layout/activity_splash_screen.xml[m
[1mindex 1da30a9..59062c0 100644[m
[1m--- a/app/src/main/res/layout/activity_splash_screen.xml[m
[1m+++ b/app/src/main/res/layout/activity_splash_screen.xml[m
[36m@@ -4,6 +4,30 @@[m
     xmlns:tools="http://schemas.android.com/tools"[m
     android:layout_width="match_parent"[m
     android:layout_height="match_parent"[m
[32m+[m[32m    android:background="@color/colorAccent"[m
     tools:context=".SplashScreenActivity">[m
 [m
[32m+[m[32m    <ImageView[m
[32m+[m[32m        android:id="@+id/imageView"[m
[32m+[m[32m        android:layout_width="wrap_content"[m
[32m+[m[32m        android:layout_height="wrap_content"[m
[32m+[m[32m        app:layout_constraintBottom_toBottomOf="parent"[m
[32m+[m[32m        android:src="@drawable/logo_launcher"[m
[32m+[m[32m        app:layout_constraintEnd_toEndOf="parent"[m
[32m+[m[32m        app:layout_constraintStart_toStartOf="parent"[m
[32m+[m[32m        app:layout_constraintTop_toTopOf="parent"[m
[32m+[m[32m        app:layout_constraintVertical_bias="0.35" />[m
[32m+[m
[32m+[m[32m    <TextView[m
[32m+[m[32m        android:layout_width="wrap_content"[m
[32m+[m[32m        android:layout_height="wrap_content"[m
[32m+[m[32m        android:text="@string/buisness"[m
[32m+[m[32m        android:textSize="20sp"[m
[32m+[m[32m        android:textColor="@color/colorWhite"[m
[32m+[m[32m        app:layout_constraintBottom_toBottomOf="parent"[m
[32m+[m[32m        app:layout_constraintEnd_toEndOf="parent"[m
[32m+[m[32m        app:layout_constraintStart_toStartOf="parent"[m
[32m+[m[32m        app:layout_constraintTop_toBottomOf="@+id/imageView"[m
[32m+[m[32m        app:layout_constraintVertical_bias="0.70" />[m
[32m+[m
 </androidx.constraintlayout.widget.ConstraintLayout>[m
\ No newline at end of file[m
[1mdiff --git a/app/src/main/res/values/colors.xml b/app/src/main/res/values/colors.xml[m
[1mindex f2b6129..5d72ed4 100644[m
[1m--- a/app/src/main/res/values/colors.xml[m
[1m+++ b/app/src/main/res/values/colors.xml[m
[36m@@ -9,4 +9,5 @@[m
     <color name="colorReject">#F4B8B8</color>[m
     <color name="colorPending">#F2EF91</color>[m
     <color name="colorGray">#777777</color>[m
[32m+[m[32m    <color name="colorWhite">#FFFFFF</color>[m
 </resources>[m
[1mdiff --git a/app/src/main/res/values/strings.xml b/app/src/main/res/values/strings.xml[m
[1mindex 1bc6175..c05af5d 100644[m
[1m--- a/app/src/main/res/values/strings.xml[m
[1m+++ b/app/src/main/res/values/strings.xml[m
[36m@@ -1,3 +1,4 @@[m
 <resources>[m
     <string name="app_name">DailyDelivery</string>[m
[32m+[m[32m    <string name="buisness">Buisness</string>[m
 </resources>[m
[1mdiff --git a/app/src/main/res/values/styles.xml b/app/src/main/res/values/styles.xml[m
[1mindex 5885930..0eb88fe 100644[m
[1m--- a/app/src/main/res/values/styles.xml[m
[1m+++ b/app/src/main/res/values/styles.xml[m
[36m@@ -1,7 +1,7 @@[m
 <resources>[m
 [m
     <!-- Base application theme. -->[m
[31m-    <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">[m
[32m+[m[32m    <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">[m
         <!-- Customize your theme here. -->[m
         <item name="colorPrimary">@color/colorPrimary</item>[m
         <item name="colorPrimaryDark">@color/colorPrimaryDark</item>[m
