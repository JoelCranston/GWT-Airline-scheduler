/**
 * This class represents an airline flight. Each flight has a unique number
 * identifying it, an origin airport identified by the airport's three-letter
 * code, a departure time, a destination airport identified by the airport's
 * three-letter code, and an arrival time.
 */
package edu.pdx.cs410J.jcrans2.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import edu.pdx.cs410J.AbstractFlight;
import java.io.Serializable;
import java.util.Date;

/**
 * Contains information about a flight.
 *
 * @author joel
 */
public class Flight extends AbstractFlight implements Comparable<AbstractFlight>, Serializable {

    private final Integer flightNumber;
    private final String source;
    private final String destination;
    private Date arrivalTime;
    private Date departureTime;
    //private final DateTimeFormat shortDate;
    private final transient DateTimeFormat shortDateTime = DateTimeFormat.getFormat("MM/dd/yyyy 'at' h:mm a");

    /**
     * Creates an instance of flight
     *
     * @param number a unique number
     * @param src the source airport
     * @param dest the destination airport
     */
    
    public Flight(int number, String src, String dest) {
        flightNumber = number;
        source = src;
        destination = dest;
    }
     /**
     * Creates an instance of flight
     *
     * @param number a unique number
     * @param src the source airport
     * @param dest the destination airport
     * @param depart departure time and date
     * @param arrive arrival time and date
     */
    public Flight(int number, String src, String dest, Date depart, Date arrive) {
        flightNumber = number;
        source = src;
        destination = dest;
        this.departureTime = depart;
        this.arrivalTime = arrive;
    }

    /**
     * Returns a number that uniquely identifies this flight.
     *
     * @return the flight number
     */
    @Override
    public int getNumber() {
        return flightNumber;
    }

    /**
     * Returns the source airport code
     *
     * @return source airport
     */
    @Override
    public String getSource() {
        return source;
    }

    /**
     * Returns this flight's departure time as a <code>Date</code>.
     *
     * @return the departure time
     */
    @Override
    public Date getDeparture() {
        return departureTime;
    }
    /**
     * Returns this flight's arrival time as a <code>Date</code>.
     * @return the arrival time
     */
    @Override
    public Date getArrival() {
        return arrivalTime;
    }
    /**
     * Returns a textual representation of this flight's departure time.
     *
     * @return the departure time as a DateFormat.short date and time
     */
    @Override
    public String getDepartureString() {
        
        return shortDateTime.format(departureTime);
    }

    /**
     * Returns the three-letter code of the airport at which this flight
     * terminates.
     *
     * @return the destination
     */
    @Override
    public String getDestination() {
        return destination;
    }

    /**
     * Returns a textual representation of this flight's arrival time.
     *
     * @return the arrival time
     */
    @Override
    public String getArrivalString() {
        return shortDateTime.format(arrivalTime);
    }

    /**
     * Sets the flights departure time and date string
     * @param dateString A string containing a date and time.
     */
    public void setDepartureTime(String dateString) throws IllegalArgumentException {

        Date date = shortDateTime.parse(dateString);
        departureTime = date;
    }
     /**
     * Sets the flights departure time and date using Date object
     *
     * @param date is a date instance
     */
    public void setDepartureTime(Date date) {
        departureTime = date;
    }
    /**
     * Sets the flights arrival time and date
     *
     * @param dateString
     */
    public void setArrivalTime(String dateString) throws IllegalArgumentException {
        
        Date date = shortDateTime.parse(dateString);
        arrivalTime = date;

    }
     /**
     * Sets the flights arrival time and date using Date object
     *
     * @param date is a date instance
     */
    public void setArrivalTime(Date date) {
        arrivalTime = date;
    }

    /**
     * Compares two flights based on src airport, departure time and flight number
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is
     * less than, equal to, or greater than the specified object.
     *
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException if the specified object's type prevents it
     * from being compared to this object.
     */
    
    @Override
    public int compareTo(AbstractFlight o) {
        //source
        int value; // return value
        //flight numbers must not be the same.
        if(this.flightNumber == o.getNumber()){
           return 0;
        }
        
        
        //if not the same return based on src.
        value = this.source.compareTo(o.getSource());
        if(value != 0){
            return value;
        }
        //be sure dates are not null  
        if(this.departureTime != null && o.getDeparture() != null){
            //compare departure times
            value = this.departureTime.compareTo(o.getDeparture());
            if(value != 0){
                return value;
            }
        }
        //flight number
        if (this.flightNumber < o.getNumber()) {
            return -1;
        } else if (this.flightNumber > o.getNumber()) {
            return 1;
        } else {
            return 0;//redundant.
        }

    }

}
