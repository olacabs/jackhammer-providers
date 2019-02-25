#!/usr/bin/env bash
apt-get -y update
apt-get -y update --fix-missing
apt-get -y install python
#setup host ip
sh $WORKSPACE/scripts/docker-host.sh
