/*
import com.dkha.communication.CommunicationApplication;
import com.dkha.communication.services.ResultProtocolAnalysis;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

*/
/**
 * @version V1.0
 * @Description: TODO:
 * @Title:
 * @Package PACKAGE_NAME
 * @author: huangyugang
 * @date: 2019/12/2 11:23
 * @Copyright: 成都电科慧安
 *//*

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {CommunicationApplication.class})
public class ResultProtocolAnalysisTest {

    @Autowired
    ResultProtocolAnalysis resultProtocolAnalysis;

    @Test
    public void testsendResultLibPush() {
        String resultMsg = "{\"head\":{\"protId\":\"CmdLibPush\",\"wappId\":168466,\"serId\":1918169211,\"ret\":0,\"errInfo\":\"\",\"version\":\"3.2.0\"},\"faceIds\":{\"img2\":{\"faceIds\":[\"711f3eb84d174c1e93e6e2e42a2a0449\"],\"reSizeH\":0,\"reSizeW\":0},\"img1\":{\"faceIds\":[\"49a7e94f7d8b48738b44e56ec04126d2\",\"3f64d5ec31f542bca9569c2b840173f0\",\"507f0100ac19486bbea2e4b1d72a4095\"],\"reSizeH\":0,\"reSizeW\":0}},\"position\":{\"711f3eb84d174c1e93e6e2e42a2a0449\":{\"faceId\":\"711f3eb84d174c1e93e6e2e42a2a0449\",\"rect\":{\"left\":62,\"top\":31,\"right\":122,\"bottom\":113,\"fscore\":0.99996495},\"p5s\":{\"x1\":77,\"y1\":63,\"x2\":107,\"y2\":63,\"x3\":91,\"y3\":80,\"x4\":79,\"y4\":93,\"x5\":103,\"y5\":94},\"extId\":\"img2\"},\"49a7e94f7d8b48738b44e56ec04126d2\":{\"faceId\":\"49a7e94f7d8b48738b44e56ec04126d2\",\"rect\":{\"left\":290,\"top\":140,\"right\":393,\"bottom\":271,\"fscore\":0.9999906},\"p5s\":{\"x1\":303,\"y1\":196,\"x2\":346,\"y2\":188,\"x3\":321,\"y3\":227,\"x4\":315,\"y4\":241,\"x5\":356,\"y5\":234},\"extId\":\"img1\"},\"3f64d5ec31f542bca9569c2b840173f0\":{\"faceId\":\"3f64d5ec31f542bca9569c2b840173f0\",\"rect\":{\"left\":133,\"top\":16,\"right\":238,\"bottom\":152,\"fscore\":0.9999906},\"p5s\":{\"x1\":169,\"y1\":76,\"x2\":213,\"y2\":62,\"x3\":209,\"y3\":93,\"x4\":185,\"y4\":121,\"x5\":222,\"y5\":107},\"extId\":\"img1\"},\"507f0100ac19486bbea2e4b1d72a4095\":{\"faceId\":\"507f0100ac19486bbea2e4b1d72a4095\",\"rect\":{\"left\":315,\"top\":324,\"right\":419,\"bottom\":470,\"fscore\":0.9999906},\"p5s\":{\"x1\":342,\"y1\":384,\"x2\":394,\"y2\":384,\"x3\":366,\"y3\":413,\"x4\":345,\"y4\":433,\"x5\":392,\"y5\":432},\"extId\":\"img1\"}},\"libId\":\"panhuiDB\",\"featIds\":[\"3f64d5ec31f542bca9569c2b840173f0\",\"49a7e94f7d8b48738b44e56ec04126d2\",\"507f0100ac19486bbea2e4b1d72a4095\",\"711f3eb84d174c1e93e6e2e42a2a0449\"]}";
        resultProtocolAnalysis.ResultProtocolRresolution(resultMsg);
    }

    @Test
    public void testsendResultLibPop() {
        String resultMsg = "{\"head\":{\"protId\":\"CmdLibPop\",\"wappId\":168466,\"serId\":1918169213,\"ret\":0,\"errInfo\":\"\",\"version\":\"3.2.0\"},\"libId\":\"panhuiDB\",\"featIds\":[\"711f3eb84d174c1e93e6e2e42a2a0449\"]}";
        resultProtocolAnalysis.ResultProtocolRresolution(resultMsg);
    }

    @Test
    public void testsendFaceSearchLib() {
        String resultMsg = "{\"head\":{\"protId\":\"CmdFSearch\",\"wappId\":168466,\"serId\":1918169211,\"ret\":0,\"errInfo\":\"\",\"version\":\"3.2.0\"},\"faceIds\":{\"img2\":{\"faceIds\":[\"943ce889946443b6aba4d54cd4c796f6\"],\"reSizeH\":0,\"reSizeW\":0},\"img1\":{\"faceIds\":[\"5e5611f9f3c42f9bbfd5d9962474115\"],\"reSizeH\":0,\"reSizeW\":0}},\"position\":{\"5e5611f9f3c42f9bbfd5d9962474115\":{\"faceId\":\"5e5611f9f3c42f9bbfd5d9962474115\",\"rect\":{\"left\":62,\"top\":31,\"right\":122,\"bottom\":113,\"fscore\":0.99996495},\"extId\":\"img1\"},\"943ce889946443b6aba4d54cd4c796f6\":{\"faceId\":\"943ce889946443b6aba4d54cd4c796f6\",\"rect\":{\"left\":72,\"top\":46,\"right\":143,\"bottom\":149,\"fscore\":0.9992924},\"extId\":\"img2\"}},\"libSchScore\":{\"943ce889946443b6aba4d54cd4c796f6\":{\"scoreList\":[{\"libId\":\"panhuiDB\",\"featId\":\"33af5fb2d2664fb2bc48c572c560ba44\",\"score\":0.88060117},{\"libId\":\"panhuiDB\",\"featId\":\"369cf5caa17748a7983abbc2e0063ada\",\"score\":0.9999906},{\"libId\":\"panhuiDB\",\"featId\":\"6e2ebaf7b99943c88acf97f9020b265f\",\"score\":0.9999906},{\"libId\":\"panhuiDB\",\"featId\":\"d338023947fc4a36b3aba51bd59fe1d1\",\"score\":0.9999906},{\"libId\":\"panhuiDB\",\"featId\":\"59daad838fe644b2afb4911e74a74c30\",\"score\":0.9999906}]},\"5e5611f9f3c42f9bbfd5d9962474115\":{\"scoreList\":[{\"libId\":\"panhuiDB\",\"featId\":\"112f19f25acb4d6291e59127143e1487\",\"score\":0.9999947},{\"libId\":\"panhuiDB\",\"featId\":\"aade68b6778649ce8419f33d1a01d46d\",\"score\":0.9999947},{\"libId\":\"panhuiDB\",\"featId\":\"33af5fb2d2664fb2bc48c572c560ba44\",\"score\":0.9999947},{\"libId\":\"panhuiDB\",\"featId\":\"2e014bf2e190449aafab054d1a5a62f9\",\"score\":0.9999947},{\"libId\":\"panhuiDB\",\"featId\":\"424edef45b8248c4a517f74c8eead741\",\"score\":0.9999947}]}},\"attribute\":{}}";
        resultProtocolAnalysis.ResultProtocolRresolution(resultMsg);
    }

    @Test
    public void testsendAlarmAndCheck() {
        String resultMsg = "{\"head\":{\"protId\":\"CmdVdFSAlarm\",\"wappId\":2419891,\"serId\":1575356122,\"ret\":0,\"errInfo\":\"\",\"version\":\"3.2.0\"},\"urls\":{\"img1\":\"g3241/M00/D5/55/wKgD8V3kzXaAJoMvAAGH7XPYKsU283.jpg\"},\"faceIds\":{\"img1\":{\"faceIds\":[\"e574a8a08128400694774069e3c9a4ff\"],\"reSizeH\":0,\"reSizeW\":0}},\"position\":{\"e574a8a08128400694774069e3c9a4ff\":{\"faceId\":\"e574a8a08128400694774069e3c9a4ff\",\"rect\":{\"left\":668,\"top\":627,\"right\":728,\"bottom\":694,\"fscore\":0.99872905},\"extId\":\"img1\"}},\"libSchScore\":{\"e574a8a08128400694774069e3c9a4ff\":{\"scoreList\":[{\"libId\":\"panhuiDB\",\"featId\":\"61a9a6ecbdf64ffdafaaaf789e07a6a1\",\"score\":0.5093412},{\"libId\":\"panhuiDB\",\"featId\":\"49a7e94f7d8b48738b44e56ec04126d2\",\"score\":0.52388746},{\"libId\":\"panhuiDB\",\"featId\":\"e267774293654788a23313d6c04baada\",\"score\":0.52388746},{\"libId\":\"panhuiDB\",\"featId\":\"98acaebe2f4c42339dda6bcca85afa4e\",\"score\":0.5469807},{\"libId\":\"panhuiDB\",\"featId\":\"3f64d5ec31f542bca9569c2b840173f0\",\"score\":0.5469807}]}},\"taskId\":\"task_id_ph_0001\",\"attribute\":{}}";
        resultProtocolAnalysis.ResultProtocolRresolution(resultMsg);
    }

    @Test
    public void testsendFaceGroup() {
        String resultMsg = "{\"head\":{\"protId\":\"CmdFGroup\",\"wappId\":2419891,\"serId\":1918169005,\"ret\":0,\"errInfo\":\"\",\"version\":\"3.2.0\"},\"faceIds\":{\"img3\":{\"faceIds\":[\"31bc742557e948cf84ea3e76163893ac\",\"246a4a7f39364865ab94ee236a133a1e\"],\"reSizeH\":0,\"reSizeW\":0},\"img2\":{\"faceIds\":[\"af4909821c5544f9856a7bc22f04e574\",\"8172d0b37884b2aaf5e9142ed8b6675\",\"f646cf7199854a41885c9f7c1e9531b9\"],\"reSizeH\":0,\"reSizeW\":0},\"img1\":{\"faceIds\":[],\"reSizeH\":0,\"reSizeW\":0}},\"position\":{\"af4909821c5544f9856a7bc22f04e574\":{\"faceId\":\"af4909821c5544f9856a7bc22f04e574\",\"rect\":{\"left\":366,\"top\":389,\"right\":534,\"bottom\":607,\"fscore\":0.99912614},\"extId\":\"img2\"},\"31bc742557e948cf84ea3e76163893ac\":{\"faceId\":\"31bc742557e948cf84ea3e76163893ac\",\"rect\":{\"left\":110,\"top\":76,\"right\":174,\"bottom\":154,\"fscore\":0.99781185},\"extId\":\"img3\"},\"f646cf7199854a41885c9f7c1e9531b9\":{\"faceId\":\"f646cf7199854a41885c9f7c1e9531b9\",\"rect\":{\"left\":549,\"top\":130,\"right\":711,\"bottom\":334,\"fscore\":0.99912614},\"extId\":\"img2\"},\"8172d0b37884b2aaf5e9142ed8b6675\":{\"faceId\":\"8172d0b37884b2aaf5e9142ed8b6675\",\"rect\":{\"left\":165,\"top\":275,\"right\":335,\"bottom\":493,\"fscore\":0.99912614},\"extId\":\"img2\"},\"246a4a7f39364865ab94ee236a133a1e\":{\"faceId\":\"246a4a7f39364865ab94ee236a133a1e\",\"rect\":{\"left\":185,\"top\":71,\"right\":239,\"bottom\":149,\"fscore\":0.99781185},\"extId\":\"img3\"}},\"fScoreComp\":[{\"feat1\":\"31bc742557e948cf84ea3e76163893ac\",\"feat2\":\"246a4a7f39364865ab94ee236a133a1e\",\"score\":0.8822819},{\"feat1\":\"31bc742557e948cf84ea3e76163893ac\",\"feat2\":\"af4909821c5544f9856a7bc22f04e574\",\"score\":0.6403769},{\"feat1\":\"31bc742557e948cf84ea3e76163893ac\",\"feat2\":\"8172d0b37884b2aaf5e9142ed8b6675\",\"score\":0.4685436},{\"feat1\":\"31bc742557e948cf84ea3e76163893ac\",\"feat2\":\"f646cf7199854a41885c9f7c1e9531b9\",\"score\":0.46368265},{\"feat1\":\"246a4a7f39364865ab94ee236a133a1e\",\"feat2\":\"af4909821c5544f9856a7bc22f04e574\",\"score\":0.5203994},{\"feat1\":\"246a4a7f39364865ab94ee236a133a1e\",\"feat2\":\"8172d0b37884b2aaf5e9142ed8b6675\",\"score\":0.4872659},{\"feat1\":\"246a4a7f39364865ab94ee236a133a1e\",\"feat2\":\"f646cf7199854a41885c9f7c1e9531b9\",\"score\":0.4220569},{\"feat1\":\"af4909821c5544f9856a7bc22f04e574\",\"feat2\":\"8172d0b37884b2aaf5e9142ed8b6675\",\"score\":0.5534415},{\"feat1\":\"af4909821c5544f9856a7bc22f04e574\",\"feat2\":\"f646cf7199854a41885c9f7c1e9531b9\",\"score\":0.5828601},{\"feat1\":\"8172d0b37884b2aaf5e9142ed8b6675\",\"feat2\":\"f646cf7199854a41885c9f7c1e9531b9\",\"score\":0.5657829}]}";
        resultProtocolAnalysis.ResultProtocolRresolution(resultMsg);
    }
    @Test
    public void testSendAlarmMsg(){
       String resultMsg="{\"head\":{\"protId\":\"CmdVdFSAlarm\",\"wappId\":2968525,\"serId\":1575455631,\"ret\":0,\"errInfo\":\"\",\"version\":\"3.2.0\"},\"urls\":{\"img1\":\"g3241/M00/24/34/wKgD8V3mI0KAJtmPAAG88qs66TU671.jpg\"},\"faceIds\":{\"img1\":{\"faceIds\":[\"c66b7165d3b94f8981493af4ab0f024f\"],\"reSizeH\":0,\"reSizeW\":0}},\"position\":{\"c66b7165d3b94f8981493af4ab0f024f\":{\"faceId\":\"c66b7165d3b94f8981493af4ab0f024f\",\"rect\":{\"left\":561,\"top\":529,\"right\":606,\"bottom\":584,\"fscore\":0.9995492},\"extId\":\"img1\"}},\"libSchScore\":{},\"taskId\":\"1201787174027800577_1201783763534360570\",\"attribute\":{}}";
        resultProtocolAnalysis.ResultProtocolRresolution(resultMsg);
    }
}
*/
