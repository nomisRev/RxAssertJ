package com.github.nomisrev.rx2assertj;


import io.reactivex.*;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.assertj.core.api.exception.RuntimeIOException;
import org.junit.Before;
import org.junit.Test;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.condition.AllOf.allOf;
import static org.assertj.core.util.Sets.newLinkedHashSet;

public class Rx2AssertionsTests {

    private TestSubscriber<Integer> testSubscriber;
    private TestObserver<Long> testObserver;
    private Subscription testSubscription;
    private static Condition<Object> FAILING_CONDITION = new Condition<Object>() {
        @Override
        public boolean matches(Object value) {
            return false;
        }
    };
    private final String testExceptionMessage = "Some error text";
    private final Throwable testExceptionCause = new IOException("File doesn't exist");
    private final Throwable testException = new IllegalStateException(testExceptionMessage, testExceptionCause);
    private final Throwable otherTestException = new RuntimeIOException("File doesn't exist");

    @Before
    public void setUp() {
        testSubscriber = new TestSubscriber<Integer>();
        testObserver = new TestObserver<Long>();
        testSubscription = new Subscription() {
            @Override
            public void request(long n) {
            }

            @Override
            public void cancel() {
            }
        };
    }

    @Test
    public void subscriptionShouldBeSubscribed() {
        Rx2Assertions.assertThat(testObserver).isNotSubscribed();
        testObserver.onSubscribe(testSubscriber);
        Rx2Assertions.assertThat(testObserver).isSubscribed();
    }

    @Test(expected = AssertionError.class)
    public void nonSubscribedObserverShouldFailSubscribedCheck() {
        Rx2Assertions.assertThat(testObserver).isSubscribed();
    }

    @Test
    public void nonSubscribedObserverShouldBeNotSubscribed() {
        Rx2Assertions.assertThat(testObserver).isNotSubscribed();
    }

