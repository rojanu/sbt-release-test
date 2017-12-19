#!/usr/bin/env bash
set -e

setup_git() {
    git config --global user.email "${GIT_USER_EMAIL}"
    git config --global user.name "${GIT_USER_NAME}"
    REPO=`git config remote.origin.url`
    SSH_REPO=${REPO/https:\/\/github.com\//git@github.com:}
    git remote rm origin
    git remote add origin ${SSH_REPO}
    eval "$(ssh-agent -s)" # Start the ssh agent
    openssl aes-256-cbc -K $encrypted_e91e22d15568_key -iv $encrypted_e91e22d15568_iv -in travis/rojanu_id_rsa.enc -out travis/rojanu_id_rsa -d
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