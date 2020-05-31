package com.sysaxiom.eventbus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import android.widget.Toast
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        publish_event.setOnClickListener {
            EventBus.getDefault().post(MessageEvent("Event Bus",input_edittext.text.toString()))
        }

    }

    override fun onStart() {
        EventBus.getDefault().register(this)
        super.onStart()
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        Toast.makeText(this, event.description, Toast.LENGTH_SHORT).show()
        displayNotification(event)
    }

    private fun displayNotification(event: MessageEvent) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channelID",
                "Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "channelID")
            .setContentTitle(event.title)
            .setContentText(event.description)
            .setSmallIcon(R.drawable.ic_android)

        notificationManager.notify(1, notification.build())
    }
}
