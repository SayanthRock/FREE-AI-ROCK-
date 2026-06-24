# FREE-AI-ROCK release rules.
# These rules protect Retrofit/Gson API models and AI SDK calls from release-only R8 issues.

# Keep Gson serialized fields stable for GitHub and image-provider models.
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

-keep class com.sayanthrock.freeairock.data.github.** { *; }
-keep class com.sayanthrock.freeairock.data.image.** { *; }
-keep class com.sayanthrock.freeairock.data.ai.** { *; }

# Retrofit interfaces are accessed through generated proxies.
-keep interface com.sayanthrock.freeairock.data.github.GitHubApiService { *; }
-keep interface com.sayanthrock.freeairock.data.image.ImageToolApi { *; }

# Retrofit / OkHttp / Gson compatibility.
-keepattributes Signature
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault

-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**

# Kotlin metadata helps reflection-heavy libraries and suspend functions.
-keep class kotlin.Metadata { *; }

# Google AI SDK model classes can rely on generated/serialized internals.
-keep class com.google.ai.client.generativeai.** { *; }
-dontwarn com.google.ai.client.generativeai.**

# Preserve AndroidX Security Crypto classes used by EncryptedSharedPreferences.
-keep class androidx.security.crypto.** { *; }
-dontwarn androidx.security.crypto.**
