package com.developertest.itunestopsongs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;

public class appTabListener implements TabListener {

	//init a placeholder fragment we use for transition 
	Fragment mFragment;
	
	//lets get a constructor
	public appTabListener(Fragment ft)
	{
		//get the fragment that has been passed in
		this.mFragment = ft;
	}

	@Override
	public void onTabReselected(Tab t, FragmentTransaction fts) {
		//no need to do much here as the tab is still active 
		
	}

	@Override
	public void onTabSelected(Tab t, FragmentTransaction fts) {
		//select the tab and load it with the UI and functions of the tab we need 
		fts.replace(R.id.fragment_container, mFragment);
		
		
	}

	@Override
	public void onTabUnselected(Tab t, FragmentTransaction fts) {
		//unselect the tab that has been active and relegate the view 
		//by removing the fragment that has been passed in
		fts.remove(mFragment);
		
	}

}
