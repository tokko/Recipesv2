package com.tokko.recipesv2.gcm;


import android.os.Bundle;
import android.widget.Toast;

public class GcmListenerService extends com.google.android.gms.gcm.GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        Toast.makeText(this, from + "\n" + data.getString("message", ""), Toast.LENGTH_SHORT).show();
    }
}
