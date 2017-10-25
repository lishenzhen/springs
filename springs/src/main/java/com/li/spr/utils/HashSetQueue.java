package com.li.spr.utils;


import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * @ClassName: HashSetQueue.java
 * @Description: 具有桶性质的队列(当队列中存在相同元素时,不入对/或者先删除再入队.避免重复排队)
 * @Author: lishenzhencn@163.com
 * @CreateDate 2017年9月23日 下午1:28:22
 * @param <T>
 */

public class HashSetQueue<T> {

    transient int                     size = 0;

    transient Node<T>                 first;

    transient Node<T>                 last;

    private HashMap<Node<T>, Node<T>> bucket;  // 用桶作为队列容器

    public HashSetQueue(){
        this.bucket = new HashMap<Node<T>, Node<T>>();
    }

    /**
     * 将元素链到最后
     */
    private void linkLast(Node<T> newNode) {
        final Node<T> l = last;
        last = newNode;
        if (l == null) first = newNode;
        else {
            l.next = newNode;
            first.prev = newNode;
        }
        size++;
    }

    /**
     * Unlinks non-null node f.
     */
    private T unlink(Node<T> f) {
        // assert f == first && f != null;
        final T element = f.item;
        if (last == first) {
            first = null;
            last = null;
        } else {
            final Node<T> next = f.next, prev = f.prev;
            next.prev = prev;
            prev.next = next;
            if (f == first) first = next;
            if (f == last) last = prev;
        }
        size--;
        return element;
    }
    
    /**
     * 元素入队,如果e存在,则不入队
     * 
     * @param e
     * @return
     */
    public boolean offerOffLine(T e) {
        if (e == null) throw new NullPointerException();
        final Node<T> newNode = new Node<T>(last, e, first);
        if (!bucket.containsKey(newNode)) {
            linkLast(newNode);
            bucket.put(newNode, newNode);
            return true;
        }
        return false;
    }

    /**
     * 元素入队,如果e存在,则先删除元素old e,然后e才入队
     * 
     * @param e
     * @return
     */
    public boolean offer(T e) {
        if (e == null) throw new NullPointerException();
        final Node<T> newNode = new Node<T>(last, e, first);
        Node<T> oldNode = bucket.remove(newNode);
        if (oldNode != null) {
            unlink(oldNode);
            newNode.next = first;
            newNode.prev = last;
        }
        linkLast(newNode);
        bucket.put(newNode, newNode);
        return true;
    }

    /**
     * 队首元素出队,队列空时抛出 NoSuchElementException
     * 
     * @return
     */
    public T remove() {
        final Node<T> f = first;
        if (f == null) throw new NoSuchElementException();
        bucket.remove(first);
        return unlink(f);
    }

    /**
     * 队首元素出队,队列空时return null
     * 
     * @return
     */
    public T poll() {
        final Node<T> f = first;
        if (f == null) return null;
        else {
            bucket.remove(f);
            unlink(f);
            return f.item;
        }
    }

    /**
     * 获取队首元素,队列空时抛出NoSuchElementException
     * 
     * @return
     */
    public T element() {
        final Node<T> f = first;
        if (f == null) throw new NoSuchElementException();
        return f.item;
    }

    /**
     * 获取队首元素,队列空时return null
     * 
     * @return
     */
    public T peek() {
        final Node<T> f = first;
        return (f == null) ? null : f.item;
    }

    /**
     * 队列长度
     * 
     * @return
     */
    public int size() {
        return size;
    }

    private static class Node<T> {

        T       item;
        Node<T> next;
        Node<T> prev;

        Node(Node<T> prev, T element, Node<T> next){
            this.item = element;
            this.next = next;
            this.prev = prev;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            if (this == null || obj == null) {
                return false;
            }
            Node<T> node = (Node<T>) obj;
            return item.equals(node.item);
        }

        @Override
        public int hashCode() {
            return item.hashCode();
        }
    }

}
