# Email Receiver

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.3/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.3/maven-plugin/build-image.html)
* [Spring Integration Test Module Reference Guide](https://docs.spring.io/spring-integration/reference/testing.html)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/3.4.3/specification/configuration-metadata/annotation-processor.html)
* [Spring Integration](https://docs.spring.io/spring-boot/3.4.3/reference/messaging/spring-integration.html)

### Guides

The following guides illustrate how to use some features concretely:

* [Integrating Data](https://spring.io/guides/gs/integration/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

### Reference

* [Spring Integration](https://spring.io/projects/spring-integration)
* [java - spring-integration-mail Receiver mail for read email program in spring boot - Stack Overflow](https://stackoverflow.com/questions/74958216/spring-integration-mail-receiver-mail-for-read-email-program-in-spring-boot)
* [java - How to read text inside body of mail using javax.mail - Stack Overflow](https://stackoverflow.com/questions/11240368/how-to-read-text-inside-body-of-mail-using-javax-mail)
* [Google App passwords](https://myaccount.google.com/apppasswords)
