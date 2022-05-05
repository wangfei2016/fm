package com.xff.basecore.excelimport;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel导入参数类.
 *
 * @author wang_fei
 * @since 2022/3/4 14:34
 */
@Getter
@Setter
public class ExcelImportParam {

    /**
     * Excel导入ID
     */
    private String excelImportId;

    /**
     * Excel导入数据隔离：用户权限ID
     */
    private String authorityId;

    /**
     * Excel导入实现类：类的注解名称
     */
    private String importClass = null;

    /**
     * Excel导入数据集：行号和导入对象的映射
     */
    private Map<Integer, Object> dataMap = new HashMap<>();

    /**
     * Excel导入结果集：Excel导入后的结果集合
     */
    private List<ExcelImportJgLog> edrjgList = new ArrayList<>();


}
