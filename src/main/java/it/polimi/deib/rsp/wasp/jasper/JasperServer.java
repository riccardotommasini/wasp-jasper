package it.polimi.deib.rsp.wasp.jasper;

import it.polimi.deib.rsp.vocals.jena.VocalsFactoryJena;
import it.polimi.sr.wasp.rsp.RSPServer;
import it.polimi.sr.wasp.utils.Config;
import it.polimi.yasper.core.engine.EngineConfiguration;
import it.polimi.yasper.core.spe.operators.r2r.QueryConfiguration;
import lombok.extern.java.Log;
import org.apache.commons.configuration.ConfigurationException;

import java.io.IOException;

@Log
public class JasperServer extends RSPServer {

    public JasperServer() throws IOException {
        super(new VocalsFactoryJena());
    }

    public static void main(String[] args) throws IOException, ConfigurationException, ClassNotFoundException {

        String path = args.length > 0 ? args[0] : "default.properties";

        Config.initialize(path);
        Config config = Config.getInstance();
        int port = config.getServerPort();
        String host = config.getHostName();
        String name = config.getServerName();

        String url = "http://" + host + ":" + port;

        JasperEngine jasper = new JasperEngine(name, url, 0, new QueryConfiguration(path), new EngineConfiguration(path));

        new JasperServer().start(jasper, config);

        log.info("Running at " + url + "/" + name);
    }

    @Override
    protected void ignite(String host, String name, int port) {
        super.ignite(host, name, port);
    }
}
