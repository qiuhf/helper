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

/**
 * synchronized锁升级过程：
 * 1. markword 记录这个线程ID（偏向锁）；
 * 2. 如果线程争用：升级为自旋锁（自旋锁）；
 * 3. 10次以后，升级为重量级锁 - 向系统申请锁（重量级锁，可重入）
 *
 * @author sz_qiuhf@163.com
 * @since 2021-03-18
 **/
public class HelloSynchronized {
    /**
     * 必须要加final, 防止object对象锁改变
     */
    private static final Object object = new Object();
    private static int count = 0;

    public void objectLock() {
        // 任何线程要执行下面的代码，都必须要先拿到object锁
        synchronized (object) {
            count++;
            System.out.println(Thread.currentThread().getName() + " count = " + count);
        }
    }

    public void thisLock1() {
        // 任何线程要执行下面的代码，都必须要先拿到this锁
        synchronized (this) {
            count++;
            System.out.println(Thread.currentThread().getName() + " count = " + count);
        }
    }

    public void thisLock3() {
        // 考虑一下这里写synchronized(this)是否可以？
        synchronized (HelloSynchronized.class) {
            decrease();
        }
    }

    private void decrease() {
        count--;
    }

    /**
     * 等同于在方法的代码执行时要synchronized(this)
     */
    public synchronized void thisLock2() {
        count++;
        System.out.println(Thread.currentThread().getName() + " count = " + count);
    }
}
