package com.dkha.platformasynresultprocess.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @version V1.0
 * @Description: WS 通讯协议常量
 * @Title:
 * @Package com.dkha.communication.common
 * @author: huangyugang
 * @date: 2019/11/28 12:59
 * @Copyright: 成都电科慧安
 */
@Component
@PropertySource({"classpath:system.properties"})
public class WSProtocalConst {
    /******************************WY 命令常量*************************************/
    /** 新增特征库*/
    public static final String CMDLIB_CREATE = "CmdLibCreate";
    /** 删除特征库*/
    public static final String CMDLIB_DELETE = "CmdLibDelete";
    /**人像入库-新增*/
    public static final String CMDLIB_PUSH = "CmdLibPush";
    /**人像入库-删除*/
    public static final String CMDLIB_POP = "CmdLibPop";
    /**人脸库检索*/
    public static final String CMDF_SEARCH = "CmdFSearch";
    /**人脸检测*/
    public static final String CMDF_SEARCHFACE= "CmdFSearchFace";
    public static final String CMDF_FDETECT= "CmdFDetect";
    /**人脸分组检索*/
    public static final String CMDFACE_GROUP= "CmdFGroup";
    /** 布控新增*/
    public static final String CMDW_TASKADD= "CmdWTaskAdd";
    /** 布控删除*/
    public static final String CMDW_TASKDEL= "CmdWTaskDel";
    /**任务查询*/
    public static final String CMDW_TASKQUERY= "CmdWTaskQuery";
    /**视频任务人脸报警*/
    public static final String CMDW_VDFSALARM= "CmdVdFSAlarm";


    /******************************HEAD 参数常量*************************************/
    /** 协议头静态常量 */
    public static final String HEADER = "head";
    /** 控制命令字段*/
    public static final String PORT_ID = "protId";
    /** wappId 常量 */
    public static final String WAPP_ID = "wappId";
    /** sdk 版本 */
    public static final String SDK_VERSION = "version";
    /** serId常亮 */
    public static final String SER_ID = "serId";
    /** 结果码 0为正确 */
    public static final String RET = "ret";
    /** 错误信息 */
    public static final String ERROR_INFO = "errInfo";
    /** 特征库集合 */
    public static final String LIB_IDS = "libIds";

    /****************************人脸特征添加******************************/

    /** 图片请求类型 */
    public static final String IMG_REQTYP = "imgReqTyp";
    /**人脸库搜索过滤配置*/
    public static final String IMG_FCFG = "fcfg";
    public static final String IMG_FCFG_FACEFILT = "faceFilt";
    public static final String IMG_FCFG_CONFIDENCE = "confidence";
    public static final String IMG_FCFG_ANGLE = "angle";
    public static final String IMG_FCFG_MINFACESIZE = "minFaceSize";
    public static final String IMG_FCFG_FACEONLYONE = "faceOnlyOne";

    /****************************人脸特征搜索******************************/
    public static final String IMG_GROUPFILT = "groupFilt";
    public static final String IMG_MINSCORE = "minScore";
    public static final String IMG_MAXRETNB = "maxRetNb";
    public static final String IMG_IMAGS= "imgs";
    public static final String IMG_DCFG= "dcfg";
    public static final String TASK_CFG= "cfg";
    public static final String TASKID_PROCID="procId";
    public static final String TASKID_VDURLTYPE="vdUrlType";
    public static final String TASKID_VDURL="vdUrl";
    public static final String TASKID_SCFG="scfg";
    public static final String TASKID_TASKIDS="taskIds";

    /** 人脸id对应的人脸位置信息列表(如果图片无人脸则为空) */
    public static final String POSITION = "position";
    /** 人脸特征Id集合 */
    public static final String FEAT_IDS = "featIds";
    /** 人脸特征Id */
    public static final String FEAT_ID = "featId";
    /** 库搜索结果 */
    public static final String LIB_SCH_SCORE = "libSchScore";
    /** 库Id */
    public static final String LIB_ID = "libId";
    /** scoreList */
    public static final String SCORE_LIST = "scoreList";
    /** 评分 */
    public static final String SCORE = "score";
    /** 人脸rect */
    public static final String RECT = "rect";

    public static final String EXT_ID = "extId";
    /** face sdk 版本 */
    public static String sdkVersion="3.2.0";
    /** 人脸置信度 <0不启用*/
    public static float confidence=-0.1f;
    /** 人脸角度过滤 <0不启用 */
    public static float angle=-0.1f;
    /** 最小人脸过滤, 单边人脸边长像素 */
    public static int minFaceSize=30;
    /** 只选取图片最大人脸 */
    public static boolean faceOnlyOne=true;
    /** 图片请求类型 */
    public static String imgReqTyp="ReqImgHttpUrl";
    public static String YES="Y";
    public static String NO="N";
    @Value("faceAttr")
    /**是否搜索人脸特征属性*/
    public static boolean faceAttr=true;
    /** 获取任务Id */
    public static final String TASK_ID = "taskId";


    @Value("ReqImgHttpUrl")
    public void setImgReqTyp(String imgReqTyp) {
        WSProtocalConst.imgReqTyp = imgReqTyp;
    }
    @Value("${weiyun.version}")
    public void setSdkVersion(String sdkVersion) {
        WSProtocalConst.sdkVersion = sdkVersion;
    }
    @Value("${confidence}")
    public void setConfidence(float confidence) {
        WSProtocalConst.confidence = confidence;
    }
    @Value("${angle}")
    public void setAngle(float angle) {
        WSProtocalConst.angle = angle;
    }
    @Value("${minFaceSize}")
    public void setMinFaceSize(int minFaceSize) {
        WSProtocalConst.minFaceSize = minFaceSize;
    }
    @Value("${faceOnlyOne}")
    public void setFaceOnlyOne(boolean faceOnlyOne) {
        WSProtocalConst.faceOnlyOne = faceOnlyOne;
    }


}
