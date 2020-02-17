package com.dkha.communication.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @version V1.0
 * @Description: 微云错误码翻译
 * @Title:
 * @Package com.dkha.communication.common
 * @author: huangyugang
 * @date: 2019/12/3 14:47
 * @Copyright: 成都电科慧安
 */

@Component
public class ErrorCodeToMsg {
    public String convertErrorCodeToMsg(String errorcodestr) {
        if (!StringUtil.isBlank(errorcodestr)) {
            StringBuilder sb=new StringBuilder();
            String[] errstr=errorcodestr.split(",");
            for (String estr : errstr) {
                int errorcode = 0;
                try{
                    if(!StringUtil.isBlank(estr)) {
                        errorcode = Integer.parseInt(estr.trim());
                        switch (errorcode) {
                            case 10001:
                                sb.append("算子句柄初始化失败| ");
                                break;
                            case 10002:
                                sb.append( "得到图片流错误, 没有传送图片数据|");
                                break;
                            case 10003:
                                sb.append( "从图片流转为图片错误|");
                                break;
                            case 11001:
                                sb.append( "创建特征失败|");
                                break;
                            case 11002:
                                sb.append( "特征值比对数量少于2|");
                                break;
                            case 12001:
                                sb.append( "特征库ID为空|");
                                break;
                            case 12002:
                                sb.append( "特征库ID已存在| ");
                                break;
                            case 12003:
                                sb.append( "创建特征库失败| ");
                                break;
                            case 12004:
                                sb.append( " 特征库ID不存在|");
                                break;
                            case 12005:
                                sb.append( "数据库查询失败|");
                                break;
                            case 12006:
                                sb.append( "特征ID为空|");
                                break;
                            case 12008:
                                sb.append( "特征添加失败，未知错误，请重试|");
                                break;
                            case 12009:
                                sb.append( "特征ID添加到数据库表失败|");
                                break;
                            case 12010:
                                sb.append( "特征ID已存在|");
                                break;
                            case 12011:
                                sb.append( "特征ID不存在| ");
                                break;
                            case 12015:
                                sb.append( "特征删除失败，未知错误，请重试| ");
                                break;
                            case 12016:
                                sb.append( "删除特征ID失败|");
                                break;
                            case 12020:
                                sb.append( "特征库算子总句柄为空|");
                                break;
                            case 12021:
                                sb.append( "创建特征库失败|");
                                break;
                            case 12022:
                                sb.append( "输入数据非法|");
                                break;
                            case 12100:
                                sb.append( "插入至特征库表失败|");
                                break;
                            case 13001:
                                sb.append( "提取人脸属性失败|");
                                break;
                            default:
                                break ;
                        }
                    }

                }catch (Exception ex){
                }
            }
            if(sb.toString().length()>0)
            { return  sb.toString();}
            else
            { return errorcodestr;}
        }
        return "成功";
    }

}
