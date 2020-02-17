package com.dkha.common.util;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.dkha.common.entity.vo.position.PositionVO;
import com.dkha.common.validate.UtilValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64ImageUtils {

    public static final Logger logger = LoggerFactory.getLogger(Base64ImageUtils.class);

	   /**
     * 将网络图片进行Base64位编码
     *
     * @param imageUrl
     *            图片的url路径，如http://.....xx.jpg
     * @return
     */
    public static String encodeImgageToBase64(URL imageUrl) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        ByteArrayOutputStream outputStream = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(imageUrl);
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", outputStream);
         // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            byte[] by=outputStream.toByteArray();
            return encoder.encode(by);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        	 if(outputStream!=null) {
             	try {
     				outputStream.close();
     			} catch (IOException e) {
     				// TODO Auto-generated catch block
     				e.printStackTrace();
     			}
             }
		}


        return null;///encoder.encode(by);// 返回Base64编码过的字节数组字符串
    }

    /**
     * 将图片流转换为Base64
     * @param bufferedImage
     * @return
     */
    public static String encodeImgageToBase64(BufferedImage bufferedImage) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        ByteArrayOutputStream outputStream = null;
        try {
//            BufferedImage bufferedImage = ImageIO.read(imageUrl);
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", outputStream);
         // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            byte[] by=outputStream.toByteArray();
            return encoder.encode(by);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        	 if(outputStream!=null) {
             	try {
     				outputStream.close();
     			} catch (IOException e) {
     				// TODO Auto-generated catch block
     				e.printStackTrace();
     			}
             }
		}


        return null;///encoder.encode(by);// 返回Base64编码过的字节数组字符串
    }

    /**
     * 将本地图片进行Base64位编码
     *
     * @param imageFile
     *            图片的url路径，如F:/.....xx.jpg
     * @return
     */
    public static String encodeImgageToBase64(File imageFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        ByteArrayOutputStream outputStream = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", outputStream);
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();

            byte[] by=outputStream.toByteArray();
            return encoder.encode(by);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        	if(outputStream!=null) {
            	try {
    				outputStream.close();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
            }
		}
        return null;
       // 返回Base64编码过的字节数组字符串
    }

    /**
     * 将Base64位编码的图片进行解码，并保存到指定目录
     *
     * @param base64
     *            base64编码的图片信息
     * @return
     */
    public static void decodeBase64ToImage(String base64, String path,
            String imgName) {
        BASE64Decoder decoder = new BASE64Decoder();
        FileOutputStream write =null;
        try {
        	write = new FileOutputStream(new File(path
                    + imgName));
            byte[] decoderBytes = decoder.decodeBuffer(base64);
            write.write(decoderBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        	try {
				write.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
    }

    /**
     * 将Base64位编码的图片进行解码，输出byte
     *
     * @param base64
     *            base64编码的图片信息
     * @return
     */
    public static byte[] decodeBase64ToImage(String base64) {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] decoderBytes = decoder.decodeBuffer(base64);
            return decoderBytes;
        } catch (IOException e) {
        	 return null;
        }finally {
		}
    }

    /**
     *
     * @return
     */
    public static InputStream encodeImgage(BufferedImage subimage) {
        ByteArrayOutputStream outputStream = null;
        try {
            /**如果根据坐标找不到人脸的时候*/
            if(UtilValidate.isNotEmpty(subimage)) {
                outputStream = new ByteArrayOutputStream();
                ImageIO.write(subimage, "png", outputStream);
                byte[] by = outputStream.toByteArray();
                /**将byte转为InputStream*/
                InputStream sbs = new ByteArrayInputStream(by);
                return sbs;
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static InputStream encodeImgage(BufferedImage subimage,String imagetype) {
        ByteArrayOutputStream outputStream = null;
        try {
            /**如果根据坐标找不到人脸的时候*/
            if(UtilValidate.isNotEmpty(subimage)) {
                outputStream = new ByteArrayOutputStream();
                ImageIO.write(subimage, "png", outputStream);
                byte[] by = outputStream.toByteArray();
                /**将byte转为InputStream*/
                InputStream sbs = new ByteArrayInputStream(by);
                return sbs;
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 对返回的人脸坐标进行扩大处理
     * deelPostion
     * @param picwidth 原图长
     * @param picheight 原图宽
     * @return  ImgPositionVO
     */
    public static PositionVO deelPostion(PositionVO positionVO,Integer picwidth,Integer picheight)
    {

        int xold = positionVO.getX();
        int yold = positionVO.getY();
        int widthold = positionVO.getW();
        int heightold = positionVO.getH();

        int x = ((xold - widthold / 2));
        int y = (yold - heightold / 2);
        int width = widthold * 2;
        int height = heightold * 2;
        if (width > picwidth) {
            width = picwidth;
        }
        if (height > picheight) {
            height = picheight;
        }

        if (x + width > picwidth) {
            x = x - picwidth;
        }

        if (x < 0) {x = 0;}
        if (y + height > picheight) {
            y = y - picheight;

        }
        if (y < 0) {y = 0;}
        return new PositionVO(x, y, width, height);
    }
    /**
     * 根据坐标 获取头部位置
     * @param
     * @param positionVo 坐标类
     * @return
     */
    public static InputStream encodeHeadImage(BufferedImage bufferedImage, PositionVO positionVo) {
        try {
//            BufferedImage bufferedImage = ImageIO.read(url);
            //获取整个背景图片的宽度和高度
//            int picWidth = bufferedImage.getWidth();
//            int picHeight = bufferedImage.getHeight();
//
//            int xOld = positionVo.getX();
//            int yOld = positionVo.getY();
//            int widthOld = positionVo.getW();
//            int heightOld = positionVo.getH();
//
//            int x = ((xOld - widthOld/2));
//            int y = (yOld - heightOld/2);
//            int width = widthOld * 2;
//            int height = heightOld * 2;
//            if(width > picWidth)
//            {
//                width = picWidth;
//            }
//            if(height > picHeight)
//            {
//                height = picHeight;
//            }
//
//            if(x+width > picWidth)
//            {
//                x = x - picWidth;
//            }
//
//            if(x < 0) {
//                x = 0;
//            }
//            if(y + height > picHeight)
//            {
//                y= y - picHeight;
//
//            }
//            if(y < 0) {
//                y = 0;
//            }
            BufferedImage subimage = bufferedImage.getSubimage(positionVo.getX(), positionVo.getY(), positionVo.getW(), positionVo.getH());
            return encodeImgage(subimage);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static void main(String [] args){
        URL url = null;
        try {
            url=new URL("http://10.51.10.201:88/xxx.jpg");
//            url = new URL("http://img1.imgtn.bdimg.com/it/u=2064054871,2037178638&fm=200&gp=0.jpg");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Integer l=276,r=381,top=190,bottom=337;
        Integer x=l,y=top,w=(r-l),h=bottom-top;
//        768 x 576
        Integer bgwidth=768,bgheight=576;
        PositionVO positionVo = new PositionVO(x,y,w,h);
        positionVo = Base64ImageUtils.deelPostion(positionVo, bgwidth, bgheight);
        try {

            BufferedImage bufferedImage = ImageIO.read(new File("D:/test/x/xxx.jpg"));
            BufferedImage bufferedImage1=bufferedImage.getSubimage(positionVo.getX(),positionVo.getY(),positionVo.getW(),positionVo.getH());
            String  baseimg=Base64ImageUtils.encodeImgageToBase64(bufferedImage1);

            //            String encoderStr = Base64ImageUtils.encodeImgageToBase64(url);
//            System.out.println(encoderStr);
            Base64ImageUtils.decodeBase64ToImage(baseimg, "D:/test/x/", "footballxx.jpg");
        }catch (Exception e)
        {

        }
    }

}
