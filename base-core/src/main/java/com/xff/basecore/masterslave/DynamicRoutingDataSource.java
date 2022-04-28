package com.xff.basecore.masterslave;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * DynamicRoutingDataSource.
 *
 * @author wang_fei
 * @since 2022/3/24 9:09
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    private static final Logger logger = LoggerFactory.getLogger(DynamicRoutingDataSource.class);

    /**
     * Set dynamic DataSource to Application Context
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        logger.info("Current DataSource is [{}]", DynamicDataSourceContextHolder.getDataSource());
        return DynamicDataSourceContextHolder.getDataSource();
    }
}
