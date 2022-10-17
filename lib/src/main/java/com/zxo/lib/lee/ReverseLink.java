package com.zxo.lib.lee;

/**
 * 链表反转
 */
public class ReverseLink {

  public static void main(String[] args) {
    ReverseLink obj = new ReverseLink();
    Node<Integer> link = obj.buildLink(5);
    obj.printLink(link);
    Node<Integer> result = obj.headInsertReverse(link);

    obj.printLink(result);
  }

  private Node<Integer> reverse(Node<Integer> input){
    if (input == null || input.next == null) {
      return input;
    }
    Node<Integer> p = input;
    Node<Integer> q = p.next;
    Node<Integer> k = q.next;
    if (k == null){
      p.next = null;
      q.next = p;
      return q;
    }
    p.next = null;
    while(k != null){
      q.next = p;
      p =q;
      q = k;
      k = k.next;
    }
    q.next= p;
    return q;
  }

  /**
   * 头插法。
   * @param head
   * @return
   */
  private Node<Integer> headInsertReverse(Node<Integer> head){
    if (head == null || head.next == null) {
      return head;
    }
    Node<Integer> temp = null, newHead = null;
    while (head != null){
      temp = head;
      head = head.next;
      temp.next = newHead;
      newHead = temp;
    }
    return newHead;
  }


  private void printLink(Node<Integer> link){
    while(link != null){
      System.out.print(link.data);
      link = link.next;
      if (link != null){
        System.out.print( "->");
      }
    }
    System.out.println( "");
  }


  private Node<Integer> buildLink(int length){
//    Node<Integer> head = new Node<>(1);
//    Node<Integer> node2 = new Node<>(2);
//    Node<Integer> node3 = new Node<>(3);
//    Node<Integer> node4 = new Node<>(4);
//    Node<Integer> node5 = new Node<>(5);
//    head.next = node2;
//    node2.next = node3;
//    node3.next = node4;
//    node4.next = node5;
    if (length <= 0){
      return null;
    }
    Node<Integer> head = new Node<>(1);
    Node<Integer> temp  = head;
    for (int i = 1; i < length ; i++) {
      Node<Integer> node = new Node<>(i+1);
      temp.next = node;
      temp = node;
    }
    return head;
  }

  private static class Node<T> {
    private T data;
    private Node<T> next;

    public Node(T t) {
      this.data = t;
    }
  }
}
