#!/bin/bash
set -xe

BRANCH="master"
PROJECT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

"$PROJECT_DIR"/gradlew --no-daemon --info clean build codeCoverageReport

if [ "$TRAVIS_BRANCH" != "$BRANCH" ]; then
  echo "Deploying..."
  "$PROJECT_DIR"/gradlew --no-daemon --info bintrayUpload
  echo "Deployed!"
fi
