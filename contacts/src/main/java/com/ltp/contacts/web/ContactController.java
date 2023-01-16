package com.ltp.contacts.web;

import com.ltp.contacts.exception.ErrorResponse;
import com.ltp.contacts.pojo.Contact;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.ltp.contacts.service.ContactService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@Tag(name = "Contact Controller", description = "Create and retrieve contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @ApiResponse(responseCode = "200", description = "Successful retrieval of contacts",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Contact.class))))
    @Operation(summary = "Retrieves contacts", description = "Provides a list of all contacts")
    @GetMapping(value = "/contact/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Contact>> getContacts() {
        List<Contact> contacts = contactService.getContacts();
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Contact doesn't exist",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "200", description = "Successful retrieval of contact",
                    content = @Content(schema = @Schema(implementation = Contact.class))),
    })
    @Operation(summary = "Get contact by Id", description = "Returns a contact based on an ID")
    @GetMapping(value = "/contact/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Contact> getContact(@PathVariable String id) {
        Contact contact = contactService.getContactById(id);
        return new ResponseEntity<>(contact, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful creation of contact"),
            @ApiResponse(responseCode = "400", description = "Bad request: unsuccessful submission",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "Create Contact", description = "Creates a contact from the provided payload")
    @PostMapping("/contact")
    public ResponseEntity<HttpStatus> createContact(@RequestBody @Valid Contact contact) {
        contactService.saveContact(contact);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Contact doesn't exist",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "200", description = "Successful update of contact",
                    content = @Content(schema = @Schema(implementation = Contact.class))),
    })
    @Operation(summary = "Update contact", description = "Updates a contact by id from the provided payload")
    @PutMapping(value = "/contact/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Contact> updateContact(@PathVariable String id, @RequestBody @Valid Contact contact) {
        contactService.updateContact(id, contact);
        return new ResponseEntity<>(contact, HttpStatus.OK);
    }

    @ApiResponse(responseCode = "204", description = "Successful deletion of contact")
    @Operation(summary = "Delete contact", description = "Deletes a contact by id")
    @DeleteMapping("/contact/{id}")
    public ResponseEntity<HttpStatus> deleteContact(@PathVariable String id) {
        contactService.deleteContact(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
