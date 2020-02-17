package com.dkha.common.page;

import com.dkha.common.validate.UtilValidate;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分页类 用于分页传参
 * @author Spring
 * @create 2016-12-10 14:38
 **/
public class PageParam {

    /**
     * 当前页码
     */
    private int pageNo = 1;

    /**
     * 每页记录数 默认10条
     */
    private int pageSize = 10;

    /**
     * 搜索条件
     */
    private Map<String,String> note=new HashMap<String,String>();

    /**
     * 排序条件
     */
    private Map<String,String> sort=new HashMap<String,String>();

    /**
     * 拼接搜索条件
     * @return
     */
    public String getSortString() {
        return sort.entrySet()
                .stream()
                //过滤掉key和value都为空的项
                .filter(x -> !StringUtils.isEmpty(x.getKey())&&!StringUtils.isEmpty(x.getValue()))
                //map转list
                .map(x -> x.getKey() + " " + x.getValue())
                //逗号拼接
                .collect(Collectors.joining(","));
    }

    /**
     * 模糊查询拼接
     * @param params
     * @param likes
     * @return
     */
    public Map<String, String> paramsToLike(Map<String, String> params, String... likes){
        for (String like : likes){
            String val = params.get(like);
            if (UtilValidate.isNotEmpty(val)){
                params.put(like, "%" + val + "%");
            }else {
                params.put(like, null);
            }
        }
        return params;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Map<String, String> getNote() {
        return note;
    }

    public void setNote(Map<String, String> note) {
        this.note = note;
    }

    public Map<String, String> getSort() {
        return sort;
    }

    public void setSort(Map<String, String> sort) {
        this.sort = sort;
    }

}
