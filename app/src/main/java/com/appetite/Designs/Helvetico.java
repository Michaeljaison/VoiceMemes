package com.appetite.Designs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Helvetico {

	 
	 public void overrideFonts(final Context context, final View v)
	 {
	    try 
	    {
	        if (v instanceof ViewGroup)
	        {
	            ViewGroup vg = (ViewGroup) v;
	            for (int i = 0; i < vg.getChildCount(); i++) 
	            {
	                View child = vg.getChildAt(i);
	                overrideFonts(context, child);
	            }
	        } 
	        else if (v instanceof TextView)
	        {
	            ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "HelveticaLight.otf"));
	        }
	    } 
	    catch (Exception e)
	    {
	    	
	    }
	 }
	 public void color(final Context context, final View v)
	 {
		  try 
		  {
		        if (v instanceof ViewGroup)
		        {
		            ViewGroup vg = (ViewGroup) v;
		            for (int i = 0; i < vg.getChildCount(); i++) 
		            {
		                View child = vg.getChildAt(i);
		                overrideFonts(context, child);
		            }
		        } 
		        else if (v instanceof TextView)
		        {
		            ((TextView) v).setTextColor(new Color().parseColor("#424848"));
		        }
		   } 
		  catch (Exception e)
		  {
			  
		  }
	 }
}
