/*
 * Copyright 2020-2021. the original qiuhaifeng .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hleper.juc.thread;

import java.util.concurrent.Executors;

/**
 * 什么是线程及启动线程的三种方式
 * 1. 继承Thread类
 * 2. 实现Runnable接口
 * 3. 线程池方式
 *
 * @author sz_qiuhf@163.com
 * @since 2021-03-17
 **/
public class HowToCreateThread {
    public static void main(String[] args) {
        System.out.println("Hello " + Thread.currentThread().getName());

        MyThread thread = new MyThread();
        // 设置线程名称
        thread.setName("MyThread");
        // run(), 可以理解: 主线程调用MyThread线程中run方法，没有真正启动线程，线程的状态：NEW
        thread.run();
        System.out.println("当前线程状态 = " + thread.getState().name());
        // 启动线程
        thread.start();
        System.out.println("当前线程状态 = " + thread.getState().name());


        Thread runnable = new Thread(new MyRunnable());
        runnable.setName("MyRunnable");
        runnable.start();

        new Thread(() -> System.out.println("Hello Lambda.")).start();

        Executors.newCachedThreadPool().submit(() ->
                System.out.println("Hello ThreadPool.")
        );
    }

    private static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("Hello " + Thread.currentThread().getName());
        }
    }

    private static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("Hello " + Thread.currentThread().getName());
        }
    }
}
