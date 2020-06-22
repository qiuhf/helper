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
 * 不仅可以解决线程同步，还可以防止反序列化。
 *
 * @author sz_qiuhf@163.com
 * @since 2020-06-19
 **/
public enum Singleton08 {
    INSTANCE;

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> TestCase.assertEquals(Singleton08.INSTANCE, Singleton08.INSTANCE)).start();
        }
    }
}
