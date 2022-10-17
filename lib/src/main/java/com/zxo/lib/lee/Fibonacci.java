package com.zxo.lib.lee;

/**
 * 斐波那契数列
 */
public class Fibonacci {

  /**
   * 斐波那契数列
   * F(0)=0，F(1)=1, F(n)=F(n - 1)+F(n - 2)
   * <p>
   * 爬楼梯问题，最终的表达式也为 F(n)=F(n - 1)+F(n - 2) 区别 F(1)=1 F(2)=2
   */

  public static void main(String[] args) {
    Fibonacci f = new Fibonacci();
//    int sum = f.fibonacci1(7);
    int sum = f.fibonacci2(7);
    System.out.println("数值="+sum);
  }
  /**
   * 递归方式简单暴力， 存在问题是重复计算
   * @param num
   * @return
   */
  private int fibonacci1(int num) {
    if (num == 0) {
      return 0;
    }

    if (num == 1) {
      return 1;
    }

    return fibonacci1(num - 1) + fibonacci1(num - 2);
  }

  private int fibonacci2(int num){

    if (num == 0) {
      return 0;
    }

    if (num == 1 || num == 2) {
      return 1;
    }
    int[] sum = new int[num];
    sum[0] = 1;
    sum[1] = 1;
    for (int i = 2; i < num; i++) {
      sum[i] = sum[i-1]+sum[i-2];
    }
    return sum[num-1];
  }

  /**
   * 假设你在爬楼梯，n阶台阶
   * 每次只能1个或者2个台阶，一共需要多少步
   */
  private void steps(){

  }
}
