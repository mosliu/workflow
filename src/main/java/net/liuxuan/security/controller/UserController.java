package net.liuxuan.security.controller;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.db.entity.RoleInfo;
import net.liuxuan.db.entity.UserDetailsEntity;
import net.liuxuan.db.entity.UserGroup;
import net.liuxuan.db.entity.UserInfo;
import net.liuxuan.db.page.PageParameter;
import net.liuxuan.db.service.RoleInfoService;
import net.liuxuan.db.service.UserDetailsEntityService;
import net.liuxuan.db.service.UserGroupService;
import net.liuxuan.db.service.UserInfoService;
import net.liuxuan.security.SecurityUtils;
import net.liuxuan.security.dto.ResetPasswordDto;
import net.liuxuan.security.dto.UserDto;
import net.liuxuan.security.dto.UserRelatedRoleDto;
import net.liuxuan.security.service.LoginService;
import net.liuxuan.springconf.CommonResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-06-02
 **/
@RestController
@Slf4j
@RequestMapping("/sys/user")
//public class UserController extends BaseController<UserInfo, Integer, UserInfoService> {
public class UserController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private UserDetailsEntityService userDetailsEntityService;

    @Autowired
    public UserInfoService userInfoService;

    @Autowired
    UserGroupService userGroupService;

    @Autowired
    RoleInfoService roleInfoService;

//    @ApiOperation(value = "获取用户详细信息", notes = "获取用户详细信息")

    /**
     * 获取用户详细信息
     */
    @GetMapping("/info")
    public CommonResponseDto findUserInfo() {
        UserDto userInfo = loginService.findUserInfo();
        return CommonResponseDto.success(userInfo);
    }

    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping("/all")
    public CommonResponseDto findAll() {
        return CommonResponseDto.success(userInfoService.findAll());
    }

    @GetMapping("/info/page")
    public CommonResponseDto findAllByPage(PageParameter parameter) {
        Page<UserInfo> allPage = userInfoService.findAllPage((PageParameter) parameter);
        log.info("{}", allPage.getContent());
        List<UserInfo> allUser = allPage.getContent();
        List<UserDto> collect = allUser.stream().filter(Objects::nonNull).map(e -> {
            UserDetailsEntity byId = userDetailsEntityService.findById(e.getId());
            return UserDto.fromUserInfoAndDetails(e, byId);
        }).collect(Collectors.toList());
        Map<String, Object> rtnM = new HashMap<>();
        rtnM.put("list", collect);
        rtnM.put("total", allPage.getTotalPages());
        return CommonResponseDto.success(rtnM);
    }


    @PostMapping()
    public CommonResponseDto create(@RequestBody UserDto userDto) {
        log.info("create:  {}", userDto);
        try {
            //TODO 应该判定是否已有?
            UserInfo ui = userDto.genUserInfo().setId(null);

            Integer deptId = userDto.getDeptId();
            UserGroup groupById = userGroupService.findById(deptId);
            if (groupById != null) {
                Set<UserGroup> userGroups = ui.getUserGroups();
                if (userGroups == null) {
                    userGroups = new HashSet<>();
                    ui.setUserGroups(userGroups);
                }
                userGroups.add(groupById);
            }
            ui = userInfoService.save(ui);
            UserDetailsEntity detailsEntity = userDto.genUserDetailsEntity();
            detailsEntity.setUid(ui.getId());
            detailsEntity = userDetailsEntityService.save(detailsEntity);
            userDto.setUid(ui.getId());


            return CommonResponseDto.success(userDto);
        } catch (Exception e) {
            return CommonResponseDto.fail(e.getMessage());
        }
    }

    /**
     * 修改必须要id
     *
     * @param userDto
     * @return
     */
    @PutMapping()
    public CommonResponseDto update(@RequestBody UserDto userDto) {
        log.info("update:  {}", userDto);
        try {
            if (userDto == null || userDto.getUid() < 1) {
                return CommonResponseDto.fail("Not legal id!");
            }
            UserInfo ui = userInfoService.findById(userDto.getUid());
            if (ui == null) {
                return CommonResponseDto.fail("Not find user id!");
            }
            userDto.updateUserInfo(ui);

            Integer deptId = userDto.getDeptId();
            UserGroup groupById = userGroupService.findById(deptId);
            if (groupById != null) {
                Set<UserGroup> userGroups = ui.getUserGroups();
                if (userGroups == null) {
                    userGroups = new HashSet<>();
                    ui.setUserGroups(userGroups);
                } else {
                    userGroups.clear();
                }
                userGroups.add(groupById);
            }


            userInfoService.save(ui);
            UserDetailsEntity detailsEntityServiceById = userDetailsEntityService.findById(userDto.getUid());
            if (detailsEntityServiceById == null) {
                detailsEntityServiceById = userDto.genUserDetailsEntity();
                detailsEntityServiceById.setUid(userDto.getUid());
            } else {
                userDto.updateUserDetailsEntity(detailsEntityServiceById);
            }
            userDetailsEntityService.save(detailsEntityServiceById);


            //TODO 更新userinfo和 userdetailsentity

            return CommonResponseDto.success(userDto);
        } catch (Exception e) {
            return CommonResponseDto.fail(e.getMessage());
        }
    }

    /**
     * 删除 只需要id即可
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public CommonResponseDto delete(@PathVariable("id") Integer id) {
        log.info("delete:  {}", id);
        try {
            userDetailsEntityService.deleteById(id);
            userInfoService.deleteById(id);
            return CommonResponseDto.success("deleted!");
        } catch (Exception e) {
            return CommonResponseDto.fail(e.getMessage());
        }
    }

    /**
     * 修改用户角色,会删除之前的角色信息。
     *
     * @param data
     * @param br
     * @return
     */
    @PostMapping("/update/roles")
    public CommonResponseDto updateRolePermissions(@RequestBody @Validated UserRelatedRoleDto data, BindingResult br) {
        UserInfo ui = userInfoService.findById(data.getUid());
        Set<RoleInfo> roleInfos = ui.getRoleInfos();
        if (roleInfos == null) {
            roleInfos = new HashSet<>();
            ui.setRoleInfos(roleInfos);
        }
        roleInfos.clear();
        Set<Integer> roles = data.getRoles();
        Iterable<RoleInfo> allById = roleInfoService.findAllById(roles);
        Set<RoleInfo> finalRoleInfos = roleInfos;
        allById.forEach(finalRoleInfos::add);
        userInfoService.save(ui);
        return CommonResponseDto.success(roleInfos.size());
//        return CommonResponseDto.success(finalRoleInfos.size());
    }


    /**
     * 重置用户密码
     */
    @PostMapping("/reset/password")
    public CommonResponseDto resetPassword(@RequestBody @Validated ResetPasswordDto pwd, BindingResult br) {
        try {
            UserInfo byId = userInfoService.findById(pwd.getUid());
            if (byId != null) {
                String newPassword = SecurityUtils.encryptPassword(pwd.getNewPwd());
                byId.setPassword(newPassword);
                byId = userInfoService.save(byId);
            }
            return CommonResponseDto.success(1);
        } catch (RuntimeException e) {
            return CommonResponseDto.fail(e.getMessage());
        }
    }


}
