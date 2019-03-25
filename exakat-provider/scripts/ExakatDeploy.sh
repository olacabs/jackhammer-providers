#!/usr/bin/env bash
php composer-setup.php --install-dir=/usr/local/bin --filename=composer
cd /home/src/Exakat/scanner
curl --silent -o apache-tinkerpop-gremlin-server-$GREMLIN_VERSION-bin.zip http://dist.exakat.io/apache-tinkerpop-gremlin-server-$GREMLIN_VERSION-bin.zip
curl --silent -o apache-tinkerpop-gremlin-server-$GREMLIN_VERSION-bin.zip http://dist.exakat.io/apache-tinkerpop-gremlin-server-$GREMLIN_VERSION-bin.zip
unzip apache-tinkerpop-gremlin-server-$GREMLIN_VERSION-bin.zip
mv apache-tinkerpop-gremlin-server-$GREMLIN_VERSION tinkergraph
cd tinkergraph
bin/gremlin-server.sh install org.apache.tinkerpop neo4j-gremlin $GREMLIN_VERSION
cd ..
curl --silent http://dist.exakat.io/index.php?file=exakat-$EXAKAT_VERSION.phar -o exakat.phar
chmod a+x exakat.*
export TERM="xterm"
apt-get install -y php-mbstring php-curl php-sqlite3
