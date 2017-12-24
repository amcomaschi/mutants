package ar.com.mercadolibre.mutants;

import ar.com.mercadolibre.mutants.controller.MutantController;
import ar.com.mercadolibre.mutants.controller.StatsController;
import ar.com.mercadolibre.mutants.services.impl.MutantServiceImpl;
import ar.com.mercadolibre.mutants.services.impl.StatsServiceImpl;
import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.PostMethod;
import com.despegar.sparkjava.test.SparkServer;
import com.google.gson.Gson;
import org.junit.ClassRule;
import org.junit.Test;
import spark.servlet.SparkApplication;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MutantAppTest {



    public static class TestContollerTestSparkApplication extends BaseTest implements SparkApplication {
        @Override
        public void init() {
            BaseTest.initAll();
            new MutantController(new MutantServiceImpl(db));
            new StatsController(new StatsServiceImpl(db));
        }

        public static void shutDownDb() {
            shutDownDatabase();
        }
    }

    @ClassRule
    public static SparkServer<TestContollerTestSparkApplication> testServer = new SparkServer<>(MutantAppTest.TestContollerTestSparkApplication.class, 4567);

    @Test
    public void statsServiceOK() throws Exception {
		/* The second parameter indicates whether redirects must be followed or not */
        GetMethod get = testServer.get("/stats", false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
    }

    @Test
    public void mutantRequestInvalidTest() throws Exception {
		/* The second parameter indicates whether redirects must be followed or not */
		DnaJson dna = new DnaJson(null);
        PostMethod post = testServer.post("/mutants/", new Gson().toJson(dna), false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(400, httpResponse.code());
    }

    @Test
    public void connectionBroken() throws Exception {

        TestContollerTestSparkApplication.shutDownDb();
        String[] dna = {"AAAA", "AGTC", "CCCC", "CTGA"};
        DnaJson json = new DnaJson(dna);

		/* The second parameter indicates whether redirects must be followed or not */
        PostMethod get = testServer.post("/mutants/", new Gson().toJson(json), false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(500, httpResponse.code());
    }
}
