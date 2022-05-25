package com.xff.servicesmgl.excelimport;

import com.xff.basecore.excelimport.ExcelImportComp;
import com.xff.basecore.excelimport.ExcelImportParam;
import com.xff.basecore.excelimport.ExcelImportResult;
import com.xff.servicesmgl.bean.Voucher;
import com.xff.servicesmgl.dao.VoucherMapper;
import com.xff.servicesmgl.po.VoucherImportPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * ExcelImportVoucher.
 *
 * @author wang_fei
 * @since 2022/4/1 15:30
 */
@Slf4j
@Component("excelImportVoucher")
public class ExcelImportVoucher extends ExcelImportComp {

    @Autowired
    private VoucherMapper voucherMapper;

    @Override
    public Map<Integer, String> doValidate(ExcelImportParam param) {
        return null;
    }

    @Override
    public ExcelImportResult doImport(ExcelImportParam param) {
        ExcelImportResult importResult = new ExcelImportResult();
        for (Map.Entry<Integer, Object> entry : param.getDataMap().entrySet()) {
            int key = entry.getKey();
            try {
                VoucherImportPO po = (VoucherImportPO) entry.getValue();
                Voucher voucher = new Voucher();
                BeanUtils.copyProperties(po, voucher);
                voucher.setStSex("女".equals(voucher.getStSex()) ? "0" : "1");
                voucher.setId(UUID.randomUUID().toString());
                int iKey = voucherMapper.insert(voucher);
                if (iKey == 1) {
                    importResult.getIdList().add(voucher.getId());
                } else {
                    importResult.getErrorMsgMap().put(key, "Excel导入时插入数据失败");
                }
            } catch (Exception e) {
                log.error("Excel导入时插入数据报错", e);
                errorHandler(importResult.getErrorMsgMap(), key, "Excel导入时插入数据报错", e);
            }
        }
        return importResult;
    }

    /**
     * 处理错误信息
     *
     * @param errorMsgMap Excel导入错误信息
     * @param drhh        导入行号
     * @param msg         错误信息
     * @param e           异常信息
     */
    private void errorHandler(Map<Integer, String> errorMsgMap, int drhh, String msg, Exception e) {
        String cwxx = StringUtils.isNotBlank(e.getMessage()) ? msg + "：" + e.getMessage() : msg;
        cwxx = cwxx.length() > 1024 ? cwxx.substring(0, 1024) : cwxx;
        errorMsgMap.put(drhh, cwxx);
    }
}
