package com.calendar.endpoints;

import com.calendar.Entities.User;
import com.google.api.client.util.DateTime;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


/**
 * Created by valishah on 2017-08-31.
 */

@Path("/api/v1")
public class SampleEndpoint {

    private User user;
    private DatastoreService ds;



    @GET
    @Path("/weeklyreport")
    //This is a cron job.
    public void weeklyUpdatingEvents() throws Exception{


        //we should go throw all users. Excange refresh token for access token.

        String access_token = null;

        inserEventsInDSAndActiveteTaskQueues(access_token);


    }



    // This method should be called only ones per every user. We are creating Useres
    // in our Data Store with their access and refresh tokens. Every time
    // we should use refresh token for getting access one.


    @GET
    @Path("/token")
    //e-mail is AW Email address
    public Response getToken(@QueryParam("email") String email){
        System.out.println();
        System.out.println("We are in getToken");
        System.out.println();
        return Response.seeOther(URI.create("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/calendar" +
                    "&access_type=offline&include_granted_scopes=true" +
                    "&state=state_parameter_passthrough_value" +
                    "&redirect_uri=https://double-aleph-181009.appspot.com/api/v1/code" +
                    "&response_type=code" +
                    "&client_id=1070546990442-r21ln2vhlf966lt6o3r6s81egv545tl3.apps.googleusercontent.com" +
                    "&login_hint="+email+
                    "&prompt=consent")).build();

    }




