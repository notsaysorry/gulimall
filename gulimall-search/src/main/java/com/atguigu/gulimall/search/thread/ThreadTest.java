package com.atguigu.gulimall.search.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThreadTest {

    public static Executor executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main start.......");
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int i = 10;
            System.out.println("线程执行了");
            return i;
        }, executor).whenComplete((res, e) -> {
            System.out.println("res： " + res);
            System.out.println("e: " + e);
        });

        Integer integer = future.get();
        System.out.println(integer);
        System.out.println("main end............");

        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf();
        CompletableFuture<Object> objectCompletableFuture = CompletableFuture.anyOf();
    }
}
