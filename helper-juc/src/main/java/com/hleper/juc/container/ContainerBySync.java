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

/**
 * wait()、notify()和notifyAll()一般是跟synchronized配合一起使用，这些方法都是Object类提供的。当线程重wait()状态下被唤醒，
 * wait()在被唤醒后还需要重新去获取锁，此时它重新请求锁时并没有具备任何特殊的优先级，要与任何其他尝试进入同步代码块的线程一起去
 * 竞争获取锁，如果获取锁失败的话，会继续停留在当前的wait()方法状态下。所以wait方法继续执行时，一般会先通过一个条件判断，所以
 * 一般是在一个循环中去调用wait()方法
 *
 * @author sz_qiuhf@163.com
 * @since 2021-03-23
 **/
public class ContainerBySync<E> {
    private final int maxSize = 16;
    private LinkedList<E> queue = new LinkedList<>();

    public static void main(String[] args) throws InterruptedException {
        ContainerBySync<String> container = new ContainerBySync<>();
        // 启动消费者
        for (int i = 0; i < 25; i++) {
            new Thread(() -> {
                for (int j = 0; j < 2; j++) {
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

    public synchronized void put(E e) throws InterruptedException {
        while (this.queue.size() == this.maxSize) {
            this.wait();
        }

        this.queue.add(e);
        // 通知消费者线程进行消费
        this.notifyAll();
    }

    public synchronized E take() throws InterruptedException {
        while (this.queue.isEmpty()) {
            this.wait();
        }

        E e = this.queue.removeFirst();
        // 通知生产者进行生产
        this.notifyAll();

        return e;
    }
}
