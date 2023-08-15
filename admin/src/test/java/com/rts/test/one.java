package com.rts.test;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class one {
    /**
     * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
     *
     * 判断方程组是否有解，有解返回1，无解返回0
     * @param a1 int整型
     * @param b1 int整型
     * @param c1 int整型
     * @param a2 int整型
     * @param b2 int整型
     * @param c2 int整型
     * @return int整型
     */
    public static int judge (int a1, int b1, int c1, int a2, int b2, int c2) {
        // write code here
//        if ((a1 ==0 && b2 ==0) || (a2 ==0 && b1 ==0)) {
//            return 1;
//        }else if ((a1 == 0 && a2 ==0) || (b1 == 0&& b2 ==0)){
//            if (c1 !=0 || c2 !=0) {
//
//            }
//        }
//        else
        if (a2 ==0 || b2 ==0) {
            return 1;
        }else
        if (a1/a2 == b1/b2) {
            if (c2 ==0) {
                return 1;
            }else
            if (a1/a2 != c1/c2) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }
    /**
     * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
     *
     * 1 2 2 0 3 4 5 6
     * 1 2 20 2 3 1 2 2 4 5 6
     *
     * 1 2 2 2 4
     * 1 2 22 4
     * 1 2 2 24
     * 12 *3
     * 1 22 *2
     * @param s string字符串 非空
     * @return int整型
     * 1*2*2*()
     */
    public static int function (String s) {
        // write code here
        System.out.println(s);
        String[] news = s.split("");
        System.out.println(Arrays.toString(news));

        return 2;
    }
    /**
     * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
     *
     * 参数words是一个数组，内含若干字符串由26个小写英文字母组成
     * @param words string字符串一维数组 一维数组，其中每个元素都是字符串
     * @return int整型
     */
    public static int MaxProduct (String[] words) {
        // write code here
        System.out.println(Arrays.toString(words));
        int max = 0;
        Boolean sl = true;
        for (int i = 0; i < words.length - 2; i++) {
            String[] newone = words[i].split("");
            for (int t = i+1; t < words.length; t++) {
                String[] newtwo = words[t].split("");
                sl = true;
                if (sl) {
                    for (int k = 0; k < newone.length; k++) {
                        System.out.println(newone[k] + "_one");
                        if (sl) {
                            for (int m = 0; m < newtwo.length; m++) {
                                System.out.println(newtwo[m] + "_two");
                                if (Objects.equals(newone[k], newtwo[m])) {
                                    sl = false;
                                    break;
                                } else {
                                    sl = true;
                                }
                            }
                        }else {break;}
                    }
                }
                if (sl) {
                    int j = newone.length * newtwo.length;
                    if (max< j) {
                        max = j;
                    }
                    System.out.println(max + "_max");
                }
            }
        }
        System.out.println(max);
        return max;
    }
    public static void main(String[] args) {
//        System.out.println(judge(1,2,13,2,1,1));
        function("123456");//{"abry","baz","foo","bar","ctfm","abcdef"};
//        String[] w = {"a", "aa"};
        //{"a","ab","abc","d","cd","bcd","abcd"};
//        MaxProduct(w);
    }
}
