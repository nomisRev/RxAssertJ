package be.vergauwen.simon;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;

public final class Rx2AssertJ {
    private Rx2AssertJ() {
    }


    public static <T> TestObserverAssert<T> assertThat(final TestObserver<T> subscriber) {
        return new TestObserverAssert<>(subscriber);
    }

    public static <T> TestSubscriberAssert<T> assertThat(final TestSubscriber<T> observer) {
        return new TestSubscriberAssert<>(observer);
    }

    public static <T> TestObserverAssert<T> assertThatASubscriberTo(final Observable<T> observable) {
        TestObserver<T> subscriber = new TestObserver<>();
        observable.subscribe(subscriber);
        return new TestObserverAssert<>(subscriber);
    }

    public static <T> TestObserverAssert<T> assertThatASubscriberTo(final Completable completable) {
        TestObserver<T> subscriber = new TestObserver<>();
        completable.subscribe(subscriber);
        return new TestObserverAssert<>(subscriber);
    }

    public static <T> TestObserverAssert<T> assertThatASubscriberTo(final Single<T> single) {
        TestObserver<T> subscriber = new TestObserver<>();
        single.subscribe(subscriber);
        return new TestObserverAssert<>(subscriber);
    }

    public static <T> TestObserverAssert<T> assertThatASubscriberTo(final Maybe<T> maybe) {
        TestObserver<T> subscriber = new TestObserver<>();
        maybe.subscribe(subscriber);
        return new TestObserverAssert<>(subscriber);
    }

    public static <T> TestSubscriberAssert<T> assertThatASubscriberTo(final Flowable<T> flowable) {
        TestSubscriber<T> subscriber = new TestSubscriber<>();
        flowable.subscribe(subscriber);
        return new TestSubscriberAssert<>(subscriber);
    }

   static class TestObserverAssert<T> extends  AbstractTestConsumerAssert<T,TestObserver<T>> {
       public TestObserverAssert(final TestObserver<T> actual) {
           super(actual);
       }

       @Override
       public AbstractTestConsumerAssert<T, TestObserver<T>> isNotSubscribed() {
           actual.assertNotSubscribed();
           return this;
       }

       @Override
       public AbstractTestConsumerAssert<T, TestObserver<T>> isSubscribed() {
           actual.assertSubscribed();
           return this;
       }
   }

    static class TestSubscriberAssert<T> extends  AbstractTestConsumerAssert<T,TestSubscriber<T>> {
        public TestSubscriberAssert(final TestSubscriber<T> actual) {
            super(actual);
        }

        @Override
        public AbstractTestConsumerAssert<T, TestSubscriber<T>> isNotSubscribed() {
            actual.assertNotSubscribed();
            return this;
        }

        @Override
        public AbstractTestConsumerAssert<T, TestSubscriber<T>> isSubscribed() {
            actual.assertSubscribed();
            return this;
        }
    }
}