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

import java.util.concurrent.Exchanger;

/**
 * @author sz_qiuhf@163.com
 * @since 2021-03-23
 **/
public class ExchangerDemo {
    public static void main(String[] args) {
        Exchanger<Integer> exchanger = new Exchanger<>();
        new Thread(() -> {
            Integer result = 100;
            System.out.println(Thread.currentThread().getName() + ", result = " + result);
            try {
                result = exchanger.exchange(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ", result = " + result);
        }, "Thread-1").start();

        new Thread(() -> {
            Integer result = 20;
            System.out.println(Thread.currentThread().getName() + ", result = " + result);
            try {
                result = exchanger.exchange(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ", result = " + result);
        }, "Thread-2").start();
    }
}
