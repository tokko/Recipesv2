package com.tokko.recipesv2.groceries;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.tokko.recipesv2.ApiFactory;
import com.tokko.recipesv2.R;
import com.tokko.recipesv2.backend.entities.groceryApi.GroceryApi;
import com.tokko.recipesv2.backend.entities.groceryApi.model.Grocery;
import com.tokko.recipesv2.gcm.GcmRegistrationService;

import java.io.IOException;
import java.util.List;


public class GroceryListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_grocery_list);
        new Downloader().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grocery_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.register) {
            startService(new Intent(this, GcmRegistrationService.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class Downloader extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... params) {
            GroceryApi reg = (GroceryApi) ApiFactory.createApi(GroceryApi.Builder.class);
            try {
                List<Grocery> items = reg.list().execute().getItems();
                return Stream.of(items).map(Grocery::getTitle).collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> list) {
            super.onPostExecute(list);
            setListAdapter(new ArrayAdapter<>(GroceryListActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, list));
        }
    }
}
