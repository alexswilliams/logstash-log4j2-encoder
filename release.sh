#!/usr/bin/env bash

(

  set -ex

  javaMajorVersion=$(java -version 2>&1 | head -n 1 | sed -r 's/.*"([0-9]+\.[0-9]+)\..*/\1/')
  if [[ ! $javaMajorVersion == "1.8" ]]; then
    echo "Wrong version of java in use - detected $javaMajorVersion but required 1.8" >&2
    exit 1
  fi

  ./mvnw -Prelease release:clean clean package

  ./mvnw -Prelease release:prepare

  echo "About to perform release.  Enter to continue."
  read -r

  ./mvnw -Prelease release:perform

)
