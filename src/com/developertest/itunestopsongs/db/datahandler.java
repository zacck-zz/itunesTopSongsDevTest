package com.developertest.itunestopsongs.db;

import java.util.ArrayList;
import java.util.List;

import com.developertest.itunestopsongs.song;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class datahandler extends SQLiteOpenHelper {
	String TAG = "DATAHANDLERERROR";

	// create the Variables we shall be using for the database
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "itunesFavs";
	public static final String TABLE_SONGS = "tbl_songs";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_SONGNAME = "songname";
	public static final String COLUMN_SONGIMG = "songimg";

	public datahandler(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/*
		 * lets make the table and define the keys so we shall be a little
		 * unconventional and set the song name as primary key so cant be added
		 * again
		 */

		String CREATE_SONGS_TABLE = "CREATE TABLE " + TABLE_SONGS + "("
				+ COLUMN_ID + " INTEGER," + COLUMN_SONGNAME
				+ " TEXT PRIMARY KEY," + COLUMN_SONGIMG + " TEXT" + ")";
		db.execSQL(CREATE_SONGS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// update the db if it comes to so
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);
		onCreate(db);

	}

	/*
	 * lets make a function to add songs to the db
	 */

	public void addSongToFavs(song s) {
		ContentValues song_values = new ContentValues();
		song_values.put(COLUMN_SONGIMG, s.getImagelink());
		song_values.put(COLUMN_SONGNAME, s.getName());

		SQLiteDatabase m_db = this.getWritableDatabase();
		try {
			m_db.insert(TABLE_SONGS, null, song_values);
			Log.d(TAG, s.getName() + " added to sqlite db");
		} catch (Exception e) {
			Log.d(TAG + "addition", e.toString());
		}
		m_db.close();
	}

	/*
	 * query the database for the favs songs
	 */
	public List<song> getFavSongs() {
		List<song> favsongs = new ArrayList<song>();
		// lets collect all them songs from the favs table
		String query = "Select * FROM " + TABLE_SONGS;
		SQLiteDatabase m_db = this.getWritableDatabase();
		try {
			Cursor mCursor = m_db.rawQuery(query, null);

			if (mCursor.moveToFirst()) 
			{
				do
				{
					song fsong = new song();
					fsong.setName(mCursor.getString(mCursor.getColumnIndex(COLUMN_SONGNAME)));
					fsong.setImageUrl(mCursor.getString(mCursor.getColumnIndex(COLUMN_SONGIMG)));
					//add the song to the list of songs 
					Log.d(TAG, mCursor.getString(mCursor.getColumnIndex(COLUMN_SONGIMG)));
					favsongs.add(fsong);
				}while(mCursor.moveToNext());

			}

		} catch (Exception e) {
			Log.d(TAG + "lIst", e.toString());
		}

		return favsongs;
	}

}
