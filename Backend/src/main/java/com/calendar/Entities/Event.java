package com.calendar.Entities;

import java.util.ArrayList;
import java.util.Date;

public class Event {

    private String id;
    private String title;
    private String description;
    private String htmlLink;
    private String location;
    private String status;

    private String creatorEmail;
    private ArrayList<String> attachmentsURLs;              //
    private ArrayList<String> attendeesEmails;              //
    private ArrayList<Reminder> reminders;                  //

    private Date startDate;
    private Date endDate;

}
