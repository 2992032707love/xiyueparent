package com.rts.test;

import java.util.Scanner;

public class TestTwo {
    /*
     * 给出n个正整数，这n个正整数中可能存在一些相同的数。现在从这n个数中选取m个正整数，
     * 分别记为X1、X2、......、Xm，使得这m个数满足如下公式： 1/X1 + 1/X2 + ...... + 1/Xm = 1
     * 请问存在多少种不同的数字组合可以使得上述公式得以成立？
     *
     * 单组输入。 第1行输入一个正整数n，表示输入的正整数个数。(n<=100) 第2行输入n个正整数，两两之间用
     * 空格隔开。
     *
     * 输出满足要求的组合个数。如果一个组合都不存在，则输出“No solution!”。
     *
     * 6
     * 1 2 2 3 4 4
     *
     * 3
     * 对于输入样例中的6个正整数，存在3种和为1的组合，分别是：1=1/1，1=1/2+1/2，1=1/2+1/4+1/4。
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println();
        int T = sc.nextInt();
        int[] arg = new int[T];
        int sum = 0;
        int K = 0;
        for (int i = 0; i < T; i++) {
            arg[i] = sc.nextInt();
        }
        for (int i = 0; i < T; i++) {

        }
    }
}
