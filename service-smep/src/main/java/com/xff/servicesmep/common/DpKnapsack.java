package com.xff.servicesmep.common;

/**
 * DpKnapsack.
 *
 * @author wang_fei
 * @since 2022/5/25 9:49
 */
public class DpKnapsack {
    public static void main(String[] args) {
        int[] w = {1,4,3,5,2}; //物品的重量
        int[] val = {1500,3000,2000,4000,1800}; //物品的价值
        int m = 6;//背包的容量
        int n = val.length; //物品的数量

        //创建二维数组：
        //v[i][j] 表示在前i个物品中能够装入容量为j的背包中最大价值
        int[][] v = new int[n+1][m+1];
        //初始化第一行和第一列，本程序可以不处理，因为默认数为0

        //这里i是从1开始的，下标代表的是二维数组的索引，故一维数组应从0开始，也就是i -1
        for (int i = 1; i < v.length; i++) {
            for (int j = 0; j <v[0].length ; j++) {
                //根据公式，当新增的商品重量大于当前背包的容量时，采取上一次的装入策略
                if (w[i-1] > j){
                    v[i][j] = v[i-1][j];
                }
                //当新增重量等于或者或者小于背包容量时，判断剩余容量的最佳策略 + 当前容量的最佳策略，与上次策略哪个大，取大的。
                else{
                    v[i][j] = Math.max(v[i-1][j],val[i-1] + v[i-1][j - w[i-1]]);
                }
            }
        }
        for(int i=0;i<v.length;i++){
            for(int j=0;j<v[i].length;j++){
                System.out.print(v[i][j]+"\t");
            }
            System.out.println();
        }
        System.out.println(v[n][m]);

    }
}
