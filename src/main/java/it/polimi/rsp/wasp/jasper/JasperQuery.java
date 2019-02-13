package it.polimi.rsp.wasp.jasper;

import it.polimi.jasper.engine.query.RSPQuery;
import it.polimi.sr.wasp.rsp.model.Query;

public class JasperQuery extends Query {
    private final RSPQuery internal_query;

    public JasperQuery(String id, String queryBody, RSPQuery continuousQuery) {
        super(id, queryBody);
        this.internal_query = continuousQuery;
    }
}
