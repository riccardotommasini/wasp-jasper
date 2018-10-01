package it.polimi.deib.rsp.wasp.jasper;

import it.polimi.sr.wasp.rsp.model.StatelessDataChannel;

public class JasperRDFStream extends StatelessDataChannel {
    public JasperRDFStream(String base, String uri, String source, JasperInjectTask jasperInjectTask) {
        super(base, uri, source);
        this.asynch_task.add(jasperInjectTask);
    }
}