    @GET
    @Path("/code")
    public void getCode(@QueryParam("state") String state, @QueryParam("code") String code, @QueryParam("scope") String scope, @QueryParam("login_hint") String email) {

        System.out.println();
        System.out.println("We are in Code");
        System.out.println();

        if(code == null){
            System.out.println("Code is empty");
            return;
        }

        ds =  DatastoreServiceFactory.getDatastoreService();

        try {
            String url = "https://www.googleapis.com/oauth2/v4/token";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            String urlParameters  = "code="+code+"&"+
                    "client_id=1070546990442-r21ln2vhlf966lt6o3r6s81egv545tl3.apps.googleusercontent.com&"+
                    "client_secret=5EyoEJI5rIalYMQK6Y6thiWj&"+
                    "grant_type=authorization_code&"+
                    "redirect_uri=https://double-aleph-181009.appspot.com/api/v1/code";

            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("Sending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);


            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println("Response: " + response.toString());
            JSONObject responseJSON = new JSONObject(response.toString());

            //Insert User in Data Store. E-mail = key;
            inserUserInDS(email, responseJSON.getString("access_token"), responseJSON.getString("token_type"),  responseJSON.getString("refresh_token"));

//            createWatch(responseJSON.getString("access_token"));
//            sendGETforEvents(responseJSON.getString("access_token"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    @POST
    @Path("/xxx")
//    @Consumes("application/json")
    public Response test(String response){

        System.out.println("this is inside test ?????????????????????????????????????????????????????????????????/");
        System.out.println(response);
        return Response.noContent().build();
    }

    @GET
    @Path("/notification")
    public void notification(@QueryParam("title") String title ) throws Exception {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("--------------------------------------------------> You have event soon : " + title);
        Calendar cal = Calendar.getInstance();
        System.out.println("Current time: " + cal.getTime().toString());
        System.out.println();
        System.out.println();

        final Logger log = Logger.getLogger(SampleEndpoint.class.getName());
        log.info("Your information log message.");

    }






    private void inserUserInDS(String email, String access_token, String token_type, String refresh_token) {

        Key key= KeyFactory.createKey("User", email);
        Entity entity = new Entity(key);

        entity.setProperty("access_token", access_token);
        entity.setProperty("token_type", token_type);
        entity.setProperty("refresh_token", refresh_token);

        ds.put(entity);
    }


    //Here we are opening connection. We should handle what to do when our calendar has some changes.
    private void createWatch(String access_token) throws Exception{

        String url = "https://www.googleapis.com/calendar/v3/calendars/vojin.pupavac@full.co/events/watch";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer "+access_token);

        con.setRequestMethod("POST");
        con.setDoOutput(true);

        OutputStream os = con.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        osw.write("{\n" +
                "\"calendarId\":\"vojin.pupavac@full.co\",\n" +
                "\"client_id\":\"5EyoEJI5rIalYMQK6Y6thiWj\",\n" +
                "\"access_token\":\""+ access_token +"\",\n" +
                "\"type\":\"web_hook\",\n" +
                "\"address\":\"https://double-aleph-181009.appspot.com/api/v1/xxx\",\n" +
                "\"id\":\"6c84fbus-12y4-11e1-840d-7b25c5ee7757\",\n" +
                "\"type\":\"web_hook\",\n" +
                "\"address\":\"https://double-aleph-181009.appspot.com/api/v1/xxx\",\n" +
                "\"id\":\"6c84fb9s-12c4-11e1-840d-7b25c5ee7757\"\n" +
                "}");
        osw.flush();
        osw.close();
        os.close();  //don't forget to close the OutputStream

        con.connect();


        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }


    //This should be called from Cron Job which will be activated weekly.
    //We should add: If access tokex expired use refresh token for getting new access token
    void sendGETforEvents(String token) throws Exception {

        Date dateThisDay = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateThisDay);
        cal.add(Calendar.DATE, 7); //minus number would decrement the days

        Date date7DaysLater = cal.getTime();

        com.google.api.client.util.DateTime dtThisDay = new DateTime(dateThisDay);
        com.google.api.client.util.DateTime dt7DaysLater = new DateTime(date7DaysLater);

        String urll = "https://www.googleapis.com/calendar/v3/calendars/vojin.pupavac@full.co/events?" +
                "access_token="+ token +
                "&timeMin=" + dtThisDay.toString() +
                "&timeMax=" + dt7DaysLater.toString();

        //it is a google bug - no negative timezones
        urll = urll.replace('+', '-');


        URL obj = new URL(urll);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            inserEventsInDSAndActiveteTaskQueues(response.toString());


            System.out.println("Exit 0");


        } else {
            System.out.println("GET events request don't work");
        }

    }

    private void inserEventsInDSAndActiveteTaskQueues(String response) throws JSONException, ParseException {


        JSONObject responseJSON = new JSONObject(response);
        JSONArray eventsJSON = responseJSON.getJSONArray("items");

        for(int i = 0; i < eventsJSON.length(); i++)
        {
            JSONObject eventJSON = eventsJSON.getJSONObject(i);

            //Skip cancelled events
            if(eventJSON.getString("status").equals("cancelled"))
                continue;


            Key key= KeyFactory.createKey("Event", eventJSON.getString("id"));
            Entity entity = new Entity(key);

            entity.setProperty("summary", eventJSON.getString("summary"));
            entity.setProperty("status", eventJSON.getString("status"));
            entity.setProperty("htmlLink", eventJSON.getString("htmlLink"));

            if (eventJSON.has("description"))
                entity.setProperty("description", eventJSON.getString("description"));

            if(eventJSON.has("location"))
                entity.setProperty("location", eventJSON.getString("location"));



            JSONObject creatorJSON = eventJSON.getJSONObject("creator");
            JSONObject startJSON = eventJSON.getJSONObject("start");
            JSONObject endJSON = eventJSON.getJSONObject("end");

            //Starting time
            if(startJSON.has("dateTime"))
                entity.setProperty("startTime", startJSON.getString("dateTime"));
            else
                entity.setProperty("startTime", startJSON.getString("date"));

            //Ending time
            if(endJSON.has("dateTime"))
                entity.setProperty("endTime", endJSON.getString("dateTime"));
            else
                entity.setProperty("endTime", endJSON.getString("date"));

            if(eventJSON.has("attendees")) {
                Text text = new Text(eventJSON.getString("attendees"));
                entity.setProperty("attendees", text);
            }

            if(eventJSON.has("attachments")) {
                Text text = new Text(eventJSON.getString("attachments"));
                entity.setProperty("attachments", text);

            }
            entity.setProperty("creatorEmail", creatorJSON.getString("email"));

            ds.put(entity);


            String startDateString = entity.getProperty("startTime").toString();
            Date startTime;
            if(startDateString.length() == 10)
                startTime = new SimpleDateFormat("yyyy-MM-dd").parse(startDateString);

            else
                startTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(startDateString);


            Calendar calendarThisDay = Calendar.getInstance();
            Date dateThisDay = new Date();
            calendarThisDay.setTime(dateThisDay);



            System.out.println("------------------------------------------------------->>>>>> " + startDateString);
            System.out.println("------------------------------------------------------->>>>>> " + startTime.toString());


            Calendar calendarNotification = Calendar.getInstance();
            calendarNotification.setTime(startTime);
            Long miliseconds = calendarNotification.getTimeInMillis() - calendarThisDay.getTimeInMillis();

            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

            Calendar calNotifUTC = Calendar.getInstance();
            calNotifUTC.setTime(startTime);

            if(miliseconds > 0) {
                Queue queue = QueueFactory.getDefaultQueue();
                queue.add(TaskOptions.Builder.withUrl("/api/v1/notification")
                        .param("title", entity.getProperty("summary").toString())
                        .countdownMillis(miliseconds)
                        .method(TaskOptions.Method.GET));

            }
        }
    }


}
