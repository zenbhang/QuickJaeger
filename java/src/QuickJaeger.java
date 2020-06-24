import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;

public class QuickJaeger {

    private HashMap<String, Span> spanMap;
    private JaegerTracer jaegerTracer;
    private String service;
    private final Logger LOG = LoggerFactory.getLogger(QuickJaeger.class);
    private Object obj;

    public QuickJaeger(String processName) {
        spanMap = new HashMap<>();
        jaegerTracer = initTrace(processName);
        this.service = processName;
    }

    public QuickJaeger(String processName, Object obj) {
        this(processName);
        this.obj = obj;
    }

    private JaegerTracer initTrace(String service) {
        JaegerTracer jaegerTracer = null;
        try{
            QuickJaegerExporter jte = QuickJaegerExporter.getInstance();
            QuickJaegerExporter.registerExporter(service);
            QuickJaegerConfig jtc = QuickJaegerConfig.getInstance();
            QuickJaegerConfig.createConfig(service);
            jaegerTracer = jtc.getConfig().getTracer();
        }catch (Exception e){
            LOG.warn("QuickJaeger Jaeger Init Exception" + e);
        }
        return jaegerTracer;
    }

    public void createSpan(String spanName) {
        try{
            Span span = jaegerTracer.buildSpan(spanName).start();
            span.setOperationName(spanName);
            if (obj != null) {
                spanEnrichment(span, obj);
            }
            spanMap.put(spanName, span);
        }catch (Exception e){
            LOG.warn("QuickJaeger createSpan Exception" + e);
        }
    }

    public void createSpan(String spanName, String parentSpan) {
        try{
            if (spanMap.containsKey(parentSpan)) {
                Span span = jaegerTracer.buildSpan(spanName).asChildOf(spanMap.get(parentSpan)).start();
                span.setOperationName(spanName);
                if (obj != null) {
                    spanEnrichment(span, obj);
                }
                spanMap.put(spanName, span);
            } else {
                LOG.warn("QuickJaeger Missing Parent Span: "+parentSpan);
            }
        }catch (Exception e){
            LOG.warn("QuickJaeger createSpan Exception: " + e);
        }
    }

    public void log(String spanName, String log) {
        try{
            if (spanMap.containsKey(spanName)) {
                Span span = spanMap.get(spanName);
                if(span != null){
                    span.log(log);
                }
            }
        }catch (Exception e){
            LOG.warn("QuickJaeger log exception: " + e);
        }
    }

    public void setTag(String spanName, String tagName, String tagString) {
        try{
            if (spanMap.containsKey(spanName)) {
                Span span = spanMap.get(spanName);
                if(span != null){
                    span.setTag(tagName, tagString);
                }
            }
        }catch (Exception e){
            LOG.warn("QuickJaeger setTag exception: " + e);
        }
    }

    public void endSpan(String spanName) {
        try{
            if (spanMap.containsKey(spanName)) {
                Span span = spanMap.get(spanName);
                if(span != null){
                    span.setTag("result", "passed");
                    span.finish();
                }
            }
        }catch (Exception e){
            LOG.warn("QuickJaeger endSpan exception: " + e);
        }
    }

    public void error(String spanName, String errormessage) {
        try{
            if (spanMap.containsKey(spanName)) {
                Span span = spanMap.get(spanName);
                if(span != null){
                    span.setTag("errorMessage", errormessage);
                    span.setTag("error", "true");
                    span.setTag("result", "failed");
                    span.finish();
                }
            }
        }catch(Exception e){
            LOG.warn("QuickJaeger error exception" + e);
        }
    }

    private void spanEnrichment(Span span, Object obj) {
        //Span logging enrichment, use this to add ClientIds, JobIds, FirmIds, etc.
        try {
            //sample with ClientId
//            if (obj.getClientId() != null) {
//                span.setTag("ClientId", obj.getClientId().toString());
//            }

        } catch (Exception e) {
            LOG.warn("QuickJaeger Exception at span enrichment" + e);
        }
    }
}

