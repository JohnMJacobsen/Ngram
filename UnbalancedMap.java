package edu.sdsu.cs.datastructures;

import java.util.LinkedList;

/**
 * Daniel Valoria
 * John Jacobsen
 * CS 310-02
 */

public class UnbalancedMap<K extends Comparable<K>, V extends Comparable<V> > implements IMap<K, V> {

    private class Node<K extends Comparable<K>, V extends Comparable<V>> {
        K key;
        V val;
        Node left, right, parent;

        Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    private Node root;
    private int size;

    public UnbalancedMap() {
    }

    UnbalancedMap(IMap<K, V> m) {
        for (Object key : m.keyset())
            add((K)key, getValue((K)key));
    }

    @Override
    public boolean contains(K key) {
        Node current = root;
        while(current != null) {
            if (key.compareTo((K) current.key) > 0)
                current = current.right;
            else if (key.compareTo((K) current.key) < 0)
                current = current.left;
            else
                return true;
        }
        return false;
    }

    @Override
    public boolean add(K key, V value) {
        if(root != null) {
            Node currentNode = root;

            while(currentNode != null) {
                if(key.compareTo((K) currentNode.key) < 0) {
                    if (currentNode.left != null)
                        currentNode = currentNode.left;
                    else {
                        currentNode.left = new Node(key, value);
                        currentNode.left.parent = currentNode;
                        ++size;
                        break;
                    }
                } else if(key.compareTo((K) currentNode.key) > 0) {
                    if(currentNode.right != null)
                        currentNode = currentNode.right;
                    else {
                        currentNode.right = new Node(key, value);
                        currentNode.right.parent = currentNode;
                        ++size;
                        break;
                    }
                } else
                    return false;
            }
            return true;
        } else {
            root = new Node(key, value);
            ++size;
            return true;
        }
    }

    @Override
    public V delete(K key) {
        V toReturn = getValue(key);
        Node n = findNode(key);
        Node replace;

        if (n == null) {
            return null;
        }
        if(n == root) {
            if (root.left == null && root.right == null)
                clear();
            else if (root.left != null && root.right == null) {
                root = root.left;
                size--;
            }
            else if(root.right != null && root.left == null) {
                root = root.right;
                size--;
            }
            else {
                replace = twoChild(root.right);
                root.key = replace.key;
                root.val = replace.val;
                deleteNode(n);
            }
        } else {
            if (n.left == null && n.right == null) {
                if(n.parent.right != null) {
                    if(n.parent.right.key.compareTo(key) == 0) {
                        n.parent.right = null;
                        size--;
                    }
                } else if(n.parent.left != null) {
                    if(n.parent.left.key.compareTo(key) == 0) {
                        n.parent.left = null;
                        size--;
                    }
                }
            }
            else if (n.left == null && n.right != null) {
                oneChild(n, n.right);
                deleteNode(n.right);
            } else if (n.right == null && n.left != null) {
                oneChild(n, n.left);
                deleteNode(n.left);
            } else {
                replace = twoChild(n.right);
                n.key = replace.key;
                n.val = replace.val;
                deleteNode(n);
            }
        }
        return toReturn;
    }

    void deleteNode(Node n) {
        if(root != null) {
            Node current = root;
            while (current != null) {
                if (n.key.compareTo(current.key) > 0)
                    current = current.right;
                else if (n.key.compareTo(current.key) < 0)
                    current = current.left;
                else {
                    current = null;
                    size--;
                }
            }
        }
    }

    void oneChild(Node n, Node child) {
        if (n.parent != null
                && n.parent.left != null && n.parent.right != null) {
            if ((n.parent.left.key).compareTo(n.key) == 0) {
                n.parent.left = child;
                child.parent = n.parent;
            } else if ((n.parent.right.key).compareTo(n.key) == 0) {
                n.parent.right = child;
                child.parent = n.parent;
            }
        } else if(n.parent != null && n.parent.left != null && n.parent.right == null) {
            if((n.parent.left.key).compareTo(n.key) == 0) {
                n.parent.left = child;
                child.parent = n.parent;
            }
        } else if(n.parent != null && n.parent.left == null && n.parent.right != null) {
            if((n.parent.right.key).compareTo(n.key) == 0) {
                n.parent.right = child;
                child.parent = n.parent;
            }
        }
    }

    Node twoChild(Node n) {
        if (n.left == null && n.right == null)
            return n;
        else if(n.left == null && n.right != null)
            return twoChild(n.right);
        else if (n.left != null)
            return twoChild(n.left);
        return null;
    }

    Node findNode(K key) {
        if(root != null) {
            Node current = root;
            while (current != null) {
                if (key.compareTo((K)current.key) > 0)
                    current = current.right;
                else if (key.compareTo((K) current.key) < 0)
                    current = current.left;
                else
                    return current;
            }
        }
        return null;
    }

    @Override
    public V getValue(K key) {
        if(contains(key))
            return (V)findNode(key).val;
        return null;
    }

    @Override
    public K getKey(V value) {
        if(root != null)
            return findKey(value);
        return null;
    }

    K findKey(V value) {
        if(root != null) {
            Node current = root;
            while (current != null) {
                if (value.compareTo((V) current.val) > 0)
                    current = current.right;
                else if (value.compareTo((V) current.val) < 0)
                    current = current.left;
                else
                    return (K) current.key;
            }
        }
        return null;
    }

    @Override
    public Iterable<K> getKeys(V value) {
        LinkedList<K> list = new LinkedList<>();
        if(root != null)
            helper(root, list, value);
        return list;
    }

    private Iterable<K> helper(Node n, LinkedList<K> keys, V value) {
        if(n != null) {
            helper(n.left, keys, value);
            if(value.compareTo((V)n.val) == 0)
                keys.add((K) n.key);
            helper(n.right, keys, value);
        }
        return keys;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Iterable<K> keyset() {
        LinkedList<K> keys = new LinkedList<>();
        if(root != null)
            return keyHelper(root, keys);
        return keys;
    }

    private Iterable<K> keyHelper(Node n, LinkedList<K> keys) {
        if(n != null) {
            keyHelper(n.left, keys);
            keys.add((K) n.key);
            keyHelper(n.right, keys);
        }
        return keys;

    }

    @Override
    public Iterable<V> values() {
        LinkedList<V> vals = new LinkedList<>();
        if(root != null)
            return valHelper(root, vals);
        return vals;
    }

    private Iterable<V> valHelper(Node n, LinkedList<V> vals) {
        if(n != null) {
            valHelper(n.left, vals);
            vals.add((V) n.val);
            valHelper(n.right, vals);
        }
        return vals;
    }
}
