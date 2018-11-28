#!/bin/bash
apt-get -y update
apt-get install -y maven
cd /home/src/findsecbugs/scripts/cli_script
unzip -o spotbugs-3.1.7.zip
