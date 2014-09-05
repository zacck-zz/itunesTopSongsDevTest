package com.developertest.itunestopsongs.frags;

import java.util.ArrayList;
import java.util.List;

import com.developertest.itunestopsongs.Localcustomadapter;
import com.developertest.itunestopsongs.R;
import com.developertest.itunestopsongs.customadapter;
import com.developertest.itunestopsongs.song;
import com.developertest.itunestopsongs.db.datahandler;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class FTabFavorites extends ListFragment {

	String TAG = "FTABFAVSSTAG";
	String[] fsong_title, fsong_image;
	List<song> fsongsList = new ArrayList<song>();
	Localcustomadapter fsongsadp;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// lets register ourlistview for a context menu
		registerForContextMenu(getListView());
		// run the async method to pull from the local db
		getFavSongs gfs = new getFavSongs();
		gfs.execute();
	}

	// show the context menu for deleting song
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.fav_songs_menu, menu);
	}

	// lets check when the song is deleted and perform
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.action_del_fav:
			int pos = (int)info.id;
			song dels = new song();
			dels.setImageUrl(fsongsList.get(pos).getImagelink());
			dels.setName(fsongsList.get(pos).getName());
			datahandler mdh = new datahandler(getActivity(), null, null, 1);
			mdh.removeFromFavs(dels);
			//invalidate the current data
			fsongsList.remove(pos);
			fsongsadp.notifyDataSetChanged();
			
			

			return true;
		default:
			return super.onContextItemSelected(item);

		}

	}

	public class getFavSongs extends AsyncTask<Void, Void, Void> {

		// lets pull all the songs from the sqlite lib
		@Override
		protected Void doInBackground(Void... params) {
			datahandler mDatahandler = new datahandler(getActivity(), null,
					null, 1);
			fsongsList = mDatahandler.getFavSongs();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			fsongsadp = new Localcustomadapter(getActivity(), fsongsList);
			setListAdapter(fsongsadp);
		}

	}

}
