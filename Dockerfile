FROM openjdk:8

COPY . /usr/src/wasp
WORKDIR /usr/src/wasp

ADD target/wasp-*.jar ./wasp.jar

ADD start.sh ./start.sh
ADD config.properties ./config.properties

RUN chmod u+x start.sh

EXPOSE 8181 8182-8200

ENTRYPOINT ["./start.sh"]
CMD ["config.properties"]

