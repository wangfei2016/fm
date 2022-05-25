package com.xff.basecore.excelimport.config;

import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel导入配置信息.
 *
 * @author wang_fei
 * @since 2022/3/3 17:50
 */
@Setter
@XmlRootElement(name = "excel-config")
public class ExcelConfig implements Serializable {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 1L;

    /**
     * Excel导入模块配置信息集合
     */
    private List<ExcelModel> excelModelList = new ArrayList<>();

    @XmlElement(name = "excel-model")
    public List<ExcelModel> getExcelModelList() {
        return excelModelList;
    }
}
