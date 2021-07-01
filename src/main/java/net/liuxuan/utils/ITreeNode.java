package net.liuxuan.utils;

import java.util.List;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-06-09
 **/
public interface ITreeNode {
    public Number getParentId();

    public boolean isRoot();

    public List getChildren();

    public void setChildren(List children);

    Number getId();
}
