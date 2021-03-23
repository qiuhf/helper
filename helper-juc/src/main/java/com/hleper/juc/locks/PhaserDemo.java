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

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * Phaser
 *
 * @author sz_qiuhf@163.com
 * @since 2021-03-23
 **/
public class PhaserDemo {
    private static MarriagePhaser phaser = new MarriagePhaser();

    public static void main(String[] args) {
        int sum = 10;
        phaser.bulkRegister(sum);
        for (int i = 2; i < sum; i++) {
            new Thread(new Guest("Guest-" + (i - 1))).start();
        }

        new Thread(new Guest("Bridegroom")).start();
        new Thread(new Guest("Bride")).start();
    }

    private static class MarriagePhaser extends Phaser {
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            switch (phase) {
                case 0:
                    System.out.println("Everyone is here！" + registeredParties);
                    System.out.println();
                    return false;
                case 1:
                    System.out.println("everyone is full！" + registeredParties);
                    System.out.println();
                    return false;
                case 2:
                    System.out.println("Everybody's gone！" + registeredParties);
                    System.out.println();
                    return false;
                case 3:
                    System.out.println("The wedding is over！Entering the bridal chamber！" + registeredParties);
                    return true;
                default:
                    return true;
            }
        }
    }

    private static class Guest implements Runnable {
        private static final Random RANDOM = new Random();
        private final String name;

        public Guest(String name) {
            this.name = name;
        }

        private void arrive() {
            doSomeThing();
            System.out.printf("%s arrived！\n", this.name);
            phaser.arriveAndAwaitAdvance();
        }

        private void eat() {
            doSomeThing();
            System.out.printf("%s is full！\n", this.name);
            phaser.arriveAndAwaitAdvance();
        }

        private void leave() {
            doSomeThing();
            System.out.printf("%s is gone！\n", this.name);
            phaser.arriveAndAwaitAdvance();
        }

        private void bridalChamber() {
            if ("Bridegroom".equals(this.name) || "Bride".equals(this.name)) {
                System.out.printf("%s bridal chamber.\n", this.name);
                doSomeThing();
                phaser.arriveAndAwaitAdvance();
            } else {
                phaser.arriveAndDeregister();
            }
        }


        @Override
        public void run() {
            arrive();
            eat();
            leave();
            bridalChamber();
        }


        private void doSomeThing() {
            try {
                TimeUnit.MILLISECONDS.sleep((RANDOM.nextInt(1000)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
