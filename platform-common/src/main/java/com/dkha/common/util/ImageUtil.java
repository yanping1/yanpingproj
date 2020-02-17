package com.dkha.common.util;

import com.dkha.common.entity.vo.position.PositionVO;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ImageUtil {

    /**
     * 验证目录是否存在
     * makeFile
     *
     * @param path void
     */
    public static void makeFile(String path) {
        File file1 = new File(path);
        if (!file1.exists()) {
            file1.mkdirs();
        }
    }

    public static byte[] bufferConverByte(BufferedImage bf) {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bf, "jpg", outputStream);
            // 对字节数组Base64编码
            byte[] by = outputStream.toByteArray();
            return by;
        } catch (MalformedURLException e1) {
        } catch (IOException e) {
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

//    public static BufferedImage iplToBufImgData(IplImage mat) {
//        if (mat.height() > 0 && mat.width() > 0) {
//            BufferedImage image = new BufferedImage(mat.width(), mat.height(),
//                    BufferedImage.TYPE_3BYTE_BGR);
//            WritableRaster raster = image.getRaster();
//            DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
//            byte[] data = dataBuffer.getData();
//            mat.getByteBuffer().get(data);
//            return image;
//        }
//        return null;
//    }

//    public static byte[] iplToByteImgData(IplImage mat) {
//        if (mat.height() > 0 && mat.width() > 0) {
//            BufferedImage image = new BufferedImage(mat.width(), mat.height(),
//                    BufferedImage.TYPE_3BYTE_BGR);
//            WritableRaster raster = image.getRaster();
//            DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
//            byte[] data = dataBuffer.getData();
////			mat.getByteBuffer().get(data);
//            return data;
//        }
//        return null;
//    }


    /**
     * 传递数据然后打码
     *
     * @param srcImg
     * @param pslist
     * @return
     */
    public static BufferedImage eyesMark(BufferedImage srcImg, List<PositionVO> pslist) {

        try {
            int srcImgWidth = srcImg.getWidth();// 获取图片的宽
            int srcImgHeight = srcImg.getHeight();// 获取图片的高
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            for (PositionVO ps : pslist) {
                makeMosic(ps, g, bufImg);//新的马赛克方法
            }
            g.dispose();
            // 输出图片
//			FileOutputStream outImgStream = new FileOutputStream(tarImgPath);
//			ImageIO.write(bufImg, "jpg", outImgStream);
//			outImgStream.flush();
//			outImgStream.close();
            return bufImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    
    public static int[] tz(int picwidth,int picheight,int xold, int yold,int widthold, int heightold) 
    {
    	int res[]=new int[4];
//    	 int picwidth = bufImg.getWidth();
//         int picheight = bufImg.getHeight();
//         int xold = position.getX();
//         int yold = position.getY();
//         int widthold = position.getWidth();
//         int heightold = position.getHeight();

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
         
         res[0]=x;
         res[1]=y;
         res[2]=width;
         res[3]=height;
         return res;
    }
    
    /**
     * 传递信息添加马赛克
     *
     * @param gs
     * @param bufImg
     */
    public static void makeMosic(PositionVO position, Graphics gs, BufferedImage bufImg) {
//		{"top":111,"left":1021,"bottom":571"right":1396
//		 top y  left x   width right-left   height bottom-left
//			int x=position.getX();
//			int y=position.getY();
//			int width=position.getWidth();
//			int height=position.getHeight();

        int picwidth = bufImg.getWidth();
        int picheight = bufImg.getHeight();

        int xold = position.getX();
        int yold = position.getY();
        int widthold = position.getW();
        int heightold = position.getH();

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
        
        
        
        
        int mosaicSize = 10;
        //2. 设置各方向绘制的马赛克块个数
        int xcount = 0; // 方向绘制个数
        int ycount = 0; // y方向绘制个数
        if (width % mosaicSize == 0) {
            xcount = width / mosaicSize;
        } else {
            xcount = width / mosaicSize + 1;
        }
        if (height % mosaicSize == 0) {
            ycount = height / mosaicSize;
        } else {
            ycount = height / mosaicSize + 1;
        }
        //3. 绘制马赛克(绘制矩形并填充颜色)
        int xTmp = x;
        int yTmp = y;
        for (int i = 0; i < xcount; i++) {
            for (int j = 0; j < ycount; j++) {
                //马赛克矩形格大小
                int mwidth = mosaicSize;
                int mheight = mosaicSize;
                if (i == xcount - 1) {   //横向最后一个比较特殊，可能不够一个size
                    mwidth = width - xTmp;
                }
                if (j == ycount - 1) {  //同理
                    mheight = height - yTmp;
                }
                //矩形颜色取中心像素点RGB值
                int centerX = xTmp;
                int centerY = yTmp;
                if (mwidth % 2 == 0) {
                    centerX += mwidth / 2;
                } else {
                    centerX += (mwidth - 1) / 2;
                }
                if (mheight % 2 == 0) {
                    centerY += mheight / 2;
                } else {
                    centerY += (mheight - 1) / 2;
                }
                Color color = new Color(bufImg.getRGB(centerX, centerY));
                gs.setColor(color);
                gs.fillRect(xTmp, yTmp, mwidth, mheight);
                yTmp = yTmp + mosaicSize;// 计算下一个矩形的y坐标
            }
            yTmp = y;// 还原y坐标
            xTmp = xTmp + mosaicSize;// 计算x坐标
        }
    }


    /**
     * 验证目录是否存在存在则删除所有文件
     * makeFile
     *
     * @param path void
     */
    //param path 文件夹完整绝对路径
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
        }
    }

    /**
     * 生成图片的方法
     * makePic
     *
     * @param bf
     * @param path void
     */
    public static void makePic(BufferedImage bf, String path) {
        try {
            ImageIO.write(bf, "jpg", new File(path));
        } catch (IOException e) {
        }
    }

    /**
     * 将Base64位编码的图片进行解码，并保存到指定目录
     *
     * @return
     */
    public static void makeImageByBase64(BufferedImage buf, String path) {
        BASE64Decoder decoder = new BASE64Decoder();
        FileOutputStream write = null;
        try {
            write = new FileOutputStream(new File(path));
            byte[] decoderBytes = decoder.decodeBuffer(Base64ImageUtils.encodeImgageToBase64(buf));
            write.write(decoderBytes);
        } catch (IOException e) {
        } finally {
            if (null == write) {
                try {
                    write.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 将Base64位编码的图片进行解码，并保存到指定目录
     *
     * @param base64 base64编码的图片信息
     * @return
     */
    public static void makeImageByBase64(String base64, String path) {
        BASE64Decoder decoder = new BASE64Decoder();
        FileOutputStream write = null;
        try {
            write = new FileOutputStream(new File(path));
            byte[] decoderBytes = decoder.decodeBuffer(base64);
            write.write(decoderBytes);
        } catch (IOException e) {
        } finally {
            if (null == write) {
                try {
                    write.close();
                } catch (IOException e) {
                }
            }
        }
    }


    /**
     * 将网络图片进行Base64位编码
     *
     * @param imageUrl 图片的url路径，如http://.....xx.jpg
     * @return
     */
    public static byte[] encodeImgage(URL imageUrl) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        ByteArrayOutputStream outputStream = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(imageUrl);
            //bufferedImage.
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            byte[] arr = outputStream.toByteArray();
            return arr;
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将网络图片进行Base64位编码
     *
     * @param
     * @return
     */
    public static String encodeImgageBase64(BufferedImage bufferedImage) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        ByteArrayOutputStream outputStream = null;
        try {
            //bufferedImage.
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            byte[] arr = outputStream.toByteArray();
            //对字节数组Base64编码  
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(arr);//返回Base64编码过的字节数组字符串  
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将base64还原图片
     * GenerateImage
     *
     * @param base64str
     * @return boolean
     */
    public static byte[] GenerateImage(String base64str) {   //对字节数组字符串进行Base64解码并生成图片
        if (base64str == null) //图像数据为空  
            return null;
        // System.out.println("开始解码");
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码  
            byte[] b = decoder.decodeBuffer(base64str);
            //  System.out.println("解码完成");
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            return b;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将base64还原图片
     * GenerateImage
     *
     * @param base64str
     * @param savepath
     * @return boolean
     */
    public static boolean GenerateImage(String base64str, String savepath) {   //对字节数组字符串进行Base64解码并生成图片
        if (base64str == null) //图像数据为空  
            return false;
        // System.out.println("开始解码");
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码  
            byte[] b = decoder.decodeBuffer(base64str);
            //  System.out.println("解码完成");
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            // System.out.println("开始生成图片");
            //生成jpeg图片  
            OutputStream out = new FileOutputStream(savepath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将本地图片进行Base64位编码
     *
     * @param imageFile 图片的url路径，如F:/.....xx.jpg
     * @return
     */
    public static byte[] encodeImgage(File imageFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        ByteArrayOutputStream outputStream = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            bufferedImage.flush();
            return outputStream.toByteArray();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                if (null != outputStream) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] encodeImgage(BufferedImage bufferedImage) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            bufferedImage.flush();
            return outputStream.toByteArray();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                if (null != outputStream) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将Base64位编码的图片进行解码，并保存到指定目录
     *
     * @param base64 base64编码的图片信息
     * @return
     */
    public static void decodeToImage(String base64, String path,
                                     String imgName) {
        BASE64Decoder decoder = new BASE64Decoder();

        FileOutputStream write = null;
        try {
            write = new FileOutputStream(new File(path
                    + imgName));
            byte[] decoderBytes = decoder.decodeBuffer(base64);
            write.write(decoderBytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null == write) {
                try {
                    write.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static BufferedImage bgrToBufferedImage(byte[] data, int width, int height) {
        int type = BufferedImage.TYPE_3BYTE_BGR;
        // bgr to rgb
        byte b;
        for (int i = 0; i < data.length; i = i + 3) {
            b = data[i];
            data[i] = data[i + 2];
            data[i + 2] = b;
        }
        BufferedImage image = new BufferedImage(width, height, type);
        image.getRaster().setDataElements(0, 0, width, height, data);
        return image;
    }
//    public static void main(String [] args){
//        URL url = null;
//        try {
//            url = new URL("http://img1.imgtn.bdimg.com/it/u=2064054871,2037178638&fm=200&gp=0.jpg");
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        byte[] encoderStr = ImageUtil.encodeImgage(url);
//        //System.out.println(encoderStr);
//
//        //Base64ImageUtils.decodeBase64ToImage(encoderStr, "E:/", "football.jpg");
//
//    }

}
