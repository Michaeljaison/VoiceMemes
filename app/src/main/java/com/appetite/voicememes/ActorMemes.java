package com.appetite.voicememes;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appetite.Designs.Helvetico;
import com.appetite.adaptar.Messages;
import com.appetite.adaptar.MyAdaptar;

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

/**
 * Created by vignesh on 17/10/15.
 */
public class ActorMemes extends AppCompatActivity
{
    AutoCompleteTextView textview;
    MyAdaptar adaptar;
    ArrayList<Messages> value=new ArrayList<>();
    ListView list;
    private ArrayAdapter<String> adapter;
    private ProgressDialog progressDialog;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actorsmemes);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        new Helvetico().overrideFonts(getApplicationContext(), findViewById(R.id.textView3));

        textview= (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        list= (ListView) findViewById(R.id.listView);
        adaptar=new MyAdaptar(getApplicationContext(),value);
        list.setAdapter(adaptar);

        adapter=new ArrayAdapter<String> (getApplicationContext(), android.R.layout.select_dialog_item )
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                text1.setTextSize(15);
                new Helvetico().overrideFonts(getApplicationContext(), text1);
                text1.setTextColor(Color.parseColor("#34495e"));
                text1.setBackgroundColor(Color.TRANSPARENT);
                return view;
            }
        };

        textview.setAdapter(adapter);

//        textview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int in, long l) {
//
//                Log.e("voicememes", ((TextView) view).getText().toString());
//                for (int i = 0; i < x.length; i++) {
//                    if (((TextView) view).getText().toString().equals(z[i])) {
//                        list.setSelection(i);
//                    }
//                }
//            }
//        });


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        update();

        //new getasyncmemes().execute(getIntent().getStringExtra("actorid"),getIntent().getStringExtra("actorid"),getIntent().getStringExtra("actorid"),getIntent().getStringExtra("actorid"));
    }

    public void update()
    {
        value.clear();

        App.db.open();
        Cursor c=App.db.getactormemes(getIntent().getStringExtra("actorid"));
        if(c.getCount()>0) {
            while (c.moveToNext()) {
                value.add(new Messages(c.getString(1), c.getString(4), App.db.getactor(c.getString(2))));
                adaptar.notifyDataSetChanged();
            }
            c.close();
            App.db.close();
            new getasyncmemes().execute(getIntent().getStringExtra("actorid"), getIntent().getStringExtra("actorid"), getIntent().getStringExtra("actorid"), getIntent().getStringExtra("actorid"));
        }
        else
        {
            new getmemes().execute(getIntent().getStringExtra("actorid"),getIntent().getStringExtra("actorid"),getIntent().getStringExtra("actorid"),getIntent().getStringExtra("actorid"));
        }
    }

    public class getasyncmemes extends AsyncTask<String, Void, String>
    {
        String results;
        private String status="";

        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                HttpClient http_client=new DefaultHttpClient();
                HttpPost http_post=new HttpPost("http://www.offersforoffer.com/voicememes/gettactorsmemes.php");
                List<NameValuePair> nameVP = new ArrayList<NameValuePair>(2);

                nameVP.add(new BasicNameValuePair("actor", params[0]));
                nameVP.add(new BasicNameValuePair("movie",params[1]));
                nameVP.add(new BasicNameValuePair("tags",params[2]));
                nameVP.add(new BasicNameValuePair("memeid", params[3]));
                http_post.setEntity(new UrlEncodedFormEntity(nameVP));
                HttpEntity entity = http_client.execute(http_post).getEntity();
                if (entity != null)
                {
                    String res = EntityUtils.toString(entity);
                    entity.consumeContent();
                    Log.e("voicememes", res);
                    http_client.getConnectionManager().shutdown();
                    JSONObject object = new JSONObject(res);
                    JSONArray jarray=object.getJSONArray("actors");

//                    x=new String[jarray.length()];
//                    y=new String[jarray.length()];
//                    z=new String[jarray.length()];

                    for(int i=0;i<jarray.length();i++)
                    {
                        final JSONObject data=jarray.getJSONObject(i);
                        App.db.open();
                        App.db.addactorsmemes(String.valueOf(i),data.getString("memeid"), data.getString("actor"), data.getString("movie"), data.getString("tag"));
                        App.db.close();
                    }
                }
                else
                {
                    results="Failure";
                }
            }
            catch (Exception e)
            {
                results=e.toString();
            }
            return results;
        }
        @Override
        protected void onPostExecute(final String result)
        {

        }
    }

    public class getmemes extends AsyncTask<String, Void, String>
    {
        String results;
        private String status="";

        @Override
        protected void onPreExecute()
        {
            progressDialog=new ProgressDialog(ActorMemes.this, AlertDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage("Connecting Callback...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setMessage("Updating your profile...");
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                HttpClient http_client=new DefaultHttpClient();
                HttpPost http_post=new HttpPost("http://www.offersforoffer.com/voicememes/gettactorsmemes.php");
                List<NameValuePair> nameVP = new ArrayList<NameValuePair>(2);

                nameVP.add(new BasicNameValuePair("actor", params[0]));
                nameVP.add(new BasicNameValuePair("movie",params[1]));
                nameVP.add(new BasicNameValuePair("tags",params[2]));
                nameVP.add(new BasicNameValuePair("memeid", params[3]));
                http_post.setEntity(new UrlEncodedFormEntity(nameVP));
                HttpEntity entity = http_client.execute(http_post).getEntity();
                if (entity != null)
                {
                    String res = EntityUtils.toString(entity);
                    entity.consumeContent();
                    Log.e("voicememes", res);
                    http_client.getConnectionManager().shutdown();
                    JSONObject object = new JSONObject(res);
                    JSONArray jarray=object.getJSONArray("actors");

//                    x=new String[jarray.length()];
//                    y=new String[jarray.length()];
//                    z=new String[jarray.length()];

                    for(int i=0;i<jarray.length();i++)
                    {
                        final JSONObject data=jarray.getJSONObject(i);
                        App.db.open();
                        App.db.addactorsmemes(String.valueOf(i),data.getString("memeid"), data.getString("actor"), data.getString("movie"), data.getString("tag"));
                        App.db.close();
                    }
                }
                else
                {
                    results="Failure";
                }
            }
            catch (Exception e)
            {
                results=e.toString();
            }
            return results;
        }
        @Override
        protected void onPostExecute(final String result)
        {
            update();
            progressDialog.cancel();
        }
    }
}
