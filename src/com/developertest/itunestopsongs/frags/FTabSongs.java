package com.developertest.itunestopsongs.frags;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.ls.LSInput;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.developertest.itunestopsongs.AppController;
import com.developertest.itunestopsongs.R;
import com.developertest.itunestopsongs.customadapter;
import com.developertest.itunestopsongs.song;
import com.developertest.itunestopsongs.db.datahandler;

import android.content.Entity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
//import android.support.v7.internal.widget.AdapterViewICS.AdapterContextMenuInfo;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

public class FTabSongs extends ListFragment {
	String TAG = "FTABSONGSTAG";
	String[] song_title, song_image;
	List<song> songsList = new ArrayList<song>();
	customadapter songsadp;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// do init and call the bg thread
		super.onActivityCreated(savedInstanceState);

		// lets register ourlistview for a context menu
		registerForContextMenu(getListView());
		// call the background thread
		getListInfo gli = new getListInfo();
		gli.execute();
	}

	// create an asyncmethod for pulling the data from teh icloudz
	public class getListInfo extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			// lets do all our heavy calls and data processing in a separate
			// thread

			// start with httpcall to get the json
			HttpClient mHttpClient = new DefaultHttpClient();
			// do the call off ssl so its as backwards compatible as possible
			HttpPost mHttpPost = new HttpPost(
					"http://itunes.apple.com/us/rss/topsongs/limit=200/json");
			try {
				HttpResponse mhttpResponse = mHttpClient.execute(mHttpPost);
				// convert the result to a JSON string and consume it
				String result = EntityUtils.toString(mhttpResponse.getEntity());
				JSONObject parentObject = new JSONObject(result);
				JSONArray entriesArray = parentObject.getJSONObject("feed")
						.getJSONArray("entry");
				song_image = new String[entriesArray.length()];
				song_title = new String[entriesArray.length()];
				// lets loop within the JSONarray to add to the string array so
				// we can use the data in out adapter
				for (int m = 0; m < entriesArray.length(); m++) {
					JSONObject detailObject = entriesArray.getJSONObject(m);
					song_title[m] = detailObject.getJSONObject("im:name")
							.getString("label");
					// Log.d(TAG, song_title[m]);
					// lets get the largest image which is always at the end of
					// the array of images
					JSONArray sImages = detailObject.getJSONArray("im:image");
					song_image[m] = sImages.getJSONObject(sImages.length() - 1)
							.getString("label");
					// Log.d(TAG, song_image[m]);

				}

			} catch (Exception e) {
				// log any errors and exceptions that are caught
				Log.d(TAG, e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// lets do any UI rendering in the OG thread
			// lets use the customadapter
			for (int i = 0; i < song_image.length; i++) {
				song s = new song();
				s.setName(song_title[i]);
				s.setImageUrl(song_image[i]);
				songsList.add(s);

			}

			// init and load the adaptet
			songsadp = new customadapter(getActivity(), songsList);
			setListAdapter(songsadp);

		}
	}

	// lets make a context menu to give the user an option of downloading the
	// image and
	// making the song a favorite
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.songs_context_menu, menu);
	}

	// handling context item click
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		RequestQueue que = Volley.newRequestQueue(getActivity());
		int pos = (int) info.id;
		String imageurl = song_image[pos];
		final String imagename = song_title[pos];
		switch (item.getItemId()) {
		case R.id.action_add_favorites:
			Toast.makeText(getActivity(), "Adding song to Favorites",
					Toast.LENGTH_LONG).show();
			// lets use the android volley library to get a bitmap and store it
			// Retrieves an image specified by the URL, and we shall store the
			// bitmap.
			ImageRequest ir4fav = new ImageRequest(imageurl,
					new Response.Listener<Bitmap>() {
						@Override
						public void onResponse(Bitmap b) {
							// lets store the bitmap and get the path
							String path = Environment
									.getExternalStorageDirectory()
									.getAbsolutePath().toString();
							OutputStream fOut = null;
							File file = new File(path, imagename + ".jpg");
							try {
								fOut = new FileOutputStream(file);

								b.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
								fOut.flush();
								fOut.close();

								MediaStore.Images.Media.insertImage(
										getActivity().getContentResolver(),
										file.getAbsolutePath(), file.getName(),
										file.getName());
								song favsong = new song();
								// since we name the image same as song without
								// issues lets use that for name too
								favsong.setName(imagename);
								// instead of url lets set the image path as a
								// the absolutepath
								favsong.setImageUrl(file.getAbsolutePath());
								datahandler mDatahandler = new datahandler(
										getActivity(), null, null, 1);
								mDatahandler.addSongToFavs(favsong);
								Toast.makeText(getActivity(),
										"Song added to favs", Toast.LENGTH_LONG)
										.show();
							} catch (Exception e) {
								Log.d(TAG, e.toString());
							}

						}

					}, 0, 0, null, null);
			que.add(ir4fav);
			return true;
		case R.id.action_save_image:
			Toast.makeText(getActivity(), "Saving Song Image",
					Toast.LENGTH_LONG).show();
			// lets use the android volley library to get a bitmap and store it
			// Retrieves an image specified by the URL, and we shall store the
			// bitmap.
			ImageRequest ir = new ImageRequest(imageurl,
					new Response.Listener<Bitmap>() {
						@Override
						public void onResponse(Bitmap b) {
							// lets store the bitmap and get the path
							String path = Environment
									.getExternalStorageDirectory()
									.getAbsolutePath().toString();
							OutputStream fOut = null;
							File file = new File(path, imagename + ".jpg");
							try {
								fOut = new FileOutputStream(file);

								b.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
								fOut.flush();
								fOut.close();

								MediaStore.Images.Media.insertImage(
										getActivity().getContentResolver(),
										file.getAbsolutePath(), file.getName(),
										file.getName());
								Toast.makeText(getActivity(), "Image saved :)",
										Toast.LENGTH_LONG).show();
							} catch (Exception e) {
								Log.d(TAG, e.toString());
							}

						}

					}, 0, 0, null, null);
			que.add(ir);

			return true;
		default:
			return super.onContextItemSelected(item);
		}

	}
}
