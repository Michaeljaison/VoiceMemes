package com.appetite.voicememes;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.appetite.adaptar.Messages;
import com.appetite.adaptar.MyAdaptar;

import java.util.ArrayList;

public class Trending extends Fragment
{
	FragmentActivity activity;
	View rootView;
	MyAdaptar adaptar;
	ArrayList<Messages> value=new ArrayList<>();
	ListView list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.trending, container, false);
		activity = getActivity();

		list= (ListView) rootView.findViewById(R.id.listView);
		adaptar=new MyAdaptar(getActivity(),value);
		list.setAdapter(adaptar);

		update();

		return rootView;
	}

	public void update()
	{
		value.clear();
		App.db.open();
		Cursor c=App.db.getdata("trending");
		while (c.moveToNext())
		{
			if(App.db.isOpen())
			{
				value.add(new Messages(c.getString(1), c.getString(4), App.db.getactor(c.getString(2))));
				adaptar.notifyDataSetChanged();
			}
			else
			{
				App.db.open();
				value.add(new Messages(c.getString(1), c.getString(4), App.db.getactor(c.getString(2))));
				adaptar.notifyDataSetChanged();
				App.db.close();
			}
		}
		c.close();
		if(App.db.isOpen())
		{
			App.db.close();
		}
		else
		{

		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
		update();
	}
}

