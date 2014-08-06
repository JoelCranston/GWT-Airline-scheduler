package edu.pdx.cs410J.jcrans2.client;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AirportNames;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A basic GWT class that makes sure that we can send an airline back from the
 * server
 */
public class AirlineGwt implements EntryPoint {

    //layout panels
    private final TabLayoutPanel flightsTabPanel;
    private final VerticalPanel mainPanel;
    private final VerticalPanel addFlightTab;
    private final VerticalPanel filterFlightTab;
    private final ScrollPanel allFlightTab;

    //flight creation
    private TextBox flightNumberBox;
    private SuggestBox srcBox;
    private SuggestBox destBox;
    private DateBox departDateBox;
    private DateBox arriveDateBox;

    //flight filter
    private final Label noFlights;
    private SuggestBox srcFilterBox;
    private SuggestBox destFilterBox;

    //airline name
    private final Button createAirlineButton;
    private ListBox selectAirlineListBox;
    private final TextBox airlineNameTextBox;
    private final Label selectAirlineLabel;

    //menu
    private final MenuBar menu;
    private final MenuBar helpMenu;
    private final MenuItem readmeMenuItem;
    
    //callbacks
    AirlineServiceAsync airlineService;
    final AsyncCallback<Set<String>> getAirlineNamesAsync;
    final AsyncCallback<AbstractAirline> getAirlineAsync;
    final AsyncCallback<Boolean> addFlightAsync;
    //DATA
    private Set<String> airlines;
    private String currentAirlineName;
    private AbstractAirline currentAirline;
    CellTable<Flight> allFlightTable;
    CellTable<Flight> filterFlightTable;

