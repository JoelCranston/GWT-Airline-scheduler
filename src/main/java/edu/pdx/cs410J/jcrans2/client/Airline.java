package edu.pdx.cs410J.jcrans2.client;
import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AbstractFlight;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
/**
 * This class represents an airline.  Each airline has a name and
 * consists of multiple flights.
 * @author joel
 */
public class Airline extends AbstractAirline {
    private String name;
    /**
     * This Collection only contains AbstractFlight objects.
     * @gwt.typeArgs <edu.pdx.cs410J.AbstractFlight>
     */
    private Set<AbstractFlight> flights = new TreeSet();
    
    public Airline(){
        this.name = "NotWorking";
    }
    
    public Airline(String name){
        this.name = name;
    }
    
    /**
     * Returns the name of the Airline
     * @return The name of the airline
     */
    @Override
    public String getName() {
        return name;
    }
    /**
     * Adds a flight to the Airline
     * @param flight 
     */
    @Override
    public void addFlight(AbstractFlight flight) {
        Boolean added = flights.add(flight);
        if(!added){
            //fight alredy exists, remove the old one and add the new one.
            flights.remove(flight);
            flights.add(flight);
        }
        
    }
    /**
     * Returns a collection of flights
     * @return A collection of flights
     * @gwt.typeArgs <edu.pdx.cs410J.AbstractFlight>
     */
    @Override
    public Collection getFlights() {
        return this.flights;
    }
    
}
