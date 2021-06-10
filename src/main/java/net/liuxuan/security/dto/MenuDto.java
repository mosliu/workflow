package net.liuxuan.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.liuxuan.utils.TreeNode;

import java.util.List;

/**
 * All rights Reserved, Designed By www.xaaef.com
 * <p>
 * 菜单权限
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 2.0
 * @date 2019/4/18 11:45
 * @copyright 2019 http://www.xaaef.com/ Inc. All rights reserved.
 */

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class MenuDto implements java.io.Serializable, TreeNode {

    private Integer pid;

    private Integer parentId;

    private String icon;

    private String resources;

    private String title;

    private List<MenuDto> children;

    @Override
    public boolean isRoot() {
        return parentId == 0;
    }

//    public List<TreeNode> getChildren() {
//        return ( List<TreeNode> )children;
//    }

    @Override
    public void setChildren(List children) {
        this.children = children;
    }

    @Override
    public Integer getId() {
        return null;
    }
}