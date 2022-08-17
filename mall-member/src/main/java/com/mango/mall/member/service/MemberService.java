package com.mango.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mango.common.utils.PageUtils;
import com.mango.mall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author Mango
 * @email mango@mango.com
 * @date 2022-03-03 21:02:23
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

