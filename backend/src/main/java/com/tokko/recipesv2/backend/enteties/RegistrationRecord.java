package com.tokko.recipesv2.backend.enteties;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * The Objectify object model for device registrations we are persisting
 */
@Entity
public class RegistrationRecord {

    @Id
    Long id;

    @Index
    private String regId;
    // you can add more fields...

    public RegistrationRecord() {
    }

    public RegistrationRecord(String first) {
        regId = first;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }
}