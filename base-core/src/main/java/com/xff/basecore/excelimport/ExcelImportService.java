package com.xff.basecore.excelimport;

import com.xff.basecore.common.constant.CommonConstant;
import com.xff.basecore.common.swagger.SwaggerResultUtil;
import com.xff.basecore.common.util.SpringContextUtil;
import com.xff.basecore.excelimport.config.ExcelColumn;
import com.xff.basecore.excelimport.config.ExcelConfigManager;
import com.xff.basecore.excelimport.config.ExcelModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Excel导入服务类.
 *
 * @author wang_fei
 * @since 2022/3/3 18:14
 */
@Slf4j
@Component
public class ExcelImportService {

    /**
     * Excel导入成功说明
     */
    private static final String EXCEL_IMPORT_CGSM = "已导入";

    /**
     * Excel导入错误信息
     */
    private static final String EXCEL_IMPORT_CWXX = "Excel导入方法出错";

    /**
     * Excel导入组件错误信息
     */
    private static final String EXCEL_IMPORT_ZJCWXX = "Excel导入组件执行导入操作出错";

    /**
     * Excel导入文件默认扩展名
     */
    private String fileExt = "xlsx";

    /**
     * 临时文件目录
     */
    private static final String TEMP_FILE_DIR = "tempfile";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Excel导入方法（暴露给外部的通用导入方法）
     *
     * @param excelImportId Excel导入ID
     * @param mfile         MultipartFile的对象
     * @param authorityId   用户权限ID
     */
    public SwaggerResultUtil<String> excelImport(String excelImportId, MultipartFile mfile, String authorityId) {
        // 错误信息
        String cwxx = "";
        // 缓存Excel导入执行标识，最长缓存时间10分钟
        String excelimportZxbs = CommonConstant.EXCEL_IMPORT_DOING + excelImportId + authorityId;
        redisTemplate.opsForValue().set(excelimportZxbs, CommonConstant.STR_Y, 600L);
        //获取临时文件目录
        String outputPath = getOutputPath();
        File tempFileDir = new File(outputPath);
        //临时文件目录不存在则创建一个目录
        if (!tempFileDir.exists()) {
            tempFileDir.mkdirs();
        }
        //创建临时文件
        if (null != mfile && StringUtils.isNotBlank(mfile.getOriginalFilename())
                && mfile.getOriginalFilename().split("\\.").length == 2) {
            fileExt = mfile.getOriginalFilename().split("\\.")[1];
        }
        File tempFile = new File(outputPath + File.separator + nameFile(fileExt));
        assert tempFile != null;
        //初始化输入流
        InputStream is = null;
        //初始化Excel导入参数
        ExcelImportParam param = null;
        try {
            //将上传的文件写入临时文件
            mfile.transferTo(tempFile);
            //根据临时文件实例化输入流
            is = Files.newInputStream(tempFile.toPath());
            //创建Workbook的对象
            Workbook wb = new XSSFWorkbook(is);
            //读取excel里面的内容
            param = readExcelContent(wb, excelImportId);
        } catch (Exception e) {
            log.error(EXCEL_IMPORT_CWXX, e);
            cwxx = null != e ? EXCEL_IMPORT_CWXX + "：" + e : EXCEL_IMPORT_CWXX + "！";
        } finally {
            try {
                //关闭输入流
                if (is != null) {
                    is.close();
                }
                //删除临时文件
                if (tempFile.exists()) {
                    tempFile.delete();
                }
                assert param != null;
                if (param.getDataMap().size() == 0 && param.getEdrjgList().size() == 0) {
                    delExcelimportZxbs(excelImportId, authorityId);
                    return SwaggerResultUtil.resultError(500, "Excel模板没有数据或填写格式不对，导入失败！");
                }
                //Excel导入组件执行导入操作
                Object actor = SpringContextUtil.getBean(param.getImportClass());
                if (null != actor) {
                    ExcelImportComp component = (ExcelImportComp) actor;
                    param.setExcelImportId(excelImportId);
                    param.setAuthorityId(authorityId);
                    component.doExcelImport(param);
                }
            } catch (Exception e) {
                log.error(EXCEL_IMPORT_ZJCWXX, e);
                cwxx = null != e ? EXCEL_IMPORT_ZJCWXX + "：" + e : EXCEL_IMPORT_ZJCWXX + "！";
            }
            //上传成功返回文件名称，否则返回错误信息
            if (StringUtils.isBlank(cwxx)) {
                return SwaggerResultUtil.resultSuccess(mfile.getOriginalFilename());
            } else {
                delExcelimportZxbs(excelImportId, authorityId);
                return SwaggerResultUtil.resultError(500, cwxx);
            }
        }
    }

