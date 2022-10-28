package com.zxo.lib.lee;

import java.util.HashMap;

/**
 * K数之和
 */
public class KSum {

  public static void main(String[] args) {

    int[] array = {1,3,5,6,9};

    KSum kSum = new KSum();
    int[] result = kSum.twoSum(10, array);
    kSum.print(result);
  }

  private void print(int[] array){
    for (int item : array) {
      System.out.print(item);
      System.out.print("  ");
    }
  }

  private int[] twoSum(int target, int[] array){
    int[] result = new int[2];

    if(array == null || array.length <= 1) {
      return result;
    }
    HashMap<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i <array.length; i++) {
      int temp = target-array[i];
      if (map.containsKey(temp)){
        result[0] = map.get(temp);
        result[1] = i;
        return result;
      }
      map.put(array[i], i );

    }
    return result;
  }
}
