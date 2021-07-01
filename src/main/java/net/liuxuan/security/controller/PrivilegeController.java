package net.liuxuan.security.controller;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.common.BaseController;
import net.liuxuan.db.entity.Privilege;
import net.liuxuan.db.service.PrivilegeService;
import net.liuxuan.springconf.CommonResponseDto;
import net.liuxuan.utils.TreeNode;
import net.liuxuan.utils.TreeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description 权限管理
 * @date 2021-06-10
 **/
@Slf4j
@RestController
@RequestMapping("/sys/privilege")
public class PrivilegeController extends BaseController<Privilege, Integer, PrivilegeService> {
    /**
     * 以树节点的形式展示 查询所有权限 <br>
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
                .map(res -> new TreeNode(res.getId().longValue(), res.getName(), 0L, res, new ArrayList<>()))
                .collect(Collectors.toList());
        return CommonResponseDto.success(TreeUtils.findRoot(collect));
    }

}
