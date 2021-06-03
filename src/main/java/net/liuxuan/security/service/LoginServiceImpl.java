package net.liuxuan.security.service;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-05-31
 **/

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.db.entity.UserInfo;
import net.liuxuan.db.service.UserInfoService;
import net.liuxuan.security.SecurityUtils;
import net.liuxuan.security.dto.ButtonDto;
import net.liuxuan.security.dto.MenuDto;
import net.liuxuan.security.dto.UserDto;
import net.liuxuan.security.exception.JwtAuthenticationException;
import net.liuxuan.security.jwt.JwtToken;
import net.liuxuan.security.jwt.JwtTokenUtil;
import net.liuxuan.security.jwt.JwtUser;
import net.liuxuan.utils.date.LocalTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.liuxuan.security.constants.SecurityConstants.*;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {


    @Autowired
    @Lazy
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    UserInfoService userInfoService;


    // 如果在WebSecurityConfigurerAdapter中，没有重新，这里就会报注入失败的异常
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //    @Override
//    public String login(String username, String password) throws AuthenticationException {
//        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
//        Authentication authentication = authenticationManager.authenticate(upToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        return jwtTokenUtil.generateToken(userDetails);
//    }
    @Override
    public UserInfo findByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("用户名不可以为空!");
        }
        UserInfo userInfo = userInfoService.fetchUserByUserName(username);
        if (userInfo == null || StringUtils.isEmpty(userInfo.getName())) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
        log.info("获取User： {}", userInfo);
        //应该获取用户所有的角色
        return userInfo;
    }

