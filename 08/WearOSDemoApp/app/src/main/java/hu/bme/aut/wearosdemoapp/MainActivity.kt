package hu.bme.aut.wearosdemoapp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import hu.bme.aut.wearosdemoapp.ui.theme.WearOSDemoAppTheme
import java.util.Date

class MainActivity : ComponentActivity() {
    companion object {
        const val NOTIFICATION_CHANNEL_ID = "my_wear_notifications"
        const val NOTIFICATION_CHANNEL_NAME = "My Wear notifications"
        const val EXTRA_VOICE_REPLY = "extra_voice_reply"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val remoteResult = getRemoteMessageText(intent)
        if (remoteResult != null) {
            Toast.makeText(applicationContext, remoteResult, Toast.LENGTH_LONG).show()
        }

        setContent {
            WearOSDemoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WearNotificationDemo(remoteResult)
                }
            }
        }
    }

    private fun getRemoteMessageText(intent: Intent): String {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        return if (remoteInput != null) {
            remoteInput.getCharSequence(MainActivity.EXTRA_VOICE_REPLY).toString()
        } else ""
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WearNotificationDemo(remoteResult: String) {
    val context = LocalContext.current

    val notifPermissionState = rememberPermissionState(
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    Column {
        Text(text = remoteResult)
        if (notifPermissionState.status.isGranted) {
            Button(onClick = {
                //showBaiscNotifTime(context)
                //showNotifOrderMap(context)
                showNotifOrderMapWithChoices(context)
            }) {
                Text(text = "Wear notification demo")
            }
        } else {
            val textToShow = if (notifPermissionState.status.shouldShowRationale) {
                "The notification is important for this app. Please grant the permission."
            } else {
                "Notification permission required for this feature to be available. " +
                        "Please grant the permission"
            }
            Text(textToShow)
            Button(onClick = { notifPermissionState.launchPermissionRequest() }) {
                Text("Request permission")
            }
        }


    }

}

@SuppressLint("MissingPermission")
private fun showBaiscNotifTime(context: Context) {
    val notificationId = 1

    // Build intent for notification content
    val viewPendingIntent = Intent(context, MainActivity::class.java).let { viewIntent ->
        viewIntent.putExtra("EXTRA_EVENT_ID", 101)
        PendingIntent.getActivity(context, 0, viewIntent, PendingIntent.FLAG_IMMUTABLE)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createNotificationChannel(context)
    }

    // Notification channel ID is ignored for Android 7.1.1
    // (API level 25) and lower.
    val notificationBuilder = NotificationCompat.Builder(context, MainActivity.NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.mydroid)
        .setContentTitle("Hello Andorid Demo")
        .setContentText(Date(System.currentTimeMillis()).toString())
        .setContentIntent(viewPendingIntent)

    NotificationManagerCompat.from(context).apply {
        notify(notificationId, notificationBuilder.build())
    }
}

@SuppressLint("MissingPermission")
private fun showNotifOrderMap(context: Context) {
    val notificationId = 1

    val mapIntent = Intent(Intent.ACTION_VIEW)
    val mapPendingIntent = Intent(Intent.ACTION_VIEW).let { mapIntent ->
        //mapIntent.data = Uri.parse("geo:0,0?q=Budapest")
        mapIntent.data = Uri.parse("waze://?q=BME&navigate=yes")
        PendingIntent.getActivity(context, 0, mapIntent,  PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createNotificationChannel(context)
    }

    val bigStyle = NotificationCompat.BigTextStyle()
    bigStyle.bigText(
        "You have a new order from our company. Please decide how you would like to pick up the item.")

    // Notification channel ID is ignored for Android 7.1.1
    // (API level 25) and lower.
    val notificationBuilder = NotificationCompat.Builder(context, MainActivity.NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.mydroid)
        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.truck))
        .setContentTitle("Order details")
        .setContentText(Date(System.currentTimeMillis()).toString())
        .addAction(R.drawable.truck, "Show store", mapPendingIntent)
        .setColor(Color.Green.toArgb())
        .setStyle(bigStyle)

    NotificationManagerCompat.from(context).apply {
        notify(notificationId, notificationBuilder.build())
    }
}


@SuppressLint("MissingPermission")
private fun showNotifOrderMapWithChoices(context: Context) {
    val notificationId = 1

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createNotificationChannel(context)
    }

    val bigStyle = NotificationCompat.BigTextStyle()
    bigStyle.bigText("You have a new order from our company. Please decide how you would like to pick up the item.")

    //-- Expanded notification choices
    val orderPendingIntent = Intent(context, MainActivity::class.java).let { orderPendingIntent ->
        orderPendingIntent.putExtra("EXTRA_ORDER_ID",
            101)
        PendingIntent.getActivity(context, 0,
            orderPendingIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
    }

    val mapIntent = Intent(Intent.ACTION_VIEW)
    val mapPendingIntent = Intent(Intent.ACTION_VIEW).let { mapIntent ->
        //mapIntent.data = Uri.parse("geo:0,0?q=Budapest")
        mapIntent.data = Uri.parse("waze://?q=BME&navigate=yes")
        PendingIntent.getActivity(context, 0, mapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
    }

    val replyLabel = "Please reply"
    val replyChoices = listOf("Home","Remote","Cancel")

    val remoteInput = RemoteInput.Builder(MainActivity.EXTRA_VOICE_REPLY)
        .setLabel(replyLabel)
        .setChoices(replyChoices.toTypedArray())
        .build()

    val action = NotificationCompat.Action.Builder(
        R.drawable.truck,
        "Delivery", orderPendingIntent
    ).addRemoteInput(remoteInput).build()

    val actionMap = NotificationCompat.Action.Builder(
        R.drawable.mydroid,
        "Store location", mapPendingIntent
    ).build()

    val wearableExtender =
        NotificationCompat.WearableExtender()
            .addAction(action)
            .addAction(actionMap)
    //---

    // Notification channel ID is ignored for Android 7.1.1
    // (API level 25) and lower.
    val notificationBuilder = NotificationCompat.Builder(context, MainActivity.NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.mydroid)
        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.truck))
        .setContentTitle("Order details")
        .setContentText(Date(System.currentTimeMillis()).toString())
        .setColor(Color.Green.toArgb())
        .setStyle(bigStyle)
        .extend(wearableExtender)

    NotificationManagerCompat.from(context).apply {
        notify(notificationId, notificationBuilder.build())
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun createNotificationChannel(context: Context) {
    val channel = NotificationChannel(
        MainActivity.NOTIFICATION_CHANNEL_ID,
        MainActivity.NOTIFICATION_CHANNEL_NAME,
        NotificationManager.IMPORTANCE_DEFAULT)

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

    notificationManager?.createNotificationChannel(channel)
}
