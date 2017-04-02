package com.github.nomisrev.rx2assertj;


import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.assertj.core.util.Sets;

import java.util.*;
import java.util.concurrent.Callable;

import static java.util.Collections.singletonList;
import static org.assertj.core.util.Sets.newLinkedHashSet;

public final class ObservableBuilder {

    static final String LUKE = "Luke";

    static final String YODA = "Yoda";

    static final String OBIWAN = "Obiwan";

    static Set<String> JEDIS = newLinkedHashSet(LUKE, YODA, OBIWAN);

    static <T> Observable<T> getObservableWithTestException(Throwable testException, T... t) {
        return Observable.<T>fromArray(t).concatWith(Observable.<T>error(testException));
    }

    static Observable<String> getJediStringEmittingObservable() {
        return Observable.fromIterable(JEDIS);
    }

    static Observable<List<String>> getJediListEmittingObservable(){
        return Observable.just(singletonList(LUKE), singletonList(YODA), singletonList(OBIWAN));
    }

    static Observable<String[]> getJediArrayEmittingObservable(){
        return Observable.just(new String[]{LUKE},new String[]{YODA}, new String[]{OBIWAN});
    }

    static Observable<Set<String>> getJediSetEmittingObservable(){
        return Observable.just(newLinkedHashSet(LUKE), newLinkedHashSet(YODA),(Set<String>) newLinkedHashSet(OBIWAN));
    }

    public Observable<Long> doSomeRxing() {
        return Observable.just(1L);
    }

    public Flowable<Integer> getFlowableIntegers() {
        Integer[] integers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        return Flowable.fromArray(integers);
    }

    public Maybe<Integer> getEmptyMaybe() {
        return Maybe.empty();
    }

    public Completable doSomeLongRxing() {
        return Completable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        });
    }

    public Single<Long> getSomeSingleValue(final int n) {
        return Single.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return ObservableBuilder.this.fibonacci(n);
            }
        });
    }

    public long fibonacci(final int n) throws InterruptedException {
        return n <= 1 ? n : fibonacci(n - 1) + fibonacci(n - 2);
    }

}