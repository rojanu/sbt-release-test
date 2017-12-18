#!/usr/bin/env bash
set -ev

if [[ ${TRAVIS_PULL_REQUEST} == false ]] ; then
    if [[ ${TRAVIS_BRANCH} == master ]] ; then
        sbt ++${TRAVIS_SCALA_VERSION} release skip-tests
    else
        if [[ ${TRAVIS_BRANCH} != "develop" ]]; then
            sed -i "s/SNAPSHOT/SNAPSHOT.${TRAVIS_BUILD_NUMBER}/g" ../version.sbt
        fi
        sbt ++${TRAVIS_SCALA_VERSION} distribution/publish
    fi
fi