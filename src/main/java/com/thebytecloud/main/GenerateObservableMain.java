package com.thebytecloud.main;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class GenerateObservableMain {

    private final static Logger log = LoggerFactory.getLogger(GenerateObservableMain.class);

    public static String[] alphabet1 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    public static void main(String[] args) {
        Observable<String> seq = makeObservable(alphabet1, 5);
        seq.subscribe(new Observer<String>() {
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
                log.info(throwable.getMessage(), throwable);
            }

            @Override
            public void onComplete() {
                log.info("onComplete");
            }
        });
    }

    public static Observable<String> makeObservable(String[] array, int maxCount){
        return
                Observable.generate(
                () -> new SequenceState(array, maxCount),
                (state, emitter) -> {
                    if(state.getCount() >= state.getMaxCount()){
                        emitter.onComplete();
                        return;
                    }
                    state.incrementCount();
                    emitter.onNext(state.getCurrentValue());
                    state.generateNextValue();
                });
    }

    public static class SequenceState {
        private String[] array;
        private int maxCount, count;
        private Random random = new Random();
        private String currentValue;

        public SequenceState(String[] array, int maxCount) {
            this.array = array;
            this.maxCount = maxCount;
            generateNextValue();
        }

        public String getCurrentValue(){
            return this.currentValue;
        }

        public int getMaxCount(){
            return maxCount;
        }

        public int getCount(){
            return count;
        }

        public void incrementCount(){
            count++;
        }

        public void generateNextValue() {
            this.currentValue = this.array[random.nextInt(this.array.length)];
        }

    }
}
