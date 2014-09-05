package com.developertest.itunestopsongs;

import com.developertest.itunestopsongs.frags.FTabFavorites;
import com.developertest.itunestopsongs.frags.FTabSongs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.Tab;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class TwoTabsActivity extends ActionBarActivity {
	ActionBar.Tab tabSongs, tabFavorites;
	Fragment fragmentTabsongs = new FTabSongs();
	Fragment fragmentTabfavs = new FTabFavorites();
	
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_tabs);
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //lets set the labels for the tabs
        tabSongs = actionBar.newTab().setText("Songs");
        tabFavorites = actionBar.newTab().setText("Favorites");
        
        //lets add listeners to the tabs so we know when they are opened 
        tabSongs.setTabListener(new appTabListener(fragmentTabsongs));
        tabFavorites.setTabListener(new appTabListener(fragmentTabfavs));
        
        
        
        actionBar.addTab(tabSongs);
        actionBar.addTab(tabFavorites);
        
    }
}