//    @Override
//    public SysUserDto findUserInfo(String username) {
//        /**
//         * 获取用户信息
//         */
//        SysUser sysUser = findByUsername(username);
//        /**
//         * 获取当前用户的所有角色
//         */
//        Set<SysRole> sysRoles = sysRoleService.selectByUserName(username);
//
//        /**
//         * 在这里我的想法是，构建一个按钮权限列表
//         * 再构建一个菜单权限列表
//         * 这样的我们在前端的写的时候，就不用解析的很麻烦了
//         * 因为权限表是一张表，在这里解析好了以后，
//         * 相当前端少做一点工作，当然这也可以放到前端去解析权限列表
//         */
//        Set<ButtonDto> ButtonDtos = new HashSet<>();
//        Set<MenuDto> MenuDtos = new HashSet<>();
//
//        sysRoles.forEach(role -> {
//            log.info("role: {}", role.getDescribe());
//            role.getPermissions().forEach(permission -> {
//                if (permission.getType().toLowerCase().equals("button")) {
//                    /*
//                     * 如果权限是按钮，就添加到按钮里面
//                     * */
//                    ButtonDtos.add(new ButtonDto(permission.getPid(), permission.getResources(), permission.getTitle()));
//                }
//                if (permission.getType().toLowerCase().equals("menu")) {
//                    /*
//                     * 如果权限是菜单，就添加到菜单里面
//                     * */
//                    MenuDtos.add(new MenuDto(permission.getPid(), permission.getFather(), permission.getIcon(), permission.getResources(), permission.getTitle()));
//                }
//            });
//        });
//
//        /**
//         * 注意这个类 TreeBuilder。因为的vue router是以递归的形式呈现菜单
//         * 所以我们需要把菜单跟vue router 的格式一一对应 而按钮是不需要的
//         */
//        SysUserDto sysUserDto =
//                new SysUserDto(sysUser.getUid(), sysUser.getAvatar(),
//                        sysUser.getNickname(), sysUser.getUsername(),
//                        sysUser.getMail(), sysUser.getAddTime(),
//                        sysUser.getRoles(), ButtonDtos, TreeBuilder.findRoots(MenuDtos));
//        return sysUserDto;
//    }


    @Override
    public UserDto findUserInfo() {
        // 从SecurityContextHolder中获取到，当前登录的用户信息。
        JwtUser userDetails = SecurityUtils.getLoginUser();
        // 根据用户Id，获取用户详细信息。
        UserInfo sysUser = userInfoService.findById(userDetails.getUid());
        UserDto result = new UserDto();
        BeanUtils.copyProperties(sysUser, result);
        // 根据用户Id，获取到拥有的 权限列表
//        Set<SysPermission> permissions = sysPermissionService.findAllByUserId(sysUser.getUid());
        List<ButtonDto> buttonDtos = new ArrayList<>();
        List<MenuDto> menuDtos = new ArrayList<>();
//        if (permissions != null && permissions.size() > 1) {
//            permissions.forEach(permission -> {
//                if (permission.getType().toLowerCase().equals(PermissionType.BUTTON)) {
//                    /*
//                     * 如果权限是按钮，就添加到按钮里面
//                     * */
//                    buttonDtos.add(
//                            new ButtonDto(
//                                    permission.getPid(),
//                                    permission.getResources(),
//                                    permission.getTitle())
//                    );
//                }
//                if (permission.getType().toLowerCase().equals(PermissionType.MENU)) {
//                    /*
//                     * 如果权限是菜单，就添加到菜单里面
//                     * */
//                    menuDtos.add(
//                            new MenuDto(
//                                    permission.getPid(),
//                                    permission.getParentId(),
//                                    permission.getIcon(),
//                                    permission.getResources(),
//                                    permission.getTitle(),
//                                    null
//                            )
//                    );
//                }
//            });
//        }
        result.setButtons(buttonDtos);
//        result.setMenus(findRoots(menuDtos));
        result.setMenus(menuDtos);
//        Set<SysRole> roles = sysRoleService.findAllByUserId(result.getUid());
//        Set<String> rolesName = roles
//                .stream()
//                .map(r -> r.getDescription())
//                .collect(Collectors.toSet());
//        String departmentName = sysDepartmentService.findById(sysUser.getDeptId()).getName();
//        result.setDepartmentName(departmentName);
//        result.setRoles(rolesName);
        return result;
    }

    @Override
    public JwtToken login(String username, String password) throws AuthenticationException {
        // 把表单提交的 username  password 封装到 UsernamePasswordAuthenticationToken中
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Object principal = authentication.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // 生成一个随机ID 跟当前用户关联
        String loginId = jwtTokenUtil.createLoginId();
        String token = jwtTokenUtil.createToken(loginId);
        log.debug("userDetails: {}", userDetails);

//        setLoginUser(loginId, userDetails);

        return JwtToken.builder()
                .header(jwtTokenUtil.getTokenHeader())
                .value(token)
                .prefix(jwtTokenUtil.getTokenHead())
                .expiration(jwtTokenUtil.getExpiration())
                .build();
    }

    @Override
    public JwtToken register(String username, String password) throws AuthenticationException {
        //TODO 不应该用AuthenticationEx
        if (isBlank(username)) {
            throw new UsernameNotFoundException(String.format("'%s'.用户名校验错误", username));
        }
        if (isBlank(password)) {
            throw new UsernameNotFoundException(String.format("'%s'.密码不合理", password));
        }
        UserInfo userInfo = userInfoService.fetchUserByUserName(username);
        if (userInfo != null) {
            throw new UsernameNotFoundException(String.format("'%s'.用户名已存在", username));
        }
        UserInfo newUser = new UserInfo();
        String sec_password = passwordEncoder.encode(password);
        log.info("gen pass for {}:  {}", password, sec_password);
        newUser.setName(username).setPassword(sec_password).setActive(1).setIsLock(0);

//        newUser = userInfoService.saveUser(newUser);
        newUser = userInfoService.save(newUser);
        return login(username, password);
    }

