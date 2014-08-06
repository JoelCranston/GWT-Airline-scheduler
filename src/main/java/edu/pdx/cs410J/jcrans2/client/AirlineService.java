/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.pdx.cs410J.jcrans2.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.pdx.cs410J.AbstractAirline;
import java.util.Set;

/**
 *
 * @author joel
 */
@RemoteServiceRelativePath("flights")
public interface AirlineService extends RemoteService {
    public Set<String> getAirlineNames();
    public Boolean addFlight(String airlineName, Flight flight);
    public AbstractAirline getAirline(String name);
    
}
