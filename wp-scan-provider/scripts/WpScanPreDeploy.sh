#!/usr/bin/env bash
apt-get -y update
apt-get install -y git-core curl zlib1g-dev build-essential libssl-dev libreadline-dev libyaml-dev libsqlite3-dev sqlite3 libxml2-dev libxslt1-dev libcurl4-openssl-dev software-properties-common libffi-dev
# install rbenv
git clone https://github.com/sstephenson/rbenv.git /root/.rbenv
git clone https://github.com/sstephenson/ruby-build.git /root/.rbenv/plugins/ruby-build

echo 'eval "$(rbenv init -)"' >> $HOME/.profile
echo 'eval "$(rbenv init -)"' >> $HOME/.bashrc

rbenv install 2.5.0
rbenv global 2.5.0
