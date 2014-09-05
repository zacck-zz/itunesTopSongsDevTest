package com.developertest.itunestopsongs;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class customadapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<song> songitems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public customadapter(Activity activity, List<song> movieItems) {
		this.activity = activity;
		this.songitems = movieItems;
	}

	@Override
	public int getCount() {
		return songitems.size();
	}

	@Override
	public Object getItem(int location) {
		return songitems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int pos, View v, ViewGroup vg) {
		// push data to view
		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (v == null)
			v = inflater.inflate(R.layout.song_item, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		NetworkImageView thumbNail = (NetworkImageView) v
				.findViewById(R.id.ivSongImage);
		TextView title = (TextView) v.findViewById(R.id.tvSongName);

		// getting movie data for the row
		song m = songitems.get(pos);

		// thumbnail image
		thumbNail.setImageUrl(m.getImagelink(), imageLoader);

		// title
		title.setText(m.getName());

		return v;

	}

}