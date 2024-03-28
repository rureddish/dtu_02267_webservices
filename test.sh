#!/usr/bin/env bash
set -e

for file in ./*/test.sh
do
    pushd `dirname $file`
        ./test.sh
    popd
done
