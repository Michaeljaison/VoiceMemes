 package com.appetite.adaptar;

 import android.app.AlertDialog;
 import android.app.ProgressDialog;
 import android.content.Context;
 import android.content.Intent;
 import android.content.pm.PackageInfo;
 import android.content.pm.PackageManager;
 import android.content.res.AssetFileDescriptor;
 import android.graphics.Bitmap;
 import android.media.AudioManager;
 import android.media.MediaPlayer;
 import android.net.Uri;
 import android.os.AsyncTask;
 import android.os.Environment;
 import android.util.DisplayMetrics;
 import android.util.Log;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.BaseAdapter;
 import android.widget.ImageView;
 import android.widget.TextView;
 import android.widget.Toast;

 import com.appetite.Designs.Helvetico;
 import com.appetite.voicememes.PlayAudio;
 import com.appetite.voicememes.R;

 import org.apache.http.HttpEntity;
 import org.apache.http.NameValuePair;
 import org.apache.http.client.HttpClient;
 import org.apache.http.client.entity.UrlEncodedFormEntity;
 import org.apache.http.client.methods.HttpPost;
 import org.apache.http.impl.client.DefaultHttpClient;
 import org.apache.http.message.BasicNameValuePair;
 import org.apache.http.util.EntityUtils;
 import org.json.JSONObject;

 import java.io.BufferedInputStream;
 import java.io.File;
 import java.io.FileDescriptor;
 import java.io.FileInputStream;
 import java.io.FileNotFoundException;
 import java.io.FileOutputStream;
 import java.io.InputStream;
 import java.io.OutputStream;
 import java.net.URL;
 import java.net.URLConnection;
 import java.util.ArrayList;
 import java.util.List;

 /**
  * AwesomeAdapter is a Custom class to implement custom row in ListView
  *
  * @author Adil Soomro
  *
  */
 public class MyAdaptar extends BaseAdapter {

     private LayoutInflater inflater;
     private Context mContext;
     private ArrayList<Messages> mMessages;
     private int swidth;
     private int sheight;
     private String id;
     ImageView viewid;
     boolean clicked;
     private MediaPlayer mediaPlayer;

     public MyAdaptar(Context context, ArrayList<Messages> message) {
         DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
         swidth = displayMetrics.widthPixels;
         sheight = displayMetrics.heightPixels;
         this.mContext = context;
         this.mMessages = message;
         inflater = LayoutInflater.from(context);
     }

     @Override
     public int getCount() {
         return mMessages.size();
     }

     @Override
     public Object getItem(int position) {
         return mMessages.get(position);
     }

     @Override
     public long getItemId(int position) {
         return position;
     }

     @Override
     public View getView(final int position, View convertView, ViewGroup parent) {

         final Messages message = (Messages) this.getItem(position);
         id=message.ID;
         View view = convertView;
         final ViewHolder holder;
         if (convertView == null) {
             view = inflater.inflate(R.layout.adapter, parent, false);
             new Helvetico().overrideFonts(mContext,view);
             holder = new ViewHolder();
             holder.name = (TextView) view.findViewById(R.id.name);
             holder.title = (TextView) view.findViewById(R.id.titlename);
             holder.whatsapp = (ImageView) view.findViewById(R.id.imageView2);
             holder.play = (ImageView) view.findViewById(R.id.imageView4);
             holder.rate = (ImageView) view.findViewById(R.id.imageView3);
             view.setTag(holder);
         } else {
             view = inflater.inflate(R.layout.adapter, parent, false);
             new Helvetico().overrideFonts(mContext,view);
             holder = new ViewHolder();
             holder.name = (TextView) view.findViewById(R.id.name);
             holder.title = (TextView) view.findViewById(R.id.titlename);
             holder.whatsapp = (ImageView) view.findViewById(R.id.imageView2);
             holder.play = (ImageView) view.findViewById(R.id.imageView4);
             holder.rate = (ImageView) view.findViewById(R.id.imageView3);
             view.setTag(holder);
         }

         holder.name.setText(message.getname());
         holder.title.setText("#"+message.getactor());

         if(swidth<600)
         {
             holder.whatsapp.getLayoutParams().width=50;
             holder.play.getLayoutParams().width=50;
             holder.rate.getLayoutParams().width=50;
         }
         else
         {
             holder.whatsapp.getLayoutParams().width=75;
             holder.play.getLayoutParams().width=75;
             holder.rate.getLayoutParams().width=75;
         }

         holder.name.setText(message.getname());

         holder.play.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(!clicked) {
                     clicked = true;
                     viewid = holder.play;
                     holder.play.setImageDrawable(mContext.getResources().getDrawable(R.drawable.playcolor));

                     try {
                         FileDescriptor fd = null;
                         File baseDir = Environment.getExternalStorageDirectory();
                         String audioPath = baseDir.getAbsolutePath() +"/voicememes/"+ message.getid() + ".mp3";
                         FileInputStream fis = new FileInputStream(audioPath);
                         fd = fis.getFD();

                         if (fd != null) {
                             mediaPlayer = new MediaPlayer();
                             mediaPlayer.setDataSource(fd);
                             mediaPlayer.prepare();
                             mediaPlayer.start();

                             mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                 @Override
                                 public void onCompletion(MediaPlayer mediaPlayer) {
                                     Log.e("voicememes","mik");
                                     clicked=false;
                                     viewid.setImageDrawable(mContext.getResources().getDrawable(R.drawable.playblack));
                                 }
                             });
                         }
                         else
                         {
                             id=message.getid();
                             new DownloadFile().execute(message.geturl());
                         }
                     }
                     catch(FileNotFoundException e)
                     {
                         id=message.getid();
                         new DownloadFile().execute(message.geturl());
                     }
                     catch (Exception e) {
                         Log.e("voicememes",e.toString());
                     }

                     new update().execute(message.getid());
                 }
                 else
                 {
                     if(holder.play==viewid)
                     {
                         if(mediaPlayer!=null)
                         {
                             mediaPlayer.stop();
                             Log.e("voicememes","mik");
                             clicked=false;
                             viewid.setImageDrawable(mContext.getResources().getDrawable(R.drawable.playblack));
                         }
                     }
                 }
             }
         });

         holder.whatsapp.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                 shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 File baseDir = Environment.getExternalStorageDirectory();
                 String audioPath = baseDir.getAbsolutePath() +"/voicememes/"+ message.getid() + ".mp3";
                 Uri uri=Uri.parse(audioPath);
                 shareIntent.setType("audio/mp3");
                 shareIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
                 shareIntent.setPackage("com.whatsapp");
