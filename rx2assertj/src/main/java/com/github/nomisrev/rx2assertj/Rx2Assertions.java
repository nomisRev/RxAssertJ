package com.github.nomisrev.rx2assertj;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;

public final class Rx2Assertions {

    public static <T> TestObserverAssert<T> assertThat(final TestObserver<T> subscriber) {
        return new TestObserverAssert<>(subscriber);
    }

    public static <T> TestSubscriberAssert<T> assertThat(final TestSubscriber<T> observer) {
        return new TestSubscriberAssert<>(observer);
    }

    public static <T> TestObserverAssert<T> assertThatSubscriberTo(final Observable<T> observable) {
        TestObserver<T> subscriber = new TestObserver<>();
        observable.subscribe(subscriber);
        return new TestObserverAssert<>(subscriber);
    }

    public static <T> TestObserverAssert<T> assertThat(final Observable<T> observable) {
        return assertThatSubscriberTo(observable);
    }

    public static <T> TestObserverAssert<T> assertThatSubscriberTo(final Completable completable) {
        TestObserver<T> subscriber = new TestObserver<>();
        completable.subscribe(subscriber);
        return new TestObserverAssert<>(subscriber);
    }

    public static <T> TestObserverAssert<T> assertThat(final Completable completable) {
        return assertThatSubscriberTo(completable);
    }

    public static <T> TestObserverAssert<T> assertThatSubscriberTo(final Single<T> single) {
        TestObserver<T> subscriber = new TestObserver<>();
        single.subscribe(subscriber);
        return new TestObserverAssert<>(subscriber);
    }

    public static <T> TestObserverAssert<T> assertThat(final Single<T> single) {
        return assertThatSubscriberTo(single);
    }

    public static <T> TestObserverAssert<T> assertThatSubscriberTo(final Maybe<T> maybe) {
        TestObserver<T> subscriber = new TestObserver<>();
        maybe.subscribe(subscriber);
        return new TestObserverAssert<>(subscriber);
    }

    public static <T> TestObserverAssert<T> assertThat(final Maybe<T> maybe) {
        return assertThatSubscriberTo(maybe);
    }

    public static <T> TestSubscriberAssert<T> assertThatSubscriberTo(final Flowable<T> flowable) {
        TestSubscriber<T> subscriber = new TestSubscriber<>();
        flowable.subscribe(subscriber);
        return new TestSubscriberAssert<>(subscriber);
    }

    public static <T> TestSubscriberAssert<T> assertThat(final Flowable<T> flowable) {
        return assertThatSubscriberTo(flowable);
    }

   public static class TestObserverAssert<T> extends  AbstractTestConsumerAssert<T,TestObserver<T>> {
       public TestObserverAssert(final TestObserver<T> actual) {
           super(actual);
       }

       /**
        * {@inheritDoc}
        */
       @Override
       public AbstractTestConsumerAssert<T, TestObserver<T>> isNotSubscribed() {
           actual.assertNotSubscribed();
           return this;
       }

       /**
        * {@inheritDoc}
        */
       @Override
       public AbstractTestConsumerAssert<T, TestObserver<T>> isSubscribed() {
           actual.assertSubscribed();
           return this;
       }
   }

    public static class TestSubscriberAssert<T> extends  AbstractTestConsumerAssert<T,TestSubscriber<T>> {
        public TestSubscriberAssert(final TestSubscriber<T> actual) {
            super(actual);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public AbstractTestConsumerAssert<T, TestSubscriber<T>> isNotSubscribed() {
            actual.assertNotSubscribed();
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public AbstractTestConsumerAssert<T, TestSubscriber<T>> isSubscribed() {
            actual.assertSubscribed();
            return this;
        }
    }


    private Rx2Assertions() {
        throw new AssertionError();
    }
}