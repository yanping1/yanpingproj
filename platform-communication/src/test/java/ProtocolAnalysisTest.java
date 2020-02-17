
import com.alibaba.fastjson.JSON;
import com.dkha.common.entity.vo.ApiVO;
import com.dkha.common.entity.vo.control.ControlVO;
import com.dkha.common.entity.vo.facelib.FaceDeleteVO;
import com.dkha.common.entity.vo.facelib.FaceLibaryVO;
import com.dkha.common.entity.vo.facelib.PersonalInformationVO;
import com.dkha.common.entity.vo.libary.LibaryVO;
import com.dkha.common.entity.vo.search.FaceCheckVO;
import com.dkha.common.entity.vo.search.FaceSearchVO;
import com.dkha.communication.CommunicationApplication;
import com.dkha.communication.common.ProtocolAnalysisConst;
import com.dkha.communication.httpws.cache.WappIdCache;
import com.dkha.communication.services.RequestProtocolAnalysis;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @version V1.0
 * @Description: TODO:
 * @Title: IndexContral
 * @Package PACKAGE_NAME
 * @author: huangyugang
 * @date: 2019/11/27 16:52
 * @Copyright: 成都电科慧安*/



//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {CommunicationApplication.class })
//@SpringBootTest(classes = {ProtocolAnalysisTest.class })
public class ProtocolAnalysisTest {
    //@Autowired
//    RequestProtocolAnalysis protocolAnalysis;
//
//    @Test
//    public void testrequestProtocolRresolution() {
//        String jsonString = "{\"cmd\": \"CmdLibCreate\",\"libIds\": [\"test1 \",\"test2 \",\"test3 \"]}";
//        Map<String, Object> rmp = (Map<String, Object>) JSON.parse(jsonString);
//        Map<String, Object> rs = protocolAnalysis.requestProtocolRresolution(rmp,null);
//        System.out.println(JSON.toJSONString(rs,true));
//    }
//
//    //01 创建特征库 {"head":{"ret":0,"errInfo":"","wappId":22122,"protId":"CmdLibCreate","serId":1},"libIds":["Test1","Test2"]}
//    @Test
//    public void testcreateLibToWSJsonString(){
//        ApiVO apiVO=new ApiVO();
//        apiVO.setCmd(ProtocolAnalysisConst.CMDLIB_CREATE);
//        LibaryVO libaryVO=new LibaryVO();
//        List<String> listids=new ArrayList<>();
//        listids.add("Test1");
//        listids.add("Test2");
//        libaryVO.setLibIds(listids);
//        apiVO.setData(libaryVO);
//        String jsonString =JSON.toJSONString(apiVO);
//        System.out.println(jsonString);
//
//        Map<String, Object> rmp = (Map<String, Object>) JSON.parse(jsonString);
//        Map<String, Object> rs = protocolAnalysis.requestProtocolRresolution(rmp,null);
//        System.out.println("创建特征库"+JSON.toJSONString(rs,true));
//
//    }
//    //02 删除特征库
//    @Test
//    public  void testdeleteLibToWSJsonString(){
//        ApiVO apiVO=new ApiVO();
//        apiVO.setCmd(ProtocolAnalysisConst.CMDLIB_DELETE);
//        LibaryVO libaryVO=new LibaryVO();
//        libaryVO.setLibIds(Arrays.asList(new String[]{"Test1","Test2"}));
//        apiVO.setData(libaryVO);
//        String jsonString =JSON.toJSONString(apiVO);
//        //Map<String, Object> rmp = (Map<String, Object>) JSON.parse(jsonString);
//        //Map<String, Object> rs = protocolAnalysis.deleteLibToWSJsonString(rmp, WappIdCache.WappIdTypeEnum.PICTURE,null);
//       // System.out.println("删除特征库 "+JSON.toJSONString(rs,true));
//    }
//
//
//    //03 人像数据新增 {"cmd":"CmdLibPop","data":{"imgs":["01.jpeg","02.jpeg","03.jpeg"],"libId":"db01","personalInformation":{"age":"22","feature":"HH","idCard":"1234566","sex":"女"}}}
//    @Test
//    public void testpushFaceinfoLibToWSJsonString() {
//        ApiVO apiVO=new ApiVO();
//        apiVO.setCmd(ProtocolAnalysisConst.CMDLIB_PUSH);
//        FaceLibaryVO facelib=new FaceLibaryVO();
//        //2019-12-2 和潘辉 确定需要该Key值为请求的地址
//            facelib.setImgs(Arrays.asList(new String[]{"http://192.168.3.240:88/mytest/1.jpg","http://192.168.3.240:88/mytest/2.jpg","http://192.168.3.240:88/mytest/3.jpg"}));
//            facelib.setLibId("db01");
//              PersonalInformationVO pvo =new PersonalInformationVO();
//              pvo.setAge("22");
//              pvo.setFeature("HH");
//              pvo.setIdCard("1234566");
//              pvo.setSex("女");
//            facelib.setPersonalInformation(pvo);
//        apiVO.setData(facelib);
//        String jsonString =JSON.toJSONString(apiVO);
//
//        Map<String, Object> rmp = (Map<String, Object>) JSON.parse(jsonString);
//        Map<String, Object> rs = protocolAnalysis.pushFaceinfoLibToWSJsonString(rmp,WappIdCache.WappIdTypeEnum.PICTURE,null);
//        System.out.println("人像数据新增"+JSON.toJSONString(rs,true));
//    }
//
//    //04 人脸数据删除
//    @Test
//    public void testpopFaceInfoLibToWSJsonString(){
//        ApiVO apiVO=new ApiVO();
//        apiVO.setCmd(ProtocolAnalysisConst.CMDLIB_POP);
//        FaceDeleteVO faceDeleteVO=new FaceDeleteVO();
//        faceDeleteVO.setLibId("db01");
//        faceDeleteVO.setFaceIds(Arrays.asList(new String[]{"f0e681068f794f08a0744aa8a2c6e210","f0e681068f794f08a0744aa8a2c6e210"}));
//        apiVO.setData(faceDeleteVO);
//        String jsonString =JSON.toJSONString(apiVO);
//
//        Map<String, Object> rmp = (Map<String, Object>) JSON.parse(jsonString);
//        Map<String, Object> rs= protocolAnalysis.popFaceInfoLibToWSJsonString(rmp,WappIdCache.WappIdTypeEnum.PICTURE,null);
//        System.out.println("人脸数据删除"+JSON.toJSONString(rs,true));
//    }
//
//    //05 人脸库检索
//    @Test
//    public void testfaceSearchLibToWSJsonString() {
//        ApiVO apiVO=new ApiVO();
//        apiVO.setCmd(ProtocolAnalysisConst.CMDF_SEARCH);
//        FaceSearchVO faceSearchVO=new FaceSearchVO();
//        faceSearchVO.setMaxRetNb(5);
//        faceSearchVO.setMinScore(7.0);
//        faceSearchVO.setImgs(Arrays.asList(new String[]{"a.jpg","b.jpg","c.jpg"}));
//        faceSearchVO.setLibIds(Arrays.asList(new String[]{"DB1","DB2","DB3"}));
//        apiVO.setData(faceSearchVO);
//
//        String orgistring = JSON.toJSONString(apiVO);
//        System.out.println(orgistring);
//       // Map<String, Object> rmp = (Map<String, Object>) JSON.parse(orgistring);
//        //Map<String, Object> rs = protocolAnalysis.faceSearchLibToWSJsonString(rmp,1,WappIdCache.WappIdTypeEnum.PICTURE,null);
//       // System.out.println("人脸库检索"+JSON.toJSONString(rs,true));
//    }
//
//    //06 人脸检测
//    @Test
//    public void testfaceSearchLibToWSJsonString02() {
//        ApiVO apiVO=new ApiVO();
//        apiVO.setCmd(ProtocolAnalysisConst.CMDF_SEARCHFACE);
//        FaceCheckVO faceSearchVO=new FaceCheckVO();
//        faceSearchVO.setImgs(Arrays.asList(new String[]{"a.jpg","b.jpg","c.jpg"}));
//        apiVO.setData(faceSearchVO);
//
//        String orgistring = JSON.toJSONString(apiVO);
//        System.out.println(orgistring);
//        Map<String, Object> rmp = (Map<String, Object>) JSON.parse(orgistring);
//        Map<String, Object> rs = protocolAnalysis.faceSearchLibToWSJsonString(rmp,2,WappIdCache.WappIdTypeEnum.PICTURE,null);
//        System.out.println("人脸检测"+JSON.toJSONString(rs,true));
//    }
//    //07 人脸分组
//    @Test
//    public void testfaceGroupLibToWSJsonString(){
//        ApiVO apiVO=new ApiVO();
//        apiVO.setCmd(ProtocolAnalysisConst.CMDF_SEARCHFACE);
//        FaceCheckVO faceSearchVO=new FaceCheckVO();
//        faceSearchVO.setImgs(Arrays.asList(new String[]{"a.jpg","b.jpg","c.jpg"}));
//        apiVO.setData(faceSearchVO);
//        String orgistring = JSON.toJSONString(apiVO);
//        System.out.println(orgistring);
//        Map<String, Object> rmp = (Map<String, Object>) JSON.parse(orgistring);
//        Map<String, Object> rs = protocolAnalysis.faceGroupLibToWSJsonString(rmp,WappIdCache.WappIdTypeEnum.PICTURE,null);
//        System.out.println("人脸检测"+JSON.toJSONString(rs,true));
//    }
//    //08 布控新增
//    @Test
//    public void testaddfaceWTaskToWSJsonString(){
//
//        ApiVO apiVO=new ApiVO();
//        apiVO.setCmd(ProtocolAnalysisConst.CMDW_TASKADD);
//        ControlVO controlVO=new ControlVO();
//        controlVO.setLibIds(Arrays.asList(new String[]{"db01","db02","db03"}));
//        controlVO.setMaxRetNb(0.5);
//        controlVO.setMinScore(2.0);
//        controlVO.setVdType("VdUrlRtsp");
//        controlVO.setVdUrl("rtsp://admin:xxx@127.0.0.1/h264/ch1");
//        controlVO.setTaskId_cameraId("task_123123123");
//        apiVO.setData(controlVO);
//        String orgistring = JSON.toJSONString(apiVO);
//        System.out.println(orgistring);
////        Map<String, Object> rmp = (Map<String, Object>) JSON.parse(orgistring);
////        Map<String, Object> rs  = protocolAnalysis.addfaceWTaskToWSJsonString(rmp,WappIdCache.WappIdTypeEnum.VIDEO,null);
////        System.out.println("人脸检测"+JSON.toJSONString(rs,true));
//
//    }
//    //09 布控删除
//    @Test
//    public void testdeletefaceWTaskToWSJsonString(){
//
//        ApiVO apiVO=new ApiVO();
//        apiVO.setCmd(ProtocolAnalysisConst.CMDW_TASKDEL);
//        ControlVO controlVO=new ControlVO();
//
//        controlVO.setTaskIdCameraIds(Arrays.asList(new String[]{"task001","task002","task003"}));
//        apiVO.setData(controlVO);
//        String orgistring = JSON.toJSONString(apiVO);
//        System.out.println(orgistring);
//        Map<String, Object> rmp = (Map<String, Object>) JSON.parse(orgistring);
//        Map<String, Object> rs  = protocolAnalysis.deletefaceWTaskToWSJsonString(rmp,WappIdCache.WappIdTypeEnum.VIDEO,null);
//        System.out.println("人脸检测"+JSON.toJSONString(rs,true));
//
//    }
//    @Test
//      public void testStringFormate(){
//        String timestampstr="12312412";
//        String msg="{\"head\":{\"protId\":\"CmdVdFSAlarm\",\"wappId\":8445669,\"serId\":1576804291,\"ret\":0,\"errInfo\":\"\",\"version\":\"3.2.0\",\"time\":1576803685},\"urls\":{\"img1\":\"g10203/M00/5B/3C/CjMKy138HWWAWwNvAAHu3sHDY00274.jpg\"},\"faceIds\":{\"img1\":{\"faceIds\":[\"ae3a6fd8c59243509b038b72a0766415\"],\"reSizeH\":0,\"reSizeW\":0}},\"position\":{\"ae3a6fd8c59243509b038b72a0766415\":{\"faceId\":\"ae3a6fd8c59243509b038b72a0766415\",\"rect\":{\"left\":533,\"top\":484,\"right\":562,\"bottom\":529,\"fscore\":0.84315395},\"extId\":\"img1\"}},\"libSchScore\":{\"ae3a6fd8c59243509b038b72a0766415\":{\"scoreList\":[{\"libId\":\"panhuiDB\",\"featId\":\"71b73f1a4abe4b46b17516f4c0fc4649\",\"score\":0.45734015},{\"libId\":\"panhuiDB\",\"featId\":\"b11d8d2011aa484aa95a349a749afa2a\",\"score\":0.48650742}]}},\"taskId\":\"taskId_cm0001\",\"attribute\":{}}";
//        String sendmsg = String.format("{\"timestamp\":%s,\"loaddata\":%s}", timestampstr, msg);
//        System.out.println(sendmsg);
//    }



}

