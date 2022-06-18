#!/bin/bash

MODE=""
while getopts 'dp' opt; do
    case "$opt" in

    d)
        MODE="dev"
        ;;
    p)
        MODE="prod"
        ;;
    t)
        MODE="test"
        ;;
    esac
done

echo $MODE
if [ -z "$MODE" ]; then
    exit 1
fi

compose="docker-compose.${MODE}.yml"
docker-compose -p $MODE -f $compose down -v && docker-compose -p $MODE -f $compose up -d --build 