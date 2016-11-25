package com.github.nomisrev.rxassertj;

import org.assertj.core.api.AbstractObjectAssert;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.observables.BlockingObservable;
import rx.observers.TestSubscriber;

public final class RxAssertions {

    public static <T> TestSubscriberAssert<T> assertThat(final TestSubscriber<T> subscriber) {
        return new TestSubscriberAssert<T>(subscriber);
    }

    public static <T> TestSubscriberAssert<T> assertThatSubscriberTo(final Observable<T> observable) {
        TestSubscriber<T> subscriber = new TestSubscriber<T>();
        observable.subscribe(subscriber);
        return new TestSubscriberAssert<T>(subscriber);
    }

    public static <T> TestSubscriberAssert<T> assertThat(final Observable<T> observable) {
        return assertThatSubscriberTo(observable);
    }

    public static <T> TestSubscriberAssert<T> assertThatSubscriberTo(final BlockingObservable<T> observable) {
        TestSubscriber<T> subscriber = new TestSubscriber<T>();
        observable.subscribe(subscriber);
        return new TestSubscriberAssert<T>(subscriber);
    }

    public static <T> TestSubscriberAssert<T> assertThat(final BlockingObservable<T> observable) {
        return assertThatSubscriberTo(observable);
    }

    public static <T> TestSubscriberAssert<T> assertThatSubscriberTo(final Completable completable) {
        TestSubscriber<T> subscriber = new TestSubscriber<T>();
        completable.subscribe(subscriber);
        return new TestSubscriberAssert<T>(subscriber);
    }

    public static <T> TestSubscriberAssert<T> assertThat(final Completable completable) {
        return assertThatSubscriberTo(completable);
    }

    public static <T> TestSubscriberAssert<T> assertThatSubscriberTo(final Single<T> single) {
        TestSubscriber<T> subscriber = new TestSubscriber<T>();
        single.subscribe(subscriber);
        TestSubscriberAssert<T> testSubscriberAssert = new TestSubscriberAssert<T>(subscriber);
        testSubscriberAssert.hasValueCount(1);
        return testSubscriberAssert;
    }

    public static <T> TestSubscriberAssert<T> assertThat(final Single<T> single) {
        return assertThatSubscriberTo(single);
    }


    public static class TestSubscriberAssert<T> extends AbstractObjectAssert<TestSubscriberAssert<T>, TestSubscriber<T>> {

        public TestSubscriberAssert(final TestSubscriber<T> actual) {
            super(actual, TestSubscriberAssert.class);
        }

        /**
         * Asserts that there is exactly one completion event.
         *
         * @throws AssertionError if there were zero, or more than one, onCompleted events
         */
        public TestSubscriberAssert<T> isCompleted() {
            actual.assertCompleted();
            return this;
        }

        /**
         * Asserts that there is exactly one completion event.
         *
         * @throws AssertionError if there were zero, or more than one, onCompleted events
         */
        public TestSubscriberAssert<T> completes() {
            return this.isCompleted();
        }

        /**
         * Asserts that there is no completion event.
         *
         * @throws AssertionError if there were one or more than one onCompleted events
         */
        public TestSubscriberAssert<T> isNotCompleted() {
            actual.assertNotCompleted();
            return this;
        }

        /**
         * Asserts that this {@code Subscriber} has received no {@code onError} notifications.
         *
         * @throws AssertionError if this {@code Subscriber} has received one or more {@code onError} notifications
         */
        public TestSubscriberAssert<T> hasNoErrors() {
            actual.assertNoErrors();
            return this;
        }

        /**
         * Asserts that this {@code Subscriber} has received no {@code onError} notifications.
         *
         * @throws AssertionError if this {@code Subscriber} has received one or more {@code onError} notifications
         */
        public TestSubscriberAssert<T> withoutErrors() {
            return this.hasNoErrors();
        }

        /**
         * Asserts that there is exactly one error event which is a subclass of the given class.
         *
         * @param clazz the class to check the error against.
         * @throws AssertionError if there were zero, or more than one, onError events, or if the single onError event did not carry an error of a subclass of the given class
         */
        public TestSubscriberAssert<T> hasError(final Class<? extends Throwable> clazz) {
            actual.assertError(clazz);
            return this;
        }

        /**
         * Asserts that this {@code Subscriber} is unsubscribed.
         *
         * @throws AssertionError if this {@code Subscriber} is not unsubscribed
         */
        public TestSubscriberAssert<T> isUnsubscribed() {
            actual.assertUnsubscribed();
            return this;
        }

        /**
         * Asserts that the received onNext events, in order, are the specified items.
         *
         * @param values the items to check
         * @throws AssertionError if the items emitted do not exactly match those specified by {@code values}
         */
        public TestSubscriberAssert<T> hasValues(final T... values) {
            actual.assertValues(values);
            return this;
        }

        public TestSubscriberAssert<T> hasValueCount(final int count) {
            actual.assertValueCount(count);
            return this;
        }

        /**
         * Asserts that there are no onNext events received.
         *
         * @throws AssertionError if there were any onNext events
         */
        public TestSubscriberAssert<T> receivedNothing() {
            actual.assertNoValues();
            return this;
        }

        /**
         * Asserts that there are no onNext events received.
         *
         * @throws AssertionError if there were any onNext events
         */
        public TestSubscriberAssert<T> emitsNothing() {
            return this.receivedNothing();
        }

        /**
         * Assert that all emitted items meet a {@link Condition}.
         * @param condition the AssertJ {@link Condition} to check
         */
        public TestSubscriberAssert<T> eachItemMatches(final Condition<? super T> condition) {
            Assertions.assertThat(actual.getOnNextEvents()).are(condition);
            return this;
        }

        /**
         * Assert that no emitted items meet a {@link Condition}.
         * @param condition the AssertJ {@link Condition} to check
         */
        public TestSubscriberAssert<T> noItemMatches(final Condition<? super T> condition) {
            Assertions.assertThat(actual.getOnNextEvents()).areNot(condition);
            return this;
        }

        /**
         * Assert that at least one of the emitted items meet a {@link Condition}.
         * @param condition the AssertJ {@link Condition} to check
         */
        public TestSubscriberAssert<T> atLeastOneItemMatches(final Condition<? super T> condition) {
            Assertions.assertThat(actual.getOnNextEvents()).areAtLeastOne(condition);
            return this;
        }

        /**
         * Assert that a {@link Condition} happens at least a certain number of times.
         * @param times number of times the condition needs to be met at least
         * @param condition the AssertJ {@link Condition} to check
         */
        public TestSubscriberAssert<T> areAtLeast(final int times, final Condition<? super T> condition) {
            Assertions.assertThat(actual.getOnNextEvents()).areAtLeast(times, condition);
            return this;
        }

        /**
         * Assert that a {@link Condition} happens at most a certain number of times.
         * @param times number of times the condition needs to be met at most
         * @param condition the AssertJ {@link Condition} to check
         */
        public TestSubscriberAssert<T> areAtMost(final int times, final Condition<? super T> condition) {
            Assertions.assertThat(actual.getOnNextEvents()).areAtMost(times, condition);
            return this;
        }

        /**
         * Assert that a {@link Condition} happens at exactly a certain number of times.
         * @param times number of times the condition needs to be met
         * @param condition the AssertJ {@link Condition} to check
         */
        public TestSubscriberAssert<T> areExactly(final int times, final Condition<? super T> condition) {
            Assertions.assertThat(actual.getOnNextEvents()).areExactly(times, condition);
            return this;
        }
    }


    private RxAssertions() {
        throw new AssertionError();
    }
}
