package com.example.campus_teamup.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.campus_teamup.R
import com.example.campus_teamup.myactivities.DrawerItemActivity
import com.example.campus_teamup.myactivities.MainActivity
import com.example.campus_teamup.myactivities.UserManager
import com.example.campus_teamup.myrepository.HomeScreenRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseMessage : FirebaseMessagingService() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var homeScreenRepository: HomeScreenRepository

    @Inject
    lateinit var userManager: UserManager

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)


        val notification = remoteMessage.notification
        val data = remoteMessage.data

        val title = notification?.title ?: "New Notification"
        val message = notification?.body ?: "You have a new message"

        val payload = data["payload"]?.let { jsonString ->
            try {
                JSONObject(jsonString)
            } catch (e: JSONException) {
                Log.e("FCM", "Error parsing payload JSON: ${e.message}")
                null
            }
        }

        val senderId = payload?.optString("senderId") ?: data["senderId"] ?: ""
        val senderName = payload?.optString("senderName") ?: data["senderName"] ?: ""
        val notificationSentOn = payload?.optString("time") ?: data["time"] ?: ""
        val phoneNumber = payload?.optString("phoneNumber") ?: data["phoneNumber"] ?: ""


        Log.d("FCM", "Received Notification - Title: $title, Message: $message")
        Log.d("FCM", "PhoneNumber is $phoneNumber , Sender ID: $senderId, Sender Name: $senderName, Time: $notificationSentOn")

        sendNotification(title, message, senderId, senderName , phoneNumber)
    }

    private fun sendNotification(title: String, message: String, userId: String, userName: String, phoneNumber : String ) {

        // to open the notification section of
        val intent = Intent(this, DrawerItemActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("DrawerItem","notifications") // this will open the notification section
            putExtra("userId", userId)
            putExtra("userName", userName)
            putExtra("phoneNumber",phoneNumber)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this,"channel_id")
            .setSmallIcon(R.drawable.notifications)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        Log.d("FCM","Notifying with notification manager")
        notificationManager.notify(0,notificationBuilder.build())

    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // storing it locally and later saving in firestore

        Log.d("FCM", "FCM is going to save locally $token <-")
        saveTokenInDataStore(token)

        sendRegistrationToServer(token)
    }

    private fun saveTokenInDataStore(token: String) {

        CoroutineScope(Dispatchers.IO).launch {

            userManager.saveUserFCM(token)
            Log.d("FCM", "FCM saved locally $token <-")

        }
    }

    private fun sendRegistrationToServer(token: String) {
        Log.d("FCM", "Going to fetch user id")
        val userId = firebaseAuth.currentUser?.uid ?: return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                userManager.userData.collectLatest {

                    Log.d("FCM", "Saving FCM Token for user: $userId")
                    homeScreenRepository.saveFcmToken(token, userId)
                    Log.d("FCM", "New token saved successfully")
                }

            } catch (e: HttpException) {
                Log.e("FCM", "HTTP error while saving token: ${e.code()} - ${e.message()}", e)
                Log.e("FCM", "Response error body: ${e.response()?.errorBody()?.string()}")
            } catch (e: Exception) {
                Log.e("FCM", "Failed to save new token", e)
            }
        }
    }
}