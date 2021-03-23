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

package com.hleper.juc.locks;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch
 *
 * @author sz_qiuhf@163.com
 * @since 2021-03-23
 **/
public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        usingCountDownLatch();

        usingJoin();
    }

    private static void usingCountDownLatch() throws InterruptedException {
        System.out.println("Using countDownLatch..");
        int threadCount = 100;
        Thread[] threads = new Thread[threadCount];
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                System.out.println(Thread.currentThread().getName());
                countDownLatch.countDown();
            }, "Thread-" + i);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        countDownLatch.await();
    }

    private static void usingJoin() throws InterruptedException {
        System.out.println("Using join..");
        int threadCount = 100;
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                System.out.println(Thread.currentThread().getName());
            }, "Thread-" + i);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }
}
