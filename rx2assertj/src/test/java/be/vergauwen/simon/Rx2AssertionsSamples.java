package be.vergauwen.simon;


import org.junit.Before;
import org.junit.Test;

import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;

import static be.vergauwen.simon.Rx2Assertions.assertThat;
import static be.vergauwen.simon.Rx2Assertions.assertThatSubscriberTo;

public class Rx2AssertionsSamples {

    private TestSubscriber<Integer> testSubscriber;
    private TestObserver<Long> testObserver;
    private DummyObservables dummyData;

    @Before
    public void setUp() {
        testSubscriber = new TestSubscriber<>();
        testObserver = new TestObserver<>();
        dummyData = new DummyObservables();
    }

    @Test
    public void testDoSomeFlowable() {
        dummyData.getFlowableIntegers().subscribe(testSubscriber);
        assertThatSubscriberTo(dummyData.getFlowableIntegers()).hasValueCount(10).hasNoErrors().isComplete();
    }

    @Test
    public void testEmptyMaybe() {
        assertThatSubscriberTo(dummyData.getEmptyMaybe()).hasNoErrors().isComplete();
    }

    @Test
    public void testDoSomeRxing() {
        dummyData.doSomeRxing().subscribe(testObserver);
        assertThat(testObserver).hasNoErrors().hasValueCount(1).hasSingleValue(1L).isComplete();
    }

    @Test
    public void testSomeLongRxing() {
        assertThatSubscriberTo(dummyData.doSomeLongRxing()).isSubscribed().isComplete();
    }

    //15 : 610 = 2 x 5 x 61
    @Test
    public void testgetSomeSingleValue() {
        assertThatSubscriberTo(dummyData.getSomeSingleValue(15)).hasNoErrors().hasResult(610L).isComplete();
    }
}