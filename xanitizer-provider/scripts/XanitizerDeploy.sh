#!/bin/bash
apt-get -y update
apt-get -y update --fix-missing
#setup host ip
sh $WORKSPACE/scripts/docker-host.sh

cd /home/src/xanitizer/scripts/cli_script 
unzip -o Xanitizer-4.0.3-lin64-obf.zip
