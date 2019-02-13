package it.polimi.rsp.wasp.jasper;

import it.polimi.jasper.engine.JenaRSPQLEngineImpl;
import it.polimi.jasper.engine.query.RSPQuery;
import it.polimi.sr.wasp.rsp.RSPEngine;
import it.polimi.sr.wasp.rsp.model.Query;
import it.polimi.sr.wasp.server.model.concept.Channel;
import it.polimi.yasper.core.enums.Maintenance;
import it.polimi.yasper.core.query.execution.ContinuousQueryExecution;
import it.polimi.yasper.core.stream.RegisteredStream;
import it.polimi.yasper.core.stream.StreamImpl;
import it.polimi.yasper.core.utils.EngineConfiguration;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class JasperEngine extends RSPEngine {

    private JenaRSPQLEngineImpl engine;

    public JasperEngine(String name, String base, EngineConfiguration config) {
        super(name, base);
        this.engine = new JenaRSPQLEngineImpl(System.currentTimeMillis(), config);

    }

    protected Query handleInternalQuery(String id, String queryBody, String uri, String s3) {

        RSPQuery continuousQuery = (RSPQuery) engine.parseQuery(queryBody);
        Model tbox = ModelFactory.createDefaultModel();

        ContinuousQueryExecution cqe = engine.register(continuousQuery, tbox, Maintenance.NAIVE, null, false);

        JasperQueryResultStream out = new JasperQueryResultStream(uri, id, continuousQuery, cqe);
        JasperQuery jasperQuery = new JasperQuery(id, queryBody, continuousQuery);
        out.apply(jasperQuery);

        return jasperQuery;
    }

    protected Channel handleInternalStream(String id, String source) {
        RegisteredStream register = (RegisteredStream) engine.register(new StreamImpl(id));
        return new JasperRDFStream(id, source, new JasperInternalSink(register, engine));
    }
}
