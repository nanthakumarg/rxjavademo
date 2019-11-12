package com.thebytecloud.main;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class ZipMain {

    private static Logger log = LoggerFactory.getLogger(ZipMain.class);

    public static String[] alphabet1 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    public static String[] alphabet2 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    public static void main(String[] args) {

        Observable<String> a1Observable = Observable.fromArray(alphabet1);
        Observable<String> a2Observable = Observable.fromArray(alphabet2);

        AtomicInteger counter = new AtomicInteger();

        final Observable<LetterPair> zip = Observable.zip(
                a1Observable,
                a2Observable,
                (letter1, letter2) -> {

                    if (counter.incrementAndGet() >= 10) {
                        throw new IllegalStateException("Got Exception");
                    }
                    return new LetterPair(letter1, letter2);
                }
        );

        zip.onErrorResumeNext(Observable.just(new LetterPair("?", "?")))
                .subscribe(new Observer<LetterPair>() {
                    private Disposable disposable;
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        this.disposable = disposable;
                        log.info("onSubscribe");
                    }

                    @Override
                    public void onNext(LetterPair letterPair) {
                        log.info("onNext - {},{} ", letterPair.getLetter1(), letterPair.getLetter2());
                        if(counter.get() >= 3) {
                            disposable.dispose();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        log.info("onError - {} ", throwable.getMessage());
                        log.error(throwable.getMessage(), throwable);
                    }

                    @Override
                    public void onComplete() {
                        log.info("onComplete");
                    }
                });
    }

    private static class LetterPair {

        private String letter1, letter2;

        public LetterPair(String letter1, String letter2) {
            this.letter1 = letter1;
            this.letter2 = letter2;
        }

        public String getLetter1() {
            return letter1;
        }

        public String getLetter2() {
            return letter2;
        }

        @Override
        public String toString() {
            return "LetterPair{" +
                    "letter1='" + letter1 + '\'' +
                    ", letter2='" + letter2 + '\'' +
                    '}';
        }
    }
}
