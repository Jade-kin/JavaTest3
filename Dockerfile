FROM registry.saas.hand-china.com/hap-cloud/base:latest

ADD check.sh ./check.sh

COPY Exam1/target/Exam1.jar /Exam1.jar

WORKDIR /

CMD ["./check.sh"]
CMD ["java","-jar","Exam1.jar"]