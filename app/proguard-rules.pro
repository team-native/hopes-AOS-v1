# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Navigation 3 NavKeySerializer는 back stack 키의 클래스 binary name을 저장하고
# Class.forName으로 복원하므로, 목적지 클래스 이름이 난독화되면 상태 복원이 깨진다.
-keepnames class com.example.hopes.navigation.HopesDestination** { *; }

# 난독화된 크래시 스택트레이스를 mapping.txt로 복원할 수 있도록 줄 번호 정보를 보존한다.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile