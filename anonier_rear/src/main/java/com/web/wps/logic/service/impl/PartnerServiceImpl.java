package com.web.wps.logic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.wps.logic.entity.WpsPartner;
import com.web.wps.logic.mapper.PartnerServiceMapper;
import com.web.wps.logic.service.PartnerService;
import org.springframework.stereotype.Service;

/**
 * @author gengchuang
 * @version v1.0
 * @PACKAGE com.web.wps.logic.service.impl
 * @date 2021/1/18/下午6:16
 **/
@Service
public class PartnerServiceImpl extends ServiceImpl<PartnerServiceMapper, WpsPartner> implements PartnerService {
}
