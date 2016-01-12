package com.appetite.voicememes;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.appetite.Designs.GridViewWithHeaderAndFooter;
import com.appetite.Designs.Helvetico;
import com.appetite.Designs.PageAdapter;
import com.appetite.Designs.Roboto;
import com.appetite.Designs.TabIndicator;
import com.appetite.adaptar.ActorsAdapter;
import com.appetite.adaptar.Messages;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

/**
 * Created by vignesh on 15/10/15.
 */
public class FirstPage extends AppCompatActivity {
    EditText searchitem;
    ArrayList<Messages> value = new ArrayList<>();
    ActorsAdapter adapter;
    GridViewWithHeaderAndFooter grid;
    private String memeid, actor, tag;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myfirstpage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        searchitem = (EditText) findViewById(R.id.searchitem);

        new Helvetico().overrideFonts(getApplicationContext(), findViewById(R.id.textView3));
        new Roboto().overrideFonts(getApplicationContext(), searchitem);
        new Helvetico().overrideFonts(getApplicationContext(), findViewById(R.id.toolbar));

        //Header view for grid with banner viewpager and options
        View header = LayoutInflater.from(this).inflate(R.layout.header, null);

        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) header.findViewById(R.id.pager);
        pager.setAdapter(new PageAdapter(getSupportFragmentManager()));

        // Bind the tabs to the ViewPager
        TabIndicator tabs = (TabIndicator) header.findViewById(R.id.tab);
        tabs.setViewPager(pager);

        grid = (GridViewWithHeaderAndFooter) findViewById(R.id.gridview);
        grid.addHeaderView(header);

        adapter = new ActorsAdapter(getApplicationContext(), value);
        grid.setAdapter(adapter);

        updateactors();

        adapter.notifyDataSetChanged();

        new getasyncmemes().execute();

        findViewById(R.id.searchbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchitem.getWindowToken(), 0);
                new getsearchitem().execute(searchitem.getText().toString(), searchitem.getText().toString(), searchitem.getText().toString(), searchitem.getText().toString());
            }
        });

        findViewById(R.id.imageView8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstPage.this, Upload.class);
                startActivity(intent);
            }
        });

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "123456");

        MaterialShowcaseView seachshow=new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.searchbutton))
                .setContentText("Search your favourite memes")
                .setDismissOnTouch(true)
                .withRectangleShape(true)
//                        .setContentTextColor(getResources().getColor(R.color.colorTab))
//                        .setMaskColour(getResources().getColor(R.color.colorYellow))
                .build();

        MaterialShowcaseView tabshow=new MaterialShowcaseView.Builder(this)
                .setTarget(tabs)
                .setDismissOnTouch(true)
                .setContentText("Display latest and trending memes")
                .withRectangleShape(true)
//                        .setContentTextColor(getResources().getColor(R.color.colorTab))
//                        .setMaskColour(getResources().getColor(R.color.colorYellow))
                .build();

        MaterialShowcaseView actorsshow=new MaterialShowcaseView.Builder(this)
                .setTarget(header.findViewById(R.id.textView))
                .setDismissOnTouch(true)
                .setContentText("Display memes based on actors")
                .withRectangleShape(true)
//                        .setContentTextColor(getResources().getColor(R.color.colorTab))
//                        .setMaskColour(getResources().getColor(R.color.colorYellow))
                .build();


        MaterialShowcaseView uploadshow=new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.imageView8))
                .setDismissOnTouch(true)
                .setContentText("Upload your memes")
