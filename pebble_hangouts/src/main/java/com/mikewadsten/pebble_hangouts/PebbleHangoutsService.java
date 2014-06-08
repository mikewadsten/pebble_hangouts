package com.mikewadsten.pebble_hangouts;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

            String sender, text;
            Object textObj;

            sender = nExtras.getString(Notification.EXTRA_TITLE);
            if (TextUtils.isEmpty(sender)) {
                sender = "(No title)";
            }

            boolean isPicture, isInbox;

            // Let's figure out what kind of notification this is.
            if (nExtras.containsKey(Notification.EXTRA_TEXT_LINES)) {
                // Since it explicitly has multiple lines, it's InboxStyle.
                isInbox = true;
                isPicture = false;
            } else if (nExtras.containsKey(Notification.EXTRA_PICTURE)) {
                // Since it has a picture, it's BigPictureStyle.
                isPicture = true;
                isInbox = false;
            } else {
                // It's a normal notification, or BigTextStyle.
                isInbox = isPicture = false;
            }

            if (isInbox) {
                // Join the lines.
                text = TextUtils.join("\n", nExtras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES));
            } else if (isPicture) {
                // Say this is a picture, and add on the summary text (if any).
                String summary = nExtras.getString(Notification.EXTRA_SUMMARY_TEXT);
                if (TextUtils.isEmpty(summary)) {
                    text = "[Picture]";
                } else {
                    text = String.format("[Picture]\n%s", summary);
                }
            } else {
                // Treat it as a normal (or BigTextStyle) notification.
                textObj = nExtras.get(Notification.EXTRA_TEXT);
                if (textObj instanceof String || textObj == null) {
                    text = (String) textObj;
                    text = TextUtils.isEmpty(text) ? "[empty]" : text;
                } else {
                    // If textObj is a SpannableString, toString does what we want.
                    // If it's anything else, let's just toString it and see what we get.
                    text = textObj.toString();
                }
            }

            final Map<String, String> data = new HashMap<String, String>();
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
