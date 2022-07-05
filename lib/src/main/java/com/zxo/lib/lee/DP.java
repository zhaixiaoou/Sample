package com.zxo.lib.lee;

/**
 * 动态规划 解决最小重叠子问题的方法
 * 动态规划的关键点在于找到迭代表达式和初始条件
 * 迭代表达式可以从递归中找到规律
 * 初始条件往往是递归的结束条件
 */
public class DP {

  public static void main(String[] args) {
//    int[] nums = new int[]{2, 7, 9, 3, 1, 14};
//    thief(nums);

//    System.out.println("斐波那契数列求和="+fibonacci(6));
    int[] coins = new int[]{1, 2};
    int[] array = new int[]{10,9,2,5,3,7,101,18};
//    System.out.println("零钱兑换问题="+coins(coins, 2));
//    System.out.println("零钱组合问题="+combineCoins(coins, 11));
//    System.out.println("最长子序列问题="+upSubArray(array));
    System.out.println("二叉搜索树问题="+binaryTree(3));
  }

  /**
   * 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
   * <p>
   * 给定一个代表每个房屋存放金额的非负整数数组，计算你 不触动警报装置的情况下 ，一夜之内能够偷窃到的最高金额。
   * <p>
   *  
   * <p>
   * 示例 1：
   * <p>
   * 输入：[1,2,3,1]
   * 输出：4
   * 解释：偷窃 1 号房屋 (金额 = 1) ，然后偷窃 3 号房屋 (金额 = 3)。
   *      偷窃到的最高金额 = 1 + 3 = 4 。
   * 示例 2：
   * <p>
   * 输入：[2,7,9,3,1]
   * 输出：12
   * 解释：偷窃 1 号房屋 (金额 = 2), 偷窃 3 号房屋 (金额 = 9)，接着偷窃 5 号房屋 (金额 = 1)。
   *      偷窃到的最高金额 = 2 + 9 + 1 = 12 。
   * <p>
   * 来源：力扣（LeetCode）
   * 链接：https://leetcode-cn.com/problems/house-robber
   * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
   */
  private static void thief(int[] nums) {
    int count = nums.length;
    int[] dp = new int[100000];
    if (count == 0) {
      System.out.println("0");
      return;
    }
    if (count == 1) {
      System.out.println("最高=" + nums[0]);
      return;
    }
    dp[0] = nums[0];
    dp[1] = Math.max(nums[0], nums[1]);
    for (int i = 2 ; i < count; i ++) {
     dp[i] = Math.max(dp[i-1], dp[i-2]+nums[i]);
    }

    System.out.println("最高=" + dp[count-1]);
  }

  /**
   * 斐波那契数列
   * F(0)=0，F(1)=1, F(n)=F(n - 1)+F(n - 2)
   *
   * 爬楼梯问题，最终的表达式也为 F(n)=F(n - 1)+F(n - 2) 区别 F(1)=1 F(2)=2
   */
  private static int fibonacci(int num){
    //该求解方法，简单暴力。 但是过多的重复， 算50的结果的话，49+48， 再算49=48+47。48则重复两次计算。
//    if (num == 0){
//      return 0;
//    }
//    if (num == 1){
//      return 1;
//    }
//    return fibonacci(num-1)+fibonacci(num-2);

      // 该方式 空间复杂度为num
//    int[] dp = new int[num];
//    if (num == 0){
//      return 0;
//    }
//    if (num == 1 || num == 2){
//     return 1;
//    }
//    dp[0] = dp[1] = 1;
//    for (int i=2;i<num; i++){
//      dp[i] = dp[i-1]+dp[i-2];
//    }
//    return dp[num-1];

    // 该方式空间复杂度为1 时间复杂度为n
    if (num ==1 || num == 2){
      return 1;
    }
    int per =1, cur = 1;
    int sum = 0;
    for (int i =2; i< num; i++){
      sum = per + cur;
      per = cur;
      cur = sum;
    }
    return sum;
  }

  /**
   * 给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
   *
   * 计算并返回可以凑成总金额所需的 最少的硬币个数 。如果没有任何一种硬币组合能组成总金额，返回-1 。
   *
   * 你可以认为每种硬币的数量是无限的。
   *
   * 示例1：
   *
   * 输入：coins = [1, 2, 5], amount = 11
   * 输出：3
   * 解释：11 = 5 + 5 + 1
   * 示例 2：
   *
   * 输入：coins = [2], amount = 3
   * 输出：-1
   * 示例 3：
   *
   * 输入：coins = [1], amount = 0
   * 输出：0
   *
   * @return
   */
  private static int coins(int[] coins, int amount){
    if (amount == 0){
      return 0;
    }
    if (coins.length ==0){
      return -1;
    }
    int[] dp = new int[amount+1];

    dp[0]=0;
    for (int i =1; i<=amount; i++){
      dp[i] = Integer.MAX_VALUE;
      for (int coin : coins) {
        if (i - coin >= 0) {
          dp[i] = Math.min(dp[i], dp[i-coin]+1);
        }
      }
    }
    return dp[amount];
  }

  /**
   * 给你一个整数数组 coins 表示不同面额的硬币，另给一个整数 amount 表示总金额。
   *
   * 请你计算并返回可以凑成总金额的硬币组合数。如果任何硬币组合都无法凑出总金额，返回 0 。
   *
   * 假设每一种面额的硬币有无限个。 
   *
   * @param coins
   * @param amount
   * @return
   */
  private static int combineCoins(int[] coins, int amount){


    int[] dp = new int[amount+1];
    dp[0]=1;
    for (int i = 1; i<= amount; i++){
      for (int coin : coins){
        if (i < coin){
          continue;
        }
        dp[i] +=dp[i-coin];
      }
    }
    return dp[amount];
  }

  /**
   * 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
   *
   * 子序列 是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。例如，[3,6,2,7] 是数组 [0,3,1,6,2,2,7] 的子序列。
   * 解题思路：数组中每一个位找出其对应的最长子序列，每一位需要遍历其之前所有的元素，找到最大的上升子序列
   *
   * @param source
   * @return
   */
  private static int upSubArray(int[] source){
    int n = source.length;
    int[] dp = new int[n];

    dp[0]=1;
    for (int i=1; i< n; i++){
      dp[i] = 1;
      for (int j=0; j<i; j++){
        if (source[i]> source[j]){
          dp[i] = Math.max(dp[i], dp[j]+1);
        }
      }
    }
    int max = 0;
    for (int item : dp){
      if (item > max){
        max = item;
      }
    }
    return max;
  }

  /**
   * 给你一个整数 n ，求恰由 n 个节点组成且节点值从 1 到 n 互不相同的 二叉搜索树 有多少种？返回满足题意的二叉搜索树的种数。
   * 解题思路：整数1..n都可以做跟节点。 假设n为9，根结点为5， 则左子树为1234，右子树为6789 则二叉树的种树为 左子树种树*右子树种树
   * 左右子树的种树与其元素无关，只与其数量相关
   * @return
   */
  private static int binaryTree(int n){
    int[] dp = new int[n+1];
    dp[0]=1;
    dp[1]=1;
    for (int i=2;i<=n; i++){
      for (int j=1;j<=i; j++){
        // 每个节点都可以是根结点
        dp[i] += dp[j-1]*dp[i-j];
      }
    }
    return dp[n];
  }


}
