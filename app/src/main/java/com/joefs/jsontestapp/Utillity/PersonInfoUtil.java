package com.joefs.jsontestapp.Utillity;

import com.joefs.jsontestapp.Model.Person;

import org.json.JSONException;
import org.json.JSONObject;

public class PersonInfoUtil {

    public static String toJSON(Person person){
        JSONObject newPerson = new JSONObject();
        try {
            newPerson.put("Name",person.getName());
            newPerson.put("Email", person.getEmail());
            newPerson.put("Phone", person.getPhone());
            newPerson.put("Message", person.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return newPerson.toString();
    }
}
