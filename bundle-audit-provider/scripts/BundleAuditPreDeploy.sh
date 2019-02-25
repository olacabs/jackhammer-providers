#!/usr/bin/env bash
apt-get -y update
apt-get -y update --fix-missing
#setup host ip
sh $WORKSPACE/scripts/docker-host.sh

#install dependencies
apt-get -y install libssl-dev git curl zlib1g-dev build-essential libssl-dev libreadline-dev libyaml-dev libsqlite3-dev sqlite3 libxml2-dev libxslt1-dev libcurl4-openssl-dev libffi-dev
