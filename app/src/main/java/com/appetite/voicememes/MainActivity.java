package com.appetite.voicememes;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.appetite.adaptar.Messages;
import com.appetite.adaptar.MyAdaptar;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    ProgressBar progressBar;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        progressBar= (ProgressBar) findViewById(R.id.progressBar2);
        button= (Button) findViewById(R.id.button2);

        if(App.shared.getBoolean("firsttime",true)==true)
        {
            new getmemes().execute();
        }
        else
        {
            startActivity(new Intent(MainActivity.this, FirstPage.class));
            finish();
        }

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, FirstPage.class));
                finish();
            }
        });
    }

    public class getmemes extends AsyncTask<String, Void, String>
    {
        String result;

        @Override
        protected void onPreExecute()
        {

        }
        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                HttpClient client=new DefaultHttpClient();
                HttpPost post=new HttpPost("http://offersforoffer.com/voicememes/getmemes.php");
                List<NameValuePair> list=new ArrayList<NameValuePair>(2);
                post.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse response=client.execute(post);
                InputStream stream=response.getEntity().getContent();
                HttpEntity entity = client.execute(post).getEntity();

                if(stream!=null)
                {
                    String res = EntityUtils.toString(entity);
                    entity.consumeContent();
                    Log.e("voicememes", res);
                    client.getConnectionManager().shutdown();
                    JSONObject object = new JSONObject(res);
                    JSONArray jarray=object.getJSONArray("trending");
                    for(int i=0;i<jarray.length();i++)
                    {
                        final JSONObject data=jarray.getJSONObject(i);
                        App.db.open();
                        App.db.addtrending(String.valueOf(i),data.getString("memeid"),data.getString("actor"),data.getString("movie"),data.getString("tag"));
                        App.db.close();
                    }

                    JSONArray jarray1=object.getJSONArray("latest");
                    for(int i=0;i<jarray1.length();i++)
                    {
                        final JSONObject data=jarray1.getJSONObject(i);
                        App.db.open();
                        App.db.addlatest(String.valueOf(i),data.getString("memeid"), data.getString("actor"), data.getString("movie"), data.getString("tag"));
                        App.db.close();
                    }

                    result="Success";
                }
                else
                {
                    result="Failure";
                }
            }
            catch(Exception e)
            {
                result="Exception"+e.toString();
                Log.e("voicememes",result);
            }
            return result;
        }

        @Override
        protected void onPostExecute(final String result)
        {
            Log.e("voicememes", result);
            new getActors().execute();
        }
    }

    public class getActors extends AsyncTask<String, Void, String>
    {
        String result;

        @Override
        protected void onPreExecute()
        {

        }
        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                HttpClient client=new DefaultHttpClient();
                HttpPost post=new HttpPost("http://offersforoffer.com/voicememes/getactors.php");
                List<NameValuePair> list=new ArrayList<NameValuePair>(2);
                post.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse response=client.execute(post);
                InputStream stream=response.getEntity().getContent();
                HttpEntity entity = client.execute(post).getEntity();

                if(stream!=null)
                {
                    String res = EntityUtils.toString(entity);
                    entity.consumeContent();
                    Log.e("voicememes", res);
                    client.getConnectionManager().shutdown();
                    JSONObject object = new JSONObject(res);
                    JSONArray jarray=object.getJSONArray("actors");
                    for(int i=0;i<jarray.length();i++)
                    {
                        final JSONObject data=jarray.getJSONObject(i);
                        App.db.open();
                        App.db.addactor(data.getString("id"),data.getString("name"));
                        App.db.close();
                    }

                    result="Success";
                }
                else
                {
                    result="Failure";
                }
            }
            catch(Exception e)
            {
                result="Exception"+e.toString();
                Log.e("voicememes",result);
            }
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            Log.e("voicememes", result);
            //progressDialog.cancel();
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
            App.editor.putBoolean("firsttime",false).commit();
        }
    }

    public static boolean deleteDirectory(File path)
    {
        if(path.exists())
        {
            File[] files = path.listFiles();
            if (files == null)
            {
                return true;
            }
            for(int i=0; i<files.length; i++)
            {
                if(files[i].isDirectory())
                {
                    deleteDirectory(files[i]);
                }
                else
                {
                    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }
}
