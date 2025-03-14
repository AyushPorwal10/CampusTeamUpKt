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
import kotlinx.coroutines.launch
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

        val title = remoteMessage.notification?.title ?: "New Notification"
        val message = remoteMessage.notification?.body ?: "You have a new message"
        val senderId = remoteMessage.data["userId"] ?: ""
        val senderName = remoteMessage.data["userName"] ?: ""
        val notificationSentOn = remoteMessage.data["time"] ?: ""

        Log.d("Testing","Time is $notificationSentOn")
        Log.d("FCM"," OnMessageReceived title $title useId $senderId userName $senderName")
        Log.d("Open","sender id is $senderId and name is $senderName")
        sendNotification(title, message, senderId, senderName)
    }

    private fun sendNotification(title: String, message: String, userId: String, userName: String) {

        // to open the notification section of
        val intent = Intent(this, DrawerItemActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("DrawerItem","notifications") // this will open the notification section
            putExtra("userId", userId)
            putExtra("userName", userName)
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
                Log.d("FCM", "Saving FCM Token for user: $userId")
                homeScreenRepository.saveFcmToken(token, userId)
                Log.d("FCM", "New token saved successfully")
            } catch (e: HttpException) {
                Log.e("FCM", "HTTP error while saving token: ${e.code()} - ${e.message()}", e)
                Log.e("FCM", "Response error body: ${e.response()?.errorBody()?.string()}")
            } catch (e: Exception) {
                Log.e("FCM", "Failed to save new token", e)
            }
        }
    }
}