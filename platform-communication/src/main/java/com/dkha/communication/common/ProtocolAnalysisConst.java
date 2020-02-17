package com.dkha.communication.common;

/**
 * @version V1.0
 * @Description:  协议解析-命令常量
 * @Title: ProtocolAnalysisConst
 * @Package com.dkha.communication.common
 * @author: huangyugang
 * @date: 2019/11/28 9:01
 * @Copyright: 成都电科慧安
 */
public  class ProtocolAnalysisConst {

    public static final String CMD_CONST = "cmd";
    public static final String CMD_DATE = "data";
    /*******************命令信息常量*************************/
    /** 新增特征库*/
    public static final String CMDLIB_CREATE = "CmdLibCreate";
    /** 删除特征库 */
    public static final String CMDLIB_DELETE = "CmdLibDelete";
    /** 人像入库-新增*/
    public static final String CMDLIB_PUSH = "CmdLibPush";
    /** 人像入库-删除*/
    public static final String CMDLIB_POP = "CmdLibPop";
    /** 人脸库检索 */
    public static final String CMDF_SEARCH = "CmdFSearch";
    /** 人脸检测*/
    public static final String CMDF_SEARCHFACE= "CmdFSearchFace";
    /** 人脸分组检索*/
    public static final String CMDFACE_GROUP= "CmdFaceGroup";
    /** 布控新增*/
    public static final String CMDW_TASKADD= "CmdWTaskAdd";
    /** 布控删除*/
    public static final String CMDW_TASKDEL= "CmdWTaskDel";
    /** 布控查询*/
    public static final String CMDW_TASKQUERY= "CmdWTaskQuery";
    /** 人脸分组1对1*/
    public static final String CMDW_TASKGROUPONETOONE= "CmdFGroupOneToOne";



    /*******************特征库*************************/
	/** 库id列表(新增加，删除）*/
	 public static final String LIBIDS= "libIds";

    /*******************人脸入库参数*************************/
	/** 入库ID*/
    public static final String FACE_LIBID="libId";
	/** 入库图片url列表*/
    public static final String FACE_IMAGES="imgs";
	/** 入库人员信息-姓名*/
	public static final String FACE_PS_NAME="name";
	/** 入库人员信息-年龄*/
	public static final String FACE_PS_AGE="age";
	/** 入库人员信息-性别*/
    public static final String FACE_PS_SEX="sex";
	/** 入库人员信息-身份证*/
	public static final String FACE_PS_IDCARD="idCard";
	/** 入库人员信息-特征信息*/
	public static final String FACE_PS_FEATURE="feature";


    /*******************人脸删除参数************************/
    public static final String FACE_FACEID="faceIds";

    /***********************人脸信息检索*********************/
    /**每个库最大返回结果数, 0~100*/
    public static final String FACE_MINSCORE="minScore";
    /**每个库最大返回结果数, 0~100 */
    public static final String FACE_MAXRETNB="maxRetNb";
    /**任务ID*/
    public static final String TASKID_CAMERAID="taskId_cameraId";
    public static final String TASKID_VDURL="vdUrl";
    public static final String TASKID_VDTYPE="vdType";
   /**任务列表*/
    public static final String TASKID_LISTS="taskIdCameraIds";





}
