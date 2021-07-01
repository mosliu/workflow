package net.liuxuan.db.service;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.common.BaseServiceImpl;
import net.liuxuan.db.entity.UserDetailsEntity;
import net.liuxuan.db.repository.UserDetailsRepository;
import org.springframework.stereotype.Service;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-06-22
 **/
@Service
@Slf4j
public class UserDetailsEntityServiceImpl extends BaseServiceImpl<UserDetailsEntity, Integer, UserDetailsRepository> implements UserDetailsEntityService {
}
