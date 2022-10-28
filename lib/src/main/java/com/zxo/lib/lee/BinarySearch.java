package com.zxo.lib.lee;

import java.util.ArrayList;
import java.util.List;

/**
 * 二分查找
 */
public class BinarySearch {

  public static void main(String[] args) {
//    int[] nums = new int[]{-1,0,3,5,8,9};
    int[] nums = new int[]{2,3,1,2,4,3};
    int result = minSubArrayLen2(7, nums);
//    int result = search(nums, 9);
//    int result = arrangeCoins(9);
//    int mid = 4 + ((5-4) >>> 1);
    System.out.println("result="+result);


  }
  private static int search(int[] nums, int target){
    if (nums== null || nums.length ==0){
      return -1;
    }


    int right = nums.length-1;
    int left = 0;
    int mid ;

    while (left <= right) {
      mid = left + ((right-left)>>1);
      if (nums[mid] ==target) {
        return mid;
      }
      if (nums[mid] < target) {
        left = mid+1;
      } else {
        right = mid -1;
      }
    }
    return -1;

  }

  private static int arrangeCoins(int n) {

    // sum = n *(n+1) /2  前n项和

    int left =1, right = n;
    int mid =0;
    while (left < right) {
      mid =  left + ((right-left+1) >> 1);
      if (mid * (mid+1)  <= 2*n) {
        left = mid;
      } else {
        right = mid -1;
      }
    }
    return left;

  }

  /**
   * 给定一个含有n个正整数的数组和一个正整数 target 。
   *
   * 找出该数组中满足其和 ≥ target 的长度最小的 连续子数组[numsl, numsl+1, ..., numsr-1, numsr] ，并返回其长度。如果不存在符合条件的子数组，返回 0 。
   */
  // 暴力破解
  private static int minSubArrayLen(int target, int[] nums) {
    int sum = 0;
    int maxLength = Integer.MAX_VALUE;

    for (int i = 0; i < nums.length ; i++) {
      sum =0;
      for (int j = i; j < nums.length; j++) {
        sum += nums[j];
        if (sum >= target) {
          maxLength = Math.min(maxLength, j-i+1);
          break;
        }
      }
    }

    if (maxLength == Integer.MAX_VALUE) {
      return 0;
    } else {
      return maxLength;
    }
  }

  private static int minSubArrayLen2(int target, int[] nums) {
    int sum = 0;
    int minLength = Integer.MAX_VALUE;
    int start = 0;
    for (int i = 0; i < nums.length; i++) {
      sum += nums[i];

      while (sum >= target) {
        minLength = Math.min(minLength, i-start+1);
        sum -= nums[start];
        start++;
      }
    }
    return minLength == Integer.MAX_VALUE ? 0: minLength;
  }

}
