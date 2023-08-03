/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gpay.merchantapp.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.gpay.merchantapp.R;
import com.gpay.merchantapp.pages.login.LoginActivity;
import com.gpay.merchantapp.utils.Global;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private LocalBroadcastManager broadcastManager;

    @Override
    public void onCreate() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    public static void sendNotification(Context context, String messageBody, String channelUrl) {

        Uri defaultSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.gaja_terompet);
        //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

        final String CHANNEL_ID = "ch01";
        if (Build.VERSION.SDK_INT >= 26) {  // Build.VERSION_CODES.O
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, "CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH);
            mChannel.setSound(defaultSoundUri, attributes);
            notificationManager.createNotificationChannel(mChannel);
        }

        int notificationId = 1;
        String channelId = "ch01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);

            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        }

        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(context,
                    0, new Intent(context, MyFirebaseMessagingService.class).addFlags(
                            Intent.FLAG_ACTIVITY_SINGLE_TOP),
                    PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(context,
                    0, new Intent(context, MyFirebaseMessagingService.class).addFlags(
                            Intent.FLAG_ACTIVITY_SINGLE_TOP),
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)

                .setSmallIcon(R.mipmap.ic_launcher_round_transparent)
                .setColor(Color.parseColor("#20000000"))  // small icon background color
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setAutoCancel(true)
                .setContentText(messageBody)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentIntent(pendingIntent);

        Intent resultIntent = new Intent(context, LoginActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);

        PendingIntent resultPendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            resultPendingIntent =
                    stackBuilder.getPendingIntent(0,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        } else {
            resultPendingIntent = PendingIntent.getActivity(context,
                    0, new Intent(context, MyFirebaseMessagingService.class).addFlags(
                            Intent.FLAG_ACTIVITY_SINGLE_TOP),
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }

        notificationBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
    }
    // [END receive_message]
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: " + remoteMessage.getFrom());


        String msg = "";
        String channelUrl = "";

        Map<String, String> dataFcm = remoteMessage.getData();
        if (remoteMessage.getData() != null) {
            msg = remoteMessage.getData().get("body");
            channelUrl = remoteMessage.getData().get("type");

            broadcastManager.sendBroadcast(new Intent(Global.NOTIF_TRANSACTION));
        } else {
            if (remoteMessage.getNotification() != null) {
                try {
                    JSONObject jsonObject = new JSONObject(remoteMessage.getNotification().getBody());
                    msg = jsonObject.getString("message");
                    channelUrl = jsonObject.getString("type");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        sendNotification(this, msg, channelUrl);
    }
}