package com.thebytecloud.main;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static String greekLetters[] = {
            "\u03b1",
            "\u03b2",
            "\u03b3",
            "\u03b4",
            "\u03b5",
            "\u03b6",
            "\u03b7",
            "\u03b8",
            "\u03b9",
            "\u03ba",
            "\u03bb",
            "\u03bc",
            "\u03bd",
            "\u03be",
            "\u03bf",
            "\u03c0",
            "\u03c1",
            "\u03c3",
            "\u03c4",
            "\u03c5",
            "\u03c6",
            "\u03c7",
            "\u03c8",
            "\u03c9"
    };

    public static void main(String[] args) {
        Observable.fromArray(greekLetters)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        log.info("onSubscribe");
                    }

                    @Override
                    public void onNext(String nextLetter) {
                        log.info("onNext - {}", nextLetter);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        log.info("onError - {}", throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        log.info("onComplete");
                    }
                });

        log.info("Cardinality Example =======================================");
        log.info("Single Observer Example =======================================");
        Single<String> firstGreekLetterOnly = Observable.fromArray(greekLetters)
                .first("?");

        firstGreekLetterOnly.subscribe(new SingleObserver<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onSuccess(String s) {
                log.info("onSuccess - {}", s);
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

        log.info("MayBe Observer Example =======================================");
        Maybe<String> maybeGreekLetterOneEvent = Observable.fromArray(greekLetters)
                .first("?")
                .filter(nextLetter -> nextLetter.equals("\u03b1"));

        Maybe<String> maybeGreekLetterNoEvents = Observable.fromArray(greekLetters)
                .first("?")
                .filter(nextLetter -> !nextLetter.equals("\u03b1"));


        final MaybeObserver<String> maybeObserver = new MaybeObserver<String>() {

            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onSuccess(String s) {
                log.info("onSuccess - {}", s);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {
                log.info("maybeObserver - onCompleted");
            }
        };

        maybeGreekLetterOneEvent.subscribe(maybeObserver);
        maybeGreekLetterNoEvents.subscribe(maybeObserver);


        log.info("Completable Observer Example =======================================");
        final Completable completableObserver = Observable.fromArray(greekLetters).ignoreElements();
        completableObserver.subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onComplete() {
                log.info("completableObserver - onCompleted");
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

    }
}
