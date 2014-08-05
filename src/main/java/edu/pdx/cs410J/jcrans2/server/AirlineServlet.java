package edu.pdx.cs410J.jcrans2.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.jcrans2.client.Flight;
//import edu.pdx.cs410J.jcrans2.client.PrettyPrinter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * The Airline Servlet maintains a database of airlines and their flights.
 * @author joel
 */
public class AirlineServlet extends RemoteServiceServlet {
    //airline database.
    private final Map<String, AbstractAirline> airlines;
    //Parameter Constants
    private static final String NAME = "name";
    private static final String FLIGHT_NUMBER = "flightNumber";
    private static final String DEST = "dest";
    private static final String SRC = "src";
    private static final String ARRIVE = "arriveTime";
    private static final String DEPART = "departTime";
    //date format
    private static final DateFormat shortDateTime = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    
    /**
     * Default constructor
     */
    public AirlineServlet() {
        this.airlines = new HashMap<>();
    }
//    /**
//     * Called when a get request is initiated
//     * @param request the <code>HttpServletRequest</code> request
//     * @param response the <code>HttpServletResponse</code> response
//     * @throws ServletException 
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException
//    {
//        response.setContentType("text/plain");
//        String name = getParameter(NAME, request);
//        String dest = getDestination(request);//may be null
//        String src = getSource(request);//may be null
//
//        if (name != null) {
//            //check to see if database contains the airline.
//            if (!airlines.containsKey(name)) {
//                airlineDoesNotExist(response, name);
//                return;
//            }
//            //System.err.println(name+src+dest);
//            // only dump whole airline if nither src, or dest is sent.
//            if (src != null || dest != null){
//                if(src == null){
//                    missingRequiredParameter(response, SRC);
//                }else if (dest == null){
//                    missingRequiredParameter(response, DEST);
//                }else {
//                    // if both are present perform search
//                    //writeFlights(name, response, src, dest);
//                }
//            // if none are present then dump whole airline.  
//            }else {
//                //dump all the flights.
//                //writeAirline(name, response);
//            }
//        } else {
//            missingRequiredParameter(response, NAME);
//        }
//    }
//    /**
//     * Called when HTTP POST is initiated
//     * @param request the <code>HttpServletRequest</code> request
//     * @param response the <code>HttpServletResponse</code> response
//     * @throws ServletException 
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException
//    {
//        response.setContentType("text/plain");
//
//        String airlineName = getParameter(NAME, request);
//        //check for valid airline name
//        if (airlineName == null) {
//            missingRequiredParameter(response, NAME);
//            return;
//        }
//        // check database for the name
//        if (!airlines.containsKey(airlineName)) {
//            //add the airline to the database
//            airlines.put(airlineName, new Airline(airlineName));
//        }
//        //add a flight to the airline.
//        Flight newFlight = createFlight(request);
//        if (newFlight != null) {
//            airlines.get(airlineName).addFlight(newFlight);
//            response.setStatus(HttpServletResponse.SC_OK);
//        } else {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        }
//
//    }
//
//    /**
//     * Sends all flights in an airline to the Pretty printer.
//     *
//     * @param name the name of the airline
//     * @param response the client to send the response to.
//     */
//    private void writeAirline(String name, HttpServletResponse response) {
//        AbstractAirline airline = airlines.get(name);
//        //get the output stream for the servlet, create a printstream and send it to the PrettyPrinter.
//        PrettyPrinter printer;
//        try (PrintStream out = (new PrintStream(response.getOutputStream()))) {
//            printer = new PrettyPrinter(out);
//            printer.dump(airline);
//        } catch (IOException ex) {
//            System.err.println("error opening printstream for servlet");
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            return;
//        }
//        response.setStatus(HttpServletResponse.SC_OK);
//    }
//
//    /**
//     * Sends the flights with the specified source and destination to the
//     * PrettyPrinter.
//     *
//     * @param name the name of the airline to search in.
//     * @param response the client to send the response to.
//     * @param src the source airport.
//     * @param dest the destination airport.
//     */
//    private void writeFlights(String name, HttpServletResponse response, String src, String dest) {
//        AbstractAirline airline = airlines.get(name);
//        //get the output stream for the servlet, create a printstream and send it to the PrettyPrinter.
//        PrettyPrinter printer;
//        try (PrintStream out = (new PrintStream(response.getOutputStream()))) {
//            printer = new PrettyPrinter(out);
//            printer.printFlights(airline, src.toUpperCase(), dest.toUpperCase());
//        } catch (IOException ex) {
//            System.err.println("error opening printstream for servlet");
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            return;
//        }
//        response.setStatus(HttpServletResponse.SC_OK);
//    }
//
//    /**
//     * Sends an error if the required parameter is not provided.
//     *
//     * @param response
//     * @param param
//     */
//    private void missingRequiredParameter(HttpServletResponse response, String param) {
//
//        try (PrintWriter pw = response.getWriter()) {
//            pw.println(String.format("The required parameter \"%s\" is missing", param));
//            pw.flush();
//            //pw.println(Messages.getMappingCount( data.size() ));
//        } catch (IOException ex) {
//            System.err.println("error creating PrintWriter for servlet");
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            return;
//        }
//        response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
//    }
//
//    /**
//     * Sends a reply to the requesting client indicating that the airline name
//     * does not exist in the database. It has no flights.
//     *
//     * @param response the <code>HttpServletResponse</code>
//     * @param name the name of the airline
//     */
//    private void airlineDoesNotExist(HttpServletResponse response, String name) {
//
//        try (PrintWriter pw = response.getWriter()) {
//            pw.println("There are no flights, " + name + " does not exist in the database yet");
//            pw.flush();
//        } catch (IOException ex) {
//            System.err.println("error creating PrintWriter for servlet");
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            return;
//        }
//        response.setStatus(HttpServletResponse.SC_OK);
//    }
//
//    /**
//     * Returns the value associated with a given parameter
//     *
//     * @param name the name of the parameter
//     * @param request the HttpServletRequest
//     * @return the value associated with name
//     */
//    private String getParameter(String name, HttpServletRequest request) {
//        String value = request.getParameter(name);
//
//        if (value == null || "".equals(value)) {
//            return null;
//
//        } else {
//            return value;
//        }
//    }
//
//    /**
//     * Creates a Flight from the POST parameters.
//     *
//     * @param request the POST
//     * @return a flight
//     */
//    private static Flight createFlight(HttpServletRequest request) {
//        Integer number = getFlightNumber(request);
//        String src = getSource(request);
//        String dest = getDestination(request);
//        Date depart = getDate(request.getParameter(DEPART));
//        Date arrive = getDate(request.getParameter(ARRIVE));
//        if (src == null || dest == null || number == null || depart == null || arrive == null) {
//            //if anything is null then abort.
//            //System.err.println("incountered a null while creating a flight.");
//            return null;
//        }
//        Flight flight = new Flight(number, src, dest);
//        flight.setArrivalTime(arrive);
//        flight.setDepartureTime(depart);
//        return flight;
//    }
//
//    /*
//     * Valid date and time formats 
//     * m/d/yy h:mm and mm/dd/yyyy hh:mm
//     */
//    private static final String DATE = "^(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)?\\d{2})$";
//    private static final String TIME = "[0-2]{0,1}\\d:[0-5]{0,1}\\d";
//    private static final String MERIDIAN = "AM|am|pm|PM";
//
//    /**
//     * Extracts and validates the source from the arguments list
//     *
//     * @param args the list of arguments
//     * @return the three digit airport code.
//     */
//    private static String getSource(HttpServletRequest request) {
//        String code = request.getParameter(SRC);
//        if (code == null) {
//            return null;
//        }
//        if (AirportNames.getName(code.toUpperCase()) == null) {
//            //System.err.println("Invalid source airport code");
//            return null;
//        }
//        return code.toUpperCase();
//    }
//
//    /**
//     * Extracts and validates the destination from the arguments list
//     *
//     * @param args the list of arguments
//     * @return the three digit airport code.
//     */
//    private static String getDestination(HttpServletRequest request) {
//        String code = request.getParameter(DEST);
//        if (code == null) {
//            return null;
//        }
//        if (AirportNames.getName(code.toUpperCase()) == null) {
//            System.err.println("Invalid source airport code");
//            return null;
//        }
//        return code.toUpperCase();
//    }
//
//    /**
//     * Extracts and validates the time and date from the a <code>String</code>.
//     *
//     * @param value the string containing the date and time.
//     * @return a <code>Date</code>.
//     */
//    private static Date getDate(String value) {
//        //validate ?
//        String[] date = value.split(" ");
//        if (date.length == 3) {
//            if (date[0].matches(DATE) && date[1].matches(TIME) & date[2].matches(MERIDIAN)) {
//                try {
//                    Date dateTime = shortDateTime.parse(value);
//                    if (dateTime != null) {
//                        return dateTime;
//                    }
//                } catch (ParseException e) {
//                    //fall through       
//                }
//            }
//        }
//
//        //System.err.println("Invalid time or date format");
//        return null;
//    }
//
//    /**
//     * Extracts and validates the flight number from the arguments list.
//     *
//     * @param args the list of arguments
//     * @return the flight number.
//     */
//    private static Integer getFlightNumber(HttpServletRequest request) {
//        try {
//            return Integer.parseInt(request.getParameter(FLIGHT_NUMBER));
//        } catch (IllegalArgumentException e) {
//            //System.err.println("Invalid Flight number");
//            return null;
//        }
//
//    }
//    protected AbstractAirline getAirline(String name){
//        return airlines.get(name);
//    }
}
