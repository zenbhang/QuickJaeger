

import io.opencensus.exporter.trace.jaeger.JaegerTraceExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.concurrent.atomic.AtomicReference;

public class QuickJaegerExporter {
    private static AtomicReference<Boolean> arCreated = new AtomicReference<Boolean>(false);
    private static volatile QuickJaegerExporter single_instance;
    private static final Logger LOG = LoggerFactory.getLogger(QuickJaegerExporter.class);
    private static String thriftEndpoint = "http://0.0.0.0:14268/api/traces";

    //Todo: make into env variable like below
    //private static String thriftEndpoint = (ConfigsKt.env("JAEGER_THRIFT_ENDPOINT", "http://0.0.0.0:14268/api/traces"));

    //Singleton JobTraceExporter
    private QuickJaegerExporter(){}

    public static synchronized QuickJaegerExporter getInstance(){
        if(single_instance == null){
            synchronized (QuickJaegerExporter.class) {
                if(single_instance == null){
                    single_instance = new QuickJaegerExporter();
                }
            }
        }
        return single_instance;
    }

    protected static void registerExporter(String service){
        if (!arCreated.get()) {
            JaegerTraceExporter.createAndRegister(thriftEndpoint, service);  // removing because of duplicate registration error
            arCreated.set(true);
        }
    }
}
