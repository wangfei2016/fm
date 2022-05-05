package com.xff.basecore.excelimport;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel导入出参类.
 *
 * @author wang_fei
 * @since 2022/3/4 14:39
 */
@Getter
@Setter
public class ExcelImportResult {

    /**
     * Excel导入业务表记录的ID集合
     */
    private List<String> idList = new ArrayList<>();

    /**
     * Excel导入错误信息
     */
    private Map<Integer, String> errorMsgMap = new HashMap<>();

}

