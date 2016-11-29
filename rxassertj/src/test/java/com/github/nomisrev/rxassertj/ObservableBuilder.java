package com.github.nomisrev.rxassertj;


import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

import java.util.Set;
import java.util.concurrent.Callable;

import static org.assertj.core.util.Sets.newLinkedHashSet;

public final class ObservableBuilder {

    static Set<String> JEDIS = newLinkedHashSet("Luke", "Yoda", "Obiwan");

    static Observable<String> getJediStringEmittingObservable() {
        return Observable.from(JEDIS);
    }

    public Observable<Long> doSomeRxing() {
        return Observable.just(1L);
    }

    public Completable doSomeLongRxing() {
        return Completable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(2500);
                return null;
            }
        }).subscribeOn(Schedulers.io());
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
