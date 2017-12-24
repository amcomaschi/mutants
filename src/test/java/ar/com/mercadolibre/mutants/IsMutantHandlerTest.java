package ar.com.mercadolibre.mutants;

import ar.com.mercadolibre.mutants.handler.Answer;
import ar.com.mercadolibre.mutants.exceptions.HandlerException;
import ar.com.mercadolibre.mutants.handler.IsMutantHandler;
import ar.com.mercadolibre.mutants.request.MutantRequest;
import ar.com.mercadolibre.mutants.services.impl.MutantServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IsMutantHandlerTest extends BaseTest{

    @Test
    public void anInvalidIsMutantReqReturnsBadRequest() {
        String[] invalidDna = {"ACT", "CTA", "A"};
        MutantRequest mr = new MutantRequest(invalidDna);

        assertFalse(mr.isValid());

        IsMutantHandler mh = new IsMutantHandler(new MutantServiceImpl(db));

        try {
            assertEquals(new Answer(400), mh.process(mr, null));
        } catch (HandlerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void aValidIsMutantReqReturnsStatus200() {
        String[] invalidDna = {"AAAA", "AGTC", "CCCC", "CTGA"};
        MutantRequest mr = new MutantRequest(invalidDna);

        assertTrue(mr.isValid());

        IsMutantHandler mh = new IsMutantHandler(new MutantServiceImpl(db));

        try {
            assertEquals(Answer.ok(""), mh.process(mr, null));
        } catch (HandlerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void aValidIsMutantReqReturnsStatus403() {
        String[] invalidDna = {"AAAT", "AGTC", "CCTC", "CTGA"};
        MutantRequest mr = new MutantRequest(invalidDna);

        assertTrue(mr.isValid());

        IsMutantHandler mh = new IsMutantHandler(new MutantServiceImpl(db));

        try {
            assertEquals(new Answer(403), mh.process(mr, null));
        } catch (HandlerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void connectionDatabaseBroken() {
        String[] invalidDna = {"AAAT", "AGTC", "CCTC", "CTGA"};
        MutantRequest mr = new MutantRequest(invalidDna);

        assertTrue(mr.isValid());

        IsMutantHandler mh = new IsMutantHandler(new MutantServiceImpl(db));
        shutDownDatabase();

        try {
            mh.process(mr, null);
        } catch (HandlerException e) {
            assertEquals(Boolean.TRUE, e.getMessage().contains("Error on database connection"));
        }
    }
}