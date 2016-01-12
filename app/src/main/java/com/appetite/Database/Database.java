package com.appetite.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Database {

	private final Context context;
	SQLiteDatabase myDatabase;
	VerificationDBOpenHelper myVerificationHelper;
	
	public Database(Context ct)
	{
		context = ct;
	}

	public Database open() throws SQLException
	{
		myVerificationHelper = new VerificationDBOpenHelper(context);
		myDatabase = myVerificationHelper.getWritableDatabase();
		return this;
	}

	public void close() throws SQLException
	{
			myVerificationHelper.close();
	}

	public void addtrending(String id,String memeid,String actor,String movie,String tag)
	{
		String where="id="+id+"";
		Cursor c=myDatabase.query("trending",null,where,null,null,null,null);
		if(c.moveToNext())
		{
			ContentValues cv = new ContentValues();
			cv.put("memeid", memeid);
			cv.put("actor", actor);
			cv.put("movie", movie);
			cv.put("tag", tag);
			myDatabase.update("trending", cv, where, null);
		}
		else
		{
			ContentValues cv = new ContentValues();
			cv.put("memeid", memeid);
			cv.put("actor", actor);
			cv.put("movie", movie);
			cv.put("tag", tag);
			myDatabase.insert("trending", null, cv);
		}
		c.close();
	}

	public void addlatest(String id,String memeid,String actor,String movie,String tag)
	{
		String where="id="+id+"";
		Cursor c=myDatabase.query("latest",null,where,null,null,null,null);
		if(c.moveToNext())
		{
			ContentValues cv = new ContentValues();
			cv.put("memeid", memeid);
			cv.put("actor", actor);
			cv.put("movie", movie);
			cv.put("tag", tag);
			myDatabase.update("latest",cv,where,null);
		}
		else
		{
			ContentValues cv = new ContentValues();
			cv.put("memeid", memeid);
			cv.put("actor", actor);
			cv.put("movie", movie);
			cv.put("tag", tag);
			myDatabase.insert("latest", null, cv);
		}
		c.close();
	}

	public void addactorsmemes(String id,String memeid,String actor,String movie,String tag)
	{
		String where="memeid="+memeid+"";
		Cursor c=myDatabase.query("actorsmemes",null,where,null,null,null,null);
		if(c.moveToNext())
		{
			ContentValues cv = new ContentValues();
			cv.put("memeid", memeid);
			cv.put("actor", actor);
			cv.put("movie", movie);
			cv.put("tag", tag);
			myDatabase.update("actorsmemes",cv,where,null);
		}
		else
		{
			ContentValues cv = new ContentValues();
			cv.put("memeid", memeid);
			cv.put("actor", actor);
			cv.put("movie", movie);
			cv.put("tag", tag);
			myDatabase.insert("actorsmemes", null, cv);
		}
		c.close();
	}

	public void addsearch(String id,String memeid,String actor,String movie,String tag)
	{
		String where="id="+id+"";
		Cursor c=myDatabase.query("search",null,where,null,null,null,null);
		if(c.moveToNext())
		{
			ContentValues cv = new ContentValues();
			cv.put("memeid", memeid);
			cv.put("actor", actor);
			cv.put("movie", movie);
			cv.put("tag", tag);
			myDatabase.update("search",cv,where,null);
		}
		else
		{
			ContentValues cv = new ContentValues();
			cv.put("memeid", memeid);
			cv.put("actor", actor);
			cv.put("movie", movie);
			cv.put("tag", tag);
			myDatabase.insert("search", null, cv);
		}
		c.close();
	}

	public void addactor(String actorid,String name)
	{
		String where="actorid='"+actorid+"'";
		Cursor c=myDatabase.query("actors",null,where,null,null,null,null);
		if(c.moveToNext())
		{
			ContentValues cv = new ContentValues();
			cv.put("actorid", actorid);
			cv.put("name", name);
			myDatabase.update("actors",cv,where,null);
		}
		else
		{
			ContentValues cv = new ContentValues();
			cv.put("actorid", actorid);
			cv.put("name", name);
			myDatabase.insert("actors", null, cv);
			downloadBitmap("http://offersforoffer.com/voicememes/actors/" + actorid + ".jpg", actorid);
		}
		c.close();
	}

	public Cursor getactor() {
		Cursor c = myDatabase.query("actors", null, null, null, null, null, null);
		return c;
	}

	public String getactor(String id)
	{
		String x;
		String where="actorid='"+id+"'";
		Cursor c=myDatabase.query("actors",null,where,null,null,null,null);
		if(c.moveToNext())
		{
			x=c.getString(2);
		}
		else
		{
			x="null";
		}
		c.close();
		return x;
	}

	public boolean isOpen()
	{
		return myDatabase.isOpen();
	}

	public Cursor getactormemes(String id)
	{
		String where="actor='"+id+"'";
		Cursor c=myDatabase.query("actorsmemes",null,where,null,null,null,null);
		return c;
	}

	public Cursor getdata(String table)
	{
		return myDatabase.query(table,null,null,null,null,null,null);
	}

	public void delete(String tablename)
	{
		myDatabase.delete(tablename, null, null);
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

