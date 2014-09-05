package com.developertest.itunestopsongs;

/*
 * Model class for the song object
 * contains the getters and setters 
 * 
 */

public class song {
	
	//vars for song 
	private String songName, songImageUrl;
	
	//constructor 
	public song()
	{
		
	}
	
	public song(String name,String image)
	{
		this.songName = name;
		this.songImageUrl = image;
		
	}
	
	//getter for the name 
	public String getName()
	{
		return songName;
	}
	
	//setter for the name 
	public 	void setName(String name)
	{
		this.songName = name;
		
	}
	
	//getter for the imageurl
	public String getImagelink()
	{
		return songImageUrl; 
	}
	
	//setter for the imageurl
	public void setImageUrl(String url)
	{
		this.songImageUrl = url;
		
	}

}
