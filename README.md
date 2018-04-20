In order to run this sample, you should create Tomcat run configuration and specify VM options:
* -Djms.brokerUrl=tcp://localhost:61616 
* -Djms.username=admin 
* -Djms.password=admin

Deploy the application and it should fail on line 48.

I entered all my comments and reasoning in `SpringBootJmsLookupsJndiApplication`
