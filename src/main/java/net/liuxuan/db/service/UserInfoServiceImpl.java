package net.liuxuan.db.service;

import net.liuxuan.db.entity.UserInfo;
import net.liuxuan.db.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-05-31
 **/
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    UserInfoRepository userInfoRepository;

    @Override
    public UserInfo fetchUserByUserName(String userName) {
        UserInfo oneByName = userInfoRepository.findOneByName(userName);
        return oneByName;
    }
}
