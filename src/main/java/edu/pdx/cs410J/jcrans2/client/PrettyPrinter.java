package edu.pdx.cs410J.jcrans2.client;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.AirportNames;
import java.io.PrintStream;
import java.text.DateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Prints an airlines flights to a file or std.out.
 * @author joel
 */
public class PrettyPrinter implements AirlineDumper{
    PrintStream out;
    DateFormat dfLong;
    public PrettyPrinter(PrintStream out) {
        this.out = out;
        dfLong = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM);
    }
    /**
     * Prints the Airlines flights to a file or std.out
     * @param airline the Airline to print 
     */
    @Override
    public void dump(AbstractAirline airline) {
        if(out != null){
            //get all flights
            Collection<Flight> flights = airline.getFlights();
            //write to output.
            writeToStream(generateTextLines(airline, flights));
        }
    }
    /**
     * Generates the lines of text to print
     * @param airline the airline to print
     * @return a List of lines to print
     */
    private List<String> generateTextLines(AbstractAirline airline, Collection<Flight> flights) {
        List<String> lines = new ArrayList();
        int numFlights = flights.size();
        
        lines.add(airline.getName()+" has " + numFlights + (numFlights == 1 ? " flight:" : " flights:" ));
        flights.stream().forEach((f) -> {
            lines.add(printFlight((Flight)f));
        });
        return lines;
    }
    /**
     * Formats a flights information into a string.
     * @param flight
     * @return a string containing the flights information. 
     */
    private String printFlight(Flight flight){
        
        StringBuilder sb = new StringBuilder("Flight Number ");
        sb.append(flight.getNumber());
        sb.append(":\n  Departs ");
        sb.append(AirportNames.getName(flight.getSource()));
        sb.append(" on ");
        sb.append(dfLong.format(flight.getDeparture()));
        sb.append("\n  Arrives at ");
        sb.append(AirportNames.getName(flight.getDestination()));
        sb.append(" on ");
        sb.append(dfLong.format(flight.getArrival()));
        sb.append("\n  Total flight time is ");
        sb.append(getFlightTime(flight));
        sb.append(".");
        
        return sb.toString();
    }
    /**
     * Writes lines to a stream.
     * @param lines a List of lines to be written.
     */
    private void writeToStream(List<String> lines){
       for(String l: lines){
            out.println(l);
       }
       out.flush();
    }    
    /**
     * Calculates the flight time from departure and arrival times.
     * @param flight the Flight to calculate.
     * @return a String with the flight duration.
     */
    private String getFlightTime(Flight flight) {

        Duration d = Duration.between(flight.getDeparture().toInstant(),flight.getArrival().toInstant());
        long minutes = d.toMinutes();//total minutes
        long hours = d.toHours();
        //long days = d.toDays();
        minutes -= hours * 60;
        StringBuilder sb = new StringBuilder("");
        
        //out.printf("%dhours and %dminutes ",hours,minutes);
        //hours
        if(hours >= 1){
            sb.append(hours);            
            if(hours == 1){
                sb.append(" hour");
            }else{
                sb.append(" hours"); 
            }
        }
        //minutes
        if(minutes >= 1){
            //add a space after hours if present
            if(hours >= 1){
                sb.append(" ");
            }
            sb.append(minutes);
            //plural/single minutes
            if (minutes == 1){
                sb.append(" minute");
            }else{
                sb.append(" minutes");
            }
        }
        return sb.toString();
    }
    /**
     * Dumps flights for a <code>Airline</code> with a specified source and destination.
     * @param airline the airline to print  
     * @param src the source airport
     * @param dest the destination airport
     */
    public void printFlights(AbstractAirline airline, String src, String dest) {
        if(out != null){
            //get all flights
            src = src.toUpperCase().trim();
            dest = dest.toUpperCase().trim();
            Collection<Flight> flights = airline.getFlights();
            Collection<Flight> filtered = new ArrayList();
            //DEBUG  System.err.printf("src %s dest %s\n",src, dest);
            for(Flight f: flights){
                //DEBUG System.err.printf("flight %d src %s dest %s\n", f.getNumber(),f.getSource(), f.getDestination());
                if(f.getSource().contentEquals(src) && f.getDestination().contentEquals(dest)){
                    filtered.add(f);
                }            
            }
            //write to output.
            writeToStream(generateTextLines(airline, filtered));
        }
    }

    
    
}
