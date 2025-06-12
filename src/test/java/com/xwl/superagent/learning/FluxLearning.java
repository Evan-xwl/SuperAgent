package com.xwl.superagent.learning;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * @author ruoling
 * @date 2025/6/11 19:11:50
 * @description
 */
public class FluxLearning {
    @Test
    public void simpleProduceAndConsumeTest() throws InterruptedException {
        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
        Flux<String> flux = sink.asFlux();
        flux.subscribe(val -> System.out.println("consume val: " + val));
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                String msg = "Message-" + i;
                System.out.println("Sending: " + msg);
                sink.tryEmitNext(msg);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.join();
    }

    @Test
    public void fluxBackProduceAndConsumeTest() throws InterruptedException {

        System.out.println("=== 不使用背压（默认缓冲） ===");
        runWithBackpressureStrategy(() -> Sinks.many().multicast().onBackpressureBuffer());

        Thread.sleep(5000);
    }

    private static void runWithBackpressureStrategy(SinkSupplier<Integer> sinkFactory) throws InterruptedException {

        // 消费者：每次处理需要 500ms
//        sink.asFlux()
//                .doOnNext(data -> {
//                    try {
//                        Thread.sleep(5000); // 模拟慢速消费
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(Thread.currentThread().getName() + " consumed: " + data);
//                })
//                .subscribe();
        SinkSupplier<Integer> factory = () -> Sinks.many().multicast().onBackpressureBuffer();
        Sinks.Many<Integer> sink = factory.get();
        Flux<Integer> flux = sink.asFlux();
        // 异步订阅消费者
        flux.subscribe(data -> {
            System.out.println("Consumed: " + data);
        });

        // 生产者：每 100ms 发送一个数，共发 20 个
        for (int i = 0; i < 20; i++) {
            int number = i;
            Thread.sleep(1000);
            new Thread(() -> {
                boolean result = sink.tryEmitNext(number).isSuccess();
                System.out.println(Thread.currentThread().getName() + " produced: " + number + " (emit success? " + result + ")");
                try {
                    Thread.sleep(100); // 控制发送频率
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @FunctionalInterface
    interface SinkSupplier<T> {
        Sinks.Many<T> get();
    }
}
