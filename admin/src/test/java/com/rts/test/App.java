package com.rts.test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class App {
    public static void main(String[] args) {
//        Person p = new Person(22);
//        grow(p);
//        System.out.println(p.age);

//        String a = "LightWAN";
//        String b = "LightWAN";
//        String c = new String("LightWAN");
//        String d = new String("LightWAN");
//        System.out.println(a == b);
//        System.out.println(b == c);
//        System.out.println(c == d);
//        System.out.println(d.equals(a));
        int corePoolSize = 2;
        int maxPoolSize = 5;
        long keepAliveSeconds = 5;
        BlockingQueue workqueue = new ArrayBlockingQueue(4);
        AtomicInteger threadId = new AtomicInteger(1);
        ThreadFactory factory = runnable -> new Thread(runnable, "thread-" + threadId.getAndIncrement());
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveSeconds, TimeUnit.SECONDS, workqueue, factory);
        for (int i = 0; i < 10; i++) {
            int k = i;
            executor.submit(() -> {
                try {
                    Thread.sleep(1000); } catch (InterruptedException ignored)
                {}
                System.out.println(Thread.currentThread().getName() + ":" + k );
            });
        }
    }
    public static void grow(Person p) {
        p = new Person();
        p.age ++;
        System.out.println(p.age + "-----");
    }
}
class Person {
    int age;
    Person(int x) {
        age = x;
    }
    Person() {
        age = 0;
    }
}