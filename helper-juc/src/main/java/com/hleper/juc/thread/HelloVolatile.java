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

import java.util.concurrent.TimeUnit;

/**
 * volatile可以保证可见性读写和有序性(防止指令重排)
 *
 * @author sz_qiuhf@163.com
 * @since 2021-03-21
 **/
public class HelloVolatile {
    /**
     * 对比一下有无volatile的情况下，整个程序运行结果的区别
     */
    private volatile boolean running = true;

    public static void main(String[] args) throws InterruptedException {
        HelloVolatile helloVolatile = new HelloVolatile();
        Thread thread0 = new Thread(helloVolatile::doSomeThing, "thread0");
        thread0.start();

        TimeUnit.MILLISECONDS.sleep(2);
        helloVolatile.running = false;
        thread0.join();

    }

    public void doSomeThing() {
        System.out.println("begin");
        while (running) {

        }
        System.out.println("end");
    }
}
