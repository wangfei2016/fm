package com.xff.servicesmgl.dao;

import com.xff.servicesmgl.bean.Voucher;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * VoucherMapper.
 *
 * @author wang_fei
 * @since 2022/4/1 16:34
 */
@Repository
public interface VoucherMapper extends Mapper<Voucher> {

    List<Voucher> queryListByCondition();

    void copyTab(@Param("tbName") String tbName, @Param("targetTbName") String targetTbName);

}
