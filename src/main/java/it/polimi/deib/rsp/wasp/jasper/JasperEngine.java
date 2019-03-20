package it.polimi.deib.rsp.wasp.jasper;

import it.polimi.jasper.engine.Jasper;
import it.polimi.jasper.spe.operators.r2r.syntax.QueryFactory;
import it.polimi.jasper.spe.operators.r2r.syntax.RSPQLJenaQuery;
import it.polimi.jasper.streams.RegisteredEPLStream;
import it.polimi.sr.wasp.rsp.exceptions.InternalEngineException;
import it.polimi.sr.wasp.rsp.model.InternalTaskWrapper;
import it.polimi.sr.wasp.rsp.model.TaskBody;
import it.polimi.sr.wasp.rsp.processor.RSPEngine;
import it.polimi.sr.wasp.server.model.concept.Channel;
import it.polimi.sr.wasp.utils.SPARQLUtils;
import it.polimi.yasper.core.engine.ConfigurationUtils;
import it.polimi.yasper.core.engine.EngineConfiguration;
import it.polimi.yasper.core.spe.operators.r2r.ContinuousQuery;
import it.polimi.yasper.core.spe.operators.r2r.QueryConfiguration;
import it.polimi.yasper.core.spe.operators.r2r.execution.ContinuousQueryExecution;
import it.polimi.yasper.core.stream.rdf.RDFStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.List;

import static it.polimi.sr.wasp.utils.URIUtils.getQueryUri;
import static it.polimi.sr.wasp.utils.URIUtils.getStreamUri;

public class JasperEngine extends RSPEngine {

    private final QueryConfiguration config;
    Jasper jasper;

    public JasperEngine(String name, String base, int t0, QueryConfiguration qc, EngineConfiguration ec) {
        super(name, base);
        jasper = new Jasper(t0, ec);
        this.config = qc;

    }

    @Override
    protected String[] extractStreams(TaskBody queryBody) {
        List<String> strings = SPARQLUtils.extractStreams(queryBody.body);
        return strings.toArray(new String[strings.size()]);
    }


    protected InternalTaskWrapper handleInternalQuery(TaskBody tb, List<Channel> list) throws InternalEngineException {

        String qid = getQueryUri(base, tb.id);
        String body = tb.body;
        String uri = getStreamUri(base, tb.id);
        String source = tb.id;
        String tbox = tb.tbox;
        String format = tb.format == null || tb.format.isEmpty() ? "JSON-LD" : tb.format;

        try {
            if (!body.contains("REGISTER"))
                body = "REGISTER RSTREAM <" + source + "> AS " + body;

            if (tbox != null && !tbox.isEmpty())
                config.setProperty(ConfigurationUtils.TBOX_LOCATION, tbox);

            RSPQLJenaQuery parse = QueryFactory.parse(jasper.getResolver(), body);


            ContinuousQueryExecution cqe = jasper.register(parse, config);

            ContinuousQuery q = cqe.getContinuousQuery();

            JasperQuerySource internal_source = new JasperQuerySource(q, cqe, format);

            JasperQueryResultStream out = new JasperQueryResultStream(base, uri, qid);

            internal_source.add(out);

            JasperQuery jq = new JasperQuery(qid, body, base, q, internal_source, out, list);

            out.add(jq);

            return jq;

        } catch (ParseCancellationException e) {
            throw new InternalEngineException("\"error\":\"parse error\", " +
                    "\"message\":\"" +
                    e.getMessage() +
                    "\"" +
                    "}");
        } catch (Exception e) {
            throw new InternalEngineException(e.getCause());
        }

    }

    @Override
    public Channel delete_stream(String id) {
        //TODO delete internal
        return super.delete_stream(id);
    }

    protected Channel handleInternalStream(String uri, String source) throws InternalEngineException {
        try {
            RegisteredEPLStream register = jasper.register(new RDFStream(uri));
            return new JasperRDFStream(base, uri, source, new JasperInjectTask(base, register));
        } catch (Exception e) {
            throw new InternalEngineException(e.getCause());
        }

    }
}
