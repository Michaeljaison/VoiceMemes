package com.appetite.voicememes;

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
public class SearchMemes extends AppCompatActivity
{
    MyAdaptar adaptar;
    ArrayList<Messages> value=new ArrayList<>();
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchmemes);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        new Helvetico().overrideFonts(getApplicationContext(), findViewById(R.id.textView3));
        list= (ListView) findViewById(R.id.listView);
        adaptar=new MyAdaptar(getApplicationContext(),value);
        list.setAdapter(adaptar);

        App.db.open();
        Cursor c=App.db.getdata("search");
        while(c.moveToNext())
        {
            value.add(new Messages(c.getString(1),c.getString(4),App.db.getactor(c.getString(2))));
            adaptar.notifyDataSetChanged();
        }
        c.close();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
