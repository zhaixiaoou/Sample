package com.zxo.lib.lee;

/**
 * 贪心算法
 *
 * 有N堆纸牌编号为1~N，每堆有若干张，但纸牌总数必为N的倍数。可在任一堆上取若干张移动。
 * 移牌规则：
 * 编号为1的堆上取的纸牌只能移到编号为2的堆上
 * 编号为N的堆上取的纸牌只能移到编号为N-1的堆上
 * 其他堆上取的纸牌可向左右相邻堆移动
 * 问最少要移动几次可使每堆上纸牌一样多
 */
public class Greedy {

  public static void main(String[] args) {

    int n = 10;
    int avg = 5;
    int[] cardArray = new int[]{2,6,3,1,6,3,8,10, 2, 9};
    int interval =0;
    int last = -1;
//    Greedy greedy = new Greedy();
//    greedy.step(0,n,-1, avg);
//    System.out.println("次数="+ greedy.interval);
    int sum = 0;
    for (int i = 0; i < n; ++i) {
      sum += cardArray[i];
      if (sum == (i - last) * avg) {
        interval++;
        sum = 0;
        last = i;
      }
    }
    System.out.println("次数="+ interval);

  }



  private  void step(int start, int end,int last, int avg){
//    int sum = 0;
//    for (int i =start; i< end; i++){
//      sum+=cardArray[i];
//      if (sum >= (i-last) * avg){
//        // 大于平均数 在此分割 移动一次
//        System.out.println("i = "+i);
//        interval ++;
//        step(0, i, last, avg);
//        step(i+1, end, i, avg);
//        break;
//      }
//    }
  }
}
