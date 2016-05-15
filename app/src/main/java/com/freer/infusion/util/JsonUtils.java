package com.freer.infusion.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

public class JsonUtils {
	
	private static Gson mGson = null;
	
	private static class JsonUtilsHolder {
		private static final JsonUtils INSTANCE =
				new JsonUtils();
	}
	
	private JsonUtils() {
		mGson = new Gson();
	}
	
	private static final JsonUtils getInstance() {
		return JsonUtilsHolder.INSTANCE;
	}
	
	/**
     * 转成json
     * 
     * @param object
     * @return
     */
    public static String toJson(Object object) {
    	getInstance();
        String json = null;
        if (mGson != null) {
        	try {
        		json = mGson.toJson(object);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        return json;
    }
	
	/**
	 * 转成bean
	 * 
	 * @param json
	 * @param classOfT
	 * @return
	 */
	public static <T> T fromJson(String json, Class<T> classOfT) {
		getInstance();
		T t = null;
		if (mGson != null) {
			try {
				t = new Gson().fromJson(json, classOfT);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return t;
	}
	
	/**
	 * 转成list
	 * 
	 * @param json
	 * @return
	 */
	public static <T> List<T> jsonToList(String json) {
		getInstance();
        List<T> list = null;
        if (mGson != null) {
        	try {
        		list = mGson.fromJson(json, 
                		new TypeToken<List<T>>() {}.getType());
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        return list;
    }
	
	/**
     * 转成list中有map的
     * 
     * @param json
     * @return
     */
    public <T> List<Map<String, T>> jsonToListMaps(String json) {
    	getInstance();
        List<Map<String, T>> list = null;
        if (mGson != null) {
        	try {
        		list = mGson.fromJson(json,
                        new TypeToken<List<Map<String, T>>>() {}.getType());
			} catch (Exception e) {
				e.printStackTrace();
			}
            
        }
        return list;
    }
    
    /**
     * 转成map的
     * 
     * @param json
     * @return
     */
    public <T> Map<String, T> jsonToMaps(String json) {
    	getInstance();
        Map<String, T> map = null;
        if (mGson != null) {
        	try {
        		map = mGson.fromJson(json, 
                		new TypeToken<Map<String, T>>() {}.getType());
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        return map;
    }
}
