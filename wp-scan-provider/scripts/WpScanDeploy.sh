#!/usr/bin/env bash
cd /home/src/WpScan/scanner
gem install bundler -v '1.17.3' && bundle install --without test
