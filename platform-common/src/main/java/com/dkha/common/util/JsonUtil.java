package com.dkha.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * @ClassName：JsonUtil
 * @Description：JSON工具
 * @company:
 * @author
 * @date
 */
public class JsonUtil {
    public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").serializeNulls() .enableComplexMapKeySerialization().setPrettyPrinting().create();
    private static JsonUtil tObj;
    private JsonUtil(){};
    public static JsonUtil instance(){
        if(tObj==null){
            synchronized (JsonUtil.class) {
                if(tObj==null){
                    tObj= new JsonUtil();
                }
            }
        }
        return tObj;
    }
    /**
     * 判断基本类型
     * @author
     * @throws
     */
    private boolean isPrimitive(Field f){
        if(f.getType() == String.class){
            return true;
        }else if(f.getType().isPrimitive()){
            return true;
        }else if(f.getType() == Integer.class){
            return true;
        }else if(f.getType() == Boolean.class){
            return true;
        }else if(f.getType() == Double.class){
            return true;
        }else if(f.getType() == Float.class){
            return true;
        }else if(f.getType() == Long.class){
            return true;
        }else if(f.getType() == Short.class){
            return true;
        }else if(f.getType() == Byte.class){
            return true;
        }else if(f.getType() == Character.class){
            return true;
        }

        return false;
    }
    public static boolean strIsNotNull(String s){
    	String nuString = "null";
        if(s==null || "".equals(s.trim()) || nuString.equals(s.trim().toLowerCase())){
            return false;
        }
        return true;
    }

    public boolean strIsNull(String s){
        return !strIsNotNull(s);
    }
    /**
     * 判断基本类型
     * @author
     * @throws
     */
    private static boolean isPrimitive(Class f){
        if(f == String.class){
            return true;
        }else if(f.isPrimitive()){
            return true;
        }else if(f == Integer.class){
            return true;
        }else if(f == Boolean.class){
            return true;
        }else if(f == Double.class){
            return true;
        }else if(f == Float.class){
            return true;
        }else if(f == Long.class){
            return true;
        }else if(f == Short.class){
            return true;
        }else if(f == Byte.class){
            return true;
        }else if(f == Character.class){
            return true;
        }

        return false;
    }
    /**
     * 获取JSON String数据
     * @author
     */
    public static final String getJsonString(JSONObject j, String key){
        Object o = j.get(key);
        if(o!=null){
            return o.toString();
        }
        return null;
    }

    /**
     * 获取JSON Integer数据
     * @author zhuowei
     */
    public static final Integer getJsonInteger(JSONObject j, String key){
        String o = getJsonString(j, key);
        if(o!=null){
            try{
                return Integer.valueOf(o);
            }catch(Exception e){

            }
        }
        return null;
    }

    /**
     * 获取JSON Double数据
     * @author
     */
    public static final Double getJsonDouble(JSONObject j, String key){
        Object o = j.get(key);
        if(o!=null){
            try{
                return j.getDoubleValue(key);
            }catch(Exception e){

            }
        }
        return null;
    }
    public static List<String> jsonArrayToList(net.sf.json.JSONArray jobArray){
        List<String> list  = new ArrayList<String>();
        if(jobArray.size()>0){
            String str = jobArray.getString(0);
            net.sf.json.JSONObject json=net.sf.json.JSONObject.fromObject(str);
            Map<String, Object> map =json;
            for (Entry<String, Object> entry : map.entrySet()) {
                list.add(String.valueOf(entry.getValue()));
            }
        }
        return list;
    }
    public static List<String> jsonArrayTolist(net.sf.json.JSONArray jobArray){
        List<String> list  = new ArrayList<String>();
        if(jobArray.size()>0){
            for (int i = 0; i < jobArray.size(); i++) {
                String str = jobArray.getString(i);
                list.add(str);
            }
        }
        return list;
    }
    /**
     * 将json转化为实体POJO
     * @param jsonStr
     * @param obj
     * @return
     */
    public static<T> Object jSONToObj(String jsonStr,Class<T> obj) {
        T t = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            t = objectMapper.readValue(jsonStr,obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static <T> T toObj(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    public static <T> T toObj(String json, Type typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }

    public static String toJson(Object obj){
        return GSON.toJson(obj);
    }

	/**
	 * 
	 * @Title: getStringByObjectKe   
	 * @Description:  
	 * @param: @param obj
	 * @param: @param key
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
    public static String getStringByObjectKe(JSONObject obj, String key){
    	if(obj != null && !obj.isEmpty() && obj.containsKey(key)) {
    		return obj.getString(key);
    	}
    	return null;
    }
    
    public static String getStringByArrayKey(JSONArray array, String key){
    	if(array != null && !array.isEmpty() && array.getJSONObject(0) != null && array.getJSONObject(0).containsKey(key)) {
    		return getStringByObjectKe(array.getJSONObject(0),key);
    	}
    	return null;
    }

    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
        @SuppressWarnings("unchecked")
        List<T> ts = (List<T>) JSONArray.parseArray(jsonString, clazz);
        return ts;
    }

    /**
     * List<T> 转 json 保存到数据库
     */
    public static <T> String listToJson(List<T> ts) {
        String jsons = JSON.toJSONString(ts);
        return jsons;
    }
}


