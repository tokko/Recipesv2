package com.tokko.recipesv2;

import android.content.Intent;

import com.tokko.recipesv2.masterdetail.ItemDetailActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/app/src/main/AndroidManifest.xml")
public class ItemDetailActivityTests {

    @Before
    public void setUp() throws Exception {

        Intent i = new Intent(RuntimeEnvironment.application, ItemDetailActivity.class);
    }

    @Test
    public void testDummy() throws Exception {

    }
}