    /**
     * 读取excel里面的内容
     *
     * @param wb            Workbook的对象
     * @param excelImportId Excel导入ID
     * @return Excel导入参数
     */
    private ExcelImportParam readExcelContent(Workbook wb, String excelImportId)
            throws Exception {
        //初始化导入参数
        ExcelImportParam param = new ExcelImportParam();
        //得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        //得到Excel的总行数
        int totalRows = sheet.getLastRowNum();
        //获取Excel导入模块配置信息
        ExcelModel excelModel = getExcelModel(excelImportId);
        param.setImportClass(excelModel.getImportClass());
        //得到Excel的起始行
        int startRow = excelModel.getStartRow();
        //得到Excel的总列数
        int totalCells = 0;
        if (totalRows >= startRow && null != sheet.getRow(0)) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        //封装字段名称与set方法的映射
        Class clazz = null;
        Map<String, Method> fieldMap = new HashMap();
        Map<Integer, List<ExcelColumn>> excelColumnMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(excelModel.getExcelColumnList())) {
            excelColumnMap =
                    excelModel.getExcelColumnList().stream().collect(Collectors.groupingBy(ExcelColumn::getIndex));
            clazz = this.getClass().getClassLoader().loadClass(excelModel.getImportPO());
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                //如果属性是序列化版本号跳过
                if ("serialVersionUID".equals(fieldName)) {
                    continue;
                }
                //如果属性是编译过程中加入的跳过
                if (field.isSynthetic()) {
                    continue;
                }
                String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method setMethod = clazz.getMethod(setMethodName, new Class[]{field.getType()});
                fieldMap.put(fieldName, setMethod);
            }
        }
        //循环Excel的行
        ExcelImportJgLog edrjg;
        for (int r = startRow; r <= totalRows; r++) {
            Row row = sheet.getRow(r);
            edrjg = new ExcelImportJgLog();
            edrjg.setDrrh(r + 1);
            edrjg.setDrmk(excelModel.getDrmk());
            edrjg.setDrjg(CommonConstant.STR_Y);
            edrjg.setDrsm(EXCEL_IMPORT_CGSM);
            // 如果是空行则跳过
            if (null == row || blankRowCheck(totalCells, row)) {
                edrjg.setDrjg(CommonConstant.STR_N);
                edrjg.setDrsm(blankRowDrsm(edrjg.getDrrh()));
                param.getEdrjgList().add(edrjg);
                continue;
            }
            //循环Excel的列
            boolean isWrongRow = false;
            Object obj = clazz.newInstance();
            for (int c = 0; c < totalCells; c++) {
                ExcelColumn excelColumn = excelColumnMap.get(c).get(0);
                //获取单元格的值
                Cell cell = row.getCell(c);
                String cellValue = "";
                if (null != cell) {
                    cellValue = getCellStrVal(cell);
                }
                //设置导入结果的标题
                String fieldName = excelColumn.getName();
                if (fieldName.equals(excelModel.getEdrjgBt())) {
                    edrjg.setDrbt(cellValue);
                }
                //去掉空格
                if (excelColumn.isNoTrim() && StringUtils.isNotBlank(cellValue)) {
                    cellValue = cellValue.trim();
                }
                //必填校验
                if (excelColumn.isRequired() && StringUtils.isBlank(cellValue)) {
                    edrjg.setDrsm(excelColumn.getChineseName() + "的值不能为空");
                    isWrongRow = true;
                    break;
                }
                //长度校验
                if (null != excelColumn.getLength() && null != cellValue
                        && cellValue.length() > excelColumn.getLength()) {
                    edrjg.setDrsm(excelColumn.getChineseName() + "的值长度不能超过" +
                            excelColumn.getLength());
                    isWrongRow = true;
                    break;
                }
                //填充对象的属性值
                Method m = fieldMap.get(fieldName);
                m.invoke(obj, cellValue);
            }
            // 非法行拦截
            if (isWrongRow) {
                edrjg.setDrjg(CommonConstant.STR_N);
                param.getEdrjgList().add(edrjg);
                continue;
            }
            //填充Excel导入参数
            param.getDataMap().put(edrjg.getDrrh(), obj);
            param.getEdrjgList().add(edrjg);
        }
        //清除Excel末尾的空行
        for (int i = param.getEdrjgList().size() - 1; i >= 0; i--) {
            ExcelImportJgLog ssEdrjg = param.getEdrjgList().get(i);
            //末尾不是空行则跳出
            if (!blankRowDrsm(ssEdrjg.getDrrh()).equals(ssEdrjg.getDrsm())) {
                break;
            }
            //末尾是空行则删除此行
            param.getEdrjgList().remove(i);
        }
        return param;
    }

    /**
     * 获取临时文件输出路径
     *
     * @return 临时文件输出路径
     */
    private String getOutputPath() {
        ApplicationHome h = new ApplicationHome(getClass());
        File jarF = h.getSource();
        return jarF.getParentFile().toString() + File.separator + TEMP_FILE_DIR;
    }

    /**
     * 命名临时文件
     *
     * @param fileExt 文件扩展名
     */
    private String nameFile(String fileExt) {
        if (StringUtils.isBlank(fileExt)) {
            return null;
        }
        // 返回临时文件名称
        String randomFileName = RandomStringUtils.randomAlphabetic(10).toLowerCase(Locale.ROOT);
        return "temp_" + randomFileName + new Date().getTime() + "." + fileExt;
    }

    /**
     * 获取Excel导入模块配置信息
     *
     * @param excelImportId Excel导入ID
     * @return Excel导入模块配置信息
     */
    private ExcelModel getExcelModel(String excelImportId) {
        List<ExcelModel> list = ExcelConfigManager.getInstance().excelModelMap.get(excelImportId);
        return CollectionUtils.isNotEmpty(list) ? list.get(0) : new ExcelModel();
    }

    /**
     * Excel空行校验
     *
     * @param totalCells Excel的总列数
     * @param row        行对象
     * @return true: 空行；false: 不是空行
     */
    private boolean blankRowCheck(int totalCells, Row row) {
        boolean isBlankRow = true;
        for (int c = 0; c < totalCells; c++) {
            Cell cell = row.getCell(c);
            if (null != cell && StringUtils.isNotBlank(getCellStrVal(cell))) {
                isBlankRow = false;
            }
        }
        return isBlankRow;
    }

    /**
     * 空行导入说明
     *
     * @param drrh 行号
     * @return 导入说明
     */
    private String blankRowDrsm(int drrh) {
        return "第" + drrh + "行是空行";
    }

    /**
     * 获取Excel单元格字符串的值
     *
     * @param cell Excel单元格对象
     * @return Excel单元格字符串的值
     */
    private String getCellStrVal(Cell cell) {
        String cellValue = "";
        try {
            //导入Excle从未打开保存过，则直接用getStringCellValue方法
            cellValue = cell.getStringCellValue();
        } catch (Exception e) {
            //对于值为数字这类单元格需先设定cell为String类型
            cell.setCellType(CellType.STRING);
            cellValue = cell.getStringCellValue();
        }
        return cellValue;
    }

    /**
     * 删除导入执行标识
     *
     * @param excelImportId Excel导入ID
     * @param authorityId   用户权限ID
     */
    private void delExcelimportZxbs(String excelImportId, String authorityId) {
        redisTemplate.delete(CommonConstant.EXCEL_IMPORT_DOING + excelImportId + authorityId);
    }

}
