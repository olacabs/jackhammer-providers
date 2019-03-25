#!/usr/bin/env bash
apt-get -y update
apt-get -y update --fix-missing
#setup host ip
sh $WORKSPACE/scripts/docker-host.sh

#install dependencies
apt-get install -y --no-install-recommends git subversion mercurial bzr lsof unzip zip
curl -sS https://getcomposer.org/installer -o composer-setup.php
export DEBIAN_FRONTEND=noninteractive
apt-get install -y php7.2 unzip
export EXAKAT_VERSION=1.6.9
export GREMLIN_VERSION=3.3.5
HASH="$(curl --silent -o - https://composer.github.io/installer.sig)"
php -r "if (hash_file('SHA384', 'composer-setup.php') === '$HASH') { echo 'Installer verified'; } else { echo 'Installer corrupt'; unlink('composer-setup.php'); } echo PHP_EOL;"
apt-get install -y software-properties-common
