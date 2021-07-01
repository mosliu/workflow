package net.liuxuan.db.service;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.common.BaseServiceImpl;
import net.liuxuan.db.entity.RoleInfo;
import net.liuxuan.db.repository.RoleInfoRepository;
import org.springframework.stereotype.Service;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-06-03
 **/
@Service
@Slf4j
public class RoleInfoServiceImpl extends BaseServiceImpl<RoleInfo, Integer, RoleInfoRepository> implements RoleInfoService {
//    public RoleInfoServiceImpl(RoleInfoRepository repository) {
//        super.repository = repository;
//    }
}
