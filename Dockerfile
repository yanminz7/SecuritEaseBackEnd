# syntax=docker/dockerfile:1

#  docker build -t securitease_api-testng:latest .

#docker run --rm -v "%cd%\reports:/app/target" securitease_api-testng:latest
# syntax=docker/dockerfile:1

FROM maven:3.8.6-openjdk-11

WORKDIR /app

# Copy POM first
COPY pom.xml .
COPY testng.xml .

# Pre-download ALL plugins and dependencies
RUN mvn dependency:go-offline
RUN mvn clean compile -DskipTests
RUN mvn surefire:test -DskipTests
RUN mvn resources:resources -DskipTests
RUN mvn compiler:compile -DskipTests

# Add explicit dependency resolution for surefire-testng
RUN mvn dependency:get -Dartifact=org.apache.maven.surefire:surefire-testng:3.2.5
RUN mvn dependency:get -Dartifact=org.apache.maven.surefire:surefire-testng-utils:3.2.5
RUN mvn dependency:get -Dartifact=org.apache.maven.surefire:surefire-grouper:3.2.5
RUN mvn dependency:get -Dartifact=org.apache.maven.surefire:common-java5:3.2.5


# Copy source code
COPY src ./src

CMD ["mvn", "test"]