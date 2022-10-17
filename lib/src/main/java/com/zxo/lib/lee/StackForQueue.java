package com.zxo.lib.lee;

import java.util.Stack;

/**
 * 两个栈实现一个队列
 */
public class StackForQueue<T> {

  private Stack<T> pushStack = new Stack<>();
  private Stack<T> popStack = new Stack<>();

  public static void main(String[] args) {
    StackForQueue<Integer> queue = new StackForQueue<>();
    queue.push(1);
    queue.push(2);
    queue.push(3);
    queue.push(4);
    System.out.println(queue.pop());
    System.out.println(queue.pop());
    queue.push(5);
    System.out.println(queue.pop());
    System.out.println(queue.pop());
    System.out.println(queue.pop());
    System.out.println(queue.pop());
    queue.push(6);
  }


  public void push(T data) {
    pushStack.push(data);
  }

  public T pop() {
    if (popStack.isEmpty()) {

      if (pushStack.isEmpty()) {
        return null;
      } else {
        while (!pushStack.isEmpty()) {
          T data = pushStack.pop();
          popStack.push(data);
        }
        return popStack.pop();
      }
    } else {
      return popStack.pop();
    }
  }

}
