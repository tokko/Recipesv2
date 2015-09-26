package com.tokko.recipesv2.backend.engines;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.inject.Inject;
import com.tokko.recipesv2.backend.entities.BaseEntity;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import java.io.IOException;
import java.util.List;

public class MessagingEngine {
    public static final String API_KEY = "AIzaSyAcfYjzlHQaAuroVdB26hczjVkZ0PKqDNc";
    private Sender sender;

    @Inject
    public MessagingEngine(Sender sender) {
        this.sender = sender;
    }

    public <T extends BaseEntity<Long>> void sendMessage(T entity, RecipeUser user) {
        Message msg = new Message.Builder().addData("id", Long.toString(entity.getId())).build();

        List<String> records = user.getRegistrationIds();
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
