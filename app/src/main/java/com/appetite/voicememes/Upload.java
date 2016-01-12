package com.appetite.voicememes;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appetite.Designs.Helvetico;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

/**
 * Created by vignesh on 19/10/15.
 */
public class Upload  extends AppCompatActivity
{
    private String path;
    TextView name;
    Button upload;
    EditText movie,actor,tag;
    String random;
    private ProgressDialog progressDialog;
    FloatingActionButton fabBUtton;

    //SeekBar seekbar;
    ImageView play;
    private MediaPlayer mp;
    boolean audioplay=false;
//    ElasticDownloadView mElasticDownloadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        new Helvetico().overrideFonts(getApplicationContext(), findViewById(R.id.toolbar));

        fabBUtton= (FloatingActionButton) findViewById(R.id.imageView6);

        movie= (EditText) findViewById(R.id.movie);
        actor= (EditText) findViewById(R.id.actor);
        tag= (EditText) findViewById(R.id.tag);

        name= (TextView) findViewById(R.id.name);
        upload= (Button) findViewById(R.id.upload);

//        seekbar= (SeekBar) findViewById(R.id.seekBar);

        play= (ImageView) findViewById(R.id.play);

        new Helvetico().overrideFonts(getApplicationContext(),movie);
        new Helvetico().overrideFonts(getApplicationContext(),actor);
        new Helvetico().overrideFonts(getApplicationContext(),tag);
        new Helvetico().overrideFonts(getApplicationContext(), name);
        new Helvetico().overrideFonts(getApplicationContext(), upload);

        actor.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on Enter key press
                    actor.clearFocus();
                    movie.requestFocus();
                    return true;
                }
                return false;
            }
        });

        movie.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on Enter key press
                    movie.clearFocus();
                    tag.requestFocus();
                    return true;
                }
                return false;
            }
        });

        tag.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on Enter key press
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(tag.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

// single example
        MaterialShowcaseView view=new MaterialShowcaseView.Builder(this)
                .setTarget(fabBUtton)
                .setDismissText("GOT IT")
                .setContentText("click here to select an audio. Audio should less than 20 seconds duration")
                .setDelay(0) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse("") // provide a unique ID used to ensure it is only shown once
                .show();

        new Helvetico().overrideFonts(getApplicationContext(),view);

//        // sequence example
//        ShowcaseConfig config = new ShowcaseConfig();
//        config.setDelay(500); // half second between each showcase view
//
//        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "1");
//
//        sequence.setConfig(config);
//
//        sequence.addSequenceItem(play,
//                "This is button one", "GOT IT");
//
//        sequence.addSequenceItem(fabBUtton,
//                "This is button two", "GOT IT");
//
//        sequence.addSequenceItem(upload,
//                "This is button three", "GOT IT");
//
//        sequence.start();


//        mElasticDownloadView= (ElasticDownloadView) findViewById(R.id.elastic_download_view);
//
//        mElasticDownloadView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Handler().post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mElasticDownloadView.startIntro();
//                    }
//                });
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mElasticDownloadView.success();
//                    }
//                }, 2 * ProgressDownloadView.ANIMATION_DURATION_BASE);
//            }
//        });

        random=getRandom();

        new Helvetico().overrideFonts(getApplicationContext(), findViewById(R.id.textView3));

        fabBUtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_upload = new Intent();
                intent_upload.setType("audio/*");
                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_upload, 1);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new upload().execute(actor.getText().toString(), movie.getText().toString(), tag.getText().toString(), random);
            }
        });

