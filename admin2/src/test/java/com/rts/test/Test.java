package com.rts.test;

public class Test {
    public static void main(String[] args) {
        System.out.println(getTestString());
    }
    public static void getException() throws Exception{
        throw new Exception();
    }
    public static String getTestString(){
        try {
            getException();
            return "return in try";
        } catch (Exception e) {
            return "return in catch";
        } finally {
            return "return in finally";
        }
    }
}
