package com.github.nomisrev.rxassertj;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.assertj.core.api.exception.RuntimeIOException;
import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.observers.TestSubscriber;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.condition.AllOf.allOf;
import static org.assertj.core.util.Sets.newLinkedHashSet;

public class RxAssertionsTests {

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

    @Test
    public void whenASubsriptionTerminatesItShouldUnsubscribe() {
        RxAssertions.assertThat(Observable.empty())
                .isCompleted()
                .isUnsubscribed();
    }

    @Test(expected = AssertionError.class)
    public void neverShouldFailUnsubscribeCheck() {
        RxAssertions.assertThat(Observable.never())
                .isCompleted()
                .isUnsubscribed();
    }

    @Test
    public void completingCompletableShouldComplete() {
        RxAssertions.assertThat(Completable.complete())
                .isCompleted()
                .completes();
    }

    @Test(expected = AssertionError.class)
    public void neverShouldFailCompletionCheck() {
        RxAssertions.assertThatSubscriberTo(Completable.never())
                .isCompleted();
    }

    @Test(expected = AssertionError.class)
    public void nonCompletionCheckForJustShouldFail() {
        RxAssertions.assertThatSubscriberTo(Observable.just(1))
                .isNotCompleted();
    }

    @Test
    public void neverObservableShouldNotComplete() {
        RxAssertions.assertThatSubscriberTo(Observable.never())
                .isNotCompleted();
    }

    @Test
    public void completingObservablesShouldComplete() {
        RxAssertions.assertThatSubscriberTo(Single.error(testException)).isTerminated();
        RxAssertions.assertThatSubscriberTo(Single.just(1)).isTerminated();
    }

    @Test(expected = AssertionError.class)
    public void neverObservableShouldFailTerminationCheck() {
        RxAssertions.assertThat(Observable.never()).isTerminated();
    }

    @Test
    public void neverObservableShouldNotTerminate() {
        RxAssertions.assertThatSubscriberTo(Observable.never())
                .isNotTerminated();
    }

    @Test(expected = AssertionError.class)
    public void nonTerminationCheckForJustShouldFail() {
        RxAssertions.assertThatSubscriberTo(Observable.just(1))
                .isNotTerminated();
    }

    @Test
    public void anErrorlessStreamShouldNotResultInErrors() {
        RxAssertions.assertThat(Observable.just(1).toBlocking())
                .hasNoErrors()
                .withoutErrors();
    }

    @Test(expected = AssertionError.class)
    public void anErrorlessStreamShouldFailWithNoErrorCheck() {
        RxAssertions.assertThat(Observable.error(testException).toBlocking())
                .hasNoErrors();
    }

    @Test(expected = AssertionError.class)
    public void anErrorlessStreamShouldFailWithWithoutErrorCheck() {
        RxAssertions.assertThat(Observable.error(testException).toBlocking())
                .withoutErrors();
    }

    @Test
    public void errorObservableShouldThrow() {
        RxAssertions.assertThatSubscriberTo(Observable.error(testException))
                .hasError(testException);
    }

    @Test(expected = AssertionError.class)
    public void errorCheckShouldFailOnWrongType() {
        RxAssertions.assertThatSubscriberTo(Observable.error(otherTestException))
                .hasError(testException);
    }

    @Test
    public void erroringObservableShouldHaveCorrectErrorType() {
        RxAssertions.assertThatSubscriberTo(Observable.error(testException))
                .hasError(IllegalStateException.class);
    }

    @Test(expected = AssertionError.class)
    public void nonErroringObservableShouldNotHaveError() {
        RxAssertions.assertThatSubscriberTo(Observable.just(1))
                .hasError(IllegalStateException.class);
    }

    @Test(expected = AssertionError.class)
    public void differentErroringObservableShouldNotHaveError() {
        RxAssertions.assertThatSubscriberTo(Observable.error(otherTestException))
                .hasError(IllegalStateException.class);
    }

    @Test
    public void singleValueEmittingObservableShouldHaveExactlyOneSpecifiedValue() {
        RxAssertions.assertThat(Single.just(1))
                .hasSingleValue(1);
    }

    @Test(expected = AssertionError.class)
    public void singleValueCheckShouldFailForMultipleEmissions() {
        RxAssertions.assertThatSubscriberTo(Observable.just(1, 2))
                .hasSingleValue(1);
    }

    @Test(expected = AssertionError.class)
    public void singleValueCheckShouldFailForIncorrectValue() {
        RxAssertions.assertThatSubscriberTo(Observable.just(2))
                .hasSingleValue(1);
    }

    @Test
    public void streamShouldContain() {
        RxAssertions.assertThat(Observable.just(1, 2, 3)).contains(1, 2);
    }

    @Test(expected = AssertionError.class)
    public void incorrectValuesContainsCheckShouldFail() {
        RxAssertions.assertThatSubscriberTo(Observable.just(1, 2, 3)).contains(4, 5);
    }

    @Test
    public void streamShouldNotContain() {
        RxAssertions.assertThatSubscriberTo(Observable.just(1, 2, 3)).doesNotContain(4, 5);
    }

