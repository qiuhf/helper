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

/**
 * 跟Singleton1是一个意思
 *
 * @author sz_qiuhf@163.com
 * @since 2020-06-19
 **/
public class Singleton02 {
    private static Singleton02 INSTANCE;

    static {
        INSTANCE = new Singleton02();
    }

    private Singleton02() {
    }

    public static Singleton02 getInstance() {
        DealWith.doSomething(1L);
        return INSTANCE;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            new Thread(() -> TestCase.assertEquals(Singleton02.getInstance().hashCode(), Singleton02.getInstance().hashCode())).start();
        }
    }
}
