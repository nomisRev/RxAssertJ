package com.github.nomisrev.rxassertj;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import rx.observers.TestSubscriber;

import static com.github.nomisrev.rxassertj.RxAssertions.assertThat;
import static com.github.nomisrev.rxassertj.RxAssertions.assertThatSubscriberTo;

public class RxAssertionsSamples {

    private TestSubscriber<Long> testSubscriber;
    private ObservableBuilder observableBuilder;

    @Before
    public void setUp() {
        testSubscriber = new TestSubscriber<Long>();
        observableBuilder = new ObservableBuilder();
    }

    @Test
    public void testDoSomeRxing() {
        observableBuilder.doSomeRxing().subscribe(testSubscriber);
        assertThat(testSubscriber).hasNoErrors().hasValueCount(1).hasValues(1L).isCompleted();
    }

    @Test
    public void testSomeLongRxing() {
        RxAssertions.assertThatSubscriberTo(observableBuilder.doSomeLongRxing()
                .toObservable().toBlocking()).isCompleted();
    }

    //15 : 610 = 2 x 5 x 61
    @Test
    public void testgetSomeSingleValue() {
        assertThatSubscriberTo(observableBuilder.getSomeSingleValue(15)).hasNoErrors().hasValues(610L).isCompleted();
    }
}
