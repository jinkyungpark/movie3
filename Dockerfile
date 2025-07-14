# jdk slim 버전 사용
FROM openjdk:17-jdk-slim
WORKDIR /app
# upload 폴더 생성(WORKDIR 기준준)
RUN mkdir -p upload
# 빌드 파일 위치
ARG JAR_FILE=target/*.jar
# 빌드위치에 있는 jar 파일을 app.jar로
COPY ${JAR_FILE} app.jar
# java -jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]