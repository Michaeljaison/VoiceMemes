 package com.appetite.adaptar;

 import android.content.Context;
 import android.content.Intent;
 import android.graphics.Bitmap;
 import android.graphics.BitmapFactory;
 import android.os.Environment;
 import android.util.DisplayMetrics;
 import android.util.Log;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.BaseAdapter;
 import android.widget.ImageView;
 import android.widget.TextView;

 import com.appetite.Designs.Helvetico;
 import com.appetite.voicememes.ActorMemes;
 import com.appetite.voicememes.GetActorsImage;
 import com.appetite.voicememes.PlayAudio;
 import com.appetite.voicememes.R;

 import java.io.File;
 import java.util.ArrayList;

 /**
  * AwesomeAdapter is a Custom class to implement custom row in ListView
  *
  * @author Adil Soomro
  *
  */
 public class ActorsAdapter extends BaseAdapter {

     private LayoutInflater inflater;
     private Context mContext;
     private ArrayList<Messages> mMessages;
     private int swidth;
     private int sheight;

     public ActorsAdapter(Context context, ArrayList<Messages> message) {
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
         View view = convertView;
         ViewHolder holder;
         if (convertView == null) {
             view = inflater.inflate(R.layout.actorsadaptar, parent, false);
             holder = new ViewHolder();
             new Helvetico().overrideFonts(mContext,view);
             holder.image= (ImageView) view.findViewById(R.id.imageView5);
             holder.name= (TextView) view.findViewById(R.id.textView2);
             view.setTag(holder);
         } else {
             view = inflater.inflate(R.layout.actorsadaptar, parent, false);
             new Helvetico().overrideFonts(mContext,view);
             holder = new ViewHolder();
             holder.image= (ImageView) view.findViewById(R.id.imageView5);
             holder.name= (TextView) view.findViewById(R.id.textView2);
             view.setTag(holder);
         }

         Log.e("voicememes",message.getid()+"");

         Bitmap bitmap= BitmapFactory.decodeFile(getFilename(message.getid()+".jpg"));

         if(bitmap!=null)
         {
             holder.image.setImageBitmap(bitmap);
         }
         else
         {
             new GetActorsImage().execute("http://offersforoffer.com/voicememes/actors/"+message.getid()+".jpg",message.getid());
         }

         holder.name.setText(message.getname());

         holder.image.getLayoutParams().height=swidth/2;

         holder.image.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent=new Intent(mContext, ActorMemes.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 intent.putExtra("actorid", message.ID);
                 mContext.startActivity(intent);
             }
         });

         return view;
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

     class ViewHolder {
         ImageView image;
         TextView name;
     }
 }