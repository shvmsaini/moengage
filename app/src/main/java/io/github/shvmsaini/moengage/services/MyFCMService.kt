package io.github.shvmsaini.moengage.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.github.shvmsaini.moengage.R
import io.github.shvmsaini.moengage.activities.MainActivity

class MyFCMService : FirebaseMessagingService() {
    var uri = ""
    var message = ""
    var title = ""
    var imageUrl = ""
    var data = ""

    companion object {
        private val TAG = MyFCMService::class.java.simpleName
        private const val CHANNEL_ID = "123"
        private const val CHANNEL_ID_INT = 123
    }

    override fun onMessageReceived(message: RemoteMessage) {
        init(message)

        createNotificationChannel()
        indentNotification()
        super.onMessageReceived(message)
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "onNewToken: $token")
        super.onNewToken(token)
    }

    fun init(remoteMessage: RemoteMessage) {
        Log.d(TAG, "init: remoteMessage.getNotification(): " + remoteMessage.notification)
        Log.d(TAG, "init: remoteMessage.getData(): " + remoteMessage.data)

        // Notification Type
        if (remoteMessage.notification != null) {
            title = remoteMessage.notification!!.title!!
            message = remoteMessage.notification!!.body!!
            imageUrl = remoteMessage.notification!!.imageUrl.toString()
        }

        // Data type
        val map = remoteMessage.data
        data = map.toString()
        if (map.containsKey("message")) message = map["message"]!!
        if (map.containsKey("title")) title = map["title"]!!
        if (map.containsKey("uri")) uri = map["uri"]!!
        if (map.containsKey("imageUrl")) imageUrl = map["imageUrl"]!!
    }

    fun indentNotification() {
        val bundle = Bundle()
        bundle.putBoolean("fromNotif", true)
        bundle.putString("uri", uri)
        bundle.putString("data", data)

        val intent = Intent(this, MainActivity::class.java)
        intent.data = Uri.parse(uri)
        intent.putExtras(bundle)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntentWithParentStack(intent)
        val pendingIntent = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = Notification()
        notification.defaults = notification.defaults or Notification.DEFAULT_SOUND
        notification.defaults = notification.defaults or Notification.DEFAULT_VIBRATE

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setVibrate(longArrayOf(1000, 1000))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        val notificationManager = NotificationManagerCompat.from(this)
        NotificationManagerCompat.from(applicationContext).cancel(
            "notificationId",
            CHANNEL_ID_INT
        )
//        if (imageUrl != null || !imageUrl.isBlank()) {
//            Glide.with(applicationContext)
//                .asBitmap()
//                .load(imageUrl)
//                .into(object : CustomTarget<Bitmap?>() {
//                    fun onLoadFailed(errorDrawable: Drawable?) {
//                        notificationManager.notify(CHANNEL_ID_INT, builder.build())
//                    }
//
//                    fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
//                        builder.setLargeIcon(resource)
//                        builder.setStyle(
//                            NotificationCompat.BigPictureStyle().bigPicture(resource)
//                                .bigLargeIcon(null)
//                        )
//                        notificationManager.notify(CHANNEL_ID_INT, builder.build())
//                    }
//
//                    fun onLoadCleared(placeholder: Drawable?) {}
//                })
//        } else {
//    }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS ) != PackageManager.PERMISSION_GRANTED ) {
            return
        } else {
            with(notificationManager) { notify(CHANNEL_ID_INT, builder.build()) }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Updates"
            val description = "Moengage updates"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                CHANNEL_ID,
                name,
                importance
            )
            channel.description = description
            channel.vibrationPattern = longArrayOf(1000, 1000, 500, 1000)
            channel.enableVibration(true)
            channel.enableLights(true)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            channel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, audioAttributes)
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}