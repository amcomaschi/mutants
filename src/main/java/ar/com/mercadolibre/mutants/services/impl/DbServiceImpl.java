package ar.com.mercadolibre.mutants.services.impl;

import ar.com.mercadolibre.mutants.exceptions.MutantDbException;
import ar.com.mercadolibre.mutants.model.Dna;
import ar.com.mercadolibre.mutants.services.DbService;
import com.mongodb.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DbServiceImpl implements DbService {

    private static final Logger logger = LoggerFactory.getLogger(DbServiceImpl.class);

    private Datastore ds;

    public DbServiceImpl() {
        this.createDataStore("localhost", 27017, "mutants");
    }

    public DbServiceImpl(String host, int port) {
        this.createDataStore(host, port, "mutants");
    }

    private void createDataStore(String host, int port, String database) {

        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.writeConcern(WriteConcern.MAJORITY);

        ServerAddress address = new ServerAddress(host, port);

        MongoClient client = new MongoClient(address, builder.build());
        Datastore ds = new Morphia().mapPackage("ar.com.mercadolibre.mutants.model").createDatastore(client, database);
        ds.ensureIndexes();

        this.ds = ds;
    }



    /**
     * Guarda una secuencia de adn en la base de datos.
     * Si la secuencia ya habia sido analizada no sera guardada.
     *
     * @param dna secuencia de adn que se quiere guardar.
     */
    public void addDNASequence(Dna dna) throws MutantDbException{

        logger.debug("Saving dna sequence " + dna.toString());
        try {
            Key<Dna> x = this.ds.save(dna);
            logger.debug("Dna sequence saved: " + x.getId());

        } catch (MongoException e) {
            logger.info("Error while saving dna sequence, see error log for more information: " + e.getMessage());
            logger.error("Error while saving dna sequence. ", e);

            throw new MutantDbException("Error on database connection: " + e.getMessage(), e.getCode());
        }
    }

    @Override
    public long getCountMutantsDNA() throws  MutantDbException{

        logger.debug("Getting count of mutants dna");
        long count = 0;

        try {
            Query<Dna> query = ds.createQuery(Dna.class);
            query.field("mutant").equal(Boolean.TRUE);

            count = query.count();
        } catch(MongoException me) {
            logger.error("Error while getting count of mutants dna: " + me.getMessage());
            me.printStackTrace();
            throw new MutantDbException("Error while getting count of mutants dna: " + me.getMessage(), me.getCode());
        }

        logger.info("Count of mutants dna founded: " + count);

        return count;
    }

    @Override
    public long getTotal() throws MutantDbException{

        logger.debug("Getting total of dna analized");
        long count = 0;

        try {
            Query<Dna> query = ds.createQuery(Dna.class);
            count = query.count();

        } catch (MongoException me) {
            logger.error("Error while getting count of dna analized: " + me.getMessage());
            me.printStackTrace();
            throw new MutantDbException("Error while getting count of dna analized: " + me.getMessage(), me.getCode());
        }

        return count;
    }
}