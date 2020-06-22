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
 * 饿汉式
 * 类加载到内存后，就实例化一个单例，JVM保证线程安全
 * 简单实用，推荐使用！
 * 唯一缺点：不管用到与否，类装载时就完成实例化
 * Class.forName("")
 * （话说你不用的，你装载它干啥）
 *
 * @author sz_qiuhf@163.com
 * @since 2020-06-19
 **/
public class Singleton01 {
    private static final Singleton01 INSTANCE = new Singleton01();

    private Singleton01() {
    }

    public static Singleton01 getInstance() {
        DealWith.handle(10L);
        return INSTANCE;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            new Thread(() -> TestCase.assertEquals(Singleton01.getInstance().hashCode(), Singleton01.getInstance().hashCode())).start();
        }
    }
}
