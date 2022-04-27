package com.xff.basecore.decollat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DateTableShardStrategy.
 *
 * @author wang_fei
 * @since 2022/4/27 17:21
 */
public class DateTableShardStrategy implements TableShardStrategy {

    public static final String PATTERN = "yyyyMM";

    @Override
    public String getTableShardName(String tableName) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(PATTERN);
        String date = sdf.format(new Date());
        return tableName + "_" + date;
    }

}
