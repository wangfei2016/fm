package com.xff.basecore.excelimport.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Excel导入配置信息加载器.
 *
 * @author wang_fei
 * @since 2022/3/3 16:52
 */
@Slf4j
public class ExcelConfigManager {

    /**
     * 初始化Excel导入模块配置信息（暴露给外部的属性）
     */
    public Map<String, List<ExcelModel>> excelModelMap = new HashMap<>();

    /**
     * 初始化Excel导入配置路径
     */
    private static final String CONFIG_PATH = "model/excelimport-config.xml";

    /**
     * 初始化Excel导入配置信息
     */
    private static ExcelConfig excelConfig = null;

    /**
     * 声明单例实例
     */
    private static ExcelConfigManager instance = new ExcelConfigManager();

    /**
     * 获取Excel导入配置信息加载器实例（暴露给外部的方法）
     *
     * @return Excel导入配置信息加载器实例
     */
    public static ExcelConfigManager getInstance() {
        return instance;
    }

    /**
     * 构造方法
     */
    private ExcelConfigManager() {
        // 加载Excel导入配置信息
        if (this.excelConfig == null) {
            this.excelConfig = loadExcelConfig();
        }
        // 加载Excel导入模块配置信息
        List<ExcelModel> excelModelList = excelConfig.getExcelModelList();
        if (CollectionUtils.isNotEmpty(excelModelList)) {
            this.excelModelMap = excelModelList.stream().collect(Collectors.groupingBy(ExcelModel::getId));
        }
    }

    /**
     * 加载Excel导入配置信息。
     *
     * @return Excel导入配置信息
     */
    private ExcelConfig loadExcelConfig() {
        InputStream is = null;
        try {
            is = ExcelConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_PATH);
        } catch (Exception e) {
            log.error("加载配置文件excelimport-config.xml出错", e);
        } finally {
            if (null != is) {
                return reloadExcelConfig(is);
            } else {
                return new ExcelConfig();
            }
        }
    }

    /**
     * 从配置文件输入流获取Excel导入配置信息。
     *
     * @param input 配置文件输入流
     * @return Excel导入配置信息
     */
    private ExcelConfig reloadExcelConfig(InputStream input) {
        JAXBContext jc;
        ExcelConfig excelConfig = null;
        try {
            jc = JAXBContext.newInstance(ExcelConfig.class);
            Unmarshaller u = jc.createUnmarshaller();
            excelConfig = (ExcelConfig) u.unmarshal(input);
        } catch (JAXBException e) {
            log.error("从配置文件输入流获取Excel导入配置信息出错", e);
        }
        return excelConfig == null ? new ExcelConfig() : excelConfig;
    }
}
