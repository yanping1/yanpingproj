//import com.dkha.communication.CommunicationApplication;
//import com.dkha.communication.services.ResultProtocolAnalysis;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.amqp.rabbit.connection.CorrelationData;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.UUID;
//
///**
// * @version V1.0
// * @Description:
// * @Title:
// * @author: huangyugang
// * @date: 2019/12/10 15:38
// * @Copyright: 成都电科慧安
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {CommunicationApplication.class })
//public class RabbitMqMsqTest {
//    @Autowired
//   ResultProtocolAnalysis resultProtocolAnalysis;
//
//    @Test
//    public void  rabbitmqTest(){
//        String msg="{\"head\":{\"protId\":\"CmdVdFSAlarm\",\"wappId\":9055931,\"serId\":1576069704,\"ret\":0,\"errInfo\":\"\",\"version\":\"3.2.0\"},\"urls\":{\"img1\":\"g10203/M00/8A/E2/CjMKy13wuhCABhPoAAEZyEW79ls538.jpg\"},\"faceIds\":{\"img1\":{\"faceIds\":[\"3ecdf23324884abe8dd00dc18605c0dc\"],\"reSizeH\":0,\"reSizeW\":0}},\"position\":{\"3ecdf23324884abe8dd00dc18605c0dc\":{\"faceId\":\"3ecdf23324884abe8dd00dc18605c0dc\",\"rect\":{\"left\":970,\"top\":391,\"right\":1023,\"bottom\":460,\"fscore\":0.9354447},\"extId\":\"img1\"}},\"libSchScore\":{},\"taskId\":\"1204662172452052993_1204662751894126593\",\"attribute\":{}}\n";
//        String msg1="{\"head\":{\"protId\":\"CmdVdStatusChg\",\"wappId\":12402098,\"serId\":1918169264,\"version\":\"3.2.0\",\"ret\":0,\"errInfo\":\"\"},\"status\":{\"1206140049546686465_0\":\"StatusOver\"}}";
//
//        resultProtocolAnalysis.ResultProtocolRresolution(msg1);
//    }
//
//
//}
