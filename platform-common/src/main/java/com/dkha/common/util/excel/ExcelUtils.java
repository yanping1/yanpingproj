
package com.dkha.common.util.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.dkha.common.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * excel工具类
 *
 * @author Mark sunlightcs@gmail.com
 */
public class ExcelUtils {
    private static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    /**
     * Excel导出
     *
     * @param response  response
     * @param fileName  文件名
     * @param list      数据List
     * @param pojoClass 对象Class
     */
    public static void exportExcel(HttpServletResponse response, String fileName, Collection<?> list,
                                   Class<?> pojoClass) throws IOException {
        if (StringUtils.isBlank(fileName)) {
            //当前日期
            fileName = DateUtils.formatDate(new Date());
        }

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), pojoClass, list);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }


//    /**
//     * 导入excel信息
//     *
//     * @param file
//     * @param pojoClass
//     * @return
//     * @author hechenggang
//     */
//    public static List<ProjectBasicInfoImportDTO> importBuildingProjectInfo(MultipartFile file, Class<ProjectBasicInfoImportDTO> pojoClass)  {
//
//        if (file == null) {
//            throw new RuntimeException("文件不存在");
//        }
//        HSSFWorkbook workbook = null;
//        try {
//            InputStream is = file.getInputStream();//获取输入流
//            workbook = new HSSFWorkbook(is);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("文件格式不正确");
//        }
//        Sheet sheet = workbook.getSheetAt(0);
//        List<ProjectBasicInfoImportDTO> list = new ArrayList<>();
//        Field[] fields = pojoClass.getDeclaredFields();
//        int rows = sheet.getPhysicalNumberOfRows();
//        for (int i = 1; i < rows; i++) {//从第一行开始读取数据，第0行为表头
//            Row row = sheet.getRow(i);
//            ProjectBasicInfoImportDTO t = new ProjectBasicInfoImportDTO();
//            int cells = row.getPhysicalNumberOfCells();
//            for (int j = 0; j < cells; j++) {
//                fields[j+1].setAccessible(true);
//                try {
//                    if (String.class.getName().equals(fields[j+1].getType().getName()) && row.getCell(j) != null) {
//                        fields[j+1].set(t, row.getCell(j).getStringCellValue());
//                    } else if (Date.class.getName().equals(fields[j+1].getType().getName()) && row.getCell(j) != null) {
//                        fields[j+1].set(t, row.getCell(j).getDateCellValue());
//                    } else if (Double.class.getName().equals(fields[j+1].getType().getName()) && row.getCell(j) != null) {
//                        fields[j+1].set(t, row.getCell(j).getNumericCellValue());
//                    } else {
//                        fields[j+1].set(t, null);
//                    }
//                } catch (IllegalAccessException e) {
//                    throw new RuntimeException("数据格式不正确");
//                }
//            }
//            list.add(t);
//        }
//
//        return list;
//    }


//    /**
//     * 导出在建项目excel
//     *
//     * @param response
//     * @param fileName
//     * @param list
//     * @param pojoClass
//     * @throws IOException
//     * @author hechenggang
//     */
//    public static void exportBuildProjectExcel(HttpServletResponse response, String fileName, Collection<?> list,
//                                               Class<?> pojoClass) throws IOException {
//        if (StringUtils.isBlank(fileName)) {
//            //当前日期
//            fileName = DateUtils.formatDate(new Date());
//        }
//        HSSFWorkbook workbook = getBuildProjectHSSFWorkbook(fileName, list, pojoClass);
//
//        response.setCharacterEncoding("UTF-8");
//        response.setHeader("content-Type", "application/vnd.ms-excel");
//        response.setHeader("Content-Disposition",
//                "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
//        ServletOutputStream out = response.getOutputStream();
//        workbook.write(out);
//        out.flush();
//    }

