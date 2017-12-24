package ar.com.mercadolibre.mutants;

import ar.com.mercadolibre.mutants.services.DbService;
import ar.com.mercadolibre.mutants.services.impl.DbServiceImpl;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;

public class BaseTest {

    protected static DbService db;
    protected static MongodProcess mongodProcess;

    @BeforeAll
    public static void initAll() {


        MongodStarter starter = MongodStarter.getDefaultInstance();

        String bindIp = "localhost";
        int port = 12345;
        IMongodConfig mongodConfig = null;
        try {
            mongodConfig = new MongodConfigBuilder()
                    .version(Version.Main.PRODUCTION)
                    .net(new Net(bindIp, port, Network.localhostIsIPv6()))
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MongodExecutable mongodExecutable = null;
        try {
            mongodExecutable = starter.prepare(mongodConfig);
            MongodProcess mongod = mongodExecutable.start();
            mongodProcess = mongod;

        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
            //if (mongodExecutable != null)
            //mongodExecutable.stop();
        }

        DbService dbService = new DbServiceImpl("localhost", 12345);
        db = dbService;
    }

    protected static void shutDownDatabase() {
        mongodProcess.stop();
    }
}
