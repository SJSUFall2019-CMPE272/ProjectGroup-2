FROM openjdk:11

ARG SBT_VERSION=1.2.8

# Install sbt
RUN \
  curl -L -o sbt-$SBT_VERSION.deb https://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get update && \
  apt-get install sbt && \
  sbt sbtVersion

COPY . /tmp
WORKDIR /tmp
RUN sbt dist
RUN unzip -d / target/universal/service-1.0-SNAPSHOT.zip
RUN rm -rf *
WORKDIR /service-1.0-SNAPSHOT
RUN rm bin/service.bat
CMD bin/service -Dplay.http.secret.key=aVerySecretKeyThatShouldBeChangedEventuallyToSomethingCryptographicallySecure
