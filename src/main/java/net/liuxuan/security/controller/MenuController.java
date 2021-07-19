package net.liuxuan.security.controller;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.common.BaseController;
import net.liuxuan.db.entity.Menu;
import net.liuxuan.db.entity.Privilege;
import net.liuxuan.db.service.MenuService;
import net.liuxuan.db.service.PrivilegeService;
import net.liuxuan.springconf.CommonResponseDto;
import net.liuxuan.utils.TreeNode;
import net.liuxuan.utils.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description 菜单管理
 * @date 2021-06-10
 **/
@Slf4j
@RestController
@RequestMapping("/sys/menu")
public class MenuController extends BaseController<Menu, Integer, MenuService> {

    @Autowired
    PrivilegeService privilegeService;

    /**
     * 以树节点的形式展示 查询所有菜单 <br>
     * <p>
     * 如果 filter 是 true，那么就是要过滤掉，按钮。如果是 false。就是菜单和按钮全要
     */
    @GetMapping("/tree")
    public CommonResponseDto tree(@RequestParam(defaultValue = "false") boolean filter) {
        List<TreeNode> collect = baseService.findAll()
                .stream()
                .distinct()
                // 如果 filter 是 true，那么就是要过滤掉，按钮。如果是 false。就是菜单和按钮全要
//                .filter(res -> filter ? MenuType.MENU.equals(res.getType().toLowerCase()) : true)
                .map(res -> new TreeNode(res.getId(), res.getName(), res.getParentId(), res, new ArrayList<>()))
                .collect(Collectors.toList());
        return CommonResponseDto.success(TreeUtils.findRoot(collect));
    }


    @Override
    @PostMapping()
    public CommonResponseDto create(@RequestBody Menu entity) {
        Integer parentId = entity.getParentId();

        Privilege privilege = new Privilege();
        privilege.setName(entity.getUrl());
        privilege.setDescription("菜单_" + entity.getName());
        privilege.setPid(0);
        privilege.setType(entity.getType());
        //获取菜单父节点
        if (parentId != null && parentId != 0) {
            Menu parent = baseService.findById(parentId);
            Set<Privilege> privileges = parent.getPrivileges();
            if (privileges != null && privileges.size() > 0) {
                Integer pid = privileges.iterator().next().getId();
                privilege.setPid(pid);
            }
        }
        entity.getPrivileges().add(privilege);
        return super.create(entity);
    }


}
