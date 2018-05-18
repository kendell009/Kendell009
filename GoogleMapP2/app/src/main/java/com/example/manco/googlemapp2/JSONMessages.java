package com.example.manco.googlemapp2;

import android.util.JsonWriter;
import android.util.Log;

import java.io.IOException;
import java.io.StringWriter;


public class JSONMessages {

    public String unregister(String id){
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        Log.i("MyConnectService", "unregister() kördes");
        try {
            jsonWriter.beginObject().name("type").value("unregister").name("id").value(id).endObject();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MyConnectService", "unregister() failed");
        }
        return stringWriter.toString();
    }


    public static String getMembers(String groupName){
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        Log.i("JSONMessages", "getMembers() kördes");
        try {
            jsonWriter.beginObject().name("type").value("members").name("group").value(groupName).endObject();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("JSONMessages", "getMembers fail");
        }
        return stringWriter.toString();
    }

    public String getAllGroups() {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        Log.i("JSONMessages", "getAllGroups() kördes");
        try {
            jsonWriter.beginObject().name("type").value("groups").endObject();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("JSONMessages","getAllGroups fail");
        }
        return stringWriter.toString();
    }

    public String registerGroup(String groupName, String userName) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        Log.i("JSONMessages", "registerGroup kördes");
        try {
            writer.beginObject().name("type").value("register").name("group").value(groupName).name("member").value(userName).endObject();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("JSONMessages", "writer.beginObject() fail");
        }
        return stringWriter.toString();
    }

    public static String sendPosition(double lat, double lng, String groupId) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        Log.i("JSONMessages", "sendPosition() kördes");
        try {
            jsonWriter.beginObject().name("type").value("location").name("id").value(groupId)
                    .name("longitude").value(String.valueOf(lng)).name("latitude")
                    .value(String.valueOf(lat)).endObject();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("JSONMessages", "sedPosition() failed");
        }
        return stringWriter.toString();
    }
}
