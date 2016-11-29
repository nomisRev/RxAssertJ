package com.github.nomisrev.rxassertj;

import org.assertj.core.api.AbstractObjectAssert;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.Subscriber;
import rx.observables.BlockingObservable;
import rx.observers.TestSubscriber;

import java.util.concurrent.TimeUnit;

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
         * Asserts that this {@code Subscriber} is unsubscribed.
         *
         * @throws AssertionError if this {@code Subscriber} is not unsubscribed
         */
        public TestSubscriberAssert<T> isUnsubscribed() {
            actual.assertUnsubscribed();
            return this;
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
         * Asserts that a single terminal event occurred, either {@link Subscriber#onCompleted()} or {@link Subscriber#onError(Throwable)}
         */
        public final TestSubscriberAssert<T> isTerminated() {
            actual.assertTerminalEvent();
            return this;
        }

        /**
         * Asserts that there are no onError and onCompleted events.
         */
        public final TestSubscriberAssert<T> isNotTerminated() {
            actual.assertNoTerminalEvent();
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
         * Assert that this TestObserver/TestSubscriber received exactly the specified onError event value. The comparison is performed via Objects.equals(); since most exceptions
         * don't implement equals(), this assertion may fail. Use the {@link #hasError(Class)} overload to test against the class of an error instead of an instance of an error.
         * @param error the error to check
         * @see #hasError(Class)
         */
        public final TestSubscriberAssert<T> hasError(Throwable error) {
            actual.assertError(error);
            return this;
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
         * Assert that exactly one onNext value is received which is equal to the given value with respect to Objects.equals.
         * @param value the value to expect
         */
        public final TestSubscriberAssert<T> hasSingleValue(T value) {
            actual.assertValue(value);
            actual.assertValueCount(1);
            return this;
        }

        /**
         * Asserts that the values received contain the specified values, in any order.
         * @param values the expected values to be contained in the stream.
         */
        public final TestSubscriberAssert<T> contains(T... values) {
            Assertions.assertThat(actual.getOnNextEvents()).contains(values);
            return this;
        }

        /**
         * Asserts that the values received do not contain any of the specified values.
         * @param values the expected values to be not contained in the stream.
         */
        public final TestSubscriberAssert<T> doesNotContain(T... values) {
            Assertions.assertThat(actual.getOnNextEvents()).doesNotContain(values);
            return this;
        }

        /**
         * Asserts that in a stream of onNext events, a certain onNext event indicated by an index a value is
         * received equal to the provided value.
         * @param index the position to assert on
         * @param value the value that should be received in the onNext value on the given index
         */
        public final TestSubscriberAssert<T> hasValueAt(int index, T value) {
            Assertions.assertThat(actual.getOnNextEvents().get(index)).isEqualTo(value);
            return this;
        }

        public TestSubscriberAssert<T> hasValueCount(final int count) {
            actual.assertValueCount(count);
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

        /**
         * Asserts that there are no onNext events received.
         *
         * @throws AssertionError if there were any onNext events
         */
        public TestSubscriberAssert<T> hasNoValues() {
            actual.assertNoValues();
            return this;
        }

        /**
         * Asserts that there are no onNext events received.
         *
         * @throws AssertionError if there were any onNext events
         */
        public TestSubscriberAssert<T> emitsNothing() {
            return this.hasNoValues();
        }

        /**
         * Awaits until the internal latch is counted down.
         * <p>If the wait times out or gets interrupted, the TestSubscriber is cancelled.
         * @param time the waiting time
         * @param unit the time unit of the waiting time
         * @throws RuntimeException wrapping an InterruptedException if the wait is interrupted
         */
        public final TestSubscriberAssert<T> awaitDone(long time, TimeUnit unit) {
            actual.awaitTerminalEvent(time,unit);
            return this;
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
        public final TestSubscriberAssert<T> haveAtLeast(final int times, final Condition<? super T> condition) {
            Assertions.assertThat(actual.getOnNextEvents()).haveAtLeast(times, condition);
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
        public final TestSubscriberAssert<T> haveAtMost(final int times, final Condition<? super T> condition) {
            Assertions.assertThat(actual.getOnNextEvents()).haveAtMost(times, condition);
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
        public final TestSubscriberAssert<T> haveExactly(final int times, final Condition<? super T> condition) {
            Assertions.assertThat(actual.getOnNextEvents()).haveExactly(times, condition);
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
