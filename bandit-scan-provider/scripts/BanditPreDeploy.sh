#!/usr/bin/env bash
apt-get -y update
apt-get -y update --fix-missing
#setup host ip
sh $WORKSPACE/scripts/docker-host.sh
#install dependencies

apt-get -y install python3-pip #install python3
