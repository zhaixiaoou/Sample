package com.zxo.lib.lee;

/**
 * 排序问题
 */
public class Sort {
  public static void main(String[] args) {
    int[] source = new int[]{2, 12, 4, 2, 6, 3, 7, 24, 13};
    Sort sort = new Sort();
    //    int[] output = sort.bubbleSort(source);
    //        int[] output = sort.insertSort(source);
    //    int[] output = sort.selectSort(source);
//    int[] output = sort.shellSort(source);
    int[] output = sort.quickSort(source, 0, source.length-1);
    sort.arrayToString(output);

  }


  public void arrayToString(int[] input) {
    for (int i : input) {
      System.out.print(i + " ");
    }
    System.out.println("");
  }

  public void swap(int[] source, int before, int after) {
//    int temp = source[before];
//    source[before] = source[after];
//    source[after] = temp;
    // 一个数对另一个数进行2次异或操作 这个数不变
    source[before] = source[before] ^ source[after];
    source[after] = source[before] ^ source[after];
    source[before] = source[before] ^ source[after];
  }

  /**
   * 冒泡排序 最大的往后一步一步往后走
   *
   * @param source
   * @return
   */
  public int[] bubbleSort(int[] source) {
    if (source == null || source.length < 2) {
      return source;
    }

    for (int i = 0; i < source.length - 1; i++) {

      for (int j = 0; j < source.length - 1 - i; j++) {
        // 如果是>这个判断则排序算法是稳定的

        if (source[j] > source[j + 1]) {
          swap(source, j, j + 1);
        }
      }
    }
    return source;
  }

  /**
   * 选择排序， 在每一次循环中，找到最小的数据坐标，和此次循环中的队首元素交换
   *
   * @param source
   * @return
   */
  public int[] selectSort(int[] source) {
    if (source == null || source.length < 2) {
      return source;
    }
    int minIndex;
    for (int i = 0; i < source.length - 1; i++) {
      minIndex = i;
      for (int j = i + 1; j < source.length; j++) {
        if (source[minIndex] > source[j]) {
          minIndex = j;
        }
      }
      if (minIndex > i) {
        swap(source, minIndex, i);
      }
      arrayToString(source);

    }
    return source;
  }

  /**
   * 插入排序，前面是已经排好序的数据，未排序的数据依次对比排好序的数据，找到最终插入的位置。
   *
   * @param source
   * @return
   */
  public int[] insertSort(int[] source) {
    if (source == null || source.length < 2) {
      return source;
    }
    for (int i = 1; i < source.length; i++) {
      int temp = source[i];
      int j = i - 1;
      while (j > 0 && source[j] > temp) {
        // 如果前面的值大于要对比的数，则该值右移，直到找到小于的数值或者队首
        source[j + 1] = source[j];
        j--;
      }
      source[j + 1] = temp;

      arrayToString(source);
    }
    return source;
  }

  /**
   * 希尔排序-- 插入排序的改进版 分组，在组内使用插入排序
   * @param source
   * @return
   */
  public int[] shellSort(int[] source) {
    if (source == null || source.length < 2) {
      return source;
    }
    arrayToString(source);
    // 无符号右移
    int gap = source.length >>> 1;
    while (gap > 0) {
      for (int i = gap; i < source.length; i++) {
        int temp = source[i];
        // 前一个要对比的元素
        int j = i - gap;
        while (j >= 0 && source[j] > temp) {
          source[j + gap] = source[j];
          j = j - gap;
        }
        source[j + gap] = temp;

        arrayToString(source);
      }
      gap = gap >>> 1;
    }
    return source;
  }

  /**
   * 快速排序
   * 找一个基准值， 普遍以第一元素为基准值，也可以取左中右的中间数据最为基准值，避免算法复杂度退化
   * 需要两个指针left、right。分别指向开始和结尾的位置。
   * 从right开始，对比基准值， 大于基准值，则right--; 如果小于则将right位置的值赋值给left位置 left++ (开始left对应的基准值已保存为temp)
   * 接着从left开始，对比基准值，小于基准值，则left++; 如果大于则将left位置的值赋值给right位置，再依次从right开始，直到left==right 再将temp赋值给left位置。
   * 此时 left之前都比temp小，之后比temp大。
   * 再分割为两个数组，重复上述过程
   * @param source
   * @return
   */
  public int[] quickSort(int[] source, int start, int end) {
    if (source == null || source.length < 2) {
      return source;
    }
    int left = start, right  = end;
    // 取左右中的中间数据作为基准值，这样避免顺序由大到小排序时，算法时间复杂度退化为n^2
    int midIndex = middleOf3Index(source, start, end);
    if (midIndex != start) {
      swap(source, start, midIndex);
    }

    int temp = source[left];

    while (left < right ){

      while (left < right && temp <= source[right]) {
        right --;
      }
      if (left < right){
        source[left] = source[right];
        left++;
      }

      while (left < right && temp >= source[left]){
        left ++;
      }
      if (left < right){
        source[right] = source[left];
        right --;
      }

    }
    source[left] = temp;
    arrayToString(source);

    if (start < left-1) {
      quickSort(source, start, left-1);
    }
    if (right +1 < end){
      quickSort(source, right+1, end);
    }
    return source;
  }

  private int middleOf3Index(int[] source, int start, int end){
    int mid = start + (end-start) >>> 1;
    if (source[mid] > source[start]) {
      if (source[mid] > source[end]){
        return source[start] > source[end] ? start : end;
      } else {
        return mid;
      }
    } else {
      if (source[start] > source[end]) {
        return source[mid]> source[end] ? mid : end;
      } else {
        return start;
      }
    }
  }
}
