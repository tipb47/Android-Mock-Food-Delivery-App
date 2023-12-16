package ttbrowne.c323.finalandroidproject

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.util.logging.Handler

class DeliveryService : Service() {

    companion object {
        const val CHANNEL_ID = "food_delivery"
        const val CHANNEL_NAME = "Food Delivery App"
        const val NOTIFICATION_ID = 1
    }

    private val handler = android.os.Handler()
    private lateinit var runnable: Runnable

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    //create channel to ensure popup
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Delivery notification!"
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    //start notiofication
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val deliveryTime = intent.getLongExtra("deliveryTime", 0L)
        runnable = Runnable {
            if (deliveryTime > 0) {
                // generate notification
                generateNotification()
                stopSelf()
            }
        }
        handler.postDelayed(runnable, deliveryTime * 60 * 1000)
        return START_NOT_STICKY
    }

    private fun generateNotification() {
        //setup notification
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Delivery Update")
            .setContentText("Your order has been delivered!")
            .setSmallIcon(R.drawable.notification)
        // other notification properties
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }
}