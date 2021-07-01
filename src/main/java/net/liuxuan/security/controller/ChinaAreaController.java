package net.liuxuan.security.controller;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.db.entity.ChinaArea;
import net.liuxuan.db.service.ChinaAreaService;
import net.liuxuan.springconf.CommonResponseDto;
import net.liuxuan.utils.TreeNode;
import net.liuxuan.utils.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Liuxuan
 * @version v1.0.0
 * @description [ 通用 ] 中国行政地区
 * @date 2021-06-10
 **/

@Slf4j
@RestController
@RequestMapping("/china/area")
public class ChinaAreaController {

    @Autowired
    private ChinaAreaService baseService;

    /**
     * 根据Id查询
     */
    @GetMapping("/{id}")
    public CommonResponseDto findById(@PathVariable("id") Long id) {
        log.info("get ID : {}", id);
        return CommonResponseDto.success(baseService.findById(id));
    }

    /**
     * 查询所有
     */
    @GetMapping("/all/{parentCode}")
    public CommonResponseDto findAll(@PathVariable Long parentCode) {
        if (parentCode == null || parentCode < 1) {
            parentCode = 0L;
        }
        List<ChinaArea> all = baseService.findAll(new ChinaArea().setParentCode(parentCode));
        return CommonResponseDto.success(all);
    }

    /**
     * 以树节点的形式展示
     */
    @GetMapping("/tree")
    public CommonResponseDto tree() {
        List<TreeNode> collect = baseService.findAll()
                .stream()
                .distinct()
                .map(res -> {
                    return new TreeNode().setId(res.getAreaCode()).setParentId(res.getParentCode()).setSource(res).setName(res.getName());
                })
                .collect(Collectors.toList());
        List<TreeNode> root = TreeUtils.findRoot(collect);
        return CommonResponseDto.success(root);
    }


}
