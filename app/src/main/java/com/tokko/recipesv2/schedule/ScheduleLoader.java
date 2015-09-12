package com.tokko.recipesv2.schedule;

import android.content.Context;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ScheduleEntry;
import com.tokko.recipesv2.masterdetail.AbstractLoader;
import com.tokko.recipesv2.util.TimeUtils;

import java.io.IOException;
import java.util.List;

public class ScheduleLoader extends AbstractLoader<ScheduleEntry>{

    private final TimeUtils timeUtils;

    @Inject
    public ScheduleLoader(Context context, TimeUtils timeUtils) {
        super(context, ScheduleEntry.class);
        this.timeUtils = timeUtils;
    }

    @Override
    public List<ScheduleEntry> loadInBackground() {
        try {
            return api.getSchedule(timeUtils.getCurrentTime().getMillis()).execute().getItems();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