    //HTML Formatted readmes
    String README
            = "<title>README</title><font size=\"5\" color=\"black\">CS410J Project5</font><br>"
            + "<font size=\"3\">A GWT web application that creates and displays airlines and their flights.<br>"
            + "Author: Joel Cranston<br></font>";
    /**
     * Constructor for AirlineGwt
     */
    public AirlineGwt() {
        this.helpMenu = new MenuBar(true);
        this.menu = new MenuBar();
        this.selectAirlineLabel = new Label("Select an exiting airline or create a new one");
        this.airlineNameTextBox = new TextBox();
        this.airlineNameTextBox.setText("Enter Name");
        this.createAirlineButton = new Button("Continue");
        this.mainPanel = new VerticalPanel();
        this.flightsTabPanel = new TabLayoutPanel(1.5, Unit.EM);
        this.addFlightTab = new VerticalPanel();
        this.filterFlightTab = new VerticalPanel();
        this.allFlightTab = new ScrollPanel();
        this.noFlights = new Label("No flights Found");
        this.readmeMenuItem = new MenuItem("readme", false, new Command() {
            @Override
            public void execute() {
                final BasicPopup popup = new BasicPopup(README);
                popup.setGlassEnabled(true);
                popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
                    @Override
                    public void setPosition(int offsetWidth, int offsetHeight) {
                        int left = (Window.getClientWidth() - offsetWidth) / 3;
                        int top = (Window.getClientHeight() - offsetHeight) / 3;
                        popup.setPopupPosition(left, top);
                    }
                });
            }
        });
        this.getAirlineNamesAsync = new AsyncCallback<Set<String>>() {
            @Override
            public void onSuccess(Set<String> names) {
                airlines = names;
                setupMainPanel();
            }

            @Override
            public void onFailure(Throwable caught) {
                final BasicPopup popup = new BasicPopup("<font size=\"4\" color=\"black\">"
                        + "An Error Ocurred retrieving the Airline Names from the Server.</font>");
                popup.setGlassEnabled(true);
                popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
                    @Override
                    public void setPosition(int offsetWidth, int offsetHeight) {
                        int left = (Window.getClientWidth() - offsetWidth) / 3;
                        int top = (Window.getClientHeight() - offsetHeight) / 3;
                        popup.setPopupPosition(left, top);
                    }
                });
                airlines = new HashSet();//make an empty set 
                setupMainPanel();
            }
        };
        this.getAirlineAsync = new AsyncCallback<AbstractAirline>() {
            @Override
            public void onSuccess(AbstractAirline result) {
                setCurrentAirline(result);
            }
            @Override
            public void onFailure(Throwable caught) {
                final BasicPopup popup = new BasicPopup("<font size=\"4\" color=\"black\">"
                        + "An Error Ocurred retrieving the Airline's Flights from the server,<br>"
                        + "Please try again.</font>");
                popup.setGlassEnabled(true);
                popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
                    @Override
                    public void setPosition(int offsetWidth, int offsetHeight) {
                        int left = (Window.getClientWidth() - offsetWidth) / 3;
                        int top = (Window.getClientHeight() - offsetHeight) / 3;
                        popup.setPopupPosition(left, top);
                    }
                });
            }
        };
        this.addFlightAsync = new AsyncCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    //do nothing, everything is ok.
                } else {
                    final BasicPopup popup = new BasicPopup("<font size=\"4\" color=\"black\">"
                            + "An Error Ocurred retrieving while sending the flight to "
                            + "the server,<br>The specified Airline did not exist.</font>");
                    popup.setGlassEnabled(true);
                    popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
                        @Override
                        public void setPosition(int offsetWidth, int offsetHeight) {
                            int left = (Window.getClientWidth() - offsetWidth) / 3;
                            int top = (Window.getClientHeight() - offsetHeight) / 3;
                            popup.setPopupPosition(left, top);
                        }
                    });
                    //maybe call getAirline ????
                }
            }
            @Override
            public void onFailure(Throwable caught) {
                final BasicPopup popup = new BasicPopup("<font size=\"4\" color=\"black\">"
                        + "An error occurred while updating the server flight database,"
                        + "<br> your flight was not saved to the server.</font>");
                popup.setGlassEnabled(true);
                popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
                    @Override
                    public void setPosition(int offsetWidth, int offsetHeight) {
                        int left = (Window.getClientWidth() - offsetWidth) / 3;
                        int top = (Window.getClientHeight() - offsetHeight) / 3;
                        popup.setPopupPosition(left, top);
                    }
                });
            }
        };
    }
    /**
     * Sets up the mainPanel 
     */
    private void setupMainPanel() {
        setupAirlinesListBox();//using empty set
        mainPanel.add(selectAirlineLabel);
        mainPanel.add(selectAirlineListBox);
        mainPanel.add(airlineNameTextBox);
        mainPanel.add(createAirlineButton);
        mainPanel.add(flightsTabPanel);
        mainPanel.setVisible(true);
    }
    /**
     * Class for creation of a popup that can be passed text to be displayed in HTML.
     */
    private static class BasicPopup extends PopupPanel {
        /**
         * Constructor for a basic popup.
         * @param text Text to be interpreted as HTML
         */
        public BasicPopup(String text) {
            super(true);//autohide
            setWidget(new HTML(text));
        }
    }

    @Override
    public void onModuleLoad() {
        airlineService = GWT.create(AirlineService.class);
        
        //====================================================================//
        //                       Select Button
        //====================================================================//
        createAirlineButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String name = airlineNameTextBox.getText();
                if (name.contentEquals("") || name.contentEquals("Enter Name")) {
                    //longer help message incase they just pressed continue.
                    selectAirlineLabel.setText("Create a new airline by entering the name and pressing continue");
                    return; ///exit without generating callback
                } else {
                    airlineService.getAirline(name, getAirlineAsync);// call set current Airline. 
                    //is a set so we dont have to worry about duplicates.
                    airlines.add(name); 
                    //reset the text box.
                    airlineNameTextBox.setText("Enter Name");
                }

            }
        });

        //====================================================================//
        //                       Add flight tab
        //====================================================================//
        //airport code box suggestions.
        MultiWordSuggestOracle airports = new MultiWordSuggestOracle();
        //add all the keys to the oracle
        airports.addAll(AirportNames.getNamesMap().keySet());

        //dateboxes
        DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM/dd/yyyy h:mm a");
        departDateBox = new DateBox();
        departDateBox.setFormat(new DateBox.DefaultFormat(dateFormat));
        departDateBox.getDatePicker().setYearArrowsVisible(true);
        arriveDateBox = new DateBox();
        arriveDateBox.setFormat(new DateBox.DefaultFormat(dateFormat));
        arriveDateBox.getDatePicker().setYearArrowsVisible(true);

        flightNumberBox = new TextBox();
        Label numberLabel = new Label("Enter flight Number");

        HorizontalPanel srcDepartPanel = new HorizontalPanel();
        HorizontalPanel srcDepartLabels = new HorizontalPanel();
        Label srcLabel = new Label("Enter source airport code");
        Label departLabel = new Label("Enter departure date and time");// <HH/dd/yyyy h:mm a>");

        srcBox = new SuggestBox(airports);
        srcDepartLabels.add(srcLabel);
        srcDepartLabels.add(departLabel);
        srcDepartPanel.add(srcBox);
        srcDepartPanel.add(departDateBox);
        srcDepartLabels.setWidth("800px");//setPixelSize(800, 40);
        srcDepartLabels.setCellWidth(srcLabel, "400px");
        srcDepartPanel.setWidth("800px");//setPixelSize(800, 40);
        srcDepartPanel.setCellWidth(srcBox, "400px");

        HorizontalPanel destArrivePanel = new HorizontalPanel();
        HorizontalPanel destArriveLabels = new HorizontalPanel();
        Label destLabel = new Label("Enter destination airport code");
        Label arriveLabel = new Label("Enter arrival date and time");
        destBox = new SuggestBox(airports);
        destArriveLabels.add(destLabel);
        destArriveLabels.add(arriveLabel);
        destArrivePanel.add(destBox);
        destArrivePanel.add(arriveDateBox);
        destArriveLabels.setWidth("800px");//setPixelSize(800, 40);
        destArriveLabels.setCellWidth(destLabel, "400px");
        destArrivePanel.setWidth("800px");//setPixelSize(800, 40);
        destArrivePanel.setCellWidth(destBox, "400px");

        Button addFlightButton = new Button("Add the Flight");
        addFlightButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Flight f = addFlight();
                //if flight was created update the server
                if(f != null){
                    airlineService.addFlight(currentAirlineName,f,addFlightAsync);
                }            
            }
        });

        addFlightTab.add(numberLabel);
        addFlightTab.add(flightNumberBox);
        addFlightTab.add(srcDepartLabels);
        addFlightTab.add(srcDepartPanel);
        addFlightTab.add(destArriveLabels);
        addFlightTab.add(destArrivePanel);
        addFlightTab.add(addFlightButton);
        //====================================================================//
        //                     Filtered flights Tab
        //====================================================================//
        Label filterLabel = new Label(
                "Enter a source and destination airport codes to see all flights between them");
        Label filterSrcLabel = new Label("Source airport code");
        Label filterDestLabel = new Label("Destination airport code");
        HorizontalPanel filterLabels = new HorizontalPanel();
        HorizontalPanel airportFilterPanel = new HorizontalPanel();
        filterLabels.setWidth("800px");
        airportFilterPanel.setWidth("800px");
        filterLabels.add(filterSrcLabel);
        filterLabels.add(filterDestLabel);
        filterLabels.setCellWidth(filterSrcLabel, "300px");
        srcFilterBox = new SuggestBox(airports);
        destFilterBox = new SuggestBox(airports);

        Button filterFlightsButton = new Button("Search");
        filterFlightsButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                filterFlights();
            }
        });
        airportFilterPanel.add(srcFilterBox);
        airportFilterPanel.setCellWidth(srcFilterBox, "300px");
        airportFilterPanel.add(destFilterBox);
        airportFilterPanel.add(filterFlightsButton);
        filterFlightTab.add(filterLabel);
        filterFlightTab.add(filterLabels);
        filterFlightTab.add(airportFilterPanel);

        //====================================================================//
        //                      all flights Tab
        //====================================================================//
        //allFlightTab.setHeight("380px");
        //====================================================================//
        //                    Flights tab panel
        //====================================================================//
        flightsTabPanel.setVisible(false);
        flightsTabPanel.add(allFlightTab, "All Flights");
        flightsTabPanel.add(filterFlightTab, "Flights by Airport");
        flightsTabPanel.add(addFlightTab, "Add a Flight");
        flightsTabPanel.setPixelSize(800, 600);

        //====================================================================//
        
        //menu
        //readmeMenuItem setup in constructor.
        helpMenu.addItem(readmeMenuItem);
        menu.addItem("Help", helpMenu);
        
        


        //mainPanel needs to be hidden until server callback is called.
        mainPanel.setVisible(false);
        
        //get list of airlines from server

        airlineService.getAirlineNames(getAirlineNamesAsync);
                    //callback should: 
                    //  on failure:
                    //      notify user that database was not accessed.
                    //  on success or failure:
                    //      Add widgets to mainPanel
                    //      call setupAirlinesListBox() as it need a list of airlines.
                    //      sets mainPanel to visable. so user can interact.
        
        
        //Root Panel
        RootPanel rootPanel = RootPanel.get();
        rootPanel.add(menu); 
        rootPanel.add(mainPanel);

    }
    //========================================================================//
    //                       End of OnModule Load
    //========================================================================//

   /**
    * Creates and sets up a listBox with the airline names.
    *  *** Depends on airlines being populated ***
    */
    private void setupAirlinesListBox() {

        selectAirlineListBox = new ListBox();
        selectAirlineListBox.addItem("Create a new Airline");
                for (String s : airlines) {
            selectAirlineListBox.addItem(s);
        }
        selectAirlineListBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                int index = selectAirlineListBox.getSelectedIndex();
                //unhide add airline widgets if "create new" is selected.
                if (index == 0) {
                    airlineNameTextBox.setVisible(true);
                    createAirlineButton.setVisible(true);
                    selectAirlineLabel.setText("Create a new airline by entering the name and pressing continue");
                    selectAirlineLabel.setVisible(true);
                    flightsTabPanel.setVisible(false);
                } else {
                    //set the airline to the name in the list box
                    airlineService.getAirline(selectAirlineListBox.getItemText(index), getAirlineAsync);
                }
            }
        });
    }
    
    
    /**
     * Adds a flight to the current airline.
     */
    private Flight addFlight() {

        //first validate entered information.
        int flightnum;
        try {
            flightnum = Integer.parseInt(flightNumberBox.getText());
        } catch (NumberFormatException e) {
            //popup error message
            final BasicPopup popup = new BasicPopup("<font size=\"4\" color=\"black\">"
                    + "Invalid Flight Number Format</font>");
            popup.setGlassEnabled(true);
            popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
                @Override
                public void setPosition(int offsetWidth, int offsetHeight) {
                    int left = (Window.getClientWidth() - offsetWidth) / 3;
                    int top = (Window.getClientHeight() - offsetHeight) / 3;
                    popup.setPopupPosition(left, top);
                }
            });
            return null;
        }
        //check if a valid date was entered.
        if (departDateBox.getValue() == null || arriveDateBox.getValue() == null) {
            final BasicPopup popup = new BasicPopup("<font size=\"4\" color=\"black\">"
                    + "Enter a valid date and time for departure and arrival"
                    + "<br>Date/Time format is MM/dd/yyyy hh:mm a</font>");
            popup.setGlassEnabled(true);
            popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
                @Override
                public void setPosition(int offsetWidth, int offsetHeight) {
                    int left = (Window.getClientWidth() - offsetWidth) / 3;
                    int top = (Window.getClientHeight() - offsetHeight) / 3;
                    popup.setPopupPosition(left, top);
                }
            });
            return null;
        }
        //check to see if arrival is after departure
        if (departDateBox.getValue().after(arriveDateBox.getValue())) {
            final BasicPopup popup = new BasicPopup("<font size=\"4\" color=\"black\">"
                    + "The departure time can not occur after the arrival time!"
                    + "<br>Date/Time format is MM/dd/yyyy hh:mm a</font>");
            popup.setGlassEnabled(true);
            popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
                @Override
                public void setPosition(int offsetWidth, int offsetHeight) {
                    int left = (Window.getClientWidth() - offsetWidth) / 3;
                    int top = (Window.getClientHeight() - offsetHeight) / 3;
                    popup.setPopupPosition(left, top);
                }
            });
            return null;
        }
        //verify src and dest airport codes.
        String src = srcBox.getText().toUpperCase();
        String dest = destBox.getText().toUpperCase();
        if (AirportNames.getName(src) == null || AirportNames.getName(dest) == null) {
            final BasicPopup popup = new BasicPopup("<font size=\"4\" color=\"black\">"
                    + "Invalid source or destination airport code</font>");
            popup.setGlassEnabled(true);
            popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
                @Override
                public void setPosition(int offsetWidth, int offsetHeight) {
                    int left = (Window.getClientWidth() - offsetWidth) / 3;
                    int top = (Window.getClientHeight() - offsetHeight) / 3;
                    popup.setPopupPosition(left, top);
                }
            });
            return null;
        }
        ///all test passed so create a flight.
        Flight newFlight = new Flight(flightnum,
                src, dest,
                departDateBox.getValue(),
                arriveDateBox.getValue());
        currentAirline.addFlight(newFlight);
        //Update the all flights tab and switch back
        //in order to insure correct ording we create a new table
        allFlightTab.remove(allFlightTable);
        allFlightTable = setupFlightsTable();
        getAllFlights();
        allFlightTab.add(allFlightTable);
        flightsTabPanel.selectTab(0);
        //flightsTabPanel.
        // clean up textboxes
        flightNumberBox.setText("");
        return newFlight;

    }


    //sets up the application to use the specified airline.
    /**
     * Sets the programs working airline 
     * Should be call from a async Callback
     * @param name name of the airline to use.
     */
    private void setCurrentAirline(AbstractAirline airline) {
        currentAirlineName = airline.getName();
        currentAirline = airline;
        //sets List box to new airline adding entry if needed.
        setListBox(selectAirlineListBox, currentAirlineName);
           
        //remove the unnessary widgets
        airlineNameTextBox.setVisible(false);
        createAirlineButton.setVisible(false);
        selectAirlineLabel.setVisible(false);
        //populate the flights tab before displaying it.
        //get new table so the flights from the old will not persist.
        //only remove it if it exists.
        if (allFlightTable != null) {
            allFlightTab.remove(allFlightTable);
        }
        //do the same for filtered flights.
        if (filterFlightTable != null) {
            filterFlightTab.remove(filterFlightTable);
        }
        //create the flights table.
        allFlightTable = setupFlightsTable();
        getAllFlights();
        allFlightTab.add(allFlightTable);
        //if no flights start in addflight panel
        if (currentAirline.getFlights().isEmpty()) {
            flightsTabPanel.selectTab(2);
        } else {
            flightsTabPanel.selectTab(0);
        }
        //flightsTabPanel.forceLayout();
        flightsTabPanel.setVisible(true);

    }
 
    /**
     * sets a list box selection to the item with a specific string,
     * It is case sensitive.
     * @param widget the lListBox
     * @param name name of item to set to 
     * @return true if name was all ready present, false if it was added.
     */
    private Boolean setListBox(ListBox widget, String name) {
        int i = 1;
        for (; i < widget.getItemCount(); i++) {
            if (widget.getItemText(i).equals(name)) {
                widget.setSelectedIndex(i);
                return true;
            }
        }
        widget.addItem(name);
        widget.setSelectedIndex(i);
        
        return false;
    }

    /**
     * Creates and configures an empty CellTable 
     * @return the CellTable
     */
    private CellTable<Flight> setupFlightsTable() {
        CellTable<Flight> flights = new CellTable<>(30);
        //Flight number
        TextColumn<Flight> numberColumn = new TextColumn<Flight>() {
            @Override
            public String getValue(Flight object) {
                return Integer.toString(object.getNumber());
            }
        };
        flights.addColumn(numberColumn, "Flight Number");
        flights.setColumnWidth(numberColumn, "140px");
        //Source Airport
        TextColumn<Flight> srcColumn = new TextColumn<Flight>() {
            @Override
            public String getValue(Flight object) {
                return object.getSource();
            }
        };
        flights.addColumn(srcColumn, "Source");
        flights.setColumnWidth(srcColumn, "90px");
        //Destination airport
        TextColumn<Flight> destColumn = new TextColumn<Flight>() {
            @Override
            public String getValue(Flight object) {
                return object.getDestination();
            }
        };
        flights.addColumn(destColumn, "Destination");
        flights.setColumnWidth(destColumn, "110px");
        //Departure Time
        DateCell dateCell = new DateCell(DateTimeFormat.getFormat("MM/dd/yyyy h:mm a"));
        Column<Flight, Date> departColumn = new Column<Flight, Date>(dateCell) {
            @Override
            public Date getValue(Flight object) {
                return object.getDeparture();
            }
        };
        flights.addColumn(departColumn, "Departure Time");
        flights.setColumnWidth(departColumn, "170px");
        //Arrival time
        Column<Flight, Date> arriveColumn = new Column<Flight, Date>(dateCell) {
            @Override
            public Date getValue(Flight object) {
                return object.getArrival();
            }
        };
        flights.addColumn(arriveColumn, "Arrival Time");
        flights.setColumnWidth(arriveColumn, "170px");

        //Details Button
        ButtonCell buttonCell = new ButtonCell();
        Column buttonColumn = new Column<Flight, String>(buttonCell) {
            @Override
            public String getValue(Flight object) {
                return "View";
            }
        };
        buttonColumn.setFieldUpdater(new FieldUpdater<Flight, String>() {
            @Override
            public void update(int index, Flight object, String value) {
                final BasicPopup popup = new BasicPopup(getFlightDetails(object));
                popup.setGlassEnabled(true);
                popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
                    @Override
                    public void setPosition(int offsetWidth, int offsetHeight) {
                        int left = (Window.getClientWidth() - offsetWidth) / 3;
                        int top = (Window.getClientHeight() - offsetHeight) / 3;
                        popup.setPopupPosition(left, top);
                    }
                });
            }

        });
        flights.addColumn(buttonColumn, "Details");
        flights.setWidth("800px", true);
        flights.setLoadingIndicator(noFlights);
        return flights;
    }

    // loads the table with all the flights in the current Airline
    private void getAllFlights() {
        if(currentAirline != null){
            allFlightTable.setRowData(0, new ArrayList(currentAirline.getFlights()));
        }else{
            System.err.println("ERR: Current Airline Was NULL");               ////////////DEBUG//////////////
        }
    }

    private void filterFlights() {
        if (filterFlightTable != null) {
            filterFlightTab.remove(filterFlightTable);
        }
        filterFlightTable = setupFlightsTable();
        String src = srcFilterBox.getText().toUpperCase();
        String dest = destFilterBox.getText().toUpperCase();

        if (AirportNames.getName(src) == null || AirportNames.getName(dest) == null) {
            final BasicPopup popup = new BasicPopup("<font size=\"4\" color=\"black\">"
                    + "Invalid source or destination airport code</font>");
            popup.setGlassEnabled(true);
            popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
                @Override
                public void setPosition(int offsetWidth, int offsetHeight) {
                    int left = (Window.getClientWidth() - offsetWidth) / 3;
                    int top = (Window.getClientHeight() - offsetHeight) / 3;
                    popup.setPopupPosition(left, top);
                }
            });
            return;
        }
        List filtered = new ArrayList();
        //new ArrayList()
        for (Flight f : (Collection<Flight>) currentAirline.getFlights()) {
            if (f.getSource().contentEquals(src) && f.getDestination().contentEquals(dest)) {
                filtered.add(f);
            }
        }
        filterFlightTable.setRowData(0, filtered);
        filterFlightTab.add(filterFlightTable);
    }

    /**
     * Creates a string containing detailed information about a flight.
     *
     * @param flight the Flight to obtain the information from.
     * @return the HTML formated string containing the flight information.
     */
    private String getFlightDetails(Flight flight) {
        //DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM/dd/yyyy 'at' h:mm a");
        DateTimeFormat dateFormat = DateTimeFormat.getFormat("EEE, MMM d, yyyy 'at' h:mm a");
        //String depart = dateFormat.format(flight.getDeparture());
        //String arrive = dateFormat.format(flight.getArrival());
                
        StringBuilder sb = new StringBuilder("<font size=\"3\" color=\"black\"> Flight Number ");
        sb.append(flight.getNumber());
        sb.append(":<br>  Departs ");
        sb.append(AirportNames.getName(flight.getSource()));
        sb.append(" on ");
        sb.append(dateFormat.format(flight.getDeparture()));
        sb.append("<br>  Arrives at ");
        sb.append(AirportNames.getName(flight.getDestination()));
        sb.append(" on ");
        sb.append(dateFormat.format(flight.getArrival()));
        sb.append("<br>  Total flight time is ");
        sb.append(getFlightTime(flight));
        sb.append(".<font>");

        return sb.toString();
    }

    /**
     * Calculates the Flight time.
     *
     * @param flight the Flight to use in the calculation.
     * @return a string containing the flight time in hours and minutes.
     */
    private String getFlightTime(Flight flight) {

        Date depart = flight.getDeparture();
        Date arrive = flight.getArrival();
        long diff = arrive.getTime() - depart.getTime();
        //60 seconds in minute * 1000 millisecond in second,
        long minutes = (diff / (60 * 1000l)) % 60;
        long hours = diff / (60 * 60 * 1000l);

        StringBuilder sb = new StringBuilder();

        //hours
        if (hours >= 1) {
            sb.append(hours);
            if (hours == 1) {
                sb.append(" hour");
            } else {
                sb.append(" hours");
            }
        }
        //minutes
        if (minutes >= 1) {
            //add a space after hours if present
            if (hours >= 1) {
                sb.append(" ");
            }
            sb.append(minutes);
            //plural/single minutes
            if (minutes == 1) {
                sb.append(" minute");
            } else {
                sb.append(" minutes");
            }
        }
        return sb.toString();
    }
}
