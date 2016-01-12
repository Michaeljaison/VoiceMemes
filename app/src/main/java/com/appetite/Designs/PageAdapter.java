package com.appetite.Designs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.appetite.voicememes.Latest;
import com.appetite.voicememes.Trending;

/**
 * Created by IntelliJ IDEA.
 * User: keith.
 * Date: 14-9-24.
 * Time: 16:10.
 */
public class PageAdapter extends FragmentPagerAdapter implements TabIndicator.TabTextProvider {

    final String[] CONTENT = new String[]{"Top Trending", "Latest"};

    private int mCount = CONTENT.length;

    public PageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
    	switch (position) {
		case 0:
			return new Trending();
		case 1:
			return new Latest();
		}
		return null;
    }

    @Override
    public String getText(int position) {
        return CONTENT[position % CONTENT.length];
    }

    @Override
    public int getCount() {
        return mCount;
    }
}