//        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!audioplay)
                {
                    mp=new MediaPlayer();

                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.stop();
                            play.setImageDrawable(getResources().getDrawable(R.drawable.play));
                            audioplay=false;
                        }
                    });

                    try
                    {
                        mp.setDataSource(path);
                        mp.prepare();
                        mp.start();
                    }
                    catch(Exception e)
                    {
                        Log.e("voicememes",e.toString());
                    }
                    play.setImageDrawable(getResources().getDrawable(R.drawable.playwhite));
                    audioplay=true;
                }
                else
                {
                    mp.stop();
                    play.setImageDrawable(getResources().getDrawable(R.drawable.play));
                    audioplay=false;
                }
            }
        });

    }

    public class upload extends AsyncTask<String, Void, String>
    {
        String results;
        private String status="";

        @Override
        protected void onPreExecute()
        {
            progressDialog=new ProgressDialog(Upload.this, AlertDialog.THEME_HOLO_LIGHT);
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
                HttpPost http_post=new HttpPost("http://www.offersforoffer.com/voicememes/upload.php");
                List<NameValuePair> nameVP = new ArrayList<NameValuePair>(2);

                nameVP.add(new BasicNameValuePair("actor", params[0]));
                nameVP.add(new BasicNameValuePair("movie",params[1]));
                nameVP.add(new BasicNameValuePair("tags",params[2]));
                nameVP.add(new BasicNameValuePair("memeid", params[3]));
                http_post.setEntity(new UrlEncodedFormEntity(nameVP));
                HttpEntity entity = http_client.execute(http_post).getEntity();
                if (entity != null)
                {
                    String response = EntityUtils.toString(entity);
                    Log.e("voicememes",response);
                    entity.consumeContent();
                    http_client.getConnectionManager().shutdown();
                    JSONObject object = new JSONObject(response);
                    status=object.getString("status");
                    if(status.equals("ok"))
                    {

                    }
                    results="Success";
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
            new audio().execute(path);
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if(requestCode == 1){

            if(resultCode == RESULT_OK)
            {
                Uri uri = data.getData();
                path = Upload.getPath(this, uri);
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(path);
                String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                mmr.release();

                if(Integer.parseInt(duration)>20000)
                {
                    Toast.makeText(getApplicationContext(),"Meme should be less than 20 seconds",Toast.LENGTH_SHORT).show();
                }
                else {
                    play.setVisibility(View.VISIBLE);
                    File file = new File(path);
                    name.setText(file.getName());
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public class audio extends AsyncTask<String, Void, String>
    {
        String x;
        private String serverResponseMessage;
        @Override
        protected void onPreExecute()
        {

        }
        @Override
        protected String doInBackground(String... params)
        {
            // TODO Auto-generated method stub

            String fileName = params[0];

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(params[0]);

            if (!sourceFile.isFile()) {
                //dialog.dismiss();
                x="uploadFile"+ "Source File not exist :" + fileName;
                Log.e("uploadFile", "Source File not exist :" + fileName);
                return null;
            }
            else
            {

                try {
                    FileInputStream fileInputStream = new FileInputStream(
                            sourceFile);
                    URL url = new URL("http://www.offersforoffer.com/voicememes/usermemes.php");

                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("uploaded_file", fileName);
                    // conn.addRequestProperty("", newValue)

                    try{
                        dos = new DataOutputStream(conn.getOutputStream());
                    }
                    catch(Exception e)
                    {
                    }
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                            + random+".mp3" + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);
                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }
                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // Responses from the server (code and message)
                    int serverResponseCode = conn.getResponseCode();

                    serverResponseMessage = conn.getResponseMessage();

                    Log.i("uploadFile", "HTTP Response is : "
                            + serverResponseMessage + ": " + serverResponseCode);
                    x="uploadFile"+ "HTTP Response is : "
                            + serverResponseMessage + ": " + serverResponseCode;

                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (MalformedURLException ex) {

                    ex.printStackTrace();
                    x="voicememes" + ex.getMessage();
                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                } catch (Exception e) {

                    e.printStackTrace();

                    x="Upload file to server"+ "error: " + e.getMessage();
                    Log.e("voicememes",e.getMessage());
                }

            }

            return serverResponseMessage;
        }

        @Override
        protected void onPostExecute(String result)
        {
            Log.e("voicememes",result);
            progressDialog.cancel();
            finish();
        }
    }

    public String getRandom()
    {
        Random rand = new Random();
        int  n = rand.nextInt(100000) + 100000;
        return "usermemes"+String.valueOf(n);
    }
}
