package it.polimi.deib.rsp.wasp.jasper;

import it.polimi.sr.wasp.rsp.model.InternalTaskWrapper;
import it.polimi.sr.wasp.server.model.concept.Channel;
import it.polimi.yasper.core.spe.operators.r2r.ContinuousQuery;
import lombok.Getter;

import java.util.List;

public class JasperQuery extends InternalTaskWrapper {
    @Getter
    private final ContinuousQuery query;

    public JasperQuery(String id, String body, String base, ContinuousQuery query, Channel out, List<Channel> in) {
        super(id, body, base, out, in);
        this.query = query;

    }
}
