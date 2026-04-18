package dsa.impl;

import dsa.iface.IPosition;

public class SplayTree<T extends Comparable<T>> extends BinarySearchTree<T> {

    public void insert(T value) {
        if (root.element == null) {
            expandExternal(root, value);
            return;
        }

        IPosition<T> p = find(root, value);
        if (isExternal(p)) {
            expandExternal(p, value);
        }
        splay(p);
    }

    public boolean contains(T value) {
        IPosition<T> p = find(root, value);
        if (isInternal(p)) {// find
            splay(p);
            return true;
        }

        // not find
        if (p != null && ((BTPosition) p).parent != null) {
            splay(((BTPosition) p).parent);
        }
        return false;
    }

    public void remove(T value) {
        assert root != null : "Tree is empty";

        IPosition<T> p = find(root, value);
        if (isExternal(p)) {
            if (((BTPosition) p).parent != null) {
                splay(((BTPosition) p).parent);
            }
            return;
        }

        BTPosition parentBeforeRemove = ((BTPosition) p).parent;
        super.remove(value);

        if (parentBeforeRemove != null && parentBeforeRemove.element != null) {
            splay(parentBeforeRemove);
        } else if (root != null && root.element != null) {
            splay(root);
        }
    }

    private void splay(IPosition<T> n) {
        if (n == null || isExternal(n)) {
            return;
        }

        BTPosition x = (BTPosition) n;
        while (x.parent != null) {
            BTPosition p = x.parent;
            BTPosition g = p.parent;

            if (g == null) {
                // zig
                if (p.left == x) {
                    rightRotate(p);
                } else {
                    leftRotate(p);
                }
            } else if (g.left == p && p.left == x) {
                // zig-zig (left-left)
                rightRotate(g);
                rightRotate(p);
            } else if (g.right == p && p.right == x) {
                // zig-zig (right-right)
                leftRotate(g);
                leftRotate(p);
            } else if (g.left == p && p.right == x) {
                // zig-zag (left-right)
                leftRotate(p);
                rightRotate(g);
            } else {
                // zig-zag (right-left)
                rightRotate(p);
                leftRotate(g);
            }
        }
    }

    private void leftRotate(BTPosition x) {
        BTPosition y = x.right;
        if (y == null) {
            return;
        }

        BTPosition parent = x.parent;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }

        y.left = x;
        x.parent = y;
        y.parent = parent;

        if (parent == null) {
            root = y;
        } else if (parent.left == x) {
            parent.left = y;
        } else {
            parent.right = y;
        }
    }

    private void rightRotate(BTPosition x) {
        BTPosition y = x.left;
        if (y == null) {
            return;
        }

        BTPosition parent = x.parent;
        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }

        y.right = x;
        x.parent = y;
        y.parent = parent;

        if (parent == null) {
            root = y;
        } else if (parent.left == x) {
            parent.left = y;
        } else {
            parent.right = y;
        }
    }
}