//    @Override
//    public Integer register(UserInfo userInfo) throws UserExistsException {
//        String username = userInfo.getName();
//        if (findByUsername(username) != null) {
//            throw new UserExistsException(String.format("'%s' 这个用用户已经存在了", username));
//        }
//        String rawPassword = userInfo.getPassword();
//        userInfo.setPassword(passwordEncoder.encode(rawPassword));
//        userInfo.setUpTime(new Date());
//        userInfo.setAddTime(new Date());
//        return baseMapper.insertSelective(userInfo);
//    }

    @Override
    public JwtToken refresh() {
        JwtUser loginUser = SecurityUtils.getLoginUser();
        // 移除登录的用户。根据tokenId
        removeLoginUser(loginUser.getLoginId());

        // 生成一个随机ID 跟当前用户关联
        String loginId = jwtTokenUtil.createLoginId();

        // 重新生成token
        String token = jwtTokenUtil.createToken(loginId);

        setLoginUser(loginId, loginUser);

        return JwtToken.builder()
                .header(jwtTokenUtil.getTokenHeader())
                .value(token)
                .prefix(jwtTokenUtil.getTokenHead())
                .expiration(jwtTokenUtil.getExpiration())
                .build();
    }

//    @Override
//    public String refreshToken(String oldToken) {
//        if (!jwtTokenUtil.isTokenExpired(oldToken)) {
//            return jwtTokenUtil.refreshToken(oldToken);
//        }
//        return "error";
//    }

    @Override
    public JwtUser validateUser(String loginId) throws AuthenticationException {
        JwtUser jwtUser = getLoginUser(loginId);
        if (jwtUser == null || org.springframework.util.StringUtils.isEmpty(jwtUser.getUsername())) {
            // 判断是否启用单点登录
            if (jwtTokenUtil.getSso()) {
                String key = FORCED_OFFLINE_KEY + loginId;
                // 判断此用户，是不是被挤下线
                String offlineTime = (String) redisTemplate.opsForValue().get(key);
                if (org.springframework.util.StringUtils.hasText(offlineTime)) {
                    // 删除 被挤下线 的消息提示
                    removeLoginUser(key);
                    String errMsg = String.format("您的账号在[ %s ]被其他用户拥下线了！", offlineTime);
                    log.info("errMsg {}", errMsg);
                    throw new JwtAuthenticationException(errMsg);
                }
            }
            throw new JwtAuthenticationException("当前登录用户不存在");
        }
        jwtUser.setLoginId(loginId);
        return jwtUser;
    }

    private JwtUser getLoginUser(String loginId) {
        JwtUser loginUser = (JwtUser) redisTemplate
                .opsForValue().get(LOGIN_TOKEN_KEY + loginId);
        return loginUser;
    }

    private void removeLoginUser(String loginId) {
        redisTemplate.delete(loginId);
    }

    private void setLoginUser(String loginId, JwtUser loginUser) {
        loginUser.setLoginId(loginId);
        String loginKey = LOGIN_TOKEN_KEY + loginId;
        // 将随机id 跟 当前登录的用户关联，在一起！
        redisTemplate.opsForValue().set(
                loginKey,
                loginUser,
                jwtTokenUtil.getExpiration(),
                TimeUnit.MINUTES
        );
        // 判断是否开启 单点登录
        if (jwtTokenUtil.getSso()) {
            String onlineUserKey = ONLINE_USER_KEY + loginUser.getUsername();

            String oldLoginKey = (String) redisTemplate.opsForValue().get(onlineUserKey);
            // 判断用户名。是否已经登录了！
            if (org.springframework.util.StringUtils.hasText(oldLoginKey)) {
                // 移除之前登录的用户
                removeLoginUser(LOGIN_TOKEN_KEY + oldLoginKey);

                // 移除在线用户
                removeLoginUser(onlineUserKey);

                // 获取当前时间
                String milli = LocalTimeUtils.currentSecondFormat();
                // 将 被强制挤下线的用户，以及时间，保存到 redis中，提示给前端用户！
                redisTemplate.opsForValue().set(
                        FORCED_OFFLINE_KEY + oldLoginKey,
                        milli,
                        5,
                        TimeUnit.MINUTES
                );
            }
            redisTemplate.opsForValue().set(
                    onlineUserKey,
                    loginId,
                    jwtTokenUtil.getExpiration(),
                    TimeUnit.MINUTES
            );
        }
    }

    @Override
    public void logout() {
        JwtUser loginUser = SecurityUtils.getLoginUser();
        // 移除登录的用户。根据tokenId
        removeLoginUser(loginUser.getLoginId());
    }
}
