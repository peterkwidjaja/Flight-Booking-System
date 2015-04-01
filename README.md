# IS2103: Enterprise System Development (AY2014/2015 Semester 2) 
## Flight Booking System

### System Requirements
Java SDK 1.7

The system is built on NetBeans 8.0.2 for Glassfish 4.2

Warning! This is a Enterprise Java Bean (EJB) project that is full of uncertainties.

### How to get this works
1. Directory for this project is STRICTLY at C:\FlightSystem\..
2. Set up Glassfish JDBC Connection: jdbc/flight (JavaDB)
3. Sequence of running: server-bean, web-server, message-driven bean, and finally the clients

### Usual Netbeans X Glassfish EJB debug:
- Undeploy everything
- Stop Glassfish server
- Build and Clean EVERY Project. Usually the error will appear regarding the interface being locked and unable to be deleted. Just try one more time.

- Restart server and deploy the beans.
