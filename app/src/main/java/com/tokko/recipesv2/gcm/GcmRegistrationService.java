package com.tokko.recipesv2.gcm;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.tokko.recipesv2.ApiFactory;
import com.tokko.recipesv2.backend.enteties.recipeUserApi.RecipeUserApi;

import java.io.IOException;

public class GcmRegistrationService extends IntentService {

    public GcmRegistrationService() {
        super(GcmRegistrationService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken("826803278070",
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            RecipeUserApi api = (RecipeUserApi) ApiFactory.createApi(RecipeUserApi.Builder.class);
            if (api != null) {
                api.insert(token).execute();
            }
            getSharedPreferences("RegistrationData", MODE_PRIVATE).edit().putString("regid", token).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
