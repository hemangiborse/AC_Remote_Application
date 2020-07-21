package com.hemangi.acremote.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceManager
{
	private static final String PREFNAME = "ac_remote";
	private Context context;
	private SharedPreferences pref; 
	private Editor editor;
	
	public SharedPreferenceManager(){		
	}
	
	public SharedPreferenceManager(Context context){	
		this.context = context;
	}
	
	public void connectDB(){
		pref = context.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
		editor = pref.edit();
	}
	
	public void closeDB(){
		editor.commit();	
	}
	
	public boolean getBoolean(String key){
		return pref.getBoolean(key, false);
	}
	
	public void setString(String key, String value){
		editor.putString(key, value);
	}
	
	public void setInt(String key, int value){	
		editor.putInt(key, value);
	}
	public void setBoolean(String key, boolean value){
		editor.putBoolean(key, value);
	}
	
	public String getString(String key){
		return pref.getString(key, "");
	}
	
	public int getInt(String key){
		return pref.getInt(key, 0);	
	}
	public void setFloat(String key, float value){	
		editor.putFloat(key, value);
	}
	
	public float getFloat(String key){
		return pref.getFloat(key,0);	
	}
}