    @Test(expected = AssertionError.class)
    public void subscribedObserverShouldFailNotSubscribedCheck() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(1)).isNotSubscribed();
    }

    @Test
    public void subscriberShouldBeNotSubscribed() {
        Rx2Assertions.assertThat(testSubscriber).isNotSubscribed();
    }

    @Test(expected = AssertionError.class)
    public void subscriptionShouldFailNotSubscribedCheck() {
        Rx2Assertions.assertThatSubscriberTo(Flowable.just(1)).isNotSubscribed();
    }

    @Test
    public void testSubscriberShouldBecomeSubscribed() {
        Rx2Assertions.assertThat(testSubscriber).isNotSubscribed();
        testSubscriber.onSubscribe(testSubscription);
        Rx2Assertions.assertThat(testSubscriber).isSubscribed();
    }

    @Test(expected = AssertionError.class)
    public void subscribedCheckShouldFailForNonSubscription() {
        Rx2Assertions.assertThat(testSubscriber).isSubscribed();
    }

    @Test
    public void completingCompletableShouldComplete() {
        Rx2Assertions.assertThat(Completable.complete())
                .isComplete();
    }

    @Test(expected = AssertionError.class)
    public void neverShouldFailIsCompleteCheck() {
        Rx2Assertions.assertThatSubscriberTo(Completable.never())
                .isComplete();
    }

    @Test(expected = AssertionError.class)
    public void neverShouldFailCompletesCheck() {
        Rx2Assertions.assertThatSubscriberTo(Completable.never())
                .completes();
    }

    @Test(expected = AssertionError.class)
    public void nonCompletionCheckForJustShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(1))
                .isNotComplete();
    }

    @Test
    public void neverObservableShouldNotComplete() {
        Rx2Assertions.assertThatSubscriberTo(Observable.never())
                .isNotComplete();
    }

    @Test
    public void completingObservablesShouldComplete() {
        Rx2Assertions.assertThatSubscriberTo(Maybe.error(testException)).isTerminated();
        Rx2Assertions.assertThatSubscriberTo(Maybe.just(1)).isTerminated();
    }

    @Test(expected = AssertionError.class)
    public void neverObservableShouldFailTerminationCheck() {
        Rx2Assertions.assertThat(Maybe.never()).isTerminated();
    }

    @Test
    public void neverObservableShouldNotTerminate() {
        Rx2Assertions.assertThatSubscriberTo(Observable.never())
                .isNotTerminated();
    }

    @Test(expected = AssertionError.class)
    public void nonTerminationCheckForJustShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(1))
                .isNotTerminated();
    }

    @Test
    public void anErrorlessStreamShouldNotResultInErrors() {
        Rx2Assertions.assertThat(Observable.just(1))
                .hasNoErrors()
                .withoutErrors();
    }

    @Test(expected = AssertionError.class)
    public void anErrorlessStreamShouldFailWithNoErrorCheck() {
        Rx2Assertions.assertThat(Observable.error(testException))
                .hasNoErrors();
    }

    @Test(expected = AssertionError.class)
    public void anErrorlessStreamShouldFailWithWithoutErrorCheck() {
        Rx2Assertions.assertThat(Observable.error(testException))
                .withoutErrors();
    }

    @Test
    public void errorObservableShouldThrow() {
        Rx2Assertions.assertThatSubscriberTo(Observable.error(testException))
                .hasError(testException);
    }

    @Test(expected = AssertionError.class)
    public void errorCheckShouldFailOnWrongType() {
        Rx2Assertions.assertThatSubscriberTo(Observable.error(otherTestException))
                .hasError(testException);
    }

    @Test
    public void erroringObservableShouldHaveCorrectErrorType() {
        Rx2Assertions.assertThatSubscriberTo(Observable.error(testException))
                .hasError(IllegalStateException.class);
    }

    @Test(expected = AssertionError.class)
    public void nonErroringObservableShouldNotHaveError() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(1))
                .hasError(IllegalStateException.class);
    }

    @Test(expected = AssertionError.class)
    public void differentErroringObservableShouldNotHaveError() {
        Rx2Assertions.assertThatSubscriberTo(Observable.error(otherTestException))
                .hasError(IllegalStateException.class);
    }

    @Test
    public void erroringObservableShouldHavePredicateMatchingError() {
        Rx2Assertions.assertThatSubscriberTo(Observable.error(testException))
                .hasError(new Predicate<Throwable>() {
                    @Override
                    public boolean test(Throwable throwable) throws Exception {
                        return throwable.equals(testException);
                    }
                });
    }

    @Test(expected = AssertionError.class)
    public void nonMatchingErrorPredicateShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(Observable.error(testException))
                .hasError(new Predicate<Throwable>() {
                    @Override
                    public boolean test(Throwable throwable) throws Exception {
                        return throwable.equals(otherTestException);
                    }
                });
    }

    @Test
    public void erroringObservableShouldHaveErrorMessage() {
        Rx2Assertions.assertThatSubscriberTo(Observable.error(testException))
                .hasErrorMessage(testExceptionMessage);
    }

    @Test(expected = AssertionError.class)
    public void incorrectErrorMessageCheckShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(Observable.error(testException))
                .hasErrorMessage("Some other message");
    }

    @Test
    public void observableWithExceptionShouldEmitSpecifiedValuesAndThenFailWithSpecificError() {
        Rx2Assertions.assertThatSubscriberTo(ObservableBuilder.getObservableWithTestException(testException, 1, 2, 3))
                .hasFailure(IllegalStateException.class, 1, 2, 3);
    }

    @Test(expected = AssertionError.class)
    public void incorrectFailureCheckShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(ObservableBuilder.getObservableWithTestException(testException, 1, 2, 3))
                .hasFailure(IOException.class, 1, 2, 3);
    }

    @Test(expected = AssertionError.class)
    public void incorrectValuesAtFailureCheckShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(ObservableBuilder.getObservableWithTestException(testException, 1, 2, 3))
                .hasFailure(IllegalStateException.class, 4, 5, 6);
    }

    @Test
    public void observableWithExceptionShouldMatchErrorPredicate() {
        Rx2Assertions.assertThatSubscriberTo(ObservableBuilder.getObservableWithTestException(testException, 1, 2, 3))
                .hasFailure(new Predicate<Throwable>() {
                    @Override
                    public boolean test(Throwable error) throws Exception {
                        return error.equals(testException);
                    }
                }, 1, 2, 3);
    }

    @Test(expected = AssertionError.class)
    public void nonMatchingPredicateAtFailureCheckShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(ObservableBuilder.getObservableWithTestException(testException, 1, 2, 3))
                .hasFailure(new Predicate<Throwable>() {
                    @Override
                    public boolean test(Throwable error) throws Exception {
                        return error.equals(otherTestException);
                    }
                }, 1, 2, 3);
    }

    @Test(expected = AssertionError.class)
    public void incorrectValuesAtFailureCheckWithPredicateShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(ObservableBuilder.getObservableWithTestException(testException, 1, 2, 3))
                .hasFailure(new Predicate<Throwable>() {
                    @Override
                    public boolean test(Throwable error) throws Exception {
                        return error.equals(testException);
                    }
                }, 4, 5, 6);
    }

    @Test
    public void errorObservableShouldFailWithExceptionAndErrorMessage() {
        Rx2Assertions.assertThatSubscriberTo(ObservableBuilder.getObservableWithTestException(testException, 1, 2, 3))
                .hasFailureAndMessage(IllegalStateException.class, testExceptionMessage, 1, 2, 3);
    }

    @Test(expected = AssertionError.class)
    public void incorrectExceptionFailureCheckShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(ObservableBuilder.getObservableWithTestException(testException, 1, 2, 3))
                .hasFailureAndMessage(IOException.class, testExceptionMessage, 1, 2, 3);
    }

    @Test(expected = AssertionError.class)
    public void incorrectMessageFailureCheckShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(ObservableBuilder.getObservableWithTestException(testException, 1, 2, 3))
                .hasFailureAndMessage(IllegalStateException.class, "Some other message", 1, 2, 3);
    }

    @Test(expected = AssertionError.class)
    public void incorrectValuesFailureCheckShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(ObservableBuilder.getObservableWithTestException(testException, 1, 2, 3))
                .hasFailureAndMessage(IOException.class, testExceptionMessage, 4, 5, 6);
    }

    @Test
    public void singleValueEmittingObservableShouldHaveExactlyOneSpecifiedValue() {
        Rx2Assertions.assertThat(Single.just(1))
                .hasSingleValue(1);
    }

    @Test(expected = AssertionError.class)
    public void singleValueCheckShouldFailForMultipleEmissions() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(1, 2))
                .hasSingleValue(1);
    }

    @Test(expected = AssertionError.class)
    public void singleValueCheckShouldFailForIncorrectValue() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(2))
                .hasSingleValue(1);
    }

    @Test
    public void emittedValuesShouldMatchPredicate() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(ObservableBuilder.JEDIS))
                .hasSingleValue(new Predicate<Set<String>>() {
                    @Override
                    public boolean test(Set<String> value) throws Exception {
                        return value.equals(ObservableBuilder.JEDIS);
                    }
                });
    }

    @Test(expected = AssertionError.class)
    public void nonMatchingValuePredicateShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(ObservableBuilder.JEDIS))
                .hasSingleValue(new Predicate<Set<String>>() {
                    @Override
                    public boolean test(Set<String> value) throws Exception {
                        return value.equals(newLinkedHashSet("something"));
                    }
                });
    }

    @Test
    public void streamShouldContain() {
        Rx2Assertions.assertThat(Flowable.just(1, 2, 3)).contains(1, 2);
    }

    @Test(expected = AssertionError.class)
    public void incorrectValuesContainsCheckShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(Flowable.just(1, 2, 3)).contains(4, 5);
    }

    @Test
    public void streamShouldNotContain() {
        Rx2Assertions.assertThatSubscriberTo(Flowable.just(1, 2, 3)).doesNotContain(4, 5);
    }

    @Test(expected = AssertionError.class)
    public void incorrectValuesNotContainCheckShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(Flowable.just(1, 2, 3)).doesNotContain(1, 2);
    }

    @Test
    public void emittedValueAtPositionShouldMatchPredicate() {
        Rx2Assertions.assertThatSubscriberTo(Observable.fromArray(1, 2, 3))
                .hasValueAt(1, new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer value) throws Exception {
                        return value.equals(2);
                    }
                });
    }

    @Test(expected = AssertionError.class)
    public void nonMatchingValueAtPositionPredicateShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(Observable.fromArray(1, 2, 3))
                .hasValueAt(1, new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer value) throws Exception {
                        return value.equals(3);
                    }
                });
    }

    @Test
    public void singleItemObservableShouldEmitOneValue() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just("one"))
                .hasValueCount(1)
                .completes()
                .withoutErrors();
    }

    @Test(expected = AssertionError.class)
    public void multipleItemObservableShouldFailEmitOneValueCheck() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just("one","two"))
                .hasValueCount(1);
    }

    @Test
    public void emittedValuesShouldMatchCheckInOrder() {
        Rx2Assertions.assertThatSubscriberTo(Observable.fromArray(1, 2, 3))
                .hasValues(1, 2, 3);
    }

    @Test(expected = AssertionError.class)
    public void wrongOrderingValueCheckShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(Observable.fromArray(1, 2, 3))
                .hasValues(1, 3, 2);
    }

    @Test
    public void emittedValuesShouldMatchValueSet() {
        Rx2Assertions.assertThatSubscriberTo(Observable.fromIterable(ObservableBuilder.JEDIS))
                .hasValueSet(ObservableBuilder.JEDIS);
    }

    @Test(expected = AssertionError.class)
    public void nonMatchingValueSetCheckShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(Observable.fromIterable(ObservableBuilder.JEDIS))
                .hasValueSet(new LinkedHashSet<String>());
    }

    @Test
    public void emittedValuesShouldMatchSequence() {
        Rx2Assertions.assertThatSubscriberTo(Observable.fromIterable(ObservableBuilder.JEDIS))
                .hasValueSequence(ObservableBuilder.JEDIS);
    }


    @Test(expected = AssertionError.class)
    public void nonMatchingValueSequenceCheckShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(Observable.fromIterable(ObservableBuilder.JEDIS))
                .hasValueSequence(new LinkedHashSet<String>());
    }

    @Test
    public void observableShouldSignalSpecifiedValuesAndComplete() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(1, 2, 3, 4))
                .hasResult(1, 2, 3, 4);
    }

    @Test(expected = AssertionError.class)
    public void incorrectResultCheckShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(1, 2, 3, 4))
                .hasResult(1, 2, 3);
    }

    @Test
    public void emptyObservableShouldNotEmitAnything() {
        Rx2Assertions.assertThat(Observable.empty())
                .emitsNothing()
                .hasNoValues();
    }

    @Test(expected = AssertionError.class)
    public void observableShouldFailEmitsNothing() {
        Rx2Assertions.assertThat(Observable.just(1))
                .emitsNothing();
    }

    @Test(expected = AssertionError.class)
    public void observableShouldFailHasNoValues() {
        Rx2Assertions.assertThat(Observable.just(1))
                .hasNoValues();
    }

    @Test
    public void delayedObservableShouldCompleteInTime() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(1).delay(250, TimeUnit.MILLISECONDS))
                .awaitDone(500, TimeUnit.MILLISECONDS)
                .isComplete();
    }

    @Test(expected = AssertionError.class)
    public void tooShortAwaitDoneShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(1).delay(500, TimeUnit.MILLISECONDS))
                .awaitDone(250, TimeUnit.MILLISECONDS)
                .isComplete();
    }

    @Test
    public void emptyObservableShouldEmitNothing() {
        Rx2Assertions.assertThat(Observable.empty())
                .emitsNothing()
                .completes()
                .withoutErrors();
    }

    @Test
    public void allItemsShouldMeetCondition() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> isNotNullOrEmpty = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value != null && !value.isEmpty();
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .eachItemMatches(isNotNullOrEmpty);
    }

    @Test(expected = AssertionError.class)
    public void allItemsConditionMatcherShouldFail() {
        Observable<Integer> observable = Observable.just(1);

        Rx2Assertions.assertThatSubscriberTo(observable)
                .eachItemMatches(FAILING_CONDITION);
    }

    @Test
    public void allItemsShouldMeetCombinedConditions() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> isNotNullOrEmpty = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value != null && !value.isEmpty();
            }
        };
        Condition<String> isJedi = new Condition<String>("jedi") {
            @Override
            public boolean matches(String value) {
                return ObservableBuilder.JEDIS.contains(value);
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .eachItemMatches(allOf(isNotNullOrEmpty, isJedi));
    }

    @Test
    public void allItemsShouldNotMeetCondition() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> isNull = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value == null;
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .noItemMatches(isNull);
    }

    @Test(expected = AssertionError.class)
    public void noItemMatchShouldFail() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> isNotNullOrEmpty = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value != null && !value.isEmpty();
            }
        };
        Rx2Assertions.assertThatSubscriberTo(observable)
                .noItemMatches(isNotNullOrEmpty);
    }

    @Test
    public void atLeastOneItemShouldMeetCondition() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> isLuke = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.equals("Luke");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .isComplete()
                .withoutErrors()
                .atLeastOneItemMatches(isLuke);

    }

    @Test(expected = AssertionError.class)
    public void atLeastOneConditionCheckShouldFail() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> isDarthVader = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.equals("Darth Vader");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .atLeastOneItemMatches(isDarthVader);
    }

    @Test
    public void atLeastTwoItemsShouldMeetCondition() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .isComplete()
                .withoutErrors()
                .areAtLeast(2, containsTheLetterA);
    }

    @Test
    public void atLeastTwoItemsShouldHaveCondition() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .isComplete()
                .withoutErrors()
                .haveAtLeast(2, containsTheLetterA);
    }

    @Test(expected = AssertionError.class)
    public void areAtLeastConditionCheckShouldFail() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .areAtLeast(3, containsTheLetterA);
    }

    @Test(expected = AssertionError.class)
    public void haveAtLeastConditionCheckShouldFail() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .haveAtLeast(3, containsTheLetterA);
    }

    @Test
    public void atMostTwoItemsShouldMeetCondition() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .isComplete()
                .withoutErrors()
                .areAtMost(2, containsTheLetterA);
    }

    @Test
    public void atMostTwoItemsShouldHaveCondition() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .isComplete()
                .withoutErrors()
                .haveAtMost(2, containsTheLetterA);
    }

    @Test(expected = AssertionError.class)
    public void areAtMostConditionCheckShouldFail() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .areAtMost(1, containsTheLetterA);
    }

    @Test(expected = AssertionError.class)
    public void haveAtMostConditionCheckShouldFail() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .haveAtMost(1, containsTheLetterA);
    }

    @Test
    public void exactlyTwoItemsShouldMeetCondition() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .isComplete()
                .withoutErrors()
                .areExactly(2, containsTheLetterA);
    }

    @Test
    public void exactlyTwoItemsShouldHaveCondition() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .isComplete()
                .withoutErrors()
                .haveExactly(2, containsTheLetterA);
    }

    @Test(expected = AssertionError.class)
    public void areExactlyConditionCheckShouldFail() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .areExactly(3, containsTheLetterA);
    }

    @Test(expected = AssertionError.class)
    public void haveExactlyConditionCheckShouldFail() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .haveExactly(3, containsTheLetterA);
    }

    @Test(expected = Exception.class)
    public void Rx2AssertionsClassShouldHavePrivateConstructor() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Constructor<Rx2Assertions> constructor = Rx2Assertions.class.getDeclaredConstructor();
        Assertions.assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
