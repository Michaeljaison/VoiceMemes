package com.appetite.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;


public class VerificationDBOpenHelper extends SQLiteOpenHelper
{
	public VerificationDBOpenHelper(Context context)
	{
		
		 super(context, Environment.getExternalStorageDirectory()
		            + File.separator + "voicememes"
		            + File.separator + "memes", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE " + "trending" + "(" +"id"
				+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"+"memeid"
				+ " TEXT ,"+"actor"
				+ " TEXT ,"+"movie"
				+ " TEXT ,"+"tag"
				+ " TEXT )");

		db.execSQL("CREATE TABLE " + "latest" + "(" +"id"
				+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"+"memeid"
				+ " TEXT ,"+"actor"
				+ " TEXT ,"+"movie"
				+ " TEXT ,"+"tag"
				+ " TEXT )");

		db.execSQL("CREATE TABLE " + "actors" + "(" +"id"
				+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"+"actorid"
				+ " TEXT ,"+"name"
				+ " TEXT )");

		db.execSQL("CREATE TABLE " + "search" + "(" +"id"
				+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"+"memeid"
				+ " TEXT ,"+"actor"
				+ " TEXT ,"+"movie"
				+ " TEXT ,"+"tag"
				+ " TEXT )");

		db.execSQL("CREATE TABLE " + "actorsmemes" + "(" +"id"
				+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"+"memeid"
				+ " TEXT ,"+"actor"
				+ " TEXT ,"+"movie"
				+ " TEXT ,"+"tag"
				+ " TEXT )");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

}