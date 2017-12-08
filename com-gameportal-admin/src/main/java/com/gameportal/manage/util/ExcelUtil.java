package com.gameportal.manage.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * User: leron
 * Date: 15-8-26
 * Time: 10:29
 * excel工具类
 */
@SuppressWarnings("rawtypes")
public class ExcelUtil {


	public static final Logger logger = Logger.getLogger(ExcelUtil.class);

    /**
     * 1997~2003格式
     */
    public static final String XLS = "xls";

    /**
     * 2007+格式
     */
    public static final String XLSX = "xlsx";

    /**
     * 创建空的XLS工作本
     *
     * @return
     */
    public static Workbook creatEmptyXLSWorkBook() {
        return createEmptyWorkBook(XLS);
    }

    /**
     * 创建空的XLSX工作本
     *
     * @return
     */
    public static Workbook creatEmptyXLSXWorkBook() {
        return createEmptyWorkBook(XLSX);
    }

    /**
     * 创建空的工作本
     *
     * @return
     */
    private static Workbook createEmptyWorkBook(String type) {
        try {
            if (XLS.equalsIgnoreCase(type)) return new HSSFWorkbook();  //.xls
            if (XLSX.equalsIgnoreCase(type)) return new XSSFWorkbook(); //.xlsx
            return null;
        } catch (Exception e) {
        	logger.error("create workbook error!", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建空的工作页
     *
     * @param workbook
     * @param sheetName
     * @return
     */
    public static Sheet createEmptyWorkSheet(Workbook workbook, String sheetName) {
        try {
            if (StringUtils.isNotEmpty(sheetName)) return workbook.createSheet(sheetName);
            return workbook.createSheet();
        } catch (Exception e) {
        	logger.error("create worksheet error!", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 保存excel工作本到本地磁盘
     *
     * @param workbook 工作本对象
     * @param filePath 文件路径
     */
    public static void saveWorkbook(Workbook workbook, String filePath) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
        	logger.error("save workbook error!", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取excel文档
     *
     * @param filePath
     * @return
     */
    public static Workbook readWorkBook(String filePath) {
        try {
            File file = new File(filePath);
            String type = filePath.substring(filePath.lastIndexOf(".") + 1);
            if (XLS.equalsIgnoreCase(type)) {
                NPOIFSFileSystem npoifsFileSytem = new NPOIFSFileSystem(file);
                HSSFWorkbook wb = new HSSFWorkbook(npoifsFileSytem.getRoot(), false);
                npoifsFileSytem.close();
                return wb;
            }
            if (XLSX.equalsIgnoreCase(type)) {
                OPCPackage opcPackage = OPCPackage.open(file);
                XSSFWorkbook wb = new XSSFWorkbook(opcPackage);
                opcPackage.close();
                return wb;
            }
            return WorkbookFactory.create(file);
        } catch (Exception e) {
        	logger.error("read workbook error!", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取excel文档
     *
     * @param is
     * @param type
     * @return
     */
    public static Workbook readWorkBookByInputStream(InputStream is, String type) {
        try {
            if (XLS.equalsIgnoreCase(type)) {
                NPOIFSFileSystem npoifsFileSytem = new NPOIFSFileSystem(is);
                HSSFWorkbook wb = new HSSFWorkbook(npoifsFileSytem.getRoot(), false);
                npoifsFileSytem.close();
                return wb;
            }
            if (XLSX.equalsIgnoreCase(type)) {
                OPCPackage opcPackage = OPCPackage.open(is);
                XSSFWorkbook wb = new XSSFWorkbook(opcPackage);
                opcPackage.close();
                return wb;
            }
            return null;
        } catch (Exception e) {
        	logger.error("read workbook error!", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成工作表(默认.xlsx格式)
     * @param sheetName  工作本名称
     * @param dataSource 数据源
     * @param viewMap    表头显示字段 key: dataSource实体类中的字段名称 value：excel表头中需要填充的显示字段
     * @return
     */
    public static <T> Workbook generateSingleWorkBook(String sheetName,List<T> dataSource,LinkedHashMap<String, String> viewMap) {
        try {
            //默认excel格式 .xlsx
            return generateSingleWorkBook(dataSource, sheetName, XLS, viewMap);
        } catch (Exception e) {
        	logger.error("generate workbook error!", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成工作表
     *
     * @param dataSource 数据源
     * @param type       XLS,XLSX
     * @param viewMap    表头显示字段 key: dataSource实体类中的字段名称 value：excel表头中需要填充的显示字段
     * @return
     */
    public static <T> Workbook generateSingleWorkBook(List<T> dataSource, String type, LinkedHashMap<String, String> viewMap) {
        try {
            return generateSingleWorkBook(dataSource, null, type, viewMap);
        } catch (Exception e) {
        	logger.error("generate workbook error!", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成工作表
     *
     * @param dataSource 数据源
     * @param sheetName  sheet名称
     * @param type       XLS,XLSX
     * @param viewMap    表头显示字段 key: dataSource实体类中的字段名称 value：excel表头中需要填充的显示字段
     * @return
     */
    public static <T> Workbook generateSingleWorkBook(List<T> dataSource, String sheetName, String type, LinkedHashMap<String, String> viewMap) {
        try {
            Map<String, List> ds = new HashMap<String, List>();
            if (StringUtils.isEmpty(sheetName)) sheetName = "sheet1";
            ds.put(sheetName, dataSource);
            Map<String, LinkedHashMap<String, String>> sheetViewMap = new HashMap<String, LinkedHashMap<String, String>>();
            sheetViewMap.put(sheetName, viewMap);
            return generateWorkBook(ds, type, sheetViewMap);
        } catch (Exception e) {
        	logger.error("generate workbook error!", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 生成多个sheet,多数据源的工作表
     *
     * @param dataSource 数据源 key:sheet名称，value: list列表
     * @param type       XLS,XLSX
     * @param viewMap    Map: key: sheet名称 ,value: LinkedHashMap:表头显示字段 key: dataSource实体类中的字段名称 value：excel表头中需要填充的显示字段
     * @return
     */
    public static Workbook generateWorkBook(Map<String, List> dataSource, String type, Map<String, LinkedHashMap<String, String>> viewMap) {
        try {
            Workbook workbook = createEmptyWorkBook(type);
            for (String sheet : dataSource.keySet()) {
                Sheet sheetObj = createEmptyWorkSheet(workbook, sheet);
                List data = dataSource.get(sheet);
                LinkedHashMap<String, String> viewFields = viewMap.get(sheet);
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                //创建表头
                Row headRow = sheetObj.createRow(0);
                headRow.setHeightInPoints(30);
                Object[] keys = viewFields.keySet().toArray();
                for (int i = 0; i < keys.length; i++) {
                    createCell(workbook, headRow, (short) i, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, viewFields.get(keys[i].toString()));
                }
                //创建表体
                for (int i = 0; i < data.size(); i++) {
                    Row row = sheetObj.createRow(i + 1);
                    Object obj = data.get(i);
                    for (int j = 0; j < keys.length; j++) {
                        String methodName = "get" + keys[j].toString().substring(0, 1).toUpperCase() + keys[j].toString().substring(1);
                        Method method = BeanUtils.findMethod(obj.getClass(), methodName, new Class[]{});
                        Object result = method.invoke(obj, new Object[]{});
                     	Cell cell = row.createCell((short)j);
                    	cell.setCellStyle(cellStyle);
                        if (result != null){
                            if(result instanceof Date){
                                Date date = (Date) result;
                                cell.setCellValue(DateUtil.getStrYMDHMSByDate(date));
                            }else{
                                cell.setCellValue(String.valueOf(result));
                            }
                        }else{
                        	cell.setCellValue("");
                        }   
                     }
                }
            }
            return workbook;
        } catch (Exception e) {
        	logger.error("generate workbook error!", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成集合，单个sheet，目前支持字段类型：Integer、Long、String、java.util.Date
     *
     * @param filePath    excel文件路径
     * @param entityClass
     * @param viewMap
     * @param <T>
     * @return
     */
    public static <T> List<T> generateData(String filePath, Class<T> entityClass, LinkedHashMap<String, String> viewMap) {
        try {
            Workbook workbook = readWorkBook(filePath);
            return generateData(workbook, entityClass, viewMap);
        } catch (Exception e) {
        	logger.error("generate workbook error!", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成集合，单个sheet，目前支持字段类型：Integer、Long、String、java.util.Date
     *
     * @param workbook
     * @param entityClass
     * @param viewMap
     * @param <T>
     * @return
     */
    public static <T> List<T> generateData(Workbook workbook, Class<T> entityClass, LinkedHashMap<String, String> viewMap) {
        try {
            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Class> classMap = new HashMap<String, Class>();
            classMap.put(sheet.getSheetName(), entityClass);

            Map<String, LinkedHashMap<String, String>> view = new HashMap<String, LinkedHashMap<String, String>>();
            view.put(sheet.getSheetName(), viewMap);
            Map<String, List> resultMap = generateData(workbook, classMap, view);
            return (List<T>) resultMap.get(sheet.getSheetName());
        } catch (Exception e) {
        	logger.error("generate workbook error!", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量生成集合（多个sheet），目前支持字段类型：Integer、Long、String、java.util.Date
     *
     * @param workbook excel对象
     * @param classMap sheet对应的要装入的class类型
     * @param viewMap  sheet也对应的标题字段和类成员变量的类型映射
     * @return key:对应sheet名称，value：集合对象
     */
    public static Map<String, List> generateData(Workbook workbook, Map<String, Class> classMap, Map<String, LinkedHashMap<String, String>> viewMap) {
        Map<String, List> resultData = new HashMap<String, List>();
        try {
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                LinkedHashMap<String, String> sheetMap = viewMap.get(sheet.getSheetName());
                Class clazz = classMap.get(sheet.getSheetName());
                List dataList = new ArrayList();
                List<String> fieldNames = new ArrayList<String>();
                for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
                    Row row = sheet.getRow(j);
                    Object rowObj = clazz.newInstance();
                    //标题头
                    if (j == 0) {
                        Iterator<Cell> iterator = row.cellIterator();
                        while (iterator.hasNext()) {
                            Cell cell = iterator.next();
                            String cellValue = cell.getStringCellValue().trim();
                            fieldNames.add(sheetMap.get(cellValue));
                        }
                    } else {
                        for (int k = 0; k < row.getPhysicalNumberOfCells(); k++) {
                            Field field = clazz.getDeclaredField(fieldNames.get(k));
                            String methodName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                            Method method = BeanUtils.findDeclaredMethod(clazz, methodName, new Class[]{field.getType()});
                            Cell cell = row.getCell(k);
//                            if (cell != null) {
//                                if (cell.getCellType() == Cell.CELL_TYPE_BLANK || cell.getCellType() == Cell.CELL_TYPE_ERROR)
//                                    continue;
//                                else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
//                                    method.invoke(rowObj, new Object[]{String.valueOf(cell.getBooleanCellValue())});
//                                else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA)
//                                    method.invoke(rowObj, new Object[]{cell.getCellFormula()});
//                                else if (cell.getCellType() == Cell.CELL_TYPE_STRING)
//                                    method.invoke(rowObj, new Object[]{cell.getStringCellValue()});
//                                else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                                    method.invoke(rowObj, new Object[]{df.format(cell.getNumericCellValue())});
//                                } else method.invoke(rowObj, new Object[]{""});
//                            }
                            if (cell != null) {
                                String obj = cell.getStringCellValue();
                                //先判断常用类型，后续在进行加入
                                if (StringUtils.isEmpty(obj)) continue;
                                if (field.getType().equals(String.class)) method.invoke(rowObj, new Object[]{obj});
                                if (field.getType().equals(Integer.class))
                                    method.invoke(rowObj, new Object[]{Integer.valueOf(obj)});
                                if (field.getType().equals(Long.class))
                                    method.invoke(rowObj, new Object[]{Long.valueOf(obj)});
                                if (field.getType().equals(Date.class))
                                    method.invoke(rowObj, new Object[]{DateUtil.getDateByStr(obj)});
                            }
                        }
                        dataList.add(rowObj);
                    }
                }
                resultData.put(sheet.getSheetName(), dataList);
            }
            return resultData;
        } catch (Exception e) {
        	logger.error("generate data error!", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 创建单元格
     *
     * @param workbook 工作本对象
     * @param row      行对象
     * @param column   列号
     * @param halign   纵向对齐方式
     * @param valign   横向对齐方式
     */
    public static void createCell(Workbook workbook, Row row, short column, short halign, short valign, String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue("Align It");
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(value);
    }

    /**
     * 批量生成集合（多个sheet），目前支持字段类型：Integer、Long、String、java.util.Date
     * 客户导入专有方法
     *
     * @param workbook excel对象
     * @param classMap sheet对应的要装入的class类型
     * @param viewMap  sheet也对应的标题字段和类成员变量的类型映射
     * @return key:对应sheet名称，value：集合对象
     * @author Green_Tea
     */
   
	public static Map<String, List> generateDataForCustomer(Workbook workbook, Map<String, Class> classMap, Map<String, LinkedHashMap<String, String>> viewMap) {
        Map<String, List> resultData = new HashMap<String, List>();
        DecimalFormat df = new DecimalFormat("#");
        try {
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                if (i == 0) {
                    Sheet sheet = workbook.getSheetAt(i);
                    LinkedHashMap<String, String> sheetMap = viewMap.get(sheet.getSheetName());
                    Class clazz = classMap.get(sheet.getSheetName());
                    List dataList = new ArrayList();
                    List<String> fieldNames = new ArrayList<String>();
                    for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
                        Row row = sheet.getRow(j);
                        if(row==null) continue;
                        Object rowObj = clazz.newInstance();
                        //标题头
                        if (j == 0) {
                            Iterator<Cell> iterator = row.cellIterator();
                            while (iterator.hasNext()) {
                                Cell cell = iterator.next();
                                String cellValue = cell.getStringCellValue().trim();
                                fieldNames.add(sheetMap.get(cellValue));
                            }
                        } else {
                            for (int k = 0; k < row.getLastCellNum(); k++) {
                                Field field = clazz.getDeclaredField(fieldNames.get(k));
                                String methodName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                                Method method = BeanUtils.findDeclaredMethod(clazz, methodName, new Class[]{field.getType()});
                                Cell cell = row.getCell(k);
//                            if (cell != null) {
//                                if (cell.getCellType() == Cell.CELL_TYPE_BLANK || cell.getCellType() == Cell.CELL_TYPE_ERROR)
//                                    continue;
//                                else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
//                                    method.invoke(rowObj, new Object[]{String.valueOf(cell.getBooleanCellValue())});
//                                else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA)
//                                    method.invoke(rowObj, new Object[]{cell.getCellFormula()});
//                                else if (cell.getCellType() == Cell.CELL_TYPE_STRING)
//                                    method.invoke(rowObj, new Object[]{cell.getStringCellValue()});
//                                else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                                    method.invoke(rowObj, new Object[]{df.format(cell.getNumericCellValue())});
//                                } else method.invoke(rowObj, new Object[]{""});
//                            }
                                if (cell != null) {
//                                                                //先判断常用类型，后续在进行加入
//                                                                if (StringUtil.isEmpty(obj)) continue;
                                    if (field.getType().equals(String.class))
                                        method.invoke(rowObj, cellFormartString(cell));
                                    if (field.getType().equals(Integer.class))
                                        method.invoke(rowObj, cellFormartInteger(cell));
                                    if (field.getType().equals(Double.class))
                                        method.invoke(rowObj, cellFormartDouble(cell));
                                    if (field.getType().equals(BigDecimal.class))
                                        method.invoke(rowObj, cellFormartBigDecimal(cell));

/*	                            if (field.getType().equals(Long.class))
	                                method.invoke(rowObj, new Object[]{Long.valueOf(obj)});
	                            if (field.getType().equals(Date.class))
	                                method.invoke(rowObj, new Object[]{DateUtil.stringToDate2(obj)});*/
                                }
                            }
                            dataList.add(rowObj);
                        }
                    }
                    resultData.put(sheet.getSheetName(), dataList);
                }
            }
            return resultData;
        } catch (Exception e) {
        	logger.error("generate data error!", e);
            throw new RuntimeException(e);
        }
    }
    /**
     * 批量生成集合（多个sheet），目前支持字段类型：Integer、Long、String、java.util.Date
     * 客户导入专有方法
     *
     * @param workbook excel对象
     * @param classMap sheet对应的要装入的class类型
     * @param viewMap  sheet也对应的标题字段和类成员变量的类型映射
     * @return key:对应sheet名称，value：集合对象
     * @author Green_Tea
     */
    public static Map<String, List> generateFormalDataForCustomer(Workbook workbook, Map<String, Class> classMap, Map<String, LinkedHashMap<String, String>> viewMap) {
        Map<String, List> resultData = new HashMap<String, List>();
        DecimalFormat df = new DecimalFormat("#");
        try {
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                if (i == 0) {
                    Sheet sheet = workbook.getSheetAt(i);
                    LinkedHashMap<String, String> sheetMap = viewMap.get(sheet.getSheetName());
                    Class clazz = classMap.get(sheet.getSheetName());
                    List dataList = new ArrayList();
                    List<String> fieldNames = new ArrayList<String>();
                    for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
                        Row row = sheet.getRow(j);
                        if(row==null) continue;
                        Object rowObj = clazz.newInstance();
                        //标题头
                        if (j == 0) {
                            Iterator<Cell> iterator = row.cellIterator();
                            while (iterator.hasNext()) {
                                Cell cell = iterator.next();
                                String cellValue = cell.getStringCellValue().trim();
                                fieldNames.add(sheetMap.get(cellValue));
                            }
                        } else {
                            for (int k = 0; k < row.getLastCellNum(); k++) {
                                Field field = clazz.getDeclaredField(fieldNames.get(k));
                                String methodName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                                Method method = BeanUtils.findDeclaredMethod(clazz, methodName, new Class[]{field.getType()});
                                Cell cell = row.getCell(k);
//                            if (cell != null) {
//                                if (cell.getCellType() == Cell.CELL_TYPE_BLANK || cell.getCellType() == Cell.CELL_TYPE_ERROR)
//                                    continue;
//                                else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
//                                    method.invoke(rowObj, new Object[]{String.valueOf(cell.getBooleanCellValue())});
//                                else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA)
//                                    method.invoke(rowObj, new Object[]{cell.getCellFormula()});
//                                else if (cell.getCellType() == Cell.CELL_TYPE_STRING)
//                                    method.invoke(rowObj, new Object[]{cell.getStringCellValue()});
//                                else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                                    method.invoke(rowObj, new Object[]{df.format(cell.getNumericCellValue())});
//                                } else method.invoke(rowObj, new Object[]{""});
//                            }
                                if (cell != null) {
                                    String obj = cell.getStringCellValue();
                                    if (field.getType().equals(String.class))
                                        method.invoke(rowObj, cellFormartString(cell));
                                    if (field.getType().equals(Integer.class))
                                        method.invoke(rowObj, cellFormartInteger(cell));
                                    if (field.getType().equals(Double.class))
                                        method.invoke(rowObj, cellFormartDouble(cell));
                                    if (field.getType().equals(BigDecimal.class))
                                        method.invoke(rowObj, cellFormartBigDecimal(cell));
                                    if (field.getType().equals(Date.class))
                                        method.invoke(rowObj, new Object[]{DateUtil.getDateByStr(obj)});

/*	                            if (field.getType().equals(Long.class))
	                                method.invoke(rowObj, new Object[]{Long.valueOf(obj)});
	                            if (field.getType().equals(Date.class))
	                                method.invoke(rowObj, new Object[]{DateUtil.stringToDate2(obj)});*/
                                }
                            }
                            dataList.add(rowObj);
                        }
                    }
                    resultData.put(sheet.getSheetName(), dataList);
                }
            }
            return resultData;
        } catch (Exception e) {
        	logger.error("generate data error!", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @param workbook
     * @param entityClass
     * @param viewMap
     * @return
     */
    public static <T> List<T> generateDataForCustomer(Workbook workbook, Class<T> entityClass, LinkedHashMap<String, String> viewMap) {
        try {
            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Class> classMap = new HashMap<String, Class>();
            classMap.put(sheet.getSheetName(), entityClass);

            Map<String, LinkedHashMap<String, String>> view = new HashMap<String, LinkedHashMap<String, String>>();
            view.put(sheet.getSheetName(), viewMap);
            Map<String, List> resultMap = generateDataForCustomer(workbook, classMap, view);
            return (List<T>) resultMap.get(sheet.getSheetName());
        } catch (Exception e) {
        	logger.error("generate workbook error!", e);
            throw new RuntimeException(e);
        }
    }
    /**
     * @param workbook
     * @param entityClass
     * @param viewMap
     * @return
     */
    public static <T> List<T> generateFormalDataForCustomer(Workbook workbook, Class<T> entityClass, LinkedHashMap<String, String> viewMap) {
        try {
            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Class> classMap = new HashMap<String, Class>();
            classMap.put(sheet.getSheetName(), entityClass);

            Map<String, LinkedHashMap<String, String>> view = new HashMap<String, LinkedHashMap<String, String>>();
            view.put(sheet.getSheetName(), viewMap);
            Map<String, List> resultMap = generateFormalDataForCustomer(workbook, classMap, view);
            return (List<T>) resultMap.get(sheet.getSheetName());
        } catch (Exception e) {
        	logger.error("generate workbook error!", e);
            throw new RuntimeException(e);
        }
    }

    private static String cellFormartString(Cell cell) {
        DecimalFormat df = new DecimalFormat("#");
        String str = null;
        if (cell != null) {
            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                str = cell.getStringCellValue();
            } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                str = df.format(cell.getNumericCellValue());
            } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                str = null;
            }
        }
        return str;
    }

    private static Integer cellFormartInteger(Cell cell) {
        Integer num = null;
        try {
            if (cell != null) {
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    num = Integer.parseInt(cell.getStringCellValue());
                } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    num = (int) (cell.getNumericCellValue() / 1);
                } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                    num = null;
                }
            }
        } catch (NumberFormatException e) {
            num = null;
        }
        return num;
    }

    private static Double cellFormartDouble(Cell cell) {
        Double num = null;
        try {
            if (cell != null) {
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    num = Double.parseDouble(cell.getStringCellValue());
                } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    num = cell.getNumericCellValue();
                } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                    num = null;
                }
            }
        } catch (NumberFormatException e) {
            num = null;
        }
        return num;
    }

    private static BigDecimal cellFormartBigDecimal(Cell cell) {
        BigDecimal num = null;
        try {
            if (cell != null) {
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    return new BigDecimal(cell.getStringCellValue());
                } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    return new BigDecimal(cell.getNumericCellValue());
                } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                    return null;
                }
            }
            return num;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
