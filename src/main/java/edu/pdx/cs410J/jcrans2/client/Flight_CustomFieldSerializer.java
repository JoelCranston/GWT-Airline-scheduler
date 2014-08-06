/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.pdx.cs410J.jcrans2.client;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import java.util.Date;

/**
 *
 * @author joel
 */
public final class Flight_CustomFieldSerializer {
    public static void deserialize(SerializationStreamReader streamReader, Flight instance)
        throws SerializationException {
         
    }

    public static Flight instantiate(SerializationStreamReader streamReader)
        throws SerializationException {
        int number = streamReader.readInt();
        String src = streamReader.readString();
        String dest = streamReader.readString();
        Date depart = (Date)streamReader.readObject();
        Date arrive = (Date)streamReader.readObject();
        return new Flight(number,src,dest,depart,arrive);
         
    }

    public static void serialize(SerializationStreamWriter streamWriter, Flight instance)
        throws SerializationException {
        streamWriter.writeInt(instance.getNumber());
        streamWriter.writeString(instance.getSource());
        streamWriter.writeString(instance.getDestination());
        streamWriter.writeObject(instance.getDeparture());
        streamWriter.writeObject(instance.getArrival());
         
    }
}
