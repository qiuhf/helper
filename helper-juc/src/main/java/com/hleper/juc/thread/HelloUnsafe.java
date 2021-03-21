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

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Unsafe类提供了类似C++手动管理内存的能力
 *
 * @author sz_qiuhf@163.com
 * @since 2021-03-21
 **/
public class HelloUnsafe {
    public static void main(String[] args) throws Exception {
        HelloUnsafe helloUnsafe = new HelloUnsafe();

        // 如果不是系统域下的类调用getUnsafe()方法将抛出SecurityException异常.
        // eg Exception in thread "main" java.lang.SecurityException: Unsafe
        // Unsafe unsafe = Unsafe.getUnsafe();
        Unsafe unsafe = helloUnsafe.getReflection();
        Xo instance = (Xo) unsafe.allocateInstance(Xo.class);

        instance.index = 9;
        System.out.println(instance.index);
        instance.dealWith();
    }


    private Unsafe getReflection() throws Exception {
        Class<?> aClass = Class.forName("sun.misc.Unsafe");
        Field theUnsafe = aClass.getDeclaredField("theUnsafe");
        boolean accessible = theUnsafe.isAccessible();
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        theUnsafe.setAccessible(accessible);
        return unsafe;
    }

    static class Xo {
        int index = 0;

        public void dealWith() {
            System.out.println("dealWith ...");
        }
    }
}
