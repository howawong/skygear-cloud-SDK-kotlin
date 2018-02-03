FROM zenika/alpine-kotlin:jdk8
RUN apk update && apk add curl unzip zip
RUN curl -s "https://get.sdkman.io" | bash
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk version && sdk install kscript"
RUN apk add maven
RUN mkdir /app
WORKDIR /app
CMD bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk version && kscript main.kt app.kt"
