package it.polimi.deib.rsp.wasp.jasper;

import it.polimi.deib.rsp.vocals.jena.VocalsFactoryJena;
import it.polimi.sr.wasp.rsp.RSPServer;
import it.polimi.sr.wasp.server.model.persist.StatusManager;
import it.polimi.yasper.core.engine.EngineConfiguration;
import lombok.extern.java.Log;
import org.apache.commons.configuration.ConfigurationException;
import spark.Spark;

import java.io.IOException;

@Log
public class JasperServer extends RSPServer {

    public JasperServer() throws IOException {
        super(new VocalsFactoryJena());
    }

    public static void main(String[] args) throws IOException, ConfigurationException {
        if (args.length > 0) {
            String db = args[1];
            JasperEngine cqels = new JasperEngine("jasper", "http://localhost:8181", 0, new EngineConfiguration(args[0]));
            new JasperServer().start(cqels, args[0]);
            log.info("Running at http://localhost:8181/jasper");
        } else {
            String path = JasperEngine.class.getClassLoader().getResource("default.properties").getPath();
            JasperEngine cqels = new JasperEngine("jasper", "http://localhost:8181", 0,new EngineConfiguration(path));
            new JasperServer().start(cqels, path);
            log.info("Running at http://localhost:8181/jasper");
        }
    }

    @Override
    protected void ignite(String host, String name, int port) {
        super.ignite(host, name, port);
        Spark.get(name + "/observers", (request, response) -> StatusManager.sinks.values());
    }
}
