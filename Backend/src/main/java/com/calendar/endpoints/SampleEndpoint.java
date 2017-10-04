package com.calendar.endpoints;

import com.calendar.Entities.User;
import com.google.api.client.util.DateTime;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.sun.javafx.util.Logging;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import sun.security.ssl.Debug;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;


/**
 * Created by valishah on 2017-08-31.
 */



@Path("/api/v1")
public class SampleEndpoint {

    User user;


    @GET
//    @Path("/authentication")
//    @Path("/code")
    public void sampleProcess(@QueryParam("state") String state, @QueryParam("code") String code, @QueryParam("scope") String scope) throws Exception{



   }


    @GET
    @Path("/xxx")
    public void test(){

        System.out.println("this is inside test ?????????????????????????????????????????????????????????????????/");

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









//    public static class TestJob implements Job {
//        @Override
//        public void execute(JobExecutionContext context) throws JobExecutionException {
//            System.out.println("this is a cron scheduled test job-----------------------------------------------------------------------------------------------------------------------");
//        }
//    }





































//    @GET
    @Path("/code")
    public void getCode(@QueryParam("state") String state, @QueryParam("code") String code, @QueryParam("scope") String scope) {

        System.out.println("Code is empty1");
        System.out.println("Code is empty2");


        System.out.println("State: " + state);
        System.out.println("Code: " + code);
        System.out.println("Scope: " + scope);

        System.out.println();
        System.out.println();
        System.out.println();

        if(code == null){
            System.out.println("Code is empty");
        }

        try {
            String url = "https://www.googleapis.com/oauth2/v4/token";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//            String urlParameters  = "code="+code+"&"+
//                    "client_id=308935464098-5o4ne078nsbi7gd0meus0lvsfn88gbls.apps.googleusercontent.com&"+
//                    "client_secret=cpyyADp0DD6HtqrXs566KNlX&"+
//                    "grant_type=authorization_code&"+
//                    "redirect_uri=http://localhost:8881/api/v1/authentication";

            String urlParameters  = "code="+code+"&"+
                    "client_id=308935464098-60g3jmr7vcnc151bi8mq0pm1s8smddlq.apps.googleusercontent.com&"+
                    "client_secret=EwQgnFRMVJXJm21v17XCIifw&"+
                    "grant_type=authorization_code&"+
                    "redirect_uri=http://localhost:8881/api/v1/code";




                    //add reuqest header
            con.setRequestMethod("POST");
//            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
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



            JSONObject responseJSON = new JSONObject(response.toString());

            user = new User("", responseJSON.getString("access_token"), "");

            System.out.println(responseJSON.getString("access_token"));
            System.out.println(response.toString());

            //get Events

            createWatch(responseJSON.getString("access_token"));
//            sendGETforEvents(responseJSON.getString("access_token"));





        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    private void createWatch(String access_token) throws Exception{



        String url = "https://www.googleapis.com/calendar/v3/calendars/vojin.pupavac@full.co/events/watch";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");

        String urlParameters  = "calendarId=vojin.pupavac@full.co&"+
                "client_id=308935464098-60g3jmr7vcnc151bi8mq0pm1s8smddlq.apps.googleusercontent.com&"+
                "client_secret=EwQgnFRMVJXJm21v17XCIifw&"+
                "grant_type=authorization_code&"+
                "token="+access_token+"&"+
                "id=6c84fb9s-12c4-11e1-840d-7b25c5ee7757";


        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
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

        //print result
        System.out.println(response.toString());





    }


    void sendGETforEvents(String token) throws Exception {


        Date dateThisDay = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateThisDay);
        cal.add(Calendar.DATE, 7); //minus number would decrement the days

        Date date7DaysLater = cal.getTime();

        com.google.api.client.util.DateTime dtThisDay = new DateTime(dateThisDay);
        com.google.api.client.util.DateTime dt7DaysLater = new DateTime(date7DaysLater);



//        String urll = "https://www.googleapis.com/calendar/v3/calendars/vojin.pupavac@full.co/events?" +
//                "access_token="+ token+"" +
//                "&timeMin=2017-09-20T10:00:00-05:00" +
//                "&timeMax=2017-09-27T10:00:00-05:00";
//        URL obj = new URL(url);
//        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//        con.setRequestMethod("GET");


        String urll = "https://www.googleapis.com/calendar/v3/calendars/vojin.pupavac@full.co/events?" +
                "access_token="+ token+"" +
                "&timeMin=2017-09-27T00:00:00-00:00" +
                "&timeMax=2017-10-04T00:00:00-00:00";


//        String urll = "https://www.googleapis.com/calendar/v3/calendars/vojin.pupavac@full.co/events?" +
//                "access_token="+ token +
//                "&timeMin=" + dtThisDay.toString() +
//                "&timeMax=" + dt7DaysLater.toString();

        //it is a google bug - no negative timezones
        urll = urll.replace('+', '-');
        System.out.println(urll);
//
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

            // print result
            System.out.println(response.toString());


            JSONObject responseJSON = new JSONObject(response.toString());
            JSONArray eventsJSON = responseJSON.getJSONArray("items");

            System.out.println();
            System.out.println();

            DatastoreService ds =  DatastoreServiceFactory.getDatastoreService();

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

                System.out.println();
                System.out.println();
                System.out.println();



                String startDateString = entity.getProperty("startTime").toString();
                Date startTime;
                if(startDateString.length() == 10)
                    startTime = new SimpleDateFormat("yyyy-MM-dd").parse(startDateString);

                else
                    startTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(startDateString);


                Calendar calendarThisDay = Calendar.getInstance();
                calendarThisDay.setTime(dateThisDay);



                System.out.println("------------------------------------------------------->>>>>> " + startDateString);
                System.out.println("------------------------------------------------------->>>>>> " + startTime.toString());


                //
                Calendar calendarNotification = Calendar.getInstance();
                calendarNotification.setTime(startTime);
                Long miliseconds = calendarNotification.getTimeInMillis() - calendarThisDay.getTimeInMillis();


                System.out.println("miliseconds: " + miliseconds);
                System.out.println("TIME ZONE: " + calendarNotification.getTimeZone().toString());
                System.out.println("NORMAL: " + calendarNotification.getTime().toString());


                TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

//                Calendar calNotifUTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//                calNotifUTC.setTime(startTime);
//                calNotifUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
//

                Calendar calNotifUTC = Calendar.getInstance();
                calNotifUTC.setTime(startTime);

                System.out.println("Maybe Works: " + calNotifUTC.getTime().toString());

                if(miliseconds > 0) {
                    Queue queue = QueueFactory.getDefaultQueue();
                    queue.add(TaskOptions.Builder.withUrl("/api/v1/notification")
                            .param("title", entity.getProperty("summary").toString())
                            .countdownMillis(miliseconds)
                            .method(TaskOptions.Method.GET));

                }

                //create notifications for events

            }







            System.out.println();
            System.out.println();
            System.out.println("Exit 0");


            System.out.println();
            System.out.println();


            System.out.println(dtThisDay.toString());
            System.out.println(dt7DaysLater.toString());

//            System.out.println(url);
            System.out.println(urll);




        } else {
            System.out.println("GET request not worked");
        }

    }




}
