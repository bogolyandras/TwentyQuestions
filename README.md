# Twenty Questions

Welcome to my small project.

## Application server configuration

### Tomcat support
In the configuration file
~~~
Context.xml
~~~
add a new Resource pointing to an empty database:
~~~xml
<Resource name="jdbc/twentyquestionsDS" auth="Container" type="javax.sql.DataSource"
              username="user"
              password="password"
              driverClassName="org.postgresql.Driver"
              url="jdbc:postgresql://localhost:5432/twentyquestions"
              maxTotal="25"
              maxIdle="10"
              validationQuery="select 1" />
~~~

### Wildfly support
In the manager GUI add a new datasource named:

~~~
java:/twentyquestionsDS
~~~