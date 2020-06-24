import io.jaegertracing.Configuration;
import java.util.concurrent.atomic.AtomicReference;

public class QuickJaegerConfig {
    private static AtomicReference<Boolean> arCreated = new AtomicReference<Boolean>(false);
    private static volatile QuickJaegerConfig single_instance;
    private static Configuration config;

    //Singleton JobTraceConfig
    private QuickJaegerConfig(){}

    public static QuickJaegerConfig getInstance(){
        if (single_instance == null){
            synchronized (QuickJaegerConfig.class){
                if(single_instance == null){
                    single_instance = new QuickJaegerConfig();
                }
            }
        }
        return single_instance;
    }

    public Configuration getConfig(){
        return config;
    }

    protected static void createConfig(String service){
        if (!arCreated.get()) {
            Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration.fromEnv().withType("const").withParam(1);
            Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration.fromEnv().withLogSpans(true);
            config = new Configuration(service).withSampler(samplerConfig).withReporter(reporterConfig);
            arCreated.set(true);
        }
    }

}
