package com.tokko.recipesv2.authentication;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.tokko.recipesv2.ApiFactory;
import com.tokko.recipesv2.gcm.GcmRegistrationService;
import com.tokko.recipesv2.groceries.GroceryListActivity;

public class LoginActivity extends Activity {
    static final int REQUEST_ACCOUNT_PICKER = 2;
    private static final String PREF_ACCOUNT_NAME = "PREF_ACCOUNT_NAME";
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inside your Activity class onCreate method
        settings = getSharedPreferences(LoginActivity.class.getSimpleName(), 0);
        ApiFactory.credential = GoogleAccountCredential.usingAudience(this, "server:client_id:826803278070-0gs94ct68qhnn9b2tpi1mnuptu5al77n.apps.googleusercontent.com");
        setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));
        if (ApiFactory.credential.getSelectedAccountName() != null) {
            continueToNextActivity();
        } else {
            chooseAccount();
        }

    }

    // setSelectedAccountName definition
    private void setSelectedAccountName(String accountName) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_ACCOUNT_NAME, accountName);
        editor.apply();
        ApiFactory.credential.setSelectedAccountName(accountName);
    }

    void chooseAccount() {
        startActivityForResult(ApiFactory.credential.newChooseAccountIntent(),
                REQUEST_ACCOUNT_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                if (data != null && data.getExtras() != null) {
                    String accountName =
                            data.getExtras().getString(
                                    AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        setSelectedAccountName(accountName);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        continueToNextActivity();
                    }
                }
                break;
        }
    }

    private void continueToNextActivity() {
        startService(new Intent(this, GcmRegistrationService.class));
        startActivity(new Intent(this, GroceryListActivity.class));
        finish();
    }
}
