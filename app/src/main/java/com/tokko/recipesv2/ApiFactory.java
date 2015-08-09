package com.tokko.recipesv2;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

import java.lang.reflect.InvocationTargetException;

public class ApiFactory {

    public static GoogleAccountCredential credential;

    public static <T extends AbstractGoogleJsonClient.Builder> AbstractGoogleJsonClient createApi(Class<T> clz) {
        try {
            return clz.getConstructor(HttpTransport.class, JsonFactory.class, HttpRequestInitializer.class)
                    .newInstance(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), credential)
                    .setRootUrl(getRootUrl()).setApplicationName("recipesv2").build();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getRootUrl() {
        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("release"))
            return ""; //TODO: release backend url
        return "http://192.168.2.13:8080/_ah/api/";
    }
}
