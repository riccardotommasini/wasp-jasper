package it.polimi.deib.rsp.wasp.jasper;

import it.polimi.sr.wasp.rsp.model.StatelessDataChannel;
import it.polimi.yasper.core.spe.operators.r2r.ContinuousQuery;
import it.polimi.yasper.core.spe.operators.r2r.execution.ContinuousQueryExecution;
import lombok.Getter;

public class JasperQueryResultStream extends StatelessDataChannel {

    @Getter
    private final ContinuousQuery query;

    public JasperQueryResultStream(String base, String id, String uri, ContinuousQuery continuousQuery, ContinuousQueryExecution cqe) {
        super(base, id, uri);
        this.query = continuousQuery;
    }
}
