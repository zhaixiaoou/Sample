package com.zxo.lib.lee;

import java.util.Random;
import java.util.Scanner;

/**
 * 并查集
 */
public class UnionFind {

  private static final  int MAX = 5005;
  // 对应节点的父节点
  private int[] fa = new int[MAX];
  // 对应节点的深度
  private int[] rank = new int[MAX];
  public static void main(String[] args) {

    // 有n个人，m个亲戚关系，询问p对亲戚关系
    int n = 10, m= 3, p = 1;
    int[] xa = new int[]{1,4,7};
    int[] ya = new int[]{3,7,8};
    UnionFind unionFind = new UnionFind();
    unionFind.init(n);
    for (int i =0;i < m; i++){
      int x = xa[i];
      int y = ya[i];
      System.out.println("合并 x="+x+"  y="+y);
      unionFind.merge(x, y);
    }

    for (int i =0; i< p; i++){
      int x = 4;
      int y = 8;
      System.out.println("查找 x="+x+"  y="+y);
      boolean relative = (unionFind.find(x) == unionFind.find(y)) ;
      System.out.println(x+","+y+"是亲戚关系吗？" +(relative ? "YES": "NO"));
    }
  }

  private void init(int n){
    for (int i=0;i<n; i++){
      fa[i] = i;
      rank[i] = 1;
    }
  }

  private int find(int x){
    // fa[x] =find(fa[x]) 压缩树的深度
    return x == fa[x] ? x : (fa[x] =find(fa[x]));
  }

  private void merge(int i, int j){
    int x = find(i);
    int y = find(j);

    if (rank[x] <= rank[y]){
      // 深度小的树，挂在深度高的数上
      fa[x] = y;
    } else {
      fa[y] = x;
    }

    if (rank[x] == rank[y] && x != y){
      rank[y]++;
    }
  }

}
