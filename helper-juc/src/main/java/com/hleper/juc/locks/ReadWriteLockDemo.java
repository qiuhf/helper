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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantLock
 *
 * @author sz_qiuhf@163.com
 * @since 2021-03-23
 **/
public class ReadWriteLockDemo<T> {
    private volatile T record;

    public static void main(String[] args) {
        ReadWriteLockDemo<Double> readWriteLockDemo = new ReadWriteLockDemo<>();

        // 对比两个方法，看read线程打印的速度

        usingReadWriteLock(readWriteLockDemo);

        usingReentrantLock(readWriteLockDemo);
    }

    private static void usingReentrantLock(ReadWriteLockDemo<Double> readWriteLockDemo) {
        final ReentrantLock reentrantLock = new ReentrantLock();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> readWriteLockDemo.read(reentrantLock), "Read-Thread-" + i).start();
        }

        for (int i = 0; i < 2; i++) {
            new Thread(() -> readWriteLockDemo.write(reentrantLock, Math.random()), "Read-Thread-" + i).start();
        }
    }

    private static void usingReadWriteLock(ReadWriteLockDemo<Double> readWriteLockDemo) {
        final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        // 共享锁
        Lock readLock = readWriteLock.readLock();
        // 独占锁
        Lock writeLock = readWriteLock.writeLock();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> readWriteLockDemo.read(readLock), "Read-Thread-" + i).start();
        }

        for (int i = 0; i < 2; i++) {
            new Thread(() -> readWriteLockDemo.write(writeLock, Math.random()), "Read-Thread-" + i).start();
        }
    }

    public void read(Lock lock) {
        lock.lock();
        try {
            // 模拟读取操作
            TimeUnit.SECONDS.sleep(1);
            System.out.println(Thread.currentThread().getName() + " -> Record:" + this.record);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void write(Lock lock, T t) {
        lock.lock();
        try {
            // 模拟写操作
            TimeUnit.SECONDS.sleep(1);
            this.record = t;
            System.out.println(Thread.currentThread().getName() + " -> Record:" + t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
