package com.na.test.batchbet.util;

import sun.applet.Main;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Administrator on 2017/5/12 0012.
 */
public class RandUtils {
    /**
     * 随机指定范围内N个不重复的数
     * 最简单最基本的方法
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n 随机数个数
     */
    public static int[] randomCommon(int min, int max, int n){
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while(count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if(num == result[j]){
                    flag = false;
                    break;
                }
            }
            if(flag){
                result[count] = num;
                count++;
            }
        }
        return result;
    }

    /**
     * 随机指定范围内N个不重复的数
     * 最简单最基本的方法
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n 随机数个数
     */
    public static Set<Integer> randomSet(int min, int max, int n){
        int[] nums = randomCommon(min,max,n);
        Set<Integer> sets = new HashSet<>();
        for (int num : nums){
            sets.add(num);
        }
        return sets;
    }

    public static int randNum(int length){
        Random rnd = new Random();
        int num = rnd.nextInt(length);
        return num;
    }

    public static int randNum(int max,int min){
        Random rnd = new Random();
        return rnd.nextInt(max)%(max-min+1) + min;
    }

    public static void main(String[] args) {
        int max = 1000;
        int min = 670;
        for (int i=0;i<10;i++){
            System.out.println(randNum(max,min));
        }
    }
}
