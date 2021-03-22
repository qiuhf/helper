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

package com.hleper.juc.container;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition类的await()、signal()和signalAll()，一般是配合Lock一起使用，是显式的线程间协调同步操作类。
 * 每个Lock中可以有多个Condition，如notEmpty、notFull等。这些方法都是在某个具体的Condition条件队列中调用，
 * 唤醒的时候，使用对应的Condition来唤醒一个或者多个等待的线程。和wait()、notify()类似，使用这些方法时也需要
 * 先通过Lock获取锁，await()方法同样会释放锁，并挂起当前线程，等待被通知唤醒去重新竞争锁。
 *
 * @author sz_qiuhf@163.com
 * @since 2021-03-23
 **/
public class ContainerByCondition<E> {
    private final int maxSize = 16;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition producer = lock.newCondition();
    private final Condition consumer = lock.newCondition();
    private LinkedList<E> queue = new LinkedList<>();

    public static void main(String[] args) throws InterruptedException {
        ContainerByCondition<String> container = new ContainerByCondition<>();
        // 启动消费者
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    try {
                        System.out.println(container.take() + " > " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "consumer-" + i).start();
        }

        TimeUnit.SECONDS.sleep(2);

        // 启动生产者
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 25; j++) {
                    try {
                        container.put(Thread.currentThread().getName() + " > " + j);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "producer-" + i).start();
        }

    }

    public void put(E e) throws InterruptedException {
        this.lock.lock();
        try {
            while (this.queue.size() == this.maxSize) {
                this.producer.await();
            }

            this.queue.add(e);
            // 通知消费者线程进行消费
            this.consumer.signalAll();
        } finally {
            this.lock.unlock();
        }
    }

    public E take() throws InterruptedException {
        E e = null;
        this.lock.lock();
        try {
            while (this.queue.isEmpty()) {
                this.consumer.await();
            }

            e = this.queue.removeFirst();
            // 通知生产者进行生产
            this.producer.signalAll();
        } finally {
            this.lock.unlock();
        }

        return e;
    }
}
