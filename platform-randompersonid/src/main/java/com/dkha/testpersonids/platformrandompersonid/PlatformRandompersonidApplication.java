package com.dkha.testpersonids.platformrandompersonid;

import com.dkha.testpersonids.utils.PersonNameIDUtils;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Random;

public class PlatformRandompersonidApplication {


    public static void main(String[] args) throws IOException {
        renameImagePath("E:\\BaiduNetdiskDownload\\CelebA\\Img\\img_celeba.7z\\img_celeba");
        printImagenameToCSV("E:\\BaiduNetdiskDownload\\CelebA\\Img\\img_celeba.7z\\img_celeba","e:\\Foreigners_img_celeba.csv");

    }

    /**
     * 重命名图片路径下的图片的名称
     */
    public static void renameImagePath(String imagePath){
        //随机生成性别
        Random random= new Random();
        //1. 遍历文件夹下的文件
        File file=new File(imagePath);
        long beginTime=System.currentTimeMillis();
        int count=0;
        if(file.exists()){
            File[] oldfiles = file.listFiles();
            for (File filetmp : oldfiles) {
                String id ="";
                if( random.nextInt(10)%2==0) {
                    id = PersonNameIDUtils.getIdNo(true);
                }else {
                    id = PersonNameIDUtils.getIdNo(false);
                }
                StringBuilder sb=new StringBuilder(40);
                sb.append(id);
                sb.append("_");
                sb.append(PersonNameIDUtils.getName());
                FixFileName(filetmp.getAbsolutePath(),sb.toString());
                count++;
                if(count%1000==0){
                    System.out.println("处理了："+count);
                }
            }
        }
        long endTime=System.currentTimeMillis();
        System.out.println("遍历时间："+(endTime-beginTime));


    }

    /**
     * 重新命名图片名称
     * @param filePath
     * @param newFileName
     * @return
     */
    private static String FixFileName(String filePath, String newFileName) {
        File f = new File(filePath);
        // 判断原文件是否存在（防止文件名冲突）
        if (!f.exists()) {
            return null;
        }
        newFileName = newFileName.trim();
        // 文件名不能为空
        if("".equals(newFileName) || newFileName == null) {
            return null;
        }
        String newFilePath = null;
        // 判断是否为文件夹

        if (f.isDirectory()) {
            newFilePath = filePath.substring(0, filePath.lastIndexOf("\\")) + "\\" + newFileName;
        } else {

            newFilePath = filePath.substring(0, filePath.lastIndexOf("\\")) + "\\" + newFileName
                    + filePath.substring(filePath.lastIndexOf("."));
        }
        File nf = new File(newFilePath);
        try {
            // 修改文件名
            f.renameTo(nf);
        } catch (Exception err) {
            err.printStackTrace();
            return null;
        }
        return newFilePath;
    }

    /**
     * 输出图片信息到CSV
     * @throws IOException
     */
    public static void printImagenameToCSV(String imagepath,String outfilepath) throws IOException {
        String destimgpath=imagepath;
        FileWriter fw=new FileWriter(outfilepath);
        PrintWriter pw =new PrintWriter(fw);
        //1. 遍历文件夹下的文件
        File file=new File(destimgpath);
        long beginTime=System.currentTimeMillis();
        int count=0;
        if(file.exists()){
            File[] oldfiles = file.listFiles();
            for (File filetmp : oldfiles) {
                pw.println( filetmp.getAbsoluteFile());
            }
        }
        pw.flush();
        pw.close();
        long endTime=System.currentTimeMillis();
        System.out.println("遍历时间："+(endTime-beginTime));
    }

    /**
     * 拷贝图片
     * @throws IOException
     */
    public static void copyPersonImage() throws IOException {
        String imgpath="E:\\10w";
        String destimgpath="E:\\new10w";
        //随机生成性别
        Random random= new Random();


        //1. 遍历文件夹下的文件
        File file=new File(imgpath);
        long beginTime=System.currentTimeMillis();
        int count=0;
        if(file.exists()){
            File[] oldfiles = file.listFiles();
            for (File filetmp : oldfiles) {
                //2. 拷贝文件
                String filesuff=filetmp.getName().substring(filetmp.getName().lastIndexOf("."));
                String id ="";
                if( random.nextInt(10)%2==0) {
                    id = PersonNameIDUtils.getIdNo(true);
                }else {
                    id = PersonNameIDUtils.getIdNo(false);
                }
                StringBuilder sb=new StringBuilder(40);
                sb.append(destimgpath);
                sb.append("\\");
                sb.append(id);
                sb.append("_");
                sb.append(PersonNameIDUtils.getName());
                sb.append(filesuff);
                File newfile=new File(sb.toString());
                FileUtils.copyFile(filetmp, newfile);
                count++;
                if(count%1000==0){
                    System.out.println("处理了："+count);
                }
            }
        }
        long endTime=System.currentTimeMillis();
        System.out.println("遍历时间："+(endTime-beginTime));
    }


}
