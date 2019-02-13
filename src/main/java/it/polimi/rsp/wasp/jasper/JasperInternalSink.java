package it.polimi.rsp.wasp.jasper;

import it.polimi.jasper.engine.JenaRSPQLEngineImpl;
import it.polimi.jasper.engine.stream.GraphStreamItem;
import it.polimi.sr.wasp.server.model.concept.Channel;
import it.polimi.sr.wasp.server.model.concept.Sink;
import it.polimi.sr.wasp.server.model.concept.Source;
import it.polimi.yasper.core.stream.RegisteredStream;
import org.apache.jena.graph.Graph;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;

import java.io.ByteArrayInputStream;

public class JasperInternalSink implements Sink {

    private final JenaRSPQLEngineImpl engine;
    private final RegisteredStream stream;

    public JasperInternalSink(RegisteredStream stream, JenaRSPQLEngineImpl engine) {
        this.engine = engine;
        this.stream = stream;
    }

    public void await(Source source, String s) {
        //TODO event time
        Graph graph = serialize(s);
        long time = System.currentTimeMillis();
        engine.process(new GraphStreamItem(time, graph, stream.getURI()));
    }

    private Graph serialize(String s) {
        return ModelFactory.createDefaultModel().read(new ByteArrayInputStream(s.getBytes()), Lang.JSONLD.getLabel()).getGraph();
    }

    public void await(Channel channel, String s) {

    }
}