//                 Intent new_intent = Intent.createChooser(shareIntent, "Share via");
//                 new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 mContext.startActivity(shareIntent);
             }
         });

         return view;
     }

     class ViewHolder {
         TextView name,title;
         ImageView whatsapp,play,rate;
     }

     private class DownloadFile extends AsyncTask<String, Integer, String> {

         @Override
         protected void onPreExecute()
         {
             Toast.makeText(mContext, "Please wait", Toast.LENGTH_LONG).show();
         }

         @Override
         protected String doInBackground(String... urls) {
             int count;
             try {
                 URL url = new URL(urls[0]);
                 URLConnection conexion = url.openConnection();
                 conexion.connect();
                 // this will be useful so that you can show a tipical 0-100% progress bar
                 int lenghtOfFile = conexion.getContentLength();

                 // downlod the file
                 InputStream input = new BufferedInputStream(url.openStream());
                 OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().toString()+"/voicememes/"+id+".mp3");

                 byte data[] = new byte[1024];

                 long total = 0;

                 while ((count = input.read(data)) != -1) {
                     total += count;
                     // publishing the progress....
                     publishProgress((int) (total * 100 / lenghtOfFile));
                     output.write(data, 0, count);
                 }

                 output.flush();
                 output.close();
                 input.close();
             } catch (Exception e) {
                 Log.e("voicememes",e.toString());
             }
             return null;
         }
         @Override
         protected void onPostExecute(final String result)
         {
             try {
                 FileDescriptor fd = null;
                     File baseDir = Environment.getExternalStorageDirectory();
                     String audioPath = baseDir.getAbsolutePath() +"/voicememes/"+ id + ".mp3";
                     FileInputStream fis = new FileInputStream(audioPath);
                     fd = fis.getFD();

                 if (fd != null) {
                     mediaPlayer = new MediaPlayer();
                     mediaPlayer.setDataSource(fd);
                     mediaPlayer.prepare();
                     mediaPlayer.start();

                     mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                         @Override
                         public void onCompletion(MediaPlayer mediaPlayer) {
                             Log.e("voicememes","mik");
                             clicked=false;
                             viewid.setImageDrawable(mContext.getResources().getDrawable(R.drawable.playblack));
                         }
                     });
                 }
             } catch (Exception e) {
                 Log.e("voicememes",e.toString());
             }
         }
     }

     public class update extends AsyncTask<String, Void, String>
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
                 HttpPost http_post=new HttpPost("http://www.offersforoffer.com/voicememes/update.php");
                 List<NameValuePair> nameVP = new ArrayList<NameValuePair>(2);
                 nameVP.add(new BasicNameValuePair("memeid", params[0]));
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

         }
     }
 }