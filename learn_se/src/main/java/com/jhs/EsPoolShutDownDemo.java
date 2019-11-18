package com.jhs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *多任务并发时，该怎样判断线程池中的任务都已经执行完毕？
 */

public class EsPoolShutDownDemo {

    public static void main(String[] args) {

        //开启线程池，corePoolSize为10 。
        ExecutorService esPool = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10000000; i++) {

            final int num = i;

            Runnable task = new Runnable() {

                public void run() {

                    try {

                        //线程执行任务
                        System.out.println(Thread.currentThread().getName()+num);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            esPool.submit(task);

        }

        //停止线程池

        esPool.shutdown();

        while (true) {

            //只有当线程池中所有线程完成任务时才会返回true，并且需要先调用线程池的shutdown方法或者shutdownNow方法。

            if (esPool.isTerminated()) {

                System.out.println("All finished");
                break;

            }

        }

    }

}
