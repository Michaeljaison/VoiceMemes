package com.appetite.voicememes;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vignesh on 17/10/15.
 */
public class GetActorsImage extends AsyncTask<String, Void, String>
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
            downloadBitmap(params[0],params[1]);
            result="Success";
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
        Log.e("voicememes",result+"");
    }
    private String downloadBitmap(String url,String name)
    {
        final DefaultHttpClient client = new DefaultHttpClient();
        final HttpGet getRequest = new HttpGet(url);
        try
        {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK)
            {
                Log.w("ImageDownloader", "Error " + statusCode +
                        " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null)
            {
                InputStream inputStream = null;
                inputStream = entity.getContent();
                String filename=getFilename(name+".jpg");
                final File file = new File(filename);
                final OutputStream output = new FileOutputStream(file);
                final byte[] buffer = new byte[1024];
                int read;
                while ((read = inputStream.read(buffer)) != -1)
                {
                    output.write(buffer, 0, read);
                }
                output.flush();
                output.close();
                return filename;
            }
        }
        catch (Exception e)
        {
            getRequest.abort();
            Log.e("voicememes", "Something went wrong while" +
                    " retrieving bitmap from " + url + e.toString());
        }
        return null;
    }

    public String getFilename(String filename)
    {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "voicememes/Actors");
        if (!file.exists())
        {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" +filename );
        return uriSting;
    }
}