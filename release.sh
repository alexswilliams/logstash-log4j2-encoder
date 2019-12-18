#!/usr/bin/env bash

(

  set -ex

  JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
  export JAVA_HOME

  ./mvnw -Prelease release:clean clean package

  ./mvnw -Prelease release:prepare

  echo "About to perform release.  Enter to continue."
  read -r

  ./mvnw -Prelease release:perform

)
