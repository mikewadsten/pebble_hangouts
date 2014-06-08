package com.mikewadsten.pebble_hangouts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void goToSettings(View view) {
        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView enabledOrNot = (TextView) findViewById(R.id.enabledOrNot);

        if (PebbleHangoutsService.isEnabled()) {
            enabledOrNot.setText("App has notification access.");
        } else {
            enabledOrNot.setText("Please grant 'PHF Service' notification access.");
        }
    }
}
