/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Remote;

/**
 *
 * @author Peter K W
 */
@Remote
public interface RMSBeanRemote {

    boolean createMessage(String name, String content, long time);
    
}
