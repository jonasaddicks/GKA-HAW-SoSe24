package util;

import java.util.HashMap;
import java.util.Map;

public class DisjointSetsComponents<E> {
    protected Map<E, DisjointSetsComponents.Node> map;
    protected int components;

    public DisjointSetsComponents() {
        this.map = new HashMap();
        components = 0;
    }

    public DisjointSetsComponents(int initialCapacity) {
        this.map = new HashMap(4 * initialCapacity / 3 + 1);
        components = 0;
    }

    public boolean add(E e) {
        Node x = (Node)this.map.get(e);
        if (x != null) {
            return false;
        } else {
            this.map.put(e, new Node());
            this.components++;
            return true;
        }
    }

    public boolean inSameSet(Object e1, Object e2) {
        Node x1 = (Node)this.map.get(e1);
        if (x1 == null) {
            return false;
        } else {
            Node x2 = (Node)this.map.get(e2);
            if (x2 == null) {
                return false;
            } else {
                return x1.root() == x2.root();
            }
        }
    }

    public boolean union(Object e1, Object e2) {
        Node x1 = (Node)this.map.get(e1);
        if (x1 == null) {
            return false;
        } else {
            Node x2 = (Node)this.map.get(e2);

            boolean success = x2 == null ? false : x1.join(x2);
            if (success) {components--;}
            return success;
        }
    }

    public boolean contains(Object e) {
        return this.map.get(e) != null;
    }

    public void clear() {
        this.map.values().forEach((node) -> {
            node.parent = null;
        });
        this.map.clear();
    }

    protected static class Node {
        Node parent = this;
        int rank = 0;

        protected Node() {
        }

        protected Node root() {
            if (this != this.parent) {
                this.parent = this.parent.root();
            }

            return this.parent;
        }

        protected boolean join(Node node) {
            Node x = this.root();
            Node y = node.root();
            if (x == y) {
                return false;
            } else {
                if (x.rank > y.rank) {
                    y.parent = x;
                } else {
                    x.parent = y;
                    if (x.rank == y.rank) {
                        ++y.rank;
                    }
                }
                return true;
            }
        }

        protected void remove() {
            // Attach children to the root of the component
            if (this.parent != this) {
                Node root = this.root();
                this.parent = root;
                root.rank = Math.max(root.rank, this.rank);
            }
        }
    }
    public boolean remove(E e) {
        Node node = this.map.get(e);
        if (node == null) {
            return false; // Node does not exist
        }

        if (node.parent == node) {
            // Node is a root
            if (this.map.values().stream().noneMatch(n -> n != node && n.root() == node)) {
                // Node is the only element in its component
                this.components--;
            }
        } else {
            // Node is not a root
            node.remove();
        }

        this.map.remove(e);
        return true;
    }

    public int getComponents() {
        return this.components;
    }
}
