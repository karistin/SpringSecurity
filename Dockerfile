FROM azul/zulu-openjdk:17
EXPOSE 8080
ADD build/libs/SpringSecurity.jar SpringSecurity.jar 
ENTRYPOINT ["java", "-DJWT_TOKEN_SECRET=1234","-jar", "/SpringSecurity.jar"]
