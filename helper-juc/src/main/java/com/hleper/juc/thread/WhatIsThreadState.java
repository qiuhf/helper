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

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/**
 * 线程状态
 *
 * @author sz_qiuhf@163.com
 * @since 2021-03-17
 **/
public class WhatIsThreadState {
    /**
     * 模拟线程被锁定场景
     */
    private static final Object LOCK = new Object();
    /**
     * 必须要加volatile，保证线程之间可见，不保证原子性
     * synchronized既保证线程之间可见性，也保证线程间原子性
     */
    private static volatile boolean stop = false;

    public static void main(String[] args) throws Exception {
        MyThread thread = new MyThread();
        // MyThread线程初始状态：NEW
        printCurrentThreadState(thread);
        // 启动MyThread线程
        thread.start();
        // MyThread线程的状态：NEW -> RUNNABLE
        // RUNNABLE内部有Ready和Running两种状态，调用Thread.yield(), Running -> ready
        printCurrentThreadState(thread);

        // 休眠1s, 让MyThread先获取到锁, 模拟获取锁场景
        TimeUnit.SECONDS.sleep(1);
        // 没拿到LOCK锁，阻塞
        synchronized (LOCK) {
            // MyThread.run()调用了wait()，MyThread线程状态：RUNNABLE -> WAITING
            printCurrentThreadState(thread);
            // 获取锁
            LOCK.notify();
            // 主线程获取到锁，MyThread线程状态：WAITING -> BLOCKED
            printCurrentThreadState(thread);

            stop = true;
        }

        // MyThread.run()调用了Thread.sleep(), MyThread线程状态：BLOCKED -> TIMED_WAITING
        printCurrentThreadState(thread);

        // 等待MyThread线程执行完
        thread.join();
        // MyThread线程状态：TIMED_WAITING -> TERMINATED
        printCurrentThreadState(thread);
    }

    private static void printCurrentThreadState(MyThread thread) {
        System.out.println("My thread state: " + thread.getState().name());
    }

    private static class MyThread extends Thread {
        @SneakyThrows
        @Override
        public void run() {
            synchronized (LOCK) {
                if (!stop) {
                    LOCK.wait();
                }
            }
            Thread.sleep(2000);
        }
    }
}
