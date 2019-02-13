package it.polimi.rsp.wasp.jasper;

import it.polimi.deib.rsp.vocals.rdf4j.VocalsFactoryRDF4J;
import it.polimi.sr.wasp.rsp.RSPServer;
import it.polimi.sr.wasp.server.model.persist.StatusManager;
import it.polimi.yasper.core.utils.EngineConfiguration;
import lombok.extern.java.Log;
import org.apache.commons.configuration.ConfigurationException;
import spark.Spark;

import java.io.IOException;

@Log
public class JasperServer extends RSPServer {

    public JasperServer() throws IOException {
        super(new VocalsFactoryRDF4J());
    }

    public static void main(String[] args) throws IOException, ConfigurationException {
        if (args.length > 0) {
            JasperEngine jasper = new JasperEngine("jasper", "", new EngineConfiguration(args[0]));
            new JasperServer().start(jasper, new EngineConfiguration(args[1]));
            log.info("Running at http://localhost:8181/jasper");
        } else {
            JasperEngine jasper = new JasperEngine("jasper", "", new EngineConfiguration(JasperServer.class.getResource("setup.properties").getPath()));
            new JasperServer().start(jasper, JasperServer.class.getResource("default.properties").getPath());
            log.info("Running at http://localhost:8181/jasper");
        }
    }

    @Ovgerride
    protected void ingnite(String host, String name, int port) {
        super.ingnite(host, name, port);
        Spark.get(name + "/observers", (request, response) -> StatusManager.sinks.values());
    }
}
