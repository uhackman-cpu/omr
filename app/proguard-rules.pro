# Keep line numbers for debugging
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** { kotlinx.serialization.KSerializer serializer(...); }
-keep,includedescriptorclasses class com.dev.omr.**$$serializer { *; }
-keepclassmembers class com.dev.omr.** { *** Companion; }
-keepclasseswithmembers class com.dev.omr.** { kotlinx.serialization.KSerializer serializer(...); }
