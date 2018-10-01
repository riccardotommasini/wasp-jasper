package it.polimi.deib.rsp.wasp.jasper;

import it.polimi.sr.wasp.rsp.model.StatelessDataChannel;
import it.polimi.sr.wasp.server.model.concept.Channel;

public class JasperQueryResultStream extends StatelessDataChannel {

    public JasperQueryResultStream(String base, String id, String uri) {
        super(base, id, uri);
    }

    @Override
    public Channel put(Object o) {
        return super.put(o);
    }
}
