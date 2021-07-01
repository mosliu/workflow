package net.liuxuan.db.service;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.common.BaseServiceImpl;
import net.liuxuan.db.entity.UserGroup;
import net.liuxuan.db.repository.UserGroupRepository;
import org.springframework.stereotype.Service;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-06-03
 **/
@Service
@Slf4j
public class UserGroupServiceImpl extends BaseServiceImpl<UserGroup, Integer, UserGroupRepository> implements UserGroupService {
//    public UserGroupServiceImpl(UserGroupRepository repository) {
//        super.repository = repository;
//    }
}
