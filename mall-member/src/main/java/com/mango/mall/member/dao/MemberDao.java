package com.mango.mall.member.dao;

import com.mango.mall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 *
 * @author Mango
 * @email mango@mango.com
 * @date 2022-03-03 21:02:23
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {

}
