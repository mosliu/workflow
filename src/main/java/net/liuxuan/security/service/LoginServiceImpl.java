package net.liuxuan.security.service;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-05-31
 **/

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.cache.CacheService;
import net.liuxuan.constants.MenuType;
import net.liuxuan.db.entity.Menu;
import net.liuxuan.db.entity.Privilege;
import net.liuxuan.db.entity.RoleInfo;
import net.liuxuan.db.entity.UserInfo;
import net.liuxuan.db.service.MenuService;
import net.liuxuan.db.service.RoleInfoService;
import net.liuxuan.db.service.UserInfoService;
import net.liuxuan.security.SecurityUtils;
import net.liuxuan.security.dto.ButtonDto;
import net.liuxuan.security.dto.MenuDto;
import net.liuxuan.security.dto.UserDto;
import net.liuxuan.security.exception.JwtAuthenticationException;
import net.liuxuan.security.jwt.JwtToken;
import net.liuxuan.security.jwt.JwtTokenUtil;
import net.liuxuan.security.jwt.JwtUser;
import net.liuxuan.utils.TreeUtils;
import net.liuxuan.utils.date.LocalTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static net.liuxuan.constants.SecurityConstants.*;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

//    @Autowired
//    @Lazy
//    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    CacheService cacheService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    RoleInfoService roleInfoService;

    @Autowired
    MenuService menuService;

    // ?????????WebSecurityConfigurerAdapter?????????????????????????????????????????????????????????
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
            throw new UsernameNotFoundException("????????????????????????!");
        }
        UserInfo userInfo = userInfoService.fetchUserByUserName(username);
        if (userInfo == null || StringUtils.isEmpty(userInfo.getName())) {
            throw new UsernameNotFoundException("??????????????????!");
        }
        log.info("??????User??? {}", userInfo);
        //?????????????????????????????????
        return userInfo;
    }

//    @Override
//    public SysUserDto findUserInfo(String username) {
//        /**
//         * ??????????????????
//         */
//        SysUser sysUser = findByUsername(username);
//        /**
//         * ?????????????????????????????????
//         */
//        Set<SysRole> sysRoles = sysRoleService.selectByUserName(username);
//
//        /**
//         * ?????????????????????????????????????????????????????????
//         * ?????????????????????????????????
//         * ????????????????????????????????????????????????????????????????????????
//         * ????????????????????????????????????????????????????????????
//         * ????????????????????????????????????????????????????????????????????????????????????
//         */
//        Set<ButtonDto> ButtonDtos = new HashSet<>();
//        Set<MenuDto> MenuDtos = new HashSet<>();
//
//        sysRoles.forEach(role -> {
//            log.info("role: {}", role.getDescribe());
//            role.getPermissions().forEach(permission -> {
//                if (permission.getType().toLowerCase().equals("button")) {
//                    /*
//                     * ????????????????????????????????????????????????
//                     * */
//                    ButtonDtos.add(new ButtonDto(permission.getPid(), permission.getResources(), permission.getTitle()));
//                }
//                if (permission.getType().toLowerCase().equals("menu")) {
//                    /*
//                     * ????????????????????????????????????????????????
//                     * */
//                    MenuDtos.add(new MenuDto(permission.getPid(), permission.getFather(), permission.getIcon(), permission.getResources(), permission.getTitle()));
//                }
//            });
//        });
//
//        /**
//         * ??????????????? TreeBuilder????????????vue router?????????????????????????????????
//         * ??????????????????????????????vue router ????????????????????? ????????????????????????
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

        // ???SecurityContextHolder?????????????????????????????????????????????
        JwtUser userDetails = SecurityUtils.getLoginUser();
        // ????????????Id??????????????????????????????
        UserInfo sysUser = userInfoService.findById(userDetails.getUid());
        List<ButtonDto> buttonDtos = new ArrayList<>();
        List<MenuDto> menuDtos = new ArrayList<>();
        Set<RoleInfo> roleInfos = sysUser.getRoleInfos();
        roleInfos.forEach(role -> {
            Set<Privilege> privileges = role.getPrivileges();
            privileges.forEach(privilege -> {
                Set<Menu> menus = privilege.getMenus();
                menus.forEach(menu -> {
                    if (menu.getType().toLowerCase().equals(MenuType.BUTTON)) {
                        buttonDtos.add(menu.toButtonDto());
                    } else if (menu.getType().toLowerCase().equals(MenuType.MENU)) {
                        menuDtos.add(menu.toMenuDto());
                    } else {
                        log.info("?????????menu?????????{}", menu);
                    }
                });


            });
        });

        UserDto result = new UserDto();
//        BeanUtils.copyProperties(sysUser, result);
        result.setUid(sysUser.getId()).setUsername(sysUser.getName());
        // ????????????Id????????????????????? ????????????
//        Set<SysPermission> permissions = sysPermissionService.findAllByUserId(sysUser.getUid());
//        menuDtos.add(new MenuDto().setTitle("A").setPid(1).setParentId(0).setResources("pre"));
//        List<Menu> allMenu = menuService.findAll();
//        if (allMenu != null && allMenu.size() > 1) {
//            allMenu.forEach(menu -> {
//                if (menu.getType().toLowerCase().equals(MenuType.BUTTON)) {
//                    //????????????????????????????????????????????????
//                    buttonDtos.add(menu.toButtonDto());
//                }
//                if (menu.getType().toLowerCase().equals(MenuType.MENU)) {
//                    //????????????????????????????????????????????????
//                    menuDtos.add(menu.toMenuDto());
//                }
//            });
//        }
        result.setButtons(buttonDtos);
        result.setMenus(TreeUtils.findRoot(menuDtos));
