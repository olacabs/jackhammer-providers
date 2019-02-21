#!/usr/bin/env bash
apt-get -y update
apt-get -y update --fix-missing
#setup host ip
sh $WORKSPACE/scripts/docker-host.sh
apt-get -y --fix-missing install python
apt-get -y --fix-missing install python-pip
