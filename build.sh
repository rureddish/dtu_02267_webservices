#!/usr/bin/env bash
set -e

# Build utils first
for util in ./MessagingUtils ./Common
do
    pushd $util
        ./build.sh
    popd
done

for file in ./*/build.sh
do
    dir=$(dirname $file)
    if [[ $dir -ef ./MessagingUtils ]]; then
        continue
    fi
    if [[ $dir -ef ./Common ]]; then
        continue
    fi
    pushd $dir
        ./build.sh
    popd
done