//        result.setMenus(menuDtos);
//        Set<SysRole> roles = sysRoleService.findAllByUserId(result.getUid());
//        Set<String> rolesName = roles
//                .stream()
//                .map(r -> r.getDescription())
//                .collect(Collectors.toSet());
//        String departmentName = sysDepartmentService.findById(sysUser.getDeptId()).getName();
//        result.setDepartmentName(departmentName);

        List<RoleInfo> allroles = roleInfoService.findAll();
        Set<RoleInfo> roles = new HashSet<>();
//        Set<String> rolesName = new TreeSet<>();
//        rolesName.add("ROLE_ADMIN");
//        result.setRoles(rolesName);
        roles.addAll(allroles);
        result.setRoles(roles);
        return result;
    }

    @Override
    public JwtToken login(String username, String password) throws AuthenticationException {
        // ?????????????????? username  password ????????? UsernamePasswordAuthenticationToken???
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Object principal = authentication.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // ??????????????????ID ?????????????????????
        String loginId = jwtTokenUtil.createLoginId();
        String token = jwtTokenUtil.createToken(loginId);
        log.debug("userDetails: {}", userDetails);

        setLoginUser(loginId, (JwtUser) userDetails);

        return JwtToken.builder()
                .header(jwtTokenUtil.getTokenHeader())
                .value(token)
                .prefix(jwtTokenUtil.getTokenHead())
                .expiration(jwtTokenUtil.getExpiration())
                .build();
    }

    @Override
    public JwtToken register(String username, String password) throws AuthenticationException {
        //TODO ????????????AuthenticationEx
        if (isBlank(username)) {
            throw new UsernameNotFoundException(String.format("'%s'.?????????????????????", username));
        }
        if (isBlank(password)) {
            throw new UsernameNotFoundException(String.format("'%s'.???????????????", password));
        }
        UserInfo userInfo = userInfoService.fetchUserByUserName(username);
        if (userInfo != null) {
            throw new UsernameNotFoundException(String.format("'%s'.??????????????????", username));
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
//            throw new UserExistsException(String.format("'%s' ??????????????????????????????", username));
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
        // ??????????????????????????????tokenId
        removeLoginUser(loginUser.getLoginId());

        // ??????????????????ID ?????????????????????
        String loginId = jwtTokenUtil.createLoginId();

        // ????????????token
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
            // ??????????????????????????????
            if (jwtTokenUtil.getSso()) {
                String key = FORCED_OFFLINE_KEY + loginId;
                // ???????????????????????????????????????
//                String offlineTime = (String) redisTemplate.opsForValue().get(key);
                String offlineTime = (String) cacheService.getValue(key);
                if (org.springframework.util.StringUtils.hasText(offlineTime)) {
                    // ?????? ???????????? ???????????????
                    removeLoginUser(key);
                    String errMsg = String.format("???????????????[ %s ]??????????????????????????????", offlineTime);
                    log.info("errMsg {}", errMsg);
                    throw new JwtAuthenticationException(errMsg);
                }
            }
            throw new JwtAuthenticationException("???????????????????????????");
        }
        jwtUser.setLoginId(loginId);
        return jwtUser;
    }

    private JwtUser getLoginUser(String loginId) {
//        JwtUser loginUser = (JwtUser) redisTemplate
//                .opsForValue().get(LOGIN_TOKEN_KEY + loginId);
        JwtUser loginUser = (JwtUser) cacheService.getValue(LOGIN_TOKEN_KEY + loginId);
        return loginUser;
    }

    private void removeLoginUser(String loginId) {
        cacheService.delete(loginId);
//        redisTemplate.delete(loginId);
    }

    private void setLoginUser(String loginId, JwtUser loginUser) {
        loginUser.setLoginId(loginId);
        String loginKey = LOGIN_TOKEN_KEY + loginId;
        // ?????????id ??? ??????????????????????????????????????????
        cacheService.setValue(loginKey, loginUser, jwtTokenUtil.getExpiration(), TimeUnit.MINUTES);
//        redisTemplate.opsForValue().set(
//                loginKey,
//                loginUser,
//                jwtTokenUtil.getExpiration(),
//                TimeUnit.MINUTES
//        );
        // ?????????????????? ????????????
        if (jwtTokenUtil.getSso()) {
            String onlineUserKey = ONLINE_USER_KEY + loginUser.getUsername();

//            String oldLoginKey = (String) redisTemplate.opsForValue().get(onlineUserKey);
            String oldLoginKey = (String) cacheService.getValue(onlineUserKey);
            // ??????????????????????????????????????????
            if (org.springframework.util.StringUtils.hasText(oldLoginKey)) {
                // ???????????????????????????
                removeLoginUser(LOGIN_TOKEN_KEY + oldLoginKey);

                // ??????????????????
                removeLoginUser(onlineUserKey);

                // ??????????????????
                String milli = LocalTimeUtils.currentSecondFormat();
                // ??? ?????????????????????????????????????????????????????? redis??????????????????????????????
                cacheService.setValue(FORCED_OFFLINE_KEY + oldLoginKey, milli, 5, TimeUnit.MINUTES);
//                redisTemplate.opsForValue().set(
//                        FORCED_OFFLINE_KEY + oldLoginKey,
//                        milli,
//                        5,
//                        TimeUnit.MINUTES
//                );
            }
            cacheService.setValue(onlineUserKey, loginId, jwtTokenUtil.getExpiration(), TimeUnit.MINUTES);
//            redisTemplate.opsForValue().set(
//                    onlineUserKey,
//                    loginId,
//                    jwtTokenUtil.getExpiration(),
//                    TimeUnit.MINUTES
//            );
        }
    }

    @Override
    public void logout() {
        JwtUser loginUser = SecurityUtils.getLoginUser();
        // ??????????????????????????????tokenId
        removeLoginUser(loginUser.getLoginId());
    }
}
