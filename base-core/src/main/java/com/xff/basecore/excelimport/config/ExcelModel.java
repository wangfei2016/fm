package com.xff.basecore.excelimport.config;

import lombok.Setter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel导入模块配置信息.
 *
 * @author wang_fei
 * @since 2022/3/3 16:33
 */
@Setter
public class ExcelModel implements Serializable {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 1L;

    /**
     * Excel导入ID
     */
    private String id;

    /**
     * Excel导入起始行
     */
    private int startRow;

    /**
     * Excel导入PO类：类的全路径名称
     */
    private String importPO;

    /**
     * Excel导入实现类：类的注解名称
     */
    private String importClass;

    /**
     * Excel导入结果标题：Excel导入后的结果标题
     */
    private String edrjgBt;

    /**
     * Excel导入模块
     */
    private String drmk;

    /**
     * Excel导入列配置信息集合
     */
    private List<ExcelColumn> excelColumnList = new ArrayList<>();

    @XmlAttribute
    public String getId() {
        return id;
    }

    @XmlAttribute
    public int getStartRow() {
        return startRow;
    }

    @XmlAttribute
    public String getImportPO() {
        return importPO;
    }

    @XmlAttribute
    public String getImportClass() {
        return importClass;
    }

    @XmlAttribute
    public String getEdrjgBt() {
        return edrjgBt;
    }

    @XmlAttribute
    public String getDrmk() {
        return drmk;
    }

    @XmlElement(name = "excel-column")
    public List<ExcelColumn> getExcelColumnList() {
        return excelColumnList;
    }
}
