package com.zxo.lib.lee;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 三色小球排序问题
 * 有三种颜色的球，白色（0表示），红色（1表示），蓝色（2表示）。
 * 给出序列[2,1,0,1,2,0,0,1,2]，请按照白色，红色，蓝色给这个序列重新排序。
 * 得到的结果为[0,0,0,1,1,1,2,2,2]。算法的时间复杂度为O(N)。
 */
public class ThreeBollSort {
  public static void main(String[] args) {
    int WHITE = 0, RED = 1, BLUE = 2;
    int[] array = new int[]{2,1,0,1,2,0,0,1,2};
    LinkedList<Integer> result = new LinkedList<>();
    int white= 0, red = 0, blue= 0;
    for (int i = 0; i < array.length; i++) {
      int data = array[i];
      if (data == WHITE){
       result.add(white, data);
        white++; red++;
      }
      if (data == RED){

        result.add( red, data);
        red++;
      }
      if (data == BLUE){

        result.add(data);

      }
    }

    for (int item : result) {
      System.out.print(item);
    }
  }
}
