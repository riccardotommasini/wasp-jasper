package it.polimi.deib.rsp.wasp.jasper;

import it.polimi.jasper.spe.operators.r2s.formatter.ConstructResponseDefaultFormatter;
import it.polimi.jasper.spe.operators.r2s.formatter.SelectResponseDefaultFormatter;
import it.polimi.sr.wasp.server.model.concept.Channel;
import it.polimi.sr.wasp.server.model.concept.Source;
import it.polimi.yasper.core.spe.operators.r2r.ContinuousQuery;
import it.polimi.yasper.core.spe.operators.r2r.execution.ContinuousQueryExecution;

import java.util.ArrayList;
import java.util.List;

public class JasperQuerySource implements Source {

    List<Channel> channels = new ArrayList<>();

    public JasperQuerySource(ContinuousQuery q, ContinuousQueryExecution cqe, String format) {
        if (q.isConstructType()) {
            cqe.add(new ConstructResponseDefaultFormatter(format, true) {
                @Override
                protected void out(String s) {
                    channels.forEach(channel -> channel.put(s));
                }
            });
        } else if (q.isSelectType()) {
            cqe.add(new SelectResponseDefaultFormatter(format, true) {
                @Override
                protected void out(String s) {
                    channels.forEach(channel -> channel.put(s));
                }
            });
        }
    }

    @Override
    public Channel add(Channel t) {
        channels.add(t);
        return t;
    }

    @Override
    public void stop() {

    }
}
