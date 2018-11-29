#!/bin/bash
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin

#pushing brakeman provider
cd brakeman-provider
mvn clean install
docker build -t brakeman-provider .
docker tag brakeman-provider $DOCKER_USERNAME/brakeman-provider
docker push $DOCKER_USERNAME/brakeman-provider

#bundle audit provider
cd ../bundle-audit-provider
mvn clean install
docker build -t bundle-audit-provider .
docker tag bundle-audit-provider $DOCKER_USERNAME/bundle-audit-provider
docker push $DOCKER_USERNAME/bundle-audit-provider

#pushing wp scan provider
cd ../wp-scan-provider
mvn clean install
docker build -t wp-scan-provider .
docker tag wp-scan-provider $DOCKER_USERNAME/wp-scan-provider
docker push $DOCKER_USERNAME/wp-scan-provider

#pushing retire js provider
cd ../retirejs-provider
mvn clean install
docker build -t retirejs-provider .
docker tag retirejs-provider $DOCKER_USERNAME/retirejs-provider
docker push $DOCKER_USERNAME/retirejs-provider


#pushing nsp provider
cd ../nsp-provider
mvn clean install
docker build -t nsp-provider .
docker tag nsp-provider $DOCKER_USERNAME/nsp-provider
docker push $DOCKER_USERNAME/nsp-provider

#pushing nmap provider
cd ../nmap-provider
mvn clean install
docker build -t nmap-provider .
docker tag nmap-provider $DOCKER_USERNAME/nmap-provider
docker push $DOCKER_USERNAME/nmap-provider

#pushing dawn scanner provider
cd ../dawn-scanner-provider
mvn clean install
docker build -t dawn-scanner-provider .
docker tag dawn-scanner-provider $DOCKER_USERNAME/dawn-scanner-provider
docker push $DOCKER_USERNAME/dawn-scanner-provider

#pushing arachni provider
cd ../arachni-provider
mvn clean install
docker build -t arachni-provider .
docker tag arachni-provider $DOCKER_USERNAME/arachni-provider
docker push $DOCKER_USERNAME/arachni-provider

#pushing andro provider
cd ../andro-scan-provider
mvn clean install
docker build -t andro-scan-provider .
docker tag andro-scan-provider $DOCKER_USERNAME/andro-scan-provider
docker push $DOCKER_USERNAME/andro-scan-provider

#pushing findsecbug-provider
cd ../findsecbug-provider
mvn clean install
docker build -t findsecbug-provider .
docker tag findsecbug-provider $DOCKER_USERNAME/findsecbug-provider
docker push $DOCKER_USERNAME/findsecbug-provider

#pushing xanitizer
#cd ../xanitizer-provider
#docker build -t xanitizer-provider .
#docker tag xanitizer-provider $DOCKER_USERNAME/xanitizer-provider
#docker push $DOCKER_USERNAME/xanitizer-provider
#jackhammer Providers 
