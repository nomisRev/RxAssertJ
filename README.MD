# RxJava AssertJ Assertions

A set of AssertJ assertions geared toward testing rxjava.

Has a module for RxJava 1 as well as RxJava 2.

While the base reactive types now have a `test()` method that gives you a TestObserver/TestSubscriber, there is still use for AssertJ-based fluent assertions:
- because you use AssertJ already, and want to keep using the same paradigms and code style when testing RxJava
- because you want to leverage the AssertJ Conditions API
- etc

This library is an extension of [AssertJ][1] which aims to make it even easier to test RxJava 1 & 2.

# Usage

Lets first remind ourselves what we get from RxJava:

**Regular assertions with RxJava's build-in TestObserver/TestSubscriber**
```java
Maybe.just(1)
	.map(v -> v + 1)
	.test()
	.assertResult(2)
	.assertNoErrors()
	.assertComplete();
```

And now with AssertJ-based assertions:

**Assertions with RxAssertions**
```java
Rx2Assertions.assertThatSubscriberTo(Observable.empty())
		.emitsNothing()
		.completes()
		.withoutErrors();
```
or

```java
Maybe<Integer> maybe = Maybe.just(1).map(v -> v + 1);

assertThatSubscriberTo(maybe)
        .hasValue(2)
        .completes()
        .withoutErrors();
```
**AssertJ Conditions**

```java
Set<String> JEDIS = newLinkedHashSet("Luke", "Yoda", "Obiwan");

Observable<String> observable = Observable.fromIterable(JEDIS);

Condition<String> isNotNullOrEmpty = new Condition<String>() {
    @Override
    public boolean matches(String value) {
        return value != null && !value.isEmpty();
    }
};
Condition<String> isJedi = new Condition<String>("jedi") {
    @Override
    public boolean matches(String value) {
        return JEDIS.contains(value);
    }
};

assertThatSubscriberTo(observable)
        .completes()
        .withoutErrors()
        .eachItemMatches(allOf(isNotNullOrEmpty, isJedi));
```

You can find other examples in the `test` folder

# Setup

Add it in your `build.gradle`

```groovy
repositories {
	...
	maven { url "https://jitpack.io" }

}
```
Add the dependency

```groovy
dependencies {
	...
	testCompile 'xxxxxx:$version'
}
```

RxAssertions uses RxJava `1.2.2` and RxJava2 `2.0.1` + AssertJ `2.5.0` as dependencies.

# Contributing



# Credits


# License

```
Copyright (C) 2016

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[1]: http://joel-costigliola.github.io/assertj/
