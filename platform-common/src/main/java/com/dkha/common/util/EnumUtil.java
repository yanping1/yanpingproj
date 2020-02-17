package com.dkha.common.util;



import com.dkha.common.validate.UtilValidate;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Bray.ye on 2016/12/22 16:54.
 */
public class EnumUtil {

    private final static String KEY = "code";
    private final static String VALUE = "name";
    //权重，用于排序
    private final static String WEIGHT = "weight";

    /**
     * 枚举转map
     * @param enumClazz
     * @param <X>
     * @return
     */
    public final static <X extends Enum<X>> Map<String, String> toMap(Class<X> enumClazz) {
        Map<String, String> map = new HashMap<>();
        try {
            Field code_ = enumClazz.getDeclaredField(KEY);
            Field name_ = enumClazz.getDeclaredField(VALUE);
            code_.setAccessible(true);
            name_.setAccessible(true);
            EnumSet<X> xes = EnumSet.allOf(enumClazz);
            map = xes.stream().collect(Collectors.toMap(
                    x -> {
                        String code = null;
                        try {
                            code = (String) code_.get(x);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                        return code;
                    },
                    x -> {
                        String name = null;
                        try {
                            name = (String) name_.get(x);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                        return name;
                    }
            ));
           /* for (X x : xes) {
                String code = (String) code_.get(x);
                String name = (String) name_.get(x);
                map.put(code, name);
                //System.out.println(code + ", " +name);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 枚举转map(根绝权重weight排序, 排升序)
     * @param enumClazz
     * @param <X>
     * @return
     */
    public final static <X extends Enum<X>> Map<String, String> toMapSortByWeight(Class<X> enumClazz) {
        //用于保存有序结果集
        List<InnerSortDS> sortList = new ArrayList<>();
        Map<String, String> resultMap = new LinkedHashMap<>();
        try {
            Field code_ = enumClazz.getDeclaredField(KEY);
            Field name_ = enumClazz.getDeclaredField(VALUE);
            Field weight_ = enumClazz.getDeclaredField(WEIGHT);
            code_.setAccessible(true);
            name_.setAccessible(true);
            EnumSet<X> xes = EnumSet.allOf(enumClazz);
            for (X x : xes) {
                InnerSortDS innerSortDS = new InnerSortDS();
                innerSortDS.setCode((String) code_.get(x));
                innerSortDS.setName((String) name_.get(x));
                innerSortDS.setWeight((int) weight_.get(x));
                sortList.add(innerSortDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sortList != null && sortList.size() > 0) {
            int listLength = sortList.size();
            InnerSortDS innerSortDSTemp;
            //冒泡排降序
            for (int i = 0; i < listLength - 1; i++) {
                for (int j = i; j < listLength; j++) {
                    if (sortList.get(i).getWeight() > sortList.get(j).getWeight()) {
                        innerSortDSTemp = sortList.get(j);
                        sortList.set(j, sortList.get(i));
                        sortList.set(i, innerSortDSTemp);
                    }
                }
            }

            /**遍历排序后的list，并转为map*/
            for (InnerSortDS innerSortDS : sortList) {
                resultMap.put(innerSortDS.getCode(), innerSortDS.getName());
            }
        }
        return resultMap;
    }
    /**
     * 枚举转list
     * @param enumClazz
     * @param <X>
     * @return
     */
    public final static <X extends Enum<X>> List<Map<String, String>> toList(Class<X> enumClazz) {
        List<Map<String, String>> list = toMap(enumClazz).entrySet().stream().map(entry -> {
            Map<String, String> map = new HashMap();
            map.put(KEY, entry.getKey());
            map.put(VALUE, entry.getValue());
            return map;
        }).collect(Collectors.toList());
        /*List<Map<String, String>> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : toMap(enumClazz).entrySet()) {
            Map<String, String> map = new HashMap();
            map.put(KEY, entry.getKey());
            map.put(VALUE, entry.getValue());
            list.add(map);
        }*/
        return list;
    }

    /**
     * 通过code获取name
     * @param enumClazz
     * @param <X>
     * @return
     */
    public final static <X extends Enum<X>> String getNameByCode(String code, Class<X> enumClazz) {
        Map<String, String> map = toMap(enumClazz);
        if (UtilValidate.isNotEmpty(map)) {
            return map.get(code);
        }
        return null;
    }

    /**
     * 用于排序内部类(数据结构)
     */
    static class InnerSortDS {

        private String code;
        private String name;
        /** 排序权重 */
        private int weight;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }

}
