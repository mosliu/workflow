package net.liuxuan.security.controller;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.common.BaseController;
import net.liuxuan.db.entity.UserGroup;
import net.liuxuan.db.service.UserGroupService;
import net.liuxuan.springconf.CommonResponseDto;
import net.liuxuan.utils.TreeNode;
import net.liuxuan.utils.TreeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description 权限管理 ] 部门管理
 * @date 2021-06-11
 **/
@Slf4j
@RestController
@RequestMapping("/sys/department")
public class UserGroupController extends BaseController<UserGroup, Integer, UserGroupService> {

    /**
     * 以树节点的形式展示
     */
    @GetMapping("/tree")
    public CommonResponseDto tree() {
        List<TreeNode> collect = baseService.findAll()
                .stream()
                .distinct()
                .map(res -> new TreeNode(res.getId(), res.getName(), res.getParentId(), res, null))
                .collect(Collectors.toList());
        List<TreeNode> roots = TreeUtils.findRoot(collect);

        return CommonResponseDto.success(roots);
    }

}
