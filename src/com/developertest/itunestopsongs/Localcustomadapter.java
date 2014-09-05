package com.developertest.itunestopsongs;

import java.io.File;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Localcustomadapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<song> songitems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public Localcustomadapter(Activity activity, List<song> movieItems) {
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
			v = inflater.inflate(R.layout.local_song_item, null);

		ImageView thumbNail = (ImageView) v.findViewById(R.id.ivlSongImage);
		TextView title = (TextView) v.findViewById(R.id.tvlSongName);

		// getting movie data for the row
		song m = songitems.get(pos);

		// lets set the local thumbnail image
		File imgFile = new File(m.getImagelink());
		if (imgFile.exists()) {

			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
					.getAbsolutePath());

			thumbNail.setImageBitmap(myBitmap);

		}
		// title
		title.setText(m.getName());

		return v;

	}

}