//    /**
//     * 获取在建项目HSSFWorkbook
//     *
//     * @param fileName
//     * @param list
//     * @param pojoClass
//     * @return
//     * @author hechenggang
//     */
//    public static HSSFWorkbook getBuildProjectHSSFWorkbook(String fileName, Collection<?> list, Class<?> pojoClass) {
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet(fileName);
//        HSSFRow row = sheet.createRow(0);//创建工作表第一行，设置表头
//        String[] value = getSwaggerValue(pojoClass);
//        Field[] fields = pojoClass.getDeclaredFields();
//        int length = value.length;
//        for (int i = 1; i < length; i++) {
//            fields[i].setAccessible(true);
//            if ("projectInvestmentTypeDTO".equals(fields[i].getName())) {
//                CellRangeAddress cra = new CellRangeAddress(0, 0, i - 1, i);
//                sheet.addMergedRegion(cra);
//            }
//            row.createCell(i - 1).setCellValue(value[i]);
//
//        }
//        int dataLength = list.size();
//        int n = 1;
//        try {
//            for (int i = 1; i < dataLength + 1; i++) {//行数,1开始
//                HSSFRow row1 = sheet.createRow(n);
//                ProjectDetailsExcelDTO dto = ((ArrayList<ProjectDetailsExcelDTO>) list).get(i - 1);
//                int projectInvestmentTypeDTOSize = dto.getProjectInvestmentTypeDTO().size();
//                for (int k = 1; k < fields.length; k++) {
//                    if ("projectInvestmentTypeDTO".equals(fields[k].getName())) {
//                        List<ProjectInvestmentTypeDTO> projectInvestmentTypeDTOList = dto.getProjectInvestmentTypeDTO();
//                        for (int m = n; m < n + projectInvestmentTypeDTOSize; m++) {
//                            int h = 0;
//                            ProjectInvestmentTypeDTO investmentTypeDTO = projectInvestmentTypeDTOList.get(h);
//                            Field idCodeInvestmentType = investmentTypeDTO.getClass().getDeclaredField("idCodeInvestmentType");
//                            Field investmentMoney = investmentTypeDTO.getClass().getDeclaredField("investmentMoney");
//                            idCodeInvestmentType.setAccessible(true);
//                            investmentMoney.setAccessible(true);
//                            if (m == n) {
//                                row1.createCell(42).setCellValue((String) idCodeInvestmentType.get(investmentTypeDTO));
//                                row1.createCell(43).setCellValue((Double) investmentMoney.get(investmentTypeDTO));
//                            } else {
//                                HSSFRow row2 = sheet.createRow(m);
//                                row2.createCell(42).setCellValue((String) idCodeInvestmentType.get(investmentTypeDTO));
//                                row2.createCell(43).setCellValue((Double) investmentMoney.get(investmentTypeDTO));
//                            }
//                            h++;
//
//                        }
//                    } else {
//                        if (projectInvestmentTypeDTOSize > 1) {
//                            CellRangeAddress cra1 = new CellRangeAddress(i, i + projectInvestmentTypeDTOSize - 1, k - 1, k - 1);
//                            sheet.addMergedRegion(cra1);
//                        }
//                        if (fields[k].getType().getName().equals(String.class.getName()) && fields[k].get(dto) != null) {
//                            row1.createCell(k - 1).setCellValue((String) fields[k].get(dto));
//                        } else if (fields[k].getType().getName().equals(Double.class.getName()) && fields[k].get(dto) != null) {
//                            row1.createCell(k - 1).setCellValue((Double) fields[k].get(dto));
//                        } else if (fields[k].getType().getName().equals(Date.class.getName()) && fields[k].get(dto) != null) {
//                            row1.createCell(k - 1).setCellValue((Date) fields[k].get(dto));
//                        } else {
//                            row1.createCell(k - 1).setCellValue("");
//                        }
//
//                    }
//
//                }
//                if (projectInvestmentTypeDTOSize == 0) {
//                    n += 1;
//                } else {
//                    n += projectInvestmentTypeDTOSize;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return workbook;
//    }

