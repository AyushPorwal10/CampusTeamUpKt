
-keep class com.example.new_campus_teamup.mydataclass.** {
    <init>();
    *;
}


-keep class com.example.new_campus_teamup.myrepository.** { *; }
-keep class com.example.new_campus_teamup.viewmodels.** { *; }

-keep class com.example.new_campus_teamup.notification.** { *; }

-keep class dagger.** { *; }
-keep interface dagger.** { *; }
-keep class javax.inject.** { *; }
-keep class dagger.hilt.** { *; }

-keep class com.example.new_campus_teamup.modules.** { *; }


-dontwarn com.google.firebase.ktx.Firebase

-keep class com.google.firebase.** { *; }

-keep class retrofit2.** { *; }
-keep class com.google.gson.** { *; }




                           #SOLUTION BY GITHUB

 # Keep Kotlin metadata (required for Kotlin reflection/coroutines)
 -keep class kotlin.Metadata { *; }

 # Keep Continuation class used in suspend functions
 -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

 # Retrofit - keep API interfaces with annotations
 -if interface * { @retrofit2.http.* <methods>; }
 -keep interface <1>

 # Retrofit response type
 -keep class retrofit2.Response { *; }

 # Gson (used by Retrofit converter or manually)
 -keep class com.google.gson.** { *; }
 -keepattributes Signature
 -keepattributes *Annotation*
 -keep class com.example.new_campus_teamup.model.** { *; }
 -keep class com.example.new_campus_teamup.remote.FcmMessage { *; }

 # Firebase Firestore - keep Firestore models
 -keep class com.google.firebase.firestore.** { *; }
 -keepattributes Signature
 -keepattributes *Annotation*

 # Keep your FCM service & models
 -keep class com.example.new_campus_teamup.remote.FCMApiService { *; }
 -keep class com.example.new_campus_teamup.remote.FcmMessage { *; }


