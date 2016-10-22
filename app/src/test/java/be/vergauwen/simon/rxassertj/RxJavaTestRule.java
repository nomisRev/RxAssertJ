package be.vergauwen.simon.rxassertj;


import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

public class RxJavaTestRule implements TestRule {
    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                //before: plugins reset, execution and schedulers hook defined
                RxJavaPlugins.getInstance().reset();
                RxJavaPlugins.getInstance().registerSchedulersHook(rxJavaSchedulersHook);

                RxAndroidPlugins.getInstance().reset();
                RxAndroidPlugins.getInstance().registerSchedulersHook(rxAndroidSchedulersHook);

                base.evaluate();

                //after: clean up
                RxJavaPlugins.getInstance().reset();
                RxAndroidPlugins.getInstance().reset();
            }
        };
    }

    //...

    RxJavaSchedulersHook rxJavaSchedulersHook = new RxJavaSchedulersHook(){
        @Override
        public Scheduler getComputationScheduler() {
            return Schedulers.immediate();
        }

        @Override
        public Scheduler getNewThreadScheduler() {
            return Schedulers.immediate();
        }

        @Override
        public Scheduler getIOScheduler() {
            return Schedulers.immediate();
        }
    };

    RxAndroidSchedulersHook rxAndroidSchedulersHook = new RxAndroidSchedulersHook(){
        @Override
        public Scheduler getMainThreadScheduler() {
            return Schedulers.immediate();
        }
    };
}