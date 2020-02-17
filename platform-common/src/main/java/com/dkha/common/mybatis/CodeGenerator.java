/*
package com.dkha.common.mybatis;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


*/
/**
 * @Description: mybatis-plus 代码生成器
 * @Author: Spring
 * @CreateDate: 2019/8/1 17:47
 *//*


public class CodeGenerator {

    */
/**
     * 代码生成
     * @param folderPath 输出文件夹路径
     *//*

    public static void generate(String folderPath) throws IOException {

        deleteDir(folderPath);

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = folderPath;
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("Spring");
        gc.setOpen(false);
        //生成时间为Date类
        gc.setDateType(DateType.ONLY_DATE);
        //实体属性 Swagger2 注解
        gc.setSwagger2(true);
        //分布式全局唯一Id生成策略
        gc.setIdType(IdType.ID_WORKER_STR);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://192.168.3.240:3301/faceapi?useUnicode=true&useSSL=false&characterEncoding=utf8");

        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.dkha.api");
        pc.setEntity("modules.entities");
        pc.setService("services");
        pc.setServiceImpl("services.impl");
        pc.setMapper("mappers");
        pc.setController("controllers");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mappers/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setSuperEntityColumns("id");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        //是否显示属性注解
        strategy.setEntityTableFieldAnnotationEnable(false);
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();

        //弹出生成代码文件夹
        Desktop.getDesktop().open(new File(folderPath));
    }

    */
/**
     * delete dir
     * @param folderPath
     *//*

    public static void deleteDir(String folderPath) {
        try {
            //删除完里面所有内容
            delAllFile(folderPath);
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    */
/**
     * delete all file
     * @param path
     * @return
     *//*

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
                //先删除文件夹里面的文件
                delAllFile(path + "/" + tempList[i]);
                //再删除空文件夹
                deleteDir(path + "/" + tempList[i]);
                flag = true;
            }
        }
        return flag;
    }

    public static void main(String[] args) throws IOException {
        generate("d:\\mybatis-code-generate");
    }
}
*/
