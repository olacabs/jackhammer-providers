#!/usr/bin/env bash
apt-get -y update
apt-get -y update --fix-missing
#setup host ip
sh $WORKSPACE/scripts/docker-host.sh

apt-get -y install curl
curl -sL https://deb.nodesource.com/setup_6.x | bash -
apt-get install -y nodejs
apt-get install -y npm
