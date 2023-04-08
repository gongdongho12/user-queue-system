FROM eclipse-temurin:17.0.6_10-jdk-alpine AS builder

ENV WORK /workspace
ENV DONGHOLAB_DOMAIN_HOME /dongholab-domain
ENV DONGHOLAB_API_HOME /dongholab-api
ENV DONGHOLAB_BATCH_HOME /dongholab-batch

RUN mkdir -p $WORK
RUN mkdir -p $WORK/gradle
RUN mkdir -p $WORK/$DONGHOLAB_DOMAIN_HOME
RUN mkdir -p $WORK/$DONGHOLAB_API_HOME
RUN mkdir -p $WORK/$DONGHOLAB_BATCH_HOME

COPY build.gradle.kts settings.gradle.kts gradlew $WORK/
COPY gradle/ $WORK/gradle
COPY ./tools/entrypoint_*.sh $WORK/
COPY ./$DONGHOLAB_DOMAIN_HOME/build.gradle.kts $WORK/$DONGHOLAB_DOMAIN_HOME
COPY ./$DONGHOLAB_DOMAIN_HOME/src/ $WORK/$DONGHOLAB_DOMAIN_HOME/src
COPY ./$DONGHOLAB_DOMAIN_HOME/libs/ $WORK/$DONGHOLAB_DOMAIN_HOME/libs
COPY ./$DONGHOLAB_API_HOME/build.gradle.kts $WORK/$DONGHOLAB_API_HOME
COPY ./$DONGHOLAB_API_HOME/src/ $WORK/$DONGHOLAB_API_HOME/src
COPY ./$DONGHOLAB_BATCH_HOME/build.gradle.kts $WORK/$DONGHOLAB_BATCH_HOME
COPY ./$DONGHOLAB_BATCH_HOME/src/ $WORK/$DONGHOLAB_BATCH_HOME/src

WORKDIR $WORK
RUN dos2unix gradlew
RUN ./gradlew clean build --parallel -x test --no-daemon

FROM eclipse-temurin:17.0.6_10-jdk-alpine

LABEL org.opencontainers.image.authors="gongdongho12@gmail.com"

ENV TZ "Asia/Seoul"
ENV PORT $PORT
ENV APPLICATION_USER dongholab

RUN adduser -D -g '' $APPLICATION_USER
RUN apk add bash

RUN mkdir /app
RUN chown -R $APPLICATION_USER /app

USER $APPLICATION_USER

ARG STAGE
ARG SERVICE_NAME
ARG TAG

RUN echo "$STAGE / $SERVICE_NAME"

ENV SERVICE_NAME $SERVICE_NAME
ENV SPRING_PROFILES_ACTIVE $STAGE
ENV TAG $TAG

COPY --from=builder /workspace/dongholab-api/build/libs/dongholab-api.jar /app/dongholab-api.jar
COPY --from=builder /workspace/dongholab-batch/build/libs/dongholab-batch.jar /app/dongholab-batch.jar
COPY --from=builder /workspace/entrypoint_${SERVICE_NAME}_${STAGE}.sh /app/entrypoint.sh

WORKDIR /app

EXPOSE $PORT
EXPOSE 8080
ENTRYPOINT ["sh", "entrypoint.sh"]
