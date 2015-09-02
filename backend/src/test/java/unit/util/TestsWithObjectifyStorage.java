package util;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;

import org.junit.After;
import org.junit.Before;

import java.io.IOException;

public abstract class TestsWithObjectifyStorage {
    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(),
                    new LocalMemcacheServiceTestConfig());

    @Before
    public void setup() {
        helper.setUp();
        ObjectifyService.begin();
    }

    @After
    public void tearDown() throws IOException {
        helper.tearDown();
    }
}
