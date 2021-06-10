package net.liuxuan.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-06-09
 **/
public class TreeUtils {

    /**
     * 查找所有根节点
     *
     * @param allNodes
     * @return Set<TreeNode>
     */
    public static <T extends TreeNode> List<T> findRoot(List<T> allNodes) {
        // 根节点
        List<T> root = new ArrayList<>();
        allNodes.forEach(node -> {
            if (node.isRoot()) {
                root.add(node);
            }
        });
        root.forEach(node -> {
            findChildren(node, allNodes);
        });
        return root;
    }


    /**
     * 递归查找子节点
     *
     * @param treeNode
     * @param treeNodes
     * @return TreeNode
     */
    private static <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
        for (T it : treeNodes) {
            if (treeNode.getId() == it.getParentId()) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.getChildren().add(findChildren(it, treeNodes));
            }
        }
        return treeNode;
    }
}
