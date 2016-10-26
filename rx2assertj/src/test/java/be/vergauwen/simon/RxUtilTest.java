package be.vergauwen.simon;


import org.junit.Before;
import org.junit.Test;

import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;

import static be.vergauwen.simon.Rx2AssertJ.assertThatASubscriberTo;

public class RxUtilTest {

    private TestSubscriber<Integer> testSubscriber;
    private TestObserver<Long> testObserver;
    private RxUtil rxUtil;

    @Before
    public void setUp() {
        testSubscriber = new TestSubscriber<>();
        testObserver = new TestObserver<>();
        rxUtil = new RxUtil();
    }

    @Test
    public void testDoSomeFlowable() {
        rxUtil.getFlowableIntegers().subscribe(testSubscriber);
        assertThatASubscriberTo(rxUtil.getFlowableIntegers()).hasValueCount(10).hasNoErrors().isComplete();
    }

    @Test
    public void testEmptyMaybe() {
        assertThatASubscriberTo(rxUtil.getEmptyMaybe()).hasNoErrors().isComplete();
    }

    @Test
    public void testDoSomeRxing() {
        rxUtil.doSomeRxing().subscribe(testObserver);
        Rx2AssertJ.assertThat(testObserver).hasNoErrors().hasValueCount(1).hasValue(1L).isComplete();
    }

    @Test
    public void testSomeLongRxing() {
         assertThatASubscriberTo(rxUtil.doSomeLongRxing()).isSubscribed().isComplete();
    }

    //15 : 610 = 2 x 5 x 61
    @Test
    public void testgetSomeSingleValue() {
        assertThatASubscriberTo(rxUtil.getSomeSingleValue(15)).hasNoErrors().hasResult(610L).isComplete();
    }
}