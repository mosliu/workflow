package net.liuxuan.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-06-10
 **/
@Data
@Accessors(chain = true)
public class TreeNode implements ITreeNode {

    Long id;

    Long parentId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<TreeNode> children;

    String name;

    Object source;

    public TreeNode() {
    }

    public TreeNode(Long id, String name, Long parentId, Object source, List<TreeNode> children) {
        this.id = id;
        this.parentId = parentId;
        this.children = children;
        this.name = name;
        this.source = source;
    }

    public TreeNode(Integer id, String name, Integer parentId, Object source, List<TreeNode> children) {
        this.id = id == null ? 0 : id.longValue();
        this.parentId = parentId == null ? 0 : parentId.longValue();
        this.children = children;
        this.name = name;
        this.source = source;
    }

    @Override
    public boolean isRoot() {
        return parentId == 0;
    }

    @Override
    public List getChildren() {
        return children;
    }

    @Override
    public void setChildren(List children) {
        this.children = children;
    }
}
