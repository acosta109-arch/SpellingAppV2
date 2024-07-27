package com.sagrd.spellingappv2

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SpellingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CounterNotificationService.COUNTER_CHANNEL_ID,
                "Counter",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Used for the increment counter notifications"

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
class CounterNotificationService(
    private val context: Context
) {

    private val notificationManager =context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(counter:Int){
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            201,
            activityIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val incrementIntent = PendingIntent.getBroadcast(
            context,
            204,
            Intent(context, CounterNotificationReceiver::class.java),
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val notification  = NotificationCompat.Builder(context, COUNTER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_360_24)
            .setContentTitle("Increment counter")
            .setContentText("the counter is: $counter")
            .setContentIntent(activityPendingIntent)
            .addAction(
                R.drawable.ic_baseline_360_24,
                "Increment",
                incrementIntent
            ).build()


        notificationManager.notify(
            1,
            notification
        )
    }

    companion object {
        const val COUNTER_CHANNEL_ID = "counter_channel"
    }
}

object Counter {
    var value = 0
}

class CounterNotificationReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent?) {
         val service = CounterNotificationService(context)

        service.showNotification(++Counter.value)
    }
}