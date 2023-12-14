package pl.ozodbek.newtestapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput

class MyService : Service() {

    private val notificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent?.action == "ACTION_REPLY") {
            val resultFromIntent = RemoteInput.getResultsFromIntent(intent)
            val answer = resultFromIntent?.getCharSequence("key")

            startActivity(
                Intent(this@MyService, NotificationActivity::class.java)
                    .putExtra("message", answer)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            val id = intent.getIntExtra("id", 0)


            val notificationBuilder = NotificationCompat.Builder(applicationContext, "channelId")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Replied!")
                .setAutoCancel(true)


            val notification = notificationBuilder.build()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Channel name"
                val descriptionText = "Channel description"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("channelId", name, importance).apply {
                    description = descriptionText
                }
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(id, notification)
        }
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}