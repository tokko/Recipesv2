/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Backend with Google Cloud Messaging" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/GcmEndpoints
*/

package com.tokko.recipesv2.backend.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.users.User;
import com.tokko.recipesv2.backend.enteties.RegistrationRecord;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static com.tokko.recipesv2.backend.OfyService.ofy;

/**
 * A registration endpoint class we are exposing for a device's GCM registration id on the backend
 * <p>
 * For more information, see
 * https://developers.google.com/appengine/docs/java/endpoints/
 * <p>
 * NOTE: This endpoint does not use any form of authorization or
 * authentication! If this app is deployed, anyone can access this endpoint! If
 * you'd like to add authentication, take a look at the documentation.
 */
@Api(name = "registration", version = "v1",
        clientIds = {Constants.ANDROID_CLIENT_ID, Constants.WEB_CLIENT_ID},
        audiences = {Constants.ANDROID_AUDIENCE}, namespace = @ApiNamespace(ownerDomain = "backend.recipesv2.tokko.com", ownerName = "backend.recipesv2.tokko.com", packagePath = ""))
public class RegistrationEndpoint {

    private static final Logger log = Logger.getLogger(RegistrationEndpoint.class.getName());

    @ApiMethod(name = "insert")
    public RegistrationRecord registerDevice(RegistrationRecord record, User user) {
        ofy().save().entity(record).now();
        return record;
    }

    @ApiMethod(name = "listDevices")
    public CollectionResponse<RegistrationRecord> listDevices(User user) {
        List<RegistrationRecord> records = Arrays.asList(new RegistrationRecord("first"), new RegistrationRecord("Second"));//ofy().load().type(RegistrationRecord.class).limit(count).list();
        return CollectionResponse.<RegistrationRecord>builder().setItems(records).build();
    }

    private RegistrationRecord findRecord(String regId) {
        return ofy().load().type(RegistrationRecord.class).filter("regId", regId).first().now();
    }

}
