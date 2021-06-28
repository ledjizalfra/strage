FROM jenkins/jenkins:latest
USER root
RUN echo 'root:root' | chpasswd
RUN apt-get update && apt-get install -y apt-transport-https \
       ca-certificates curl gnupg2 \
       software-properties-common
RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -
RUN apt-key fingerprint 0EBFCD88
RUN add-apt-repository \
       "deb [arch=amd64] https://download.docker.com/linux/debian \
       $(lsb_release -cs) stable"
#RUN apt-get update && apt-get install -y docker-ce-cli
RUN apt-get update && apt-get install -y docker-ce docker-ce-cli containerd.io
RUN apt-get update && apt-get install -y npm
RUN apt-get update && apt-get install -y nodejs
USER jenkins
#RUN echo 'jenkins:jenkins' | chpasswd

#RUN jenkins-plugin-cli --plugins blueocean:latest
#RUN jenkins-plugin-cli --plugins docker:latest
