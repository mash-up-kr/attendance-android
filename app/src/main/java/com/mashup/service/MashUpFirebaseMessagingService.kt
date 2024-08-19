package com.mashup.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mashup.BuildConfig
import com.mashup.R
import com.mashup.constant.EXTRA_LINK
import com.mashup.constant.log.LOG_ALARM_LIST
import com.mashup.constant.log.LOG_SIGN_UP
import com.mashup.ui.splash.SplashActivity
import com.mashup.util.AnalyticsManager
import dagger.hilt.android.AndroidEntryPoint
import java.net.URL

@AndroidEntryPoint
class MashUpFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        if (BuildConfig.DEBUG) {
            Log.d(TAG, token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title
        val body = message.notification?.body
        val imageUrl = message.notification?.imageUrl

        if (title != null && body != null) {
            createNotificationChannel()
            notifyPushMessage(
                title = title,
                body = body,
                imageUrl = imageUrl,
                data = message.data
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun notifyPushMessage(
        title: String,
        body: String,
        imageUrl: Uri?,
        data: Map<String, String>
    ) {
        AnalyticsManager.addEvent(
            eventName = LOG_ALARM_LIST,
            params = bundleOf(
                "place" to "PUSH",
                "type" to PushLinkType.getPushLinkType(data[EXTRA_LINK].orEmpty()).name
            )
        )
        val splashIntent = Intent(this, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_LINK, data[EXTRA_LINK])
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            splashIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuild = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        // 이미지가 전달된 경우
        getBitmapFromUrl(imageUrl)?.let { bitmapImage ->
            notificationBuild
                .setLargeIcon(bitmapImage)
                .setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmapImage)
                )
        }

        NotificationManagerCompat.from(this)
            .notify(NOTIFICATION_ID++, notificationBuild.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Android O 이상부터 NotificationChannel 생성 필요
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            manager.createNotificationChannel(channel)
        }
    }

    private fun getBitmapFromUrl(imageUrl: Uri?): Bitmap? {
        return try {
            val url = URL(imageUrl?.toString())
            BitmapFactory.decodeStream(url.openConnection().inputStream)
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val TAG = "MashUpFirebaseMessagingService"

        private const val CHANNEL_ID = "MashUpNotificationChannel"
        private const val CHANNEL_NAME = "Mash-Up Notification"
        private var NOTIFICATION_ID = 1001
    }
}
