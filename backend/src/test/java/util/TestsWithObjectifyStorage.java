package util;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Objectify;
import com.tokko.recipesv2.backend.resourceaccess.OfyService;

import org.junit.After;
import org.junit.Before;

import java.io.IOException;

public abstract class TestsWithObjectifyStorage {
    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(),
                    new LocalMemcacheServiceTestConfig());
    protected Objectify ofy;

    @Before
    public void setup() {
        helper.setUp();
        ofy = OfyService.ofy();
    }

    @After
    public void tearDown() throws IOException {
        helper.tearDown();
    }
}
