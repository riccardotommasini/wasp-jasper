package it.polimi.rsp.wasp.jasper;

import it.polimi.sr.wasp.rsp.model.StatelessDataChannel;

public class JasperRDFStream extends StatelessDataChannel {

    public JasperRDFStream(String base, String id, String uri, JasperInternalSink regs) {
        super(base, id, uri);
        this.sinks.add(regs);
    }
}
