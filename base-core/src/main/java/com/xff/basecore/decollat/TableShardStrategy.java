package com.xff.basecore.decollat;

/**
 * TableShardStrategy.
 *
 * @author wang_fei
 * @since 2022/4/27 17:20
 */
public interface TableShardStrategy {

    String getTableShardName(String tableName);

}
