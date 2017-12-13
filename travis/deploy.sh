#!/usr/bin/env bash
set -ev

if [[ ${TRAVIS_PULL_REQUEST} == false ]] ; then
    if [[ ${TRAVIS_BRANCH} == master ]] ; then
        sbt ++${TRAVIS_SCALA_VERSION} release
    else
        sbt ++${TRAVIS_SCALA_VERSION} publish
    fi
fi