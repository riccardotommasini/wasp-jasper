package it.polimi.deib.rsp.wasp.jasper;

import it.polimi.jasper.engine.Jasper;
import it.polimi.jasper.streams.RegisteredEPLStream;
import it.polimi.sr.wasp.rsp.RSPEngine;
import it.polimi.sr.wasp.rsp.SPARQLUtils;
import it.polimi.sr.wasp.rsp.exceptions.InternalEngineException;
import it.polimi.sr.wasp.rsp.model.InternalTaskWrapper;
import it.polimi.sr.wasp.rsp.model.QueryBody;
import it.polimi.sr.wasp.server.model.concept.Channel;
import it.polimi.yasper.core.engine.EngineConfiguration;
import it.polimi.yasper.core.spe.operators.r2r.ContinuousQuery;
import it.polimi.yasper.core.spe.operators.r2r.execution.ContinuousQueryExecution;
import it.polimi.yasper.core.stream.rdf.RDFStream;

import java.util.List;

public class JasperEngine extends RSPEngine {

    Jasper jasper;

    public JasperEngine(String name, String base, int t0, EngineConfiguration ec) {
        super(name, base);
        jasper = new Jasper(t0, ec);
    }

    @Override
    protected String[] extractStreams(QueryBody queryBody) {
        List<String> strings = SPARQLUtils.extractStreams(queryBody.body);
        return strings.toArray(new String[strings.size()]);
    }


    protected InternalTaskWrapper handleInternalQuery(String qid, String body, String uri, String source, List<Channel> list) throws InternalEngineException {

        try {
            if (!body.contains("REGISTER"))
                body = "REGISTER STREAM " + source + " AS " + body;

            ContinuousQueryExecution cqe = jasper.register(body);

            ContinuousQuery q = cqe.getContinuousQuery();

            JasperQueryResultStream out = new JasperQueryResultStream(base, uri, qid, q, cqe);

            JasperQuery jq = new JasperQuery(qid, body, base, q, out, list);

            out.add(jq);

            return jq;
        } catch (Exception e) {
            throw new InternalEngineException(e.getCause());
        }

    }

    @Override
    public Channel delete_stream(String id) {
        //TODO delete internal
        return super.delete_stream(id);
    }

    protected Channel handleInternalStream(String id, String uri) throws InternalEngineException {
        try {
            RegisteredEPLStream register = jasper.register(new RDFStream(uri));
            return new JasperRDFStream(base, id, uri, new JasperInjectTask(base, register));
        } catch (Exception e) {
            throw new InternalEngineException(e.getCause());
        }

    }
}
