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

package com.hleper.juc.lock;


import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;

/**
 * AtomicXXX Vs Synchronized Vs LongAdder
 *
 * @author sz_qiuhf@163.com
 * @since 2021-03-19
 **/
public class AtomicVsSyncVsLongAdder {
    private static final int CYCLE = 100000;
    private static final int THREAD_SUM = 200;
    private static long count = 0L;
    private static AtomicLong atomicLong = new AtomicLong(0L);
    private static LongAdder longAdder = new LongAdder();

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[THREAD_SUM];

        synchronize(threads);

        atomicXXX(threads);

        longAdder(threads);
    }

    private static void longAdder(Thread[] threads) throws InterruptedException {
        for (int i = 0; i < THREAD_SUM; i++) {
            threads[i] = new Thread(() -> {
                for (int k = 0; k < CYCLE; k++) {
                    longAdder.increment();
                }
            });
        }

        execute(threads, "LongAdder", r -> longAdder.sum());
    }

    private static void atomicXXX(Thread[] threads) throws InterruptedException {
        for (int i = 0; i < THREAD_SUM; i++) {
            threads[i] = new Thread(() -> {
                for (int k = 0; k < CYCLE; k++) {
                    atomicLong.incrementAndGet();
                }
            });
        }
        execute(threads, "Atomic", r -> atomicLong.get());
    }

    private static void synchronize(Thread[] threads) throws InterruptedException {
        final Object object = new Object();
        for (int i = 0; i < THREAD_SUM; i++) {
            threads[i] = new Thread(() -> {
                for (int k = 0; k < CYCLE; k++) {
                    synchronized (object) {
                        count++;
                    }
                }
            });
        }
        execute(threads, "Synchronized", r -> count);
    }

    private static void execute(Thread[] threads, String name, Function<Void, Long> callback) throws InterruptedException {
        long start = System.currentTimeMillis();

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long end = System.currentTimeMillis();

        System.out.println(String.format(Locale.ENGLISH, "%s: sum = %d , time= %dms", name, callback.apply(null),
                (end - start)));
    }
}
