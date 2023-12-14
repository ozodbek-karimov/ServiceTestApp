package pl.ozodbek.newtestapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import androidx.core.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import pl.ozodbek.newtestapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val notificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.notificationButton.setOnClickListener {

            val channelId = "channelId"
            val notificationId = 1
            val EXTRA_TEXT_REPLY = "key"

            val intent = Intent(this@MainActivity, MyService::class.java)
            intent.action = "ACTION_REPLY"
            intent.putExtra("id", notificationId)

            val pendingIntent =
                PendingIntent.getService(this@MainActivity, 1, intent, PendingIntent.FLAG_MUTABLE )

            val remoteInput = RemoteInput.Builder(EXTRA_TEXT_REPLY)
                .setLabel("Send message..")
                .build()

            val action = NotificationCompat.Action.Builder(
                android.R.drawable.ic_menu_send,
                "Reply",
                pendingIntent
            ).addRemoteInput(remoteInput).build()

            val notificationBuilder = NotificationCompat.Builder(this@MainActivity, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Notification title")
                .setContentText("Notification content text")
                .addAction(action)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Channel name"
                val descriptionText = "Channel description"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("channelId", name, importance).apply {
                    description = descriptionText
                }
                notificationManager.createNotificationChannel(channel)
            }

            val notification = notificationBuilder.build()
            notificationManager.notify(notificationId, notification)

        }

        binding.notificationDeleteButton.setOnClickListener {
            val notificationId = 1 // Use the same notification ID to cancel
            notificationManager.cancel(notificationId)
        }
    }
}
