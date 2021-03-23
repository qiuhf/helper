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
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock API使用
 * <p>
 * 1. 生产者与消费者 {@link com.hleper.juc.container.ContainerByCondition}
 *
 * @author sz_qiuhf@163.com
 * @since 2021-03-21
 **/
public class ReentrantLockDemo {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLockDemo reentrantLockDemo = new ReentrantLockDemo();

        System.out.println(" ================== lockDemo ====================== ");
        reentrantLockDemo.lockDemo();

        System.out.println(" ================== tryLockDemo ===================== ");
        reentrantLockDemo.tryLockDemo();

        System.out.println(" ================== lockInterruptiblyDemo ========================= ");
        reentrantLockDemo.lockInterruptiblyDemo();

        System.out.println(" ================== lockNonFairOrFair ========================= ");
        reentrantLockDemo.lockNonFairOrFair(true);

    }

    public void lockNonFairOrFair(boolean nonFair) {
        // ReentrantLock 参数为true表示为公平锁，请对比输出结果
        Lock lock = new ReentrantLock(nonFair);
        Thread thread1 = new Thread(() -> fireLock(lock), "thread-1");
        Thread thread2 = new Thread(() -> fireLock(lock), "thread-2");
        thread1.start();
        thread2.start();
    }

    private void fireLock(Lock lock) {
        for (int i = 0; i < 100; i++) {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " get lock.");
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 对interrupt()方法做出响应
     */
    public void lockInterruptiblyDemo() throws InterruptedException {
        Lock lock = new ReentrantLock();
        Thread thread1 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Get lock");
                TimeUnit.DAYS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println("Release lock.");
            }
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            try {
                lock.lockInterruptibly();
                System.out.println("lockInterruptiblyDemo start");
                TimeUnit.SECONDS.sleep(2);
                System.out.println("lockInterruptiblyDemo end");
            } catch (InterruptedException e) {
                System.out.println(e);
                thread1.interrupt();
            } finally {
                lock.unlock();
            }
        });
        thread2.start();

        TimeUnit.SECONDS.sleep(2);
        thread2.interrupt();

        thread1.join();
        thread2.join();
    }

    /**
     * 使用tryLock进行尝试锁定，不管锁定与否，方法都将继续执行
     * 可以根据tryLock的返回值来判定是否锁定
     * 也可以指定tryLock的时间，由于tryLock(time)抛出异常，所以要注意unclock的处理，必须放到finally中
     */
    public void tryLockDemo() throws InterruptedException {
        Lock lock = new ReentrantLock();
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Get lock, sleep...");
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            System.out.println("Release lock.");
        }).start();

        Thread thread = new Thread(() -> {
            boolean isLock = false;
            try {
                System.out.println("tryLockDemo start.");
                // isLock = lock.tryLock();
                isLock = lock.tryLock(2, TimeUnit.SECONDS);
                System.out.println("tryLockDemo lock state = " + isLock);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (isLock) {
                    lock.unlock();
                }
            }
        });

        thread.start();
        thread.join();
    }

    /**
     * lock()进行锁定，lock()方法后面必须紧跟try{}finally{},避免死锁
     */
    public void lockDemo() throws InterruptedException {
        Lock lock = new ReentrantLock();
        new Thread(() -> {
            lock.lock();
            try {
                for (int i = 0; i < 5; i++) {
                    System.out.println("lockDemo-" + i);
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }).start();

        Thread thread = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("ScrambleLock get lock, sleep...");
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            System.out.println("ScrambleLock release lock.");
        });

        thread.start();
        thread.join();
    }
}
