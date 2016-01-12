package com.appetite.voicememes;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.appetite.Designs.ImageAdapter;

public class WelcomePage extends AppCompatActivity {

	FloatingActionButton Explore;
	RadioGroup group;
	RadioButton[] radio;
	@Override
	protected void onCreate(Bundle instance)
	{
		super.onCreate(instance);
		setContentView(R.layout.welcome);
		Explore=(FloatingActionButton) findViewById(R.id.fab);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ImageAdapter adapter = new ImageAdapter(this);
        viewPager.setAdapter(adapter);
        
        group=(RadioGroup) findViewById(R.id.radioGroup1);
        radio=new RadioButton[5];
        
        radio[0]=(RadioButton) findViewById(R.id.radio0);
        radio[1]=(RadioButton) findViewById(R.id.radio1);
        radio[2]=(RadioButton) findViewById(R.id.radio2);
        radio[3]=(RadioButton) findViewById(R.id.radio3);
        radio[4]=(RadioButton) findViewById(R.id.radio4);
        
        for(int i=0;i<5;i++)
        {
        	radio[i].setEnabled(false);
        }
        
        viewPager.setOnPageChangeListener(new OnPageChangeListener()
        {
			
			@Override
			public void onPageSelected(int arg0)
			{
				if(arg0==4)
				{
					Explore.setVisibility(View.VISIBLE);
				}
				else
				{
					Explore.setVisibility(View.GONE);
				}
				
				radio[arg0].setChecked(true);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) 
			{
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) 
			{
				
			}
		});
        
        Explore.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v)
			{
				finish();
				startActivity(new Intent(WelcomePage.this, FirstPage.class));
			}
		});
	}
}
