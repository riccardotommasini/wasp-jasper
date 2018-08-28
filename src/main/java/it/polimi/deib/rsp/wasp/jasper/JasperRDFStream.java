package it.polimi.deib.rsp.wasp.jasper;

import it.polimi.sr.wasp.rsp.model.StatelessDataChannel;

public class JasperRDFStream extends StatelessDataChannel {
    public JasperRDFStream(String base, String id, String uri, JasperInjectTask jasperInjectTask) {
        super(base, id, uri);
        this.asynch_task.add(jasperInjectTask);
    }
}
