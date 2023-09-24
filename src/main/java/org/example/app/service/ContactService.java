package org.example.app.service;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.app.entity.Contact;
import java.net.URI;
import java.util.*;

public class ContactService {
    private static final List<Contact> contacts;

    static {
        contacts = new ArrayList<>();
        contacts.add(new Contact(1L, "Mary", "+380 (98) 456 38 01"));
        contacts.add(new Contact(2L, "Steve", "+380 (66) 753 12 59"));
        contacts.add(new Contact(3L, "Emma", "+380 (67) 379 25 74"));
        contacts.add(new Contact(4L, "Harry", "+380 (99) 801 66 52"));
    }

    @GET
    @Path("{id: [0-9]+}")
    public Contact getContact(@PathParam("id") Long id) {
        Contact contact = new Contact(id, null, null);

        int index = Collections.binarySearch(contacts, contact, Comparator.comparing(Contact::getId));

        if (index >= 0)
            return contacts.get(index);
        else
            throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @POST
    @Consumers({MediaType.APPLICATION_JSON})
    public Response createContact(Contact contact) {
        if (Objects.isNull(contact.getId()))
            throw new WebApplicationException(Response.Status.BAD_REQUEST);

        int index = Collections.binarySearch(contacts, contact, Comparator.comparing(Contact::getId));

        if (index < 0) {
            contacts.add(contact);
            return Response
                    .status(Response.Status.CREATED)
                    .location(URI.create(String.format("/api/v1.0/contacts/%s", contact.getId())))
                    .build();
        } else
            throw new WebApplicationException(Response.Status.CONFLICT);
    }

    @PUT
    @Path("{id: [0-9]+}")
    @Consumers({MediaType.APPLICATION_JSON})
    public Response updateContact(@PathParam("id") Long id, Contact contact) {
        contact.setId(id);
        int index = Collections.binarySearch(contacts, contact, Comparator.comparing(Contact::getId));

        if (index >= 0) {
            Contact updatedContact = contacts.get(index);
            updatedContact.setPhone(contact.getPhone());
            contacts.set(index, updatedContact);
            return Response
                    .status(Response.Status.NO_CONTENT)
                    .build();
        } else
            throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @DELETE
    @Path("{id: [0-9]+}")
    public Response deleteContact(@PathParam("id") Long id) {
        Contact contact = new Contact(id, null, null);
        int index = Collections.binarySearch(contacts, contact, Comparator.comparing(Contact::getId));

        if (index >= 0) {
            contacts.remove(index);
            return Response
                    .status(Response.Status.NO_CONTENT)
                    .build();
        } else
             throw new WebApplicationException(Response.Status.NOT_FOUND);
    }



}
