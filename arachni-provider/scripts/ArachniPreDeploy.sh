#!/usr/bin/env bash
apt-get -y update
apt-get -y update --fix-missing
#setup host ip
sh $WORKSPACE/scripts/docker-host.sh

apt-get install -y make gcc zlib1g-dev 
apt-get install -y ruby2.3-dev
apt-get -y --fix-missing install ruby

#install PHANTOM_JS 
apt-get -y install libfontconfig
wget https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-2.1.1-linux-x86_64.tar.bz2
tar xvfj ./phantomjs-2.1.1-linux-x86_64.tar.bz2
ln -sf $(pwd)/phantomjs-2.1.1-linux-x86_64/bin/phantomjs /usr/bin

#apt-get install -y phantomjs
