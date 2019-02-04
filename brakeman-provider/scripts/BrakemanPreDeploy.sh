#!/bin/bash
apt-get -y update
apt-get -y update --fix-missing
#setup host ip
sh $WORKSPACE/scripts/docker-host.sh

apt-get -y --fix-missing install ruby
