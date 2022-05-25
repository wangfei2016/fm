package com.xff.basecore.excelimport.config;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**
 * Excel导入列配置信息.
 *
 * @author wang_fei
 * @since 2022/3/3 16:36
 */
@Getter
@Setter
public class ExcelColumn implements Serializable {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 1L;

    /**
     * Excel导入列号
     */
    private int index;

    /**
     * Excel导入属性名称
     */
    private String name;

    /**
     * Excel导入属性中文名称
     */
    private String chineseName;

    /**
     * Excel导入属性长度
     */
    private Integer length = null;

    /**
     * Excel导入属性必填校验
     */
    private boolean required = false;

    /**
     * Excel导入属性值去空格
     */
    private boolean noTrim = true;

    @XmlAttribute
    public int getIndex() {
        return index;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    @XmlAttribute
    public String getChineseName() {
        return chineseName;
    }

}
