package com.xff.basecore.excelimport;

import com.xff.basecore.common.constant.CommonConstant;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

/**
 * Excel导入组件类.
 *
 * @author wang_fei
 * @since 2022/3/4 14:25
 */
@Component
public abstract class ExcelImportComp {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Excel导入主方法
     *
     * @param param Excel导入参数
     */
    @Async
    public void doExcelImport(ExcelImportParam param) {
        // 1、先执行校验
        Map<Integer, String> errorMsgMap = doValidate(param);
        // 2、校验后同步Excel导入结果对象
        synchEdrjg(param, errorMsgMap);
        // 3、校验后把不合法记录从数据集中剔除
        Iterator<Map.Entry<Integer, Object>> it = param.getDataMap().entrySet().iterator();
        if (null != errorMsgMap && errorMsgMap.size() > 0) {
            while (it.hasNext()) {
                if (errorMsgMap.containsKey(it.next().getKey())) {
                    it.remove();
                }
            }
        }
        // 4、再执行导入
        ExcelImportResult importResult = doImport(param);
        // 5、更新业务表记录的用户信息
//        updateUserForBusiness(param.getLoginUser(), importResult.getIdList());
        // 6、导入后同步Excel导入结果对象
        synchEdrjg(param, importResult.getErrorMsgMap());
        // 7、最后保存导入结果
//        edrjgService.batchAddEdrjg(param.getLoginUser(), param.getEdrjgList());
        // 9、清除导入执行标识
        redisTemplate.delete(CommonConstant.EXCEL_IMPORT_DOING + param.getExcelImportId() + param.getAuthorityId());
    }

    /**
     * 执行导入校验
     *
     * @param param Excel导入参数
     * @return 导入校验的错误信息
     */
    public abstract Map<Integer, String> doValidate(ExcelImportParam param);

    /**
     * 执行导入操作
     *
     * @param param Excel导入参数
     * @return 导入出参信息
     */
    public abstract ExcelImportResult doImport(ExcelImportParam param);

    /**
     * 更新业务表记录的用户信息
     *
     * @param loginUser 当前用户信息
     * @param idList    业务表记录的ID集合
     */
//    public abstract void updateUserForBusiness(SystemSsoUser loginUser, List<String> idList);

    /**
     * 同步Excel导入结果对象
     *
     * @param param       Excel导入参数
     * @param errorMsgMap Excel导入错误信息
     */
    private void synchEdrjg(ExcelImportParam param, Map<Integer, String> errorMsgMap) {
        if (null == param || CollectionUtils.isEmpty(param.getEdrjgList()) || null == errorMsgMap) {
            return;
        }
        param.getEdrjgList().stream().filter(edrjg -> errorMsgMap.containsKey(edrjg.getDrrh()))
                .forEach(edrjg -> {
                    edrjg.setDrjg(CommonConstant.STR_N);
                    edrjg.setDrsm(errorMsgMap.get(edrjg.getDrrh()));
                });
    }
}
