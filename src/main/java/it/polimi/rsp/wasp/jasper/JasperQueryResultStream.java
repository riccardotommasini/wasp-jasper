package it.polimi.rsp.wasp.jasper;

import it.polimi.jasper.engine.query.RSPQuery;
import it.polimi.jasper.engine.query.response.ConstructResponse;
import it.polimi.jasper.engine.query.response.SelectResponse;
import it.polimi.sr.wasp.rsp.model.StatelessDataChannel;
import it.polimi.yasper.core.query.execution.ContinuousQueryExecution;
import it.polimi.yasper.core.query.formatter.QueryResponseFormatter;
import lombok.extern.java.Log;
import org.apache.jena.query.ResultSetFormatter;

import java.io.ByteArrayOutputStream;
import java.util.Observable;

@Log
public class JasperQueryResultStream extends StatelessDataChannel {

    private final RSPQuery q;
    private final ContinuousQueryExecution cqe;

    public JasperQueryResultStream(String base, String id, String source, RSPQuery q1, ContinuousQueryExecution cqe1) {
        super(base, id, source);
        this.q = q1;
        this.cqe = cqe1;
        this.cqe.addObserver(new GenericResponseDispatcher(true));
    }

    private class GenericResponseDispatcher extends QueryResponseFormatter {

        long last_result = -1L;
        boolean distinct;

        public GenericResponseDispatcher(boolean distinct) {
            this.distinct = distinct;
        }


        @Override
        public void update(Observable o, Object arg) {

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            if (arg instanceof SelectResponse) {
                SelectResponse sr = (SelectResponse) arg;
                if (sr.getCep_timestamp() != last_result && distinct) {
                    last_result = sr.getCep_timestamp();
                    log.info("[" + System.currentTimeMillis() + "] Result at [" + last_result + "]");
                    ResultSetFormatter.out(os, sr.getResults());
                }
            } else if (arg instanceof ConstructResponse) {
                ConstructResponse sr = (ConstructResponse) arg;
                if (sr.getCep_timestamp() != last_result && distinct) {
                    log.info("[" + System.currentTimeMillis() + "] Result at [" + last_result + "]");
                    last_result = sr.getCep_timestamp();
                    sr.getResults().write(os, "JSON-LD");
                }
            }

            put(os.toString());
        }
    }
}
