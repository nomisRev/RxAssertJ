#!/bin/bash
set -xe

JDK="oraclejdk8"
BRANCH="master"
PROJECT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

"$PROJECT_DIR"/gradlew --no-daemon --info clean build codeCoverageReport

if [ "$TRAVIS_JDK_VERSION" != "$JDK" ]; then
  echo "Skipping snapshot deployment: wrong JDK. Expected '$JDK' but was '$TRAVIS_JDK_VERSION'."
elif [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
  echo "Skipping snapshot deployment: was pull request."
elif [ "$TRAVIS_BRANCH" != "$BRANCH" ]; then
  echo "Skipping snapshot deployment: wrong branch. Expected '$BRANCH' but was '$TRAVIS_BRANCH'."
else
  echo "Deploying snapshot..."
  "$PROJECT_DIR"/gradlew --no-daemon --info rx2assertj:bintrayUpload
  echo "Snapshot deployed!"
fi
