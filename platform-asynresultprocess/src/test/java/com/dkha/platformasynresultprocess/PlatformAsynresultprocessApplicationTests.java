//package com.dkha.platformasynresultprocess;
//
//
//import com.alibaba.fastjson.JSONObject;
//import com.dkha.platformasynresultprocess.common.WSProtocalConst;
//import com.dkha.platformasynresultprocess.service.AlarmMsgReceiver;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Map;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {PlatformAsynresultprocessApplication.class})
//public class PlatformAsynresultprocessApplicationTests {
//
//    @Autowired
//    AlarmMsgReceiver alarmMsgReceiver;
//
//    @Test
//    public void SendAlarmMsgTest() throws Exception {
//        String msg=" {\"head\":{\"protId\":\"CmdVdFSAlarm\",\"wappId\":9055931,\"serId\":1576153180,\"ret\":0,\"errInfo\":\"\",\"version\":\"3.2.0\"},\"urls\":{\"img1\":\"g10203/M00/AD/92/CjMKy13x89aASl-eAACHezFmpko445.jpg\"},\"faceIds\":{\"img1\":{\"faceIds\":[\"3e5063099428435ebe9f1ac754f95f2f\",\"7c5703ae9124520ab91e9c4a77f3891\",\"2e2bb34e0d854a3a8ba9d3457632ab1b\"],\"reSizeH\":0,\"reSizeW\":0}},\"position\":{\"2e2bb34e0d854a3a8ba9d3457632ab1b\":{\"faceId\":\"2e2bb34e0d854a3a8ba9d3457632ab1b\",\"rect\":{\"left\":947,\"top\":324,\"right\":1015,\"bottom\":413,\"fscore\":0.99999845},\"extId\":\"img1\"},\"3e5063099428435ebe9f1ac754f95f2f\":{\"faceId\":\"3e5063099428435ebe9f1ac754f95f2f\",\"rect\":{\"left\":588,\"top\":307,\"right\":808,\"bottom\":567,\"fscore\":0.99999845},\"extId\":\"img1\"},\"7c5703ae9124520ab91e9c4a77f3891\":{\"faceId\":\"7c5703ae9124520ab91e9c4a77f3891\",\"rect\":{\"left\":346,\"top\":71,\"right\":541,\"bottom\":325,\"fscore\":0.99999845},\"extId\":\"img1\"}},\"libSchScore\":{\"3e5063099428435ebe9f1ac754f95f2f\":{\"scoreList\":[{\"libId\":\"1205010013104058369\",\"featId\":\"101e25fe9a1c4713ae3f32888a9b7ec8\",\"score\":0.89275336}]},\"7c5703ae9124520ab91e9c4a77f3891\":{\"scoreList\":[{\"libId\":\"1205010013104058369\",\"featId\":\"86856d2c65694cd38554cf8c72479f0b\",\"score\":0.9114249}]}},\"taskId\":\"1205030695036694529_1204662751894126593\",\"attribute\":{}}\n";
//        String msg1="{\"head\":{\"protId\":\"CmdVdFSAlarm\",\"wappId\":9055931,\"serId\":1576153180,\"ret\":0,\"errInfo\":\"\",\"version\":\"3.2.0\"},\"urls\":{\"img1\":\"g10203/M00/AD/92/CjMKy13x89aASl-eAACHezFmpko445.jpg\"},\"faceIds\":{\"img1\":{\"faceIds\":[\"3e5063099428435ebe9f1ac754f95f2f\",\"7c5703ae9124520ab91e9c4a77f3891\",\"2e2bb34e0d854a3a8ba9d3457632ab1b\"],\"reSizeH\":0,\"reSizeW\":0}},\"position\":{\"2e2bb34e0d854a3a8ba9d3457632ab1b\":{\"faceId\":\"2e2bb34e0d854a3a8ba9d3457632ab1b\",\"rect\":{\"left\":947,\"top\":324,\"right\":1015,\"bottom\":413,\"fscore\":0.99999845},\"extId\":\"img1\"},\"3e5063099428435ebe9f1ac754f95f2f\":{\"faceId\":\"3e5063099428435ebe9f1ac754f95f2f\",\"rect\":{\"left\":588,\"top\":307,\"right\":808,\"bottom\":567,\"fscore\":0.99999845},\"extId\":\"img1\"},\"7c5703ae9124520ab91e9c4a77f3891\":{\"faceId\":\"7c5703ae9124520ab91e9c4a77f3891\",\"rect\":{\"left\":346,\"top\":71,\"right\":541,\"bottom\":325,\"fscore\":0.99999845},\"extId\":\"img1\"}},\"libSchScore\":{},\"taskId\":\"1205030695036694529_1204662751894126593\",\"attribute\":{}}\n";
//        String msg2=" {\"timestamp\":1576229773415,\"loaddata\":{\"head\":{\"protId\":\"CmdVdStatusChg\",\"wappId\":8963385,\"serId\":6585,\"ret\":0,\"errInfo\":\"\",\"version\":\"3.2.0\"},\"status\":{\"1205421152681111554_1204600573426089986\":\"StatusStarting\"}}}\n";
//String msg3="{\"timestamp\":1576564127315,\"loaddata\":{\"head\":{\"protId\":\"CmdVdFSAlarm\",\"wappId\":16542894,\"serId\":1576566615,\"ret\":0,\"errInfo\":\"\",\"version\":\"3.2.0\"},\"urls\":{\"img1\":\"g10203/M00/15/CE/CjMKy134daWAVW5RAABq3_a4Ga0979.jpg\"},\"faceIds\":{\"img1\":{\"faceIds\":[\"736311dd4e6e4dba8f49fa63e4a747a8\"],\"reSizeH\":0,\"reSizeW\":0}},\"position\":{\"736311dd4e6e4dba8f49fa63e4a747a8\":{\"faceId\":\"736311dd4e6e4dba8f49fa63e4a747a8\",\"rect\":{\"left\":152,\"top\":89,\"right\":473,\"bottom\":461,\"fscore\":0.99941146},\"extId\":\"img1\"}},\"libSchScore\":{},\"taskId\":\"1206779071159787521_1206777167948210178\",\"attribute\":{\"736311dd4e6e4dba8f49fa63e4a747a8\":{\"age\":42,\"gender\":1}}}}";
//        //        Map<String, Object> rsmap = (Map<String, Object>) JSONObject.parseObject(msg);
////        if (rsmap != null) {
////            //数据判断
////            Map<String, Object> head = (Map<String, Object>) rsmap.get(WSProtocalConst.HEADER);
////            alarmMsgReceiver.isCheckAlarmIsHitCompare(head, rsmap);
////        }
//        alarmMsgReceiver.process(msg3,null,null);
//
//
//
//    }
//
//
//}
