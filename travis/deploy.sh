#!/usr/bin/env bash
set -e

if [[ ${TRAVIS_PULL_REQUEST} == false ]] ; then
    if [[ ${TRAVIS_BRANCH} != develop ]]; then
        sed -i "s/-SNAPSHOT/.${TRAVIS_BUILD_NUMBER}-SNAPSHOT/g" ${TRAVIS_BUILD_DIR}/version.sbt
    fi
    if [[ ${TRAVIS_BRANCH} == master ]]; then
        sed -i "s/-SNAPSHOT//g" ${TRAVIS_BUILD_DIR}/version.sbt
    fi
    sbt ++${TRAVIS_SCALA_VERSION} publish
fi
