package com.xff.basecore.excelimport;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Excel导入结果日志.
 *
 * @author wang_fei
 * @since 2022/3/4 15:27
 */
@Getter
@Setter
public class ExcelImportJgLog implements Serializable {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 1L;

    /**
     * 导入行号
     */
    @ApiModelProperty(value = "导入行号")
    private int drrh;

    /**
     * 导入标题
     */
    @ApiModelProperty(value = "导入标题")
    private String drbt;

    /**
     * 导入结果
     */
    @ApiModelProperty(value = "导入结果：Y导入成功 N导入失败")
    private String drjg;

    /**
     * 导入说明
     */
    @ApiModelProperty(value = "导入说明")
    private String drsm;

    /**
     * 组织id
     */
    @ApiModelProperty(value = "组织id")
    private String bmid;

    /**
     * 导入模块
     */
    @ApiModelProperty(value = "导入模块")
    private String drmk;

    /**
     * 是否最新记录
     */
    @ApiModelProperty(value = "是否最新记录(Y/N)")
    private String sfzxjl;

    /**
     * 创建人名称
     */
    @ApiModelProperty(value = "创建人名称")
    private String cjrmc;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String cjsj;

}
