/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.pdx.cs410J.jcrans2.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.jcrans2.client.Airline;
import edu.pdx.cs410J.jcrans2.client.AirlineService;
import edu.pdx.cs410J.jcrans2.client.Flight;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class AirlineServiceImpl extends RemoteServiceServlet implements AirlineService {
    
    private static final Map<String, AbstractAirline> airlines = new HashMap<>();
    /**
     * 
     * @return a set of airline names
     * @gwt.typeArgs <java.lang.String>
     */
    @Override
    public Set<String> getAirlineNames() {
        return new HashSet(airlines.keySet());
    }

    @Override
    public Boolean addFlight(String airlineName, Flight flight) {
        AbstractAirline a = airlines.get(airlineName);
        if(a != null){
            a.addFlight(flight);
            return true;
        }
        //airline does not exist (unlikly)
        else{
            System.err.println("ERR: Airline did not exist");
            return false;
        }
                
    }

     /**
     * 
     * @param name the name of the Airline to return.
     * @return a AbstractAirline
     * @gwt.typeArgs <edu.pdx.cs410J.AbstractAirline>
     */
    @Override
    public AbstractAirline getAirline(String name) {
        //if the name is not in the set create a new airline
        AbstractAirline a = airlines.get(name);
        if (a == null){
            a = new Airline(name);
            airlines.put(name, a);
            return a;
        //if it already exists return it.
        }else{
            return a;
        }
    }
    
}
