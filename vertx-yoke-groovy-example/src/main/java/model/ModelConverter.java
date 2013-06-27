package model;

import java.util.List;

import model.types.GenderType;

import org.stjs.server.json.gson.GsonAdapters;

import adapters.GsonEnumTypeAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ModelConverter {
	public static String createJson(User user) {
		GsonBuilder gsonBuilder = new GsonBuilder();
	    GsonAdapters.addAll(gsonBuilder);
	    
	    Gson gson = gsonBuilder.create();
	    
	    return gson.toJson(user);	    
	}
	
	public static String createJson(List<User> users) {
		GsonBuilder gsonBuilder = new GsonBuilder();
	    GsonAdapters.addAll(gsonBuilder);
	    
	    Gson gson = gsonBuilder.create();
	    
	    return gson.toJson(users);	    
	}
	
	public static User createUser(String json) {
		GsonBuilder gsonBuilder = new GsonBuilder();
	    GsonAdapters.addAll(gsonBuilder);
	    gsonBuilder.registerTypeAdapter(GenderType.class, new GsonEnumTypeAdapter<GenderType>(GenderType.class));
	    
	    Gson gson = gsonBuilder.create();
	    
	    return gson.fromJson(json, User.class);   
	}
}
