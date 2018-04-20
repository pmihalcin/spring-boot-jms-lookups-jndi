package mihalcin.jmsjndi;

import javax.jms.ConnectionFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.jndi.JndiLocatorDelegate;
import org.springframework.util.Assert;

@SpringBootApplication
public class SpringBootJmsLookupsJndiApplication implements CommandLineRunner {

    private static final String CONNECTION_FACTORY_MUST_NOT_BE_NULL = "connection factory must not be null";
    private final JmsProperties properties;

    public SpringBootJmsLookupsJndiApplication(JmsProperties properties) {
        this.properties = properties;
    }


    public static void main(String[] args) {
        SpringApplication.run(SpringBootJmsLookupsJndiApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        // copy pasted from JndiConnectionFactoryAutoConfiguration
        // question is if there is another way to specify properties when I have to use web.xml (because of weblogic)
        // as you can see I have to specify context-param in web.xml
        // and spring.jms.jndi-name=${jms.jndi-name} in application properties
        // what is kinda weird
        ConnectionFactory connectionFactory = new JndiLocatorDelegate().lookup(this.properties.getJndiName(), ConnectionFactory.class);
        Assert.notNull(connectionFactory, CONNECTION_FACTORY_MUST_NOT_BE_NULL);

        // this is how spring boot looks up JMS connection factory using JndiLocatorDelegate in JndiConnectionFactoryAutoConfiguration
        connectionFactory = new JndiLocatorDelegate().lookup("java:comp/env/jms.ConnectionFactory", ConnectionFactory.class);
        Assert.notNull(connectionFactory, CONNECTION_FACTORY_MUST_NOT_BE_NULL);

        // this is what I propose to use so that I won't have to prepend factory name with java:comp/env/
        // I had to debug through JndiConnectionFactoryAutoConfiguration to understand why connection factory is not looked up
        connectionFactory = JndiLocatorDelegate.createDefaultResourceRefLocator().lookup("jms.ConnectionFactory", ConnectionFactory.class);
        Assert.notNull(connectionFactory, CONNECTION_FACTORY_MUST_NOT_BE_NULL);

        connectionFactory = new JndiLocatorDelegate().lookup("jms.ConnectionFactory", ConnectionFactory.class);
        // this returns null, so application fails to start
        Assert.notNull(connectionFactory, CONNECTION_FACTORY_MUST_NOT_BE_NULL);
    }
}
