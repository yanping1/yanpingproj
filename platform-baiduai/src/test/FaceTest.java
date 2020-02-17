import com.baidu.aip.face.AipFace;
import com.dkha.baiduai.common.constant.ImageTypeEnum;
import com.dkha.baiduai.util.AipFaceClientUtil;
import com.dkha.baiduai.util.UniqueIDCreater;
import com.dkha.common.util.Base64ImageUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;

/**
 * @author hechenggang
 * @Date 2019/12/26 9:46
 */
public class FaceTest {

    private AipFace aipFace;

    @Before
    public void init() {
        aipFace = AipFaceClientUtil.getInstance();
    }

    @Test
    public void groupAdd() {

        JSONObject res = aipFace.groupAdd(String.valueOf(UniqueIDCreater.generateID()), new HashMap<String, String>());
        System.out.println(res.toString(2));
        System.out.println(res);
    }

    @Test
    public void groupDelete() {

        JSONObject res = aipFace.groupDelete("407535243999059968", new HashMap<String, String>());
        System.out.println(res.toString(2));
        System.out.println(res);
    }

    @Test
    public void addUser() {
        File file = new File("D:\\chome_download\\20191225140201.png");
        JSONObject res = aipFace.addUser(Base64ImageUtils.encodeImgageToBase64(file), ImageTypeEnum.BASE64.getValue(), "as132asd12312", String.valueOf(UniqueIDCreater.generateID()), new HashMap<String, String>());
        System.out.println(res.toString(2));
        System.out.println(res);
    }

    @Test
    public void detect() {
        File file = new File("D:\\chome_download\\20191225140201.png");
        JSONObject res = aipFace.detect(Base64ImageUtils.encodeImgageToBase64(file), ImageTypeEnum.BASE64.getValue(), new HashMap<String, String>());
        System.out.println(res.toString(2));
        System.out.println(res);
    }
}
