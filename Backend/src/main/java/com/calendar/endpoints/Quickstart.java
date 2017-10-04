package com.calendar.endpoints;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.util.DateTime;

import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;


public class Quickstart {



	public Quickstart(){


		try {
			System.out.println("Before Entering");
			service = getCalendarService();

			System.out.println("After Entering");
		} catch (IOException e) {
			System.out.println("Error :((");
			e.printStackTrace();
		}

		try {
			addEvent();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	
	//voja

	String eventId1;
	String eventId2;

	/** Application name. */
	private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";

	/** Directory to store user credentials for this application. */
//	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
//			".credentials/calendar-java-quickstart");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/calendar-java-quickstart
	 */
	private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	public static Credential authorize() throws IOException {
		// Load client secrets.
		InputStream in = Quickstart.class.getResourceAsStream("/client_secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();


		//		flow.getApprovalPrompt();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
//		System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		return credential;
	}

	/**
	 * Build and return an authorized Calendar client service.
	 * 
	 * @return an authorized Calendar client service
	 * @throws IOException
	 */
	
	com.google.api.services.calendar.Calendar service;
	
	
	public com.google.api.services.calendar.Calendar getCalendarService() throws IOException {
	
		if(service != null)
			return service;
		
		Credential credential = authorize();
		return new com.google.api.services.calendar.Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
	}

	

	private void deleteEvent() throws IOException {

		service.events().delete("primary", eventId2).execute();

	}

	private void updateEvent() throws IOException {

		// Retrieve the event from the API
		Event event = service.events().get("primary", eventId1).execute();

		// Make a change
		event.setSummary("This is changed one");


		// Update the event
		Event updatedEvent = service.events().update("primary", event.getId(), event).execute();

		System.out.println(updatedEvent.getUpdated());

	}

	public void getEvents() throws IOException{


		// Retrieve a specific calendar list entry
		CalendarListEntry calendarListEntry = service.calendarList().get("primary").execute();
		System.out.println("^^^^^^^^^^^^^^^^^^");
		System.out.println(calendarListEntry.toString());
		System.out.println("________________");

		// List the next 10 events from the primary calendar.
		DateTime now = new DateTime(System.currentTimeMillis());
		Events events = service.events().list("primary").setMaxResults(10).setTimeMin(now).setOrderBy("startTime")
				.setSingleEvents(true).execute();
		List<Event> items = events.getItems();

		if (items.size() == 0) {
			System.out.println("No upcoming events found.");
		} else {

			eventId1 = items.get(0).getId();
			eventId2 = items.get(1).getId();
			System.out.println("Upcoming events");
			for (Event event : items) {
				DateTime start = event.getStart().getDateTime();
				if (start == null) {
					start = event.getStart().getDate();
				}
				System.out.printf("%s (%s)\n", event.getSummary(), start);
			}
		}


	}
	

	public void addEvent() throws IOException {

		// Refer to the Java quickstart on how to setup the environment:
		// https://developers.google.com/google-apps/calendar/quickstart/jFava
		// Change the scope to CalendarScopes.CALENDAR and delete any stored
		// credentials.

		Event event = new Event().setSummary("Google I/O 2015").setLocation("800 Howard St., San Francisco, CA 94103")
				.setDescription("VOJIN JE CARRRR!");

		DateTime startDateTime = new DateTime("2017-09-28T09:00:00-07:00");
		EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("America/Los_Angeles");
		event.setStart(start);

		DateTime endDateTime = new DateTime("2017-09-28T17:00:00-07:00");
		EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone("America/Los_Angeles");
		event.setEnd(end);

		String[] recurrence = new String[] { "RRULE:FREQ=DAILY;COUNT=2" };
		event.setRecurrence(Arrays.asList(recurrence));

		EventAttendee[] attendees = new EventAttendee[] {
				new EventAttendee().setEmail("vojin96bg@gmail.com"),
				new EventAttendee().setEmail("vojin.pupavac@full.co")
		};

		event.setAttendees(Arrays.asList(attendees));

		EventReminder[] reminderOverrides = new EventReminder[] {
				new EventReminder().setMethod("email").setMinutes(24 * 60),
				new EventReminder().setMethod("popup").setMinutes(10), };
		Event.Reminders reminders = new Event.Reminders().setUseDefault(false)
				.setOverrides(Arrays.asList(reminderOverrides));
		event.setReminders(reminders);

		String calendarId = "primary";
			event = service.events().insert(calendarId, event).execute();
		System.out.printf("Event created: %s\n", event.getHtmlLink());

	}

    public void loadMain(){
		
		System.out.println("Trying to return CREATE PAGE");

		

		try {
			service = getCalendarService();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		getEvents();
		System.out.println("Now we will try to add");
		System.out.println("Now we will try to add");
		System.out.println("Now we will try to add");
		
		
//		addEvent();
//		updateEvent();
//		deleteEvent();

		System.out.println("Method finished");
    	
    }
}