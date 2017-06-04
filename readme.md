## Video upload sample rest api
[![Build Status](https://travis-ci.org/bowczarek/videos.svg?branch=master)](https://travis-ci.org/bowczarek/videos)
## What is it?
This app serves nothing more than exercise purpose to rapidly build simple rest api for video files upload.

There are following endpoints exposed:
```
    GET  /videos - retrieves list of uploaded videos
    POST /videos - uploads video file
    GET  /videos/{id} - retrieves selected video file information
    GET  /videos/{id}/stream - downloads selected file
    GET  /videos/{id}/metadata - retrieves video file metadata information (size, duration, video_codec etc)
```

By default rest api accepts only up to **25mb** files. However, you can change this settings in *application.properties* file:
```
    spring.http.multipart.max-file-size=25MB
    spring.http.multipart.max-request-size=25MB
```

Sample videos can be downloaded from http://www.sample-videos.com/.

## Technology stack
* [Spring Boot](https://projects.spring.io/spring-boot/) with following [starters](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-starters)
    * Web
    * JPA
    * Security
    * Thymeleaf
    * Data Rest
    * OAuth2
* [Maven](http://maven.apache.org/)
* [Docker](https://www.docker.com/) and [Docker Maven Plugin](https://github.com/spotify/docker-maven-plugin)
* [FFmpeg wrapper](https://github.com/bramp/ffmpeg-cli-wrapper)
* [HyperSQL](http://hsqldb.org/)

I used IntelliJ IDEA Community Edition with following plugins:
* [Lombok](https://github.com/mplushnikov/lombok-intellij-plugin)
* [Docker Integration](https://plugins.jetbrains.com/plugin/7724-docker-integration)
* [Markdown](https://daringfireball.net/projects/markdown/)

Project was created with [Spring Initializr](https://start.spring.io/) and then imported to IntelliJ IDEA.
## Prerequisites
You will need to install:
* [Oracle JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Docker](https://www.docker.com/)
* [Maven](http://maven.apache.org/)
* [FFmpeg](https://ffmpeg.org/) - if run locally
## Authentication
The app is configured to use google oauth2. For the demo purposes it is preconfigured
with my *videos-demo*. However, if you'd like to use your own app, then go to [google developer console](https://console.developers.google.com/),
register your own one with OAuth login and update following settings in *application.properties*:

```
security.oauth2.client.clientId=
security.oauth2.client.clientSecret=
```
## Configuration
There are three configuration files:
```
application.properties - contains general settings
application-docker.properties - contains docker specific settings (ffmpeg location)
application-local.properties - contains my local windows specific settings (ffmpeg location)
```
## Running the app locally
Make sure that you installed FFmpeg on your local machine and you propely set:
```
ffmpeg.mpegPath=c:\\tools\\ffmpeg\\bin\\ffmpeg.exe
ffmpeg.probePath=c:\\tools\\ffmpeg\\bin\\ffprobe.exe
```
in *application-local.properties* file.

Run:
``` bash
$ mvn spring-boot:run -Dspring.profiles.active={local}
```
to start embedded Tomcat server. Your application should be available at http://localhost:8080.
## Running the app locally in docker container
Firstly, you need to make sure that Docker service is running on your machine:
```bash
$ docker ps
```
If Docker is properly installed then it should print currently running containers.
Additionally, if you are on windows like me, make sure to turn on *'Expose daemon on tcp://localhost:2375 without TLS'* in Docker app.
If you are on linux, then make sure to set following environment variable:
```
$ export DOCKER_HOST=unix:///private/var/tmp/docker.sock
```
Now, you need to build the Docker image:
``` bash
$ mvn package docker:build
```
Finally, you need to start the Docker container:
``` bash
$ docker run -e "SPRING_PROFILES_ACTIVE=docker" -p 8080:8080 -t bartoszowczarek/videos
```
As you can see we are binding container port 8080 to your local machine port 8080.
Once container is up and running, you should be able to access site at http://localhost:8080.
## Demo
http://videos-demo.c7e45562.svc.dockerapp.io
## CI
Automatic build & deployment is configured in [Travis CI](https://travis-ci.org/bowczarek/videos) & [Docker Cloud](https://cloud.docker.com). Each commit will trigger a build that will push
the latest docker image to [bartoszowczarek/videos](https://hub.docker.com/r/bartoszowczarek/videos/) repo in *Docker Hub*
where *Docker Cloud* service will automatically deploy latest image to the demo service endpoint.
