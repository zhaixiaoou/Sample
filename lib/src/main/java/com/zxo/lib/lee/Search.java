package com.zxo.lib.lee;

import java.util.List;

/**
 * 搜索类 相关算法
 * 深度优先遍历 DFS 相当于前序遍历
 * 广度优先搜索 BFS 相当于层序遍历
 *
 * 邻接矩阵、邻接表
 */
public class Search {

  public static void main(String[] args) {
    int [] input = new int[]{1,2,3,4,5};
    wholeArrangement(input, 0);
  }

  /**
   * 全排列问题
   * 思路：
   *  第一个位置可以是任意数，以此类推，每个位置都可以是任意数
   */
  private static void wholeArrangement(int[] source, int index){
    int length = source.length;
    if (index >= length) {
      return ;
    }
    int[][] output = new int[length][length];

    for (int i = index+1; i < length; i++) {
      int[] opSource = getOpSource(source);
      int temp = opSource[index];
      int target = opSource[i];
      opSource[i] = temp;
      opSource[index] = target;
      output[i-1] = opSource;
    }
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < length; j++) {
        System.out.print(output[i][j]);
        System.out.print(" ");
      }
      System.out.println("");
    }
    index ++;
    wholeArrangement(source, index);
  }

  private static int[] getOpSource(int[] source) {
    int [] opSource = new int[source.length];
    for (int i = 0; i < source.length; i++) {
      opSource[i] = source[i];
    }
    return opSource;
  }

  private static void searchString(List<List<Character>> source, String target){
    for (int i=0;i<source.size(); i++){
      // 外层数组

    }
  }
}
