package com.zxo.lib.lee;

public class xiaoK {

  public static void main(String[] args) {
    floorEgg(100,3);
  }

  /**
   * 有一栋100层的楼，和2个坚硬的鸡蛋，从楼上扔下鸡蛋，鸡蛋会在大于某一层刚好开始碎，那最少几次能测出鸡蛋能承受的最大楼层呢？
   * @param r
   * @param e
   */
  private static void floorEgg(int r, int e){
    int floor = 101;
    int egg = 4;
    int[][] f = new int[floor][egg];
    // 初始化数据
    for (int i =0; i< floor; i++){
      for (int j = 0; j< egg; j++){
        f[i][j] = 0x7ffffff;
      }
    }

    // i层楼 1个鸡蛋
    for (int i=0; i<floor; i++) {
      f[i][1] = i;
    }
    // 0 1层篓 i个鸡蛋
    for (int i =0; i< egg; i++){
      f[0][i] = 0;
      f[1][i] = 1;
    }

    for (int j=2; j<egg ; j++){
      for (int i=2; i<floor; i++){
        // 尝试从1到i层楼扔下，在最坏的情况下取最好的结果
        for (int k=1; k<i ; k++){
          // f[k-1]f[j-1] 在k层摔碎，则在k-1层间继续尝试，此时鸡蛋只有j-1个
          // f[i-k][j] 在k层没有摔碎，则在i-k层继续尝试，此时鸡蛋仍然是j个
          // 在两者中取出次数多的一种情况，最为最好的结果 再加上k层这一次的结果
          int temp = Math.max(f[k-1][j-1], f[i-k][j]) + 1;
          f[i][j] = Math.min(temp, f[i][j]);
        }
      }
    }

    System.out.println(r+"层楼，"+e+"个鸡蛋，最少尝试"+f[r][e]+"次，能找到在哪层破碎");

  }

  /**
   * 并查集
   */
  private static void unionFind(int n){
    int[] fa = new int[5000];
    // 初始化数据，假设每个元素单独为一个集合
    for (int i =0; i<n; i++){
      fa[i] = i;
    }

  }
}
