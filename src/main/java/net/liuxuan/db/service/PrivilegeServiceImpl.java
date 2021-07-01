package net.liuxuan.db.service;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.common.BaseServiceImpl;
import net.liuxuan.db.entity.Privilege;
import net.liuxuan.db.repository.PrivilegeRepository;
import org.springframework.stereotype.Service;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-06-03
 **/
@Service
@Slf4j
public class PrivilegeServiceImpl extends BaseServiceImpl<Privilege, Integer, PrivilegeRepository> implements PrivilegeService {
//    public PrivilegeServiceImpl(PrivilegeRepository repository) {
//        super.repository = repository;
//    }
}
