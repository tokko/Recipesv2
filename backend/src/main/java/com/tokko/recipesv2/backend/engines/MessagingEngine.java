package com.tokko.recipesv2.backend.engines;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.inject.Inject;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import java.io.IOException;
import java.util.List;

public class MessagingEngine {
    public static final String API_KEY = "AIzaSyAcfYjzlHQaAuroVdB26hczjVkZ0PKqDNc";
    private RecipeUserCrudEngine registrationRA;
    private Sender sender;

    @Inject
    public MessagingEngine(RecipeUserCrudEngine recipeUserEngine, Sender sender) {
        this.registrationRA = recipeUserEngine;
        this.sender = sender;
    }

    public <T> void sendMessage(T entity, String email) {
        Message msg = new Message.Builder().addData("message", new Gson().toJson(entity)).build();
        RecipeUser recipeUser = registrationRA.getUserByEmail(email);

        List<String> records = recipeUser.getRegistrationIds();
        try {
            for (String record : records) {
                Result res = sender.send(msg, record, 5);

            /* TODO: look into this
                if(res == null) continue;
                if (res.getCanonicalRegistrationId() == null)
                    registrationRA.deleteRegistration(record);
                else if (!record.getRegId().equals(res.getCanonicalRegistrationId())) {
                    record.setRegId(res.getCanonicalRegistrationId());
                    registrationRA.saveRegistration(record);
                }
                */
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
