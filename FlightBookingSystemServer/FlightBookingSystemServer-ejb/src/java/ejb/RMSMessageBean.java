/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.RequestEntity;
import entity.UserEntity;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Random;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peter K W
 */
@MessageDriven(mappedName = "jms/Topic", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "RMSMessageBean"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "RMSMessageBean")
})
public class RMSMessageBean implements MessageListener {
    @PersistenceContext()
    EntityManager em;
    @Resource(mappedName="jms/TopicConnectionFactory")
    private ConnectionFactory topicConnectionFactory;
    private Random processingTime = new Random();
    public RMSMessageBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        MapMessage msg = null;
        try{
            if(message instanceof MapMessage){
                msg = (MapMessage) message;
                Thread.sleep(processingTime.nextInt(5)*1000);
                setUpRequest(msg);
            }
        }
        catch(InterruptedException ie){
            System.out.println("RMSMessageBean.onMessage: InterruptedException: " + ie.toString());
        }
        catch (Throwable te){
            System.out.println("RMSMessageBean.onMessage: Exception: " + te.toString());
        }
    }
    
    void setUpRequest(MapMessage msg){
        try{
            String name = msg.getString("name");
            String content = msg.getString("content");
            long time = msg.getLong("time");
            
            UserEntity u = em.find(UserEntity.class, name);
            if(u!=null){
                RequestEntity request = new RequestEntity();
                request.create(content,"UNREAD", "");
                request.setOwner(u);
                Timestamp t = new Timestamp(time);
                request.setTime(t);
                u.getRequests().add(request);
                em.persist(request);
            }
        }
        catch(IllegalArgumentException e){
            System.err.println("RMSMessageBean setUp: Illegal Argument "+e);
        }
        catch (Exception e){
            System.err.println("RMSMessageBean setUp: " + e);
        }
        try {
            Connection connection = topicConnectionFactory.createConnection();
        } catch (Exception ex) {
            System.out.println("RMSMessageBean.setUpEntities: Exception: Unable to connect to JMS provider: " + ex.toString());
        }        
    }
    
}
