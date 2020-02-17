
package com.dkha.common.util;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.dkha.common.exception.DkException;
import com.dkha.common.validate.UtilValidate;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * excel工具类
 *
 * @author Mark sunlightcs@gmail.com
 */
public class ExcelUtils {
    private static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    /**
     * 获取workbook对象
     *
     * @param multipartFile
     * @return
     * @throws Exception
     */
    public static Workbook getWorkbook(MultipartFile multipartFile) throws Exception {
        if (multipartFile.isEmpty()) {
            throw new DkException("文件不存在");
        }
        if (!multipartFile.getOriginalFilename().endsWith("xls") && !multipartFile.getOriginalFilename().endsWith("xlsx")) {

            throw new DkException("请上传excel文件");
        }
        InputStream is = multipartFile.getInputStream();
        Workbook workbook = new HSSFWorkbook(is);
        return workbook;
    }


    /**
     * Excel导出
     *
     * @param response  response
     * @param fileName  文件名
     * @param list      数据List
     * @param pojoClass 对象Class
     */
    public static <T> void exportExcel(HttpServletResponse response, String fileName, ArrayList<T> list,
                                       Class<T> pojoClass) throws Exception {
        if (StringUtils.isBlank(fileName)) {
            //当前日期
            fileName = DateUtils.formatDate(new Date());
        }
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), pojoClass, list);
        write(response, workbook, fileName);

    }

    /**
     * 导出excel表头
     *
     * @param response
     * @param pojo
     * @param <T>
     */
    public static <T> Workbook downloadTemplate(HttpServletResponse response, Class<T> pojo) throws Exception {
        String[] swaggerValues = getSwaggerValue(pojo);
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        for (int i = 0; i < swaggerValues.length; i++) {
            row.createCell(i).setCellValue(swaggerValues[i]);
        }
//        //锁定第一行无法修改
//        sheet.createFreezePane(0, 1, 0, 1);

        return workbook;
    }


    /**
     * 导入excel信息
     *
     * @param file
     * @param pojoClass
     * @return
     * @author hechenggang
     */
    public static <T> List<T> importExcel(MultipartFile file, Class<T> pojoClass) throws Exception {

        if (file.isEmpty()) {
            throw new RuntimeException("文件不存在");
        }
        if (!file.getOriginalFilename().endsWith("xls") || file.getOriginalFilename().endsWith("xlsx")) {

            throw new RuntimeException("请上传excel文件");
        }
        InputStream is = file.getInputStream();
        Workbook workbook = new HSSFWorkbook(is);
        List<T> dataList = new ArrayList<>();
        List erroList = new ArrayList();
//        Field[] fields = pojoClass.getFields();
//        int rows = workbook.getSheetAt(0).getPhysicalNumberOfRows();
//        for (int i = 0; i < rows; i++) {
//            Row row = workbook.getSheetAt(0).getRow(i);
//            int cells = row.getPhysicalNumberOfCells();
//            Map<String, Object> map = new HashMap<>();
//            for (int j = 0; j < cells; j++) {
//                if (String.class.getName().equals(fields[j].getType().getName())) {
//                    map.put(fields[j].getName(), row.getCell(j).getStringCellValue());
//                } else if (Date.class.getName().equals(fields[j].getType().getName())) {
//                    map.put(fields[j].getName(), row.getCell(j).getDateCellValue());
//                } else if (Double.class.getName().equals(fields[j].getType().getName())) {
//                    map.put(fields[j].getName(), row.getCell(j).getNumericCellValue());
//                } else {
//
//                }
//            }

//
//        }
//        Sheet sheet = workbook.getSheetAt(0);
//

        return dataList;
    }


    /**
     * 获取swagger属性值
     *
     * @param pojoClass
     * @return
     * @author hechenggang
     */
    public static String[] getSwaggerValue(Class<?> pojoClass) {
        Field[] fields = pojoClass.getDeclaredFields();
        Field field;
        int length = fields.length;
        String[] value = new String[length];
        for (int i = 0; i < length; i++) {
            fields[i].setAccessible(true);
        }
        for (int i = 0; i < length; i++) {
            try {
                field = pojoClass.getDeclaredField(fields[i].getName());
                ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
                if (apiModelProperty != null) {
                    value[i] = apiModelProperty.value();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }


    /**
     * //     * Excel导出，先sourceList转换成List<targetClass>，再导出
     * //     *
     * //     * @param response    response
     * //     * @param fileName    文件名
     * //     * @param sourceList  原数据List
     * //     * @param targetClass 目标对象Class
     * //
     */
//    public static void exportExcelToTarget(HttpServletResponse response, String fileName, Collection<?> sourceList,
//                                           Class<?> targetClass) throws Exception {
//        List targetList = new ArrayList<>(sourceList.size());
//        for (Object source : sourceList) {
//            Object target = targetClass.newInstance();
//            BeanUtils.copyProperties(source, target);
//            targetList.add(target);
//        }
//
//        exportExcel(response, fileName, targetList, targetClass);
//    }

//    public static List<Map> readExcel(MultipartFile file, int sheetNo, int startRowNo, int startColNo, int fieldRowNo)
//            throws Exception {
//        Workbook wb = null;
//        String fileName = file.getOriginalFilename();
//        String extString = StringUtils.substringAfterLast(fileName, ".");
//        if ("xls".equalsIgnoreCase(extString)) {
//            wb = new HSSFWorkbook(file.getInputStream());
//        } else if ("xlsx".equalsIgnoreCase(extString)) {
//            wb = new XSSFWorkbook(file.getInputStream());
//        } else {
//            throw new Exception("请上传Excel文件！");
//        }
//        Map map = null;
//        Row row = null;
//        Sheet sheet = wb.getSheetAt(sheetNo);
//        Row fieldRow = sheet.getRow(fieldRowNo);
//        int rowNum = sheet.getPhysicalNumberOfRows();
//        int colNum = fieldRow.getPhysicalNumberOfCells();
//        List<Map> datas = new ArrayList<Map>();
//        for (int i = startRowNo; i <= rowNum; i++) {
//            map = new HashMap();
//            row = sheet.getRow(i);
//            for (int j = startColNo; j < colNum; j++) {
//                map.put(getCellContent(fieldRow.getCell(j)), getCellContent(row.getCell(j)));
//            }
//            datas.add(map);
//        }
//        return datas;
//    }
//
//    private static String getCellContent(Cell cell) {
//        if (null == cell) {
//            return null;
//        }
//        StringBuffer buffer = new StringBuffer();
//        switch (cell.getCellType()) {
//            case Cell.CELL_TYPE_NUMERIC: // 数字
//                buffer.append(String.valueOf(cell.getNumericCellValue()));
//                break;
//            case Cell.CELL_TYPE_STRING: // 字符串
//                buffer.append(cell.getStringCellValue());
//                break;
//            case Cell.CELL_TYPE_FORMULA: // 公式
//                buffer.append(cell.getCellFormula());
//                break;
//            default:
//                return null;
//        }
//        return buffer.toString();
//    }

    //    /**
//     * @param map
//     * @param obj
//     * @return
//     * @throws Exception
//     */
//    public static Object parseMap2Bean(Map map, Object obj) throws Exception {
//        try {
//            org.apache.commons.beanutils.BeanUtils.populate(obj, map);
//        } catch (Exception e) {
//            throw new Exception("转换失败！", e);
//        }
//        return obj;
//    }
//
//    /**
//     * @param list
//     * @param
//     * @return
//     * @throws Exception
//     */
//    public static List parseMap2Bean(List<Map> list, Class cla zz) throws Exception {
//        List<Object> lst = new ArrayList<Object>();
//        try {
//            for (Map map : list) {
//                lst.add(parseMap2Bean(map, clazz.newInstance()));
//            }
//        } catch (Exception e) {
//            throw new Exception("转换失败！", e);
//        }
//        return lst;
//    }
    public static void write(HttpServletResponse response, Workbook workbook, String fileName) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
//        response.setHeader("content-Type", "application/octet-stream");
        if (UtilValidate.isEmpty(fileName)) {
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode("模板文件", "UTF-8") + ".xls");
        } else {
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
        }
        response.setContentType("*/*");
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }

    public static void write(HttpServletResponse response, List<List<String>> error, String fileName, Class<?> pojo) throws Exception {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
//        response.setHeader("content-Type", "application/octet-stream");
        if (UtilValidate.isEmpty(fileName)) {
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + System.currentTimeMillis() + ".xls");
        } else {
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
        }
        response.setContentType("*/*");
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        //设置表头
        Row firstRow = sheet.createRow(0);
        String[] swagger = getSwaggerValue(pojo);
        int i = 0;
        for (i = 0; i < swagger.length; i++) {
            Cell cell = firstRow.createCell(i);
            cell.setCellValue(swagger[i]);
        }
        firstRow.createCell(i).setCellValue("错误原因（修改数据后请删除此列再上传）");
        //

        for (i = 0; i < error.size(); i++) {
            Row row = sheet.createRow(i + 1);
            List<String> rowList = error.get(i);
            for (int j = 0; j < rowList.size(); j++) {
                if (rowList.size() > 10) {
                    if (j == 6 || j == 7) {
                        double month = Double.parseDouble(rowList.get(j));
                        Date date = HSSFDateUtil.getJavaDate(month);

                        row.createCell(j).setCellValue(DateUtils.formatDate(date, "yyyy-MM"));
                    } else {
                        row.createCell(j).setCellValue(rowList.get(j));
                    }
                } else {
                    if (j == 2) {
                        double month = Double.parseDouble(rowList.get(j));
                        Date date = HSSFDateUtil.getJavaDate(month);

                        row.createCell(j).setCellValue(DateUtils.formatDate(date, "yyyy-MM"));
                    } else {
                        row.createCell(j).setCellValue(rowList.get(j));
                    }
                }
            }

        }
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }

}
