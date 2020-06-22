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
 * 静态内部类方式
 * JVM保证单例
 * 加载外部类时不会加载内部类，这样可以实现懒加载
 *
 * @author sz_qiuhf@163.com
 * @since 2020-06-19
 **/
public class Singleton07 {
    private Singleton07() {
    }

    public static InnerSingleton getInstance() {
        return InnerSingleton.INSTANCE;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> TestCase.assertEquals(Singleton07.getInstance().hashCode(), Singleton07.getInstance().hashCode())).start();
        }
    }

    private static class InnerSingleton {
        private static final InnerSingleton INSTANCE = new InnerSingleton();
    }
}
