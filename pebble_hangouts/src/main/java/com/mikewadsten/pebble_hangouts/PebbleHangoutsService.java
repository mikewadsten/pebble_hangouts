package com.mikewadsten.pebble_hangouts;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mike on 6/8/2014.
 */
public class PebbleHangoutsService extends NotificationListenerService {
    private static final String HANGOUTS_PKG_NAME = "com.google.android.talk";
    private static final String SEND_TO_PEBBLE = "com.getpebble.action.SEND_NOTIFICATION";

    public static String TAG = "PHFService";

    private static boolean isEnabled = false;

    @Override
    public IBinder onBind(Intent intent) {
        isEnabled = true;

        Log.d(TAG, "onBind");
        return super.onBind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isEnabled = false;

        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    public static final boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (HANGOUTS_PKG_NAME.equalsIgnoreCase(sbn.getPackageName())) {
            final Intent i = new Intent(SEND_TO_PEBBLE);

            Notification n = sbn.getNotification();
            Bundle nExtras = n.extras;

            String sender = nExtras.getString(Notification.EXTRA_TITLE);
            String text = nExtras.getString(Notification.EXTRA_TEXT);

            final Map data = new HashMap();
            data.put("title", sender);
            data.put("body", text);

            final JSONObject jsonData = new JSONObject(data);
            final String nData = new JSONArray().put(jsonData).toString();

            i.putExtra("messageType", "PEBBLE_ALERT");
            i.putExtra("sender", "Pebble Hangouts Fix");
            i.putExtra("notificationData", nData);

            sendBroadcast(i);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {}
}