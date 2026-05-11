FROM tomcat:9.0-jdk17

COPY target/ecommerce.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080