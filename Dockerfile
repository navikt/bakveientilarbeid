FROM navikt/java:13-appdynamics
COPY target/bakveientilarbeid-jar-with-dependencies.jar /app/app.jar
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 \
               -XX:+HeapDumpOnOutOfMemoryError \
               -XX:HeapDumpPath=/oom-dump.hprof"
ENV PORT=8080
EXPOSE $PORT
