/*
 * Copyright 2020-2020. the original qiuhaifeng .
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

package com.helper.dp.singleton;

import junit.framework.TestCase;

import java.util.Objects;

/**
 * lazy loading
 * 也称懒汉式
 * 虽然达到了按需初始化的目的，但却带来线程不安全的问题
 * 可以通过synchronized解决，但也带来效率下降
 *
 * @author sz_qiuhf@163.com
 * @since 2020-06-19
 **/
public class Singleton05 {
    private static Singleton05 INSTANCE;

    private Singleton05() {
    }

    public static synchronized Singleton05 getInstance() {
        if (Objects.isNull(INSTANCE)) {
            // 妄图通过减小同步代码块的方式提高效率，然而不可行
            synchronized (Singleton05.class) {
                DealWith.doSomething(10L);
                INSTANCE = new Singleton05();
            }
        }
        return INSTANCE;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> TestCase.assertEquals(Singleton05.getInstance().hashCode(), Singleton05.getInstance().hashCode())).start();
        }
    }
}
