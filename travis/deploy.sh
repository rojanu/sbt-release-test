#!/usr/bin/env bash
set -ex

setup_git() {
    git config --global user.email "${GIT_USER_EMAIL}"
    git config --global user.name "${GIT_USER_NAME}"
    eval "$(ssh-agent -s)" # Start the ssh agent
    ls -la ${TRAVIS_BUILD_DIR}
    ls -la ${TRAVIS_BUILD_DIR}/travis
    chmod 600 ${TRAVIS_BUILD_DIR}/travis/rojanu_id_rsa
    ssh-add ${TRAVIS_BUILD_DIR}/travis/rojanu_id_rsa
}

if [[ ${TRAVIS_PULL_REQUEST} == false ]] ; then
    if [[ ${TRAVIS_BRANCH} == master ]] ; then
        git checkout master
        setup_git
        sbt ++${TRAVIS_SCALA_VERSION} 'release with-defaults skip-tests'
    else
        if [[ ${TRAVIS_BRANCH} != "develop" ]]; then
            sed -i "s/-SNAPSHOT/.${TRAVIS_BUILD_NUMBER}-SNAPSHOT/g" ${TRAVIS_BUILD_DIR}/version.sbt
        fi
        sbt ++${TRAVIS_SCALA_VERSION} distribution/publish
    fi
fi