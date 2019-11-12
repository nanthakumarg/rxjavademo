package com.thebytecloud.main;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class BackpressureMain {

    private static Logger log = LoggerFactory.getLogger(BackpressureMain.class);

    public static void main(String[] args) throws InterruptedException {

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        final Flowable<Integer> rangeOfNumbers = Flowable.range(1, 1_000_000_000)
                .repeat()
                .doOnNext(nextInt -> log.info("emiting int {}", nextInt))
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread(), false, 3);

        final FlowableSubscriber<Integer> flowableSubscriber = new FlowableSubscriber<Integer>() {

            private AtomicInteger counter = new AtomicInteger();
            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                log.info("onSubscribe");
                this.subscription = subscription;
            }

            @Override
            public void onNext(Integer integer) {
                log.info("onNext - {} ", integer);
                /*try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

                /*if ((counter.incrementAndGet() % 3) == 0) {
                    subscription.request(3);
                }

                if ((counter.get() % 6) == 0) {
                    countDownLatch.countDown();
                }*/

            }

            @Override
            public void onError(Throwable throwable) {
                log.info("onError - {}", throwable.getMessage());
                log.error(throwable.getMessage(), throwable);
            }

            @Override
            public void onComplete() {
                log.info("onComplete");
                countDownLatch.countDown();
            }
        };

        rangeOfNumbers.subscribe(flowableSubscriber);


        countDownLatch.await();

    }
}