//    /**
//     * 获取swagger属性值
//     *
//     * @param pojoClass
//     * @return
//     * @author hechenggang
//     */
//    public static String[] getSwaggerValue(Class<?> pojoClass) {
//        Field[] fields = pojoClass.getDeclaredFields();
//        Field field;
//        int length = fields.length;
//        String[] value = new String[length];
//        for (int i = 0; i < length; i++) {
//            fields[i].setAccessible(true);
//        }
//        for (int i = 0; i < length; i++) {
//            try {
//                field = pojoClass.getDeclaredField(fields[i].getName());
//                ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
//                if (apiModelProperty != null) {
//                    value[i] = apiModelProperty.value();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return value;
//    }


    /**
     * Excel导出，先sourceList转换成List<targetClass>，再导出
     *
     * @param response    response
     * @param fileName    文件名
     * @param sourceList  原数据List
     * @param targetClass 目标对象Class
     */
    public static void exportExcelToTarget(HttpServletResponse response, String fileName, Collection<?> sourceList,
                                           Class<?> targetClass) throws Exception {
        List targetList = new ArrayList<>(sourceList.size());
        for (Object source : sourceList) {
            Object target = targetClass.newInstance();
            BeanUtils.copyProperties(source, target);
            targetList.add(target);
        }

        exportExcel(response, fileName, targetList, targetClass);
    }

    public static List<Map> readExcel(MultipartFile file, int sheetNo, int startRowNo, int startColNo, int fieldRowNo)
            throws Exception {
        Workbook wb = null;
        String fileName = file.getOriginalFilename();
        String extString = StringUtils.substringAfterLast(fileName, ".");
        if ("xls".equalsIgnoreCase(extString)) {
            wb = new HSSFWorkbook(file.getInputStream());
        } else if ("xlsx".equalsIgnoreCase(extString)) {
            wb = new XSSFWorkbook(file.getInputStream());
        } else {
            throw new Exception("请上传Excel文件！");
        }
        Map map = null;
        Row row = null;
        Sheet sheet = wb.getSheetAt(sheetNo);
        Row fieldRow = sheet.getRow(fieldRowNo);
        int rowNum = sheet.getPhysicalNumberOfRows();
        int colNum = fieldRow.getPhysicalNumberOfCells();
        List<Map> datas = new ArrayList<Map>();
        for (int i = startRowNo; i <= rowNum; i++) {
            map = new HashMap();
            row = sheet.getRow(i);
            for (int j = startColNo; j < colNum; j++) {
                map.put(getCellContent(fieldRow.getCell(j)), getCellContent(row.getCell(j)));
            }
            datas.add(map);
        }
        return datas;
    }

    private static String getCellContent(Cell cell) {
        if (null == cell) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: // 数字
                buffer.append(String.valueOf(cell.getNumericCellValue()));
                break;
            case Cell.CELL_TYPE_STRING: // 字符串
                buffer.append(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: // 公式
                buffer.append(cell.getCellFormula());
                break;
            default:
                return null;
        }
        return buffer.toString();
    }

    /**
     * @param map
     * @param obj
     * @return
     * @throws Exception
     */
    public static Object parseMap2Bean(Map map, Object obj) throws Exception {
        try {
//            org.apache.commons.beanutils.BeanUtils.populate(obj, map);
        } catch (Exception e) {
            throw new Exception("转换失败！", e);
        }
        return obj;
    }

    /**
     * @param list
     * @param
     * @return
     * @throws Exception
     */
    public static List parseMap2Bean(List<Map> list, Class clazz) throws Exception {
        List<Object> lst = new ArrayList<Object>();
        try {
            for (Map map : list) {
                lst.add(parseMap2Bean(map, clazz.newInstance()));
            }
        } catch (Exception e) {
            throw new Exception("转换失败！", e);
        }
        return lst;
    }
}
