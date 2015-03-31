/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;

/**
 *
 * @author Peter K W
 */
@Stateless
public class RMSBean implements RMSBeanRemote {
    @Resource(mappedName = "jms/TopicConnectionFactory")
    private ConnectionFactory topicConnectionFactory;
    @Resource(mappedName = "jms/Topic")
    private Topic topic;
    
    public RMSBean() {
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public boolean createMessage(String name, String content, String time) {
        Connection topicConnection = null;
        Session session = null;
        MapMessage message = null;
        MessageProducer producer;
        try{
            topicConnection = topicConnectionFactory.createConnection();
            session = topicConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            topicConnection.start();
            producer = session.createProducer(topic);
            message = session.createMapMessage();
            message.setString("name", name);
            message.setString("content", content);
            message.setString("time", time);
            producer.send(message);
            System.out.println("Delivery success!");
            
        }
        catch(Exception e){
            System.err.println("RMSBean Exception");
        }
        finally{
            if(topicConnection != null){
                try{
                    topicConnection.close();
                }
                catch(Exception e){
                    System.out.println("RMSBean: Close: " + e);
                    return false;
                }
            }
        }
        return true;
    }
}
