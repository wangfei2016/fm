package com.xff.basecore.decollat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TableShard.
 *
 * @author wang_fei
 * @since 2022/4/27 17:19
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TableShard {

    Class<? extends TableShardStrategy> shardStrategy();

}
