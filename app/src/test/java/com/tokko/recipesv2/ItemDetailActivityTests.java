package com.tokko.recipesv2;

import android.content.Intent;
import android.widget.TextView;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonString;
import com.google.api.client.util.Key;
import com.tokko.recipesv2.backend.entities.groceryApi.model.Grocery;
import com.tokko.recipesv2.masterdetail.ItemDetailActivity;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/app/src/main/AndroidManifest.xml")
public class ItemDetailActivityTests {

    private ItemDetailActivity activity;
    private Grocery g;

    @Before
    public void setUp() throws Exception {
        Intent i = new Intent(RuntimeEnvironment.application, ItemDetailActivity.class);
        g = new Grocery();
        g.setId(1L);
        g.setTitle("Title");
        i.putExtra(ItemDetailFragment.EXTRA_CLASS, Grocery.class);
        i.putExtra(ItemDetailFragment.EXTRA_ENTITY, new AndroidJsonFactory().toString(g));
        activity = Robolectric.buildActivity(ItemDetailActivity.class).withIntent(i).create().start().resume().visible().get();
    }

    @Test
    public void testGroceryDetails_TitleDisplayed() throws Exception {
        String s = ((TextView) activity.findViewById(R.id.grocery_title)).getText().toString();
        assertEquals(g.getTitle(), s);
    }

    private class Tester extends GenericJson {
        @Key
        @JsonString
        private Long id;
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
}
