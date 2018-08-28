package it.polimi.deib.rsp.wasp.jasper;

import it.polimi.jasper.streams.RegisteredEPLStream;
import it.polimi.sr.wasp.server.model.concept.Channel;
import it.polimi.sr.wasp.server.model.concept.tasks.SynchTask;
import org.apache.jena.graph.Graph;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;

import java.io.ByteArrayInputStream;

public class JasperInjectTask implements SynchTask {
    private final RegisteredEPLStream stream;

    public JasperInjectTask(RegisteredEPLStream register) {
        this.stream = register;
    }

    @Override
    public void await(String s) {
        stream.put(_parse(s), System.currentTimeMillis());
    }

    private Graph _parse(String s) {
        return ModelFactory.createDefaultModel().read(new ByteArrayInputStream(s.getBytes()), Lang.JSONLD.getLabel()).getGraph();
    }

    @Override
    public String iri() {
        return stream.getURI();
    }

    @Override
    public Channel out() {
        return null;
    }

    @Override
    public Channel[] in() {
        return new Channel[0];
    }
}