    @Test(expected = AssertionError.class)
    public void incorrectValuesNotContainCheckShouldFail() {
        RxAssertions.assertThatSubscriberTo(Observable.just(1, 2, 3)).doesNotContain(1, 2);
    }

    @Test
    public void valuesShouldContainItemAtPosition() {
        RxAssertions.assertThat(Observable.just("something","somethingElse",testExceptionMessage))
                .hasValueAt(2, testExceptionMessage);
    }

    @Test(expected = AssertionError.class)
    public void incorrectValueShouldContainItemAtPositionCheckShouldFail() {
        RxAssertions.assertThat(Observable.just("something","somethingElse","thatOtherThing"))
                .hasValueAt(2, testExceptionMessage);
    }

    @Test
    public void emittedValuesShouldMatchCheckInOrder() {
        RxAssertions.assertThatSubscriberTo(Observable.from(Arrays.array(1,2,3)))
                .hasValues(1, 2, 3);
    }

    @Test(expected = AssertionError.class)
    public void wrongOrderingValueCheckShouldFail() {
        RxAssertions.assertThatSubscriberTo(Observable.from(Arrays.array(1,2,3)))
                .hasValues(1, 3, 2);
    }

    @Test
    public void emptyObservableShouldNotEmitAnything() {
        RxAssertions.assertThat(Observable.empty())
                .emitsNothing()
                .hasNoValues();
    }

    @Test(expected = AssertionError.class)
    public void observableShouldFailEmitsNothing() {
        RxAssertions.assertThat(Observable.just(1))
                .emitsNothing();
    }

    @Test(expected = AssertionError.class)
    public void observableShouldFailHasNoValues() {
        RxAssertions.assertThat(Observable.just(1))
                .hasNoValues();
    }

    @Test
    public void delayedObservableShouldCompleteInTime() {
        RxAssertions.assertThatSubscriberTo(Observable.just(1).delay(250, TimeUnit.MILLISECONDS))
                .awaitDone(500, TimeUnit.MILLISECONDS)
                .isCompleted();
    }

    @Test(expected = AssertionError.class)
    public void tooShortAwaitDoneShouldFail() throws RuntimeException {
        RxAssertions.assertThatSubscriberTo(Observable.just(1).delay(1000, TimeUnit.MILLISECONDS))
                .awaitDone(250, TimeUnit.MILLISECONDS)
                .isCompleted();
    }

    @Test
    public void singleItemObservableShouldEmitOneValue() {
        RxAssertions.assertThatSubscriberTo(Observable.just("one"))
                .hasValueCount(1)
                .completes()
                .withoutErrors();
    }

    @Test(expected = AssertionError.class)
    public void multipleItemObservableShouldFailEmitOneValueCheck() {
        RxAssertions.assertThatSubscriberTo(Observable.just("one","two"))
                .hasValueCount(1);
    }

    @Test
    public void emptyObservableShouldEmitNothing() {
        RxAssertions.assertThatSubscriberTo(Observable.empty())
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

        RxAssertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .eachItemMatches(isNotNullOrEmpty);
    }

    @Test(expected = AssertionError.class)
    public void allItemsConditionMatcherShouldFail() {
        Observable<Integer> observable = Observable.just(1);

        RxAssertions.assertThatSubscriberTo(observable)
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

        RxAssertions.assertThatSubscriberTo(observable)
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

        RxAssertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .noItemMatches(isNull);
    }

    @Test(expected = AssertionError.class)
    public void noItemMatch() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> isNotNullOrEmpty = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value != null && !value.isEmpty();
            }
        };
        RxAssertions.assertThatSubscriberTo(observable)
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

        RxAssertions.assertThatSubscriberTo(observable)
                .completes()
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

        RxAssertions.assertThatSubscriberTo(observable)
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

        RxAssertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .areAtLeast(2, containsTheLetterA)
                .haveAtLeast(2, containsTheLetterA);
    }

    @Test(expected = AssertionError.class)
    public void AreAtLeastConditionCheckShouldFail() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        RxAssertions.assertThatSubscriberTo(observable)
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

        RxAssertions.assertThatSubscriberTo(observable)
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

        RxAssertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .areAtMost(2, containsTheLetterA)
                .haveAtMost(2, containsTheLetterA);
    }

    @Test(expected = AssertionError.class)
    public void AreAtMostConditionCheckShouldFail() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        RxAssertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .areAtMost(1, containsTheLetterA);
    }

    @Test(expected = AssertionError.class)
    public void HaveAtMostConditionCheckShouldFail() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        RxAssertions.assertThatSubscriberTo(observable)
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

        RxAssertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .areExactly(2, containsTheLetterA)
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

        RxAssertions.assertThatSubscriberTo(observable)
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

        RxAssertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .haveExactly(3, containsTheLetterA);
    }

    @Test(expected = Exception.class)
    public void Rx2AssertionsClassShouldHavePrivateConstructor() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Constructor<RxAssertions> constructor = RxAssertions.class.getDeclaredConstructor();
        Assertions.assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
