package net.liuxuan.db.service;

import net.liuxuan.db.entity.UserInfo;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-05-31
 **/
public interface UserInfoService extends BaseService<UserInfo, Integer> {
    UserInfo fetchUserByUserName(String userName);

//    UserInfo saveUser(UserInfo userinfo);
}
