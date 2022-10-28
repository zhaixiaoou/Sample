package com.zxo.lib.lee;

import java.util.LinkedList;

/**
 * 数据结构
 */
public class Struct {

  public static void main(String[] args) {
    Struct struct = new Struct();
    ListNode link1 = new ListNode(5);
//    LinkNode<Integer> link12 = new LinkNode<Integer>(4);
//    LinkNode<Integer> link13 = new LinkNode<Integer>(3);
//    link1.next = link12;
//    link12.next = link13;

    ListNode link2 = new ListNode(5);
//    LinkNode<Integer> link22 = new LinkNode<Integer>(6);
//    LinkNode<Integer> link23 = new LinkNode<Integer>(4);
//    link2.next = link22;
//    link22.next = link23;

//    ListNode result = struct.addTwoNum(link1, link2);

    ListNode l1 = new ListNode(1);
    ListNode l12 = new ListNode(2);
    ListNode l13 = new ListNode(3);
    ListNode l14 = new ListNode(4);
    l1.next = l12;
    l12.next = l13;
//    l13.next = l14;
    ListNode result = struct.swapPairs(l1);

    print(result);
  }

  public static void print(ListNode linkNode){
    if (linkNode == null){
      System.out.println("链表为空");
      return;
    }
    while (linkNode != null){
      System.out.print(linkNode.val +"-->");
      linkNode = linkNode.next;
    }
  }


  /**
   * 给你两个非空 的链表，表示两个非负的整数。它们每位数字都是按照逆序的方式存储的，并且每个节点只能存储一位数字。
   * <p>
   * 请你将两个数相加，并以相同形式返回一个表示和的链表。
   * <p>
   * 你可以假设除了数字 0 之外，这两个数都不会以 0开头。
   * <p>
   * 输入：l1 = [2,4,3], l2 = [5,6,4]
   * 输出：[7,0,8]
   * 解释：342 + 465 = 807.
   *
   * @param l1
   * @param l2
   * @return
   */
  private ListNode addTwoNum(ListNode l1, ListNode l2) {
    if (l1 == null) {
      return l2;
    }
    if (l2 == null) {
      return l1;
    }
    int carry = 0;
    ListNode head = null;
    ListNode tail = null;
    while (l1 != null || l2 != null) {
      int n1= l1 != null ? l1.val : 0;
      int n2 = l2 != null ? l2.val : 0;
      int sum = n1 + n2 + carry;
      if (head == null){
        tail = new ListNode(sum % 10);
        head = tail;
      } else {
        tail.next = new ListNode(sum % 10);
        tail = tail.next;
      }
      carry = sum / 10;
      if (l1 != null){
        l1 = l1.next;
      }
      if (l2 != null){
        l2=l2.next;
      }
    }

    if (carry > 0){
     tail.next = new ListNode(carry);
    }
    return head;
  }

  /**
   * 给你一个链表，两两交换其中相邻的节点，并返回交换后链表的头节点。你必须在不修改节点内部的值的情况下完成本题（即，只能进行节点交换）
   * @param head
   * @return
   */
  private ListNode swapPairs(ListNode head){
    if (head == null || head.next == null){
      return head;
    }
    ListNode dumpHead = new ListNode(0);
    dumpHead.next = head;
    ListNode temp = dumpHead;
    while (temp.next != null && temp.next.next != null){
      ListNode node1 = temp.next;
      ListNode node2 = temp.next.next;
       temp.next = node2;
       node1.next = node2.next;
       node2.next = node1;
       temp = node1;
    }
    return dumpHead.next;

  }

  public static class ListNode{
    int val;
    ListNode next;

    public ListNode() {}

    public ListNode(int data) {
      this.val = data;
    }

    public ListNode(int data, ListNode next) {
      this.val = data;
      this.next = next;
    }
  }
}
