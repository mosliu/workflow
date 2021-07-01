package net.liuxuan.security.controller;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.common.BaseController;
import net.liuxuan.db.entity.Privilege;
import net.liuxuan.db.entity.RoleInfo;
import net.liuxuan.db.service.PrivilegeService;
import net.liuxuan.db.service.RoleInfoService;
import net.liuxuan.security.dto.RolePrivilegeDto;
import net.liuxuan.springconf.CommonResponseDto;
import net.liuxuan.utils.TreeNode;
import net.liuxuan.utils.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description 角色管理
 * @date 2021-06-11
 **/

@Slf4j
@RestController
@RequestMapping("/sys/role")
public class RoleController extends BaseController<RoleInfo, Integer, RoleInfoService> {

    @Autowired
    private PrivilegeService privilegeService;

    /**
     * 查询角色详情，包括权限列表  根据Id查询
     */
    @GetMapping("/info/{rid}")
    public CommonResponseDto roleInfo(@PathVariable Integer rid) {
        Map<String, Object> map = new HashMap<>(2);
        // 获取当前角色，拥有的权限
        RoleInfo roleInfo = baseService.findById(rid);
        Set<Privilege> havePrivilegeCollect = roleInfo.getPrivileges();//角色权限列表

        // 获取全部的权限
        List<TreeNode> allPrivilegeCollect = privilegeService.findAll()
                .stream()
                .distinct()
                .map(res -> new TreeNode(res.getId(), res.getDescription(), res.getPid(), null, null))
                .collect(Collectors.toList());

        // 将当前角色拥有的权限 pid，挑选出来
        Set<Integer> hashSet = havePrivilegeCollect.stream().map(res -> res.getId()).collect(Collectors.toSet());
        // 再次遍历，当前角色拥有的权限，然后 移除父节点 只留下子节点。
        havePrivilegeCollect.forEach(res -> hashSet.remove(res.getPid()));
        // 将全部权限，递归成树节点的形式
        map.put("all", TreeUtils.findRoot(allPrivilegeCollect));
        // 将当前角色拥有的权限Id，以列表的形式返回
        map.put("have", hashSet);
        return CommonResponseDto.success(map);
    }


    /**
     * 修改角色权限,会删除之前的权限信息。
     */
    @PostMapping("/update/permissions")
    public CommonResponseDto updateRolePermissions(@RequestBody @Validated RolePrivilegeDto data, BindingResult br) {
        RoleInfo byId = baseService.findById(data.getRid());
        Iterable<Privilege> allPrivilegeById = privilegeService.findAllById(data.getPermissions());
        Set<Privilege> ps = new HashSet<>();
        allPrivilegeById.forEach(ps::add);
        byId.setPrivileges(ps);
        RoleInfo save = baseService.save(byId);
//        int result = baseService.updateRolePermissions(data.getRid(), data.getPermissions());
        return CommonResponseDto.success(save);
    }
}
