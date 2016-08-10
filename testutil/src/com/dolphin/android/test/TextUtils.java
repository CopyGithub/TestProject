package com.dolphin.android.test;

public class TextUtils {

	public static boolean isEmpty(String text){
		return null == text || "".equals(text.trim());
	}
	
    public static String joinStringArray(String[] array, String divider) {
        StringBuilder builder = new StringBuilder();
        if(null != array && array.length > 0){
        	for (String text : array) {
        		builder.append(text).append(divider);
        	}
        	builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }
    
    public static String joinIntArray(int[] array, String divider) {
        StringBuilder builder = new StringBuilder();
        if(null != array && array.length > 0){
        	for (int text : array) {
        		builder.append(text).append(divider);
        	}
        	builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }
    
    public static String toUrlPath(String path){
    	String urlPath = "";
    	if(!isEmpty(path)){
    		urlPath = path.replace("\\", "/");
    	}
    	return urlPath;		
    }
	
}
