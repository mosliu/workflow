package net.liuxuan.db.service;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.common.BaseServiceImpl;
import net.liuxuan.db.entity.UserInfo;
import net.liuxuan.db.repository.UserInfoRepository;
import org.springframework.stereotype.Service;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-05-31
 **/
@Service
@Slf4j
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfo, Integer, UserInfoRepository> implements UserInfoService {

//    @Autowired
//    UserInfoRepository userInfoRepository;

    @Override
    public UserInfo fetchUserByUserName(String userName) {
        UserInfo oneByName = repository.findOneByName(userName);
        return oneByName;
    }

//    @Override
//    public UserInfo saveUser(UserInfo userinfo) {
//        if (userinfo == null) return null;
//        UserInfo save = repository.save(userinfo);
//        return save;
//    }
}
