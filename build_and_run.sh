#!/usr/bin/env bash
set -e

deploy_arg=""

if [ "$1" ]
then
    deploy_arg=$(echo "$1" | sed 's/[[:upper:]]/-&/g;s/^-//' | tr '[:upper:]' '[:lower:]')
    echo "Only building service: $1"
    echo "Only redeploying service: $deploy_arg"
    pushd $1
    ./build.sh
    popd
else
    ./build.sh
fi

./deploy.sh $deploy_arg
sleep 5

./test.sh

