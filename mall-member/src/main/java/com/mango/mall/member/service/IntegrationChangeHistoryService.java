package com.mango.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mango.common.utils.PageUtils;
import com.mango.mall.member.entity.IntegrationChangeHistoryEntity;

import java.util.Map;

/**
 * 积分变化历史记录
 *
 * @author Mango
 * @email mango@mango.com
 * @date 2022-03-03 21:02:23
 */
public interface IntegrationChangeHistoryService extends IService<IntegrationChangeHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

