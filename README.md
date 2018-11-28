## Jackhammer Providers
  Tools integration with jackhammer

## Provider Features
  When scan target added by user, jackhammer will indetify require tools to be run and it will send request.
  Provider does scanning,parsing results and it will send back scan results to jackhammer 
  
## Onbaording Tool
  Take tool payload json from payloads directory and upload in jackhammer client. Naviagate Settings => Tools Configuration => Add Tool 
## Adding New provider
  Add Pre Deployment, Deployment , Post deployment steps into individual shell script file and place all files into script directory .And env variables for script files in docker file,refer any provider docker file 
  
##### Pre Deployment
    Ex: steps to downloading tool tar/zip file steps or installing dependecy libraries
##### Pre Deployment
   Ex: Untar/unzip which are downloaded or install the tool 
##### Post Deployment
    Ex: Remvoing tar/zip files which are downloaded 
##### Scannning steps 
   implement scanning methods in com.olacabs.jch.services.androscanner.controllers.ScanController 
##### Parsing Scan results 
   implement parsing results in jackhammer accepting format in com.olacabs.jch.services.androscanner.controllers. ResultParserController.java 
   
## Make payload json 
    make docker file and build image . after building image push image to local registery, configure docker image into paylaod json. 
