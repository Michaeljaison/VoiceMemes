package com.appetite.voicememes;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vignesh on 17/10/15.
 */
public class GetActors extends AsyncTask<String, Void, String>
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
    protected void onPostExecute(final String result)
    {

    }
}