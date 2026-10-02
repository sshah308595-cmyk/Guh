package com.example.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R
import com.example.model.OrderState

object StreetoNotificationHelper {

    const val CHANNEL_ID = "streeto_order_updates_channel"
    private const val CHANNEL_NAME = "Streeto Order Tracking"
    private const val CHANNEL_DESCRIPTION = "Real-time status updates for your street food orders"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
                enableLights(true)
                lightColor = 0xFFE23744.toInt()
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showOrderStatusNotification(
        context: Context,
        orderId: String,
        vendorName: String,
        state: OrderState,
        driverName: String? = null
    ) {
        createNotificationChannel(context)

        // Check permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission not granted yet, skip system tray notification gracefully
                return
            }
        }

        val (title, contentText) = when (state) {
            OrderState.CONFIRMED -> Pair(
                "Order Confirmed! 👨‍🍳",
                "$vendorName has accepted your order #$orderId and is queuing your fresh food."
            )
            OrderState.PREPARING -> Pair(
                "Cooking at the Stall! 🥘",
                "The chef is frying and seasoning your authentic street feast right now."
            )
            OrderState.OUT_FOR_DELIVERY -> Pair(
                "Out for Delivery! 🛵",
                "${driverName ?: "Delivery partner"} has picked up your order and is heading to your doorstep."
            )
            OrderState.ARRIVED -> Pair(
                "Food Arrived at Doorstep! 😋",
                "Hot & crispy street food is waiting for you. Grab your meal and enjoy!"
            )
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("order_id", orderId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            orderId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val largeIcon = try {
            BitmapFactory.decodeResource(context.resources, R.drawable.ic_streeto_logo_v2_1790929092412)
        } catch (e: Exception) {
            null
        }

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_order)
            .setContentTitle(title)
            .setContentText(contentText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setColor(0xFFE23744.toInt()) // Streeto Red
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 300, 150, 300))

        if (largeIcon != null) {
            notificationBuilder.setLargeIcon(largeIcon)
        }

        val notificationId = (orderId.hashCode() and 0x7FFFFFFF) + state.stepIndex
        try {
            NotificationManagerCompat.from(context).notify(notificationId, notificationBuilder.build())
        } catch (e: SecurityException) {
            // Handled safely
        }
    }
}
