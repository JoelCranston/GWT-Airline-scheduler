/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.pdx.cs410J.jcrans2.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.pdx.cs410J.AbstractAirline;
import java.util.Set;

/**
 *
 * @author joel
 */
public interface AirlineServiceAsync {

    public void getAirlineNames(AsyncCallback<Set<String>> callback);
    public void addFlight(String airlineName, Flight flight, AsyncCallback<Boolean> callback);
    //public void getAirline(String name,AsyncCallback<AbstractAirline> callback);
}