//                        .setContentTextColor(getResources().getColor(R.color.colorTab))
//                        .setMaskColour(getResources().getColor(R.color.colorYellow))
                .build();

        new Helvetico().overrideFonts(getApplicationContext(),seachshow);
        new Helvetico().overrideFonts(getApplicationContext(),tabshow);
        new Helvetico().overrideFonts(getApplicationContext(),actorsshow);
        new Helvetico().overrideFonts(getApplicationContext(),uploadshow);
        new Helvetico().overrideFonts(getApplicationContext(),header.findViewById(R.id.textView));

        sequence.setConfig(config);

        sequence.addSequenceItem(seachshow);
        sequence.addSequenceItem(tabshow);
        sequence.addSequenceItem(actorsshow);
        sequence.addSequenceItem(uploadshow);

        sequence.start();
    }

    public void updateactors()
    {
        App.db.open();
        Cursor c = App.db.getactor();
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                value.add(new Messages(c.getString(1), c.getString(2), ""));
            }
            adapter.notifyDataSetChanged();
            c.close();
            App.db.close();
        } else {
            new GetActors().execute();
        }
    }

    public class getasyncmemes extends AsyncTask<String, Void, String> {
        String result;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://offersforoffer.com/voicememes/getmemes.php");
                List<NameValuePair> list = new ArrayList<NameValuePair>(2);
                post.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse response = client.execute(post);
                InputStream stream = response.getEntity().getContent();
                HttpEntity entity = client.execute(post).getEntity();

                if (stream != null) {
                    String res = EntityUtils.toString(entity);
                    entity.consumeContent();
                    Log.e("voicememes", res);
                    client.getConnectionManager().shutdown();
                    JSONObject object = new JSONObject(res);
                    JSONArray jarray = object.getJSONArray("trending");
                    for (int i = 0; i < jarray.length(); i++) {
                        final JSONObject data = jarray.getJSONObject(i);
                        App.db.open();
                        App.db.addtrending(String.valueOf(i + 1), data.getString("memeid"), data.getString("actor"), data.getString("movie"), data.getString("tag"));
                        App.db.close();
                    }

                    JSONArray jarray1 = object.getJSONArray("latest");
                    for (int i = 0; i < jarray1.length(); i++) {
                        final JSONObject data = jarray1.getJSONObject(i);
                        App.db.open();
                        App.db.addlatest(String.valueOf(i + 1), data.getString("memeid"), data.getString("actor"), data.getString("movie"), data.getString("tag"));
                        App.db.close();
                    }

                    result = "Success";
                } else {
                    result = "Failure";
                }
            } catch (Exception e) {
                result = "Exception" + e.toString();
                Log.e("voicememes", result);
            }
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            Log.e("voicememes", result);
        }
    }

    public class getsearchitem extends AsyncTask<String, Void, String> {
        String results;
        private String status = "";

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(FirstPage.this, AlertDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage("Connecting Callback...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setMessage("Updating your profile...");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient http_client = new DefaultHttpClient();
                HttpPost http_post = new HttpPost("http://www.offersforoffer.com/voicememes/gettsearchmemes.php");
                List<NameValuePair> nameVP = new ArrayList<NameValuePair>(2);

                nameVP.add(new BasicNameValuePair("actor", params[0]));
                nameVP.add(new BasicNameValuePair("movie", params[1]));
                nameVP.add(new BasicNameValuePair("tags", params[2]));
                nameVP.add(new BasicNameValuePair("memeid", params[3]));
                http_post.setEntity(new UrlEncodedFormEntity(nameVP));
                HttpEntity entity = http_client.execute(http_post).getEntity();
                if (entity != null) {
                    String response = EntityUtils.toString(entity);
                    Log.e("voicememes", response);
                    entity.consumeContent();
                    http_client.getConnectionManager().shutdown();
                    JSONObject object = new JSONObject(response);
                    JSONArray jarray = object.getJSONArray("actors");

                    App.db.open();
                    App.db.delete("search");
                    App.db.close();

                    for (int i = 0; i < jarray.length(); i++) {
                        final JSONObject data = jarray.getJSONObject(i);
                        memeid = data.getString("memeid");
                        actor = data.getString("actor");
                        tag = data.getString("tag");
                        App.db.open();
                        App.db.addsearch(String.valueOf(i + 1), data.getString("memeid"), data.getString("actor"), data.getString("movie"), data.getString("tag"));
                        App.db.close();
                    }
                    status = object.getString("status");
                    if (status.equals("ok")) {

                    }
                    results = "Success";
                } else {
                    results = "Failure";
                }
            } catch (Exception e) {
                results = e.toString();
            }
            return results;
        }

        @Override
        protected void onPostExecute(final String result) {
            progressDialog.cancel();
            startActivity(new Intent(FirstPage.this, SearchMemes.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        searchitem.setText("");
    }

    public class GetActors extends AsyncTask<String, Void, String> {
        String result;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://offersforoffer.com/voicememes/getactors.php");
                List<NameValuePair> list = new ArrayList<NameValuePair>(2);
                post.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse response = client.execute(post);
                InputStream stream = response.getEntity().getContent();
                HttpEntity entity = client.execute(post).getEntity();

                if (stream != null) {
                    String res = EntityUtils.toString(entity);
                    entity.consumeContent();
                    Log.e("voicememes", res);
                    client.getConnectionManager().shutdown();
                    JSONObject object = new JSONObject(res);
                    JSONArray jarray = object.getJSONArray("actors");
                    for (int i = 0; i < jarray.length(); i++) {
                        final JSONObject data = jarray.getJSONObject(i);
                        App.db.open();
                        App.db.addactor(data.getString("id"), data.getString("name"));
                        App.db.close();
                    }

                    result = "Success";
                } else {
                    result = "Failure";
                }
            } catch (Exception e) {
                result = "Exception" + e.toString();
                Log.e("voicememes", result);
            }
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            updateactors();
        }
    }
}
