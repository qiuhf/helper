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
import java.util.concurrent.locks.LockSupport;

/**
 * @author sz_qiuhf@163.com
 * @since 2021-03-23
 **/
public class LockSupportDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() ->
        {
            for (int i = 0; i < 10; i++) {
                if (i == 5) {
                    LockSupport.park();
                }
                System.out.println(Thread.currentThread().getName() + ", index = " + i);
                try {
                    TimeUnit.MICROSECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread");

        thread.start();

        //  LockSupport.unpark(thread);
        TimeUnit.SECONDS.sleep(1);
        System.out.println(thread.getState());

        LockSupport.unpark(thread);
        System.out.println(thread.getState());

        thread.join();
        System.out.println(thread.getState());
    }
}
