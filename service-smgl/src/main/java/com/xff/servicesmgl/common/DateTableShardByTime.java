package com.xff.servicesmgl.common;

import com.xff.basecore.decollat.DateTableShardStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;

/**
 * DateTableShardByTime.
 *
 * @author wang_fei
 * @since 2022/4/28 9:20
 */
@Slf4j
public class DateTableShardByTime extends DateTableShardStrategy {

    @Override
    public String getTableShardName(String tableName) {
        // 按当前时间秒的奇偶分表
        int second = Calendar.getInstance().get(Calendar.SECOND);
        log.info("当前时间秒：" + second);
        return tableName.toLowerCase() + "_" + second % 2;
    }
}
