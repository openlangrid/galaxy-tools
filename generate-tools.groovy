/*
*/
@Grab('org.langrid:jp.go.nict.langrid.client.soap:1.0.7')
@Grab('org.langrid:jp.go.nict.langrid.service.management_1_2:1.0.7')
@Grab('com.floreysoft:jmte:3.1.1')
@Grab(group='commons-io', module='commons-io', version='2.4')
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.Order;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceManagementService;
import com.floreysoft.jmte.Engine;
import org.apache.commons.io.FileUtils


if(args.length != 5){
  println "[usage] groovy generate-tools.groovy targetDir toolId toolName serviceGridUrl serviceGridUserId";
  return
}
print "Password: "
def targetDir = args[0]
def toolId = args[1]
def toolName = args[2]
def serviceGridUrl = args[3]
def serviceGridUserId = args[4]
def serviceGridPassword = new BufferedReader(new InputStreamReader(System.in)).readLine()
println "";

config = new ConfigSlurper().parse(new File("./generate-tools.config").toURL());

def templatesDir = new File("./templates");
def targetToolsDir = new File(targetDir, toolId);
targetToolsDir.mkdirs();

Engine jmte = Engine.createNonCachingEngine()

// generate config.groovy
println "writing config.groovy..."
def bindings = new HashMap();
bindings.put("serviceGridUrl", serviceGridUrl);
bindings.put("serviceGridUserId", serviceGridUserId);
bindings.put("serviceGridPassword", serviceGridPassword);
new File(targetToolsDir, "config.groovy").write(jmte.transform(
	new File(templatesDir, "config.groovy.template.jmte").getText("UTF-8"),
	bindings
	), "UTF-8");

// generate merge.xml
println "writing merge.xml..."
bindings = new HashMap();
bindings.put("groovyPath", config.groovyPath);
new File(targetToolsDir, "/merge.xml").write(jmte.transform(
	new File(templatesDir, "merge.xml.template.jmte").getText("UTF-8"),
	bindings
	), "UTF-8");

// generate merge.groovy
FileUtils.copyFile(
	new File(templatesDir, "merge.groovy"),
	new File(targetToolsDir, "merge.groovy"))

// generate each tools
println "retrieving service list...";
def client = new SoapClientFactory().create(
  ServiceManagementService.class,
  new URL(serviceGridUrl + "services/ServiceManagement"),
  serviceGridUserId, serviceGridPassword)
def services = new ArrayList();
def toolTempls = new HashMap();
for(i in 0..10){
  result = client.searchServices(i * 100, 100, new MatchingCondition[0],
    [new Order("gridId", "ASCENDANT"), new Order("serviceId", "ASCENDANT")] as Order[],
    "ACCESSIBLE");
  if(result.elements.length == 0) break;
  result.elements.each{
    if(it.instanceType == "BPEL") return;
    toolTempl = toolTempls.get(it.serviceType);
    if(toolTempl == null){
      t = new File(templatesDir, it.serviceType + ".xml.template.jmte");
      if(t.exists()){
        toolTempl = t.getText("UTF-8");
        toolTempls.put(it.serviceType, toolTempl);
        FileUtils.copyFile(
          new File(templatesDir, "${it.serviceType}.groovy"),
          new File(targetToolsDir, "${it.serviceType}.groovy"))
        
      }
    }
    if(toolTempl == null){
      println "no tempaltes for ${it.serviceId}[${it.serviceType}]${it.instanceType}";
    } else{
      bindings = new HashMap();
      bindings.put("groovyPath", config.groovyPath);
      bindings.put("serviceId", it.serviceId);
      bindings.put("serviceType", it.serviceType);
      bindings.put("serviceName", it.serviceName.replaceAll("<", "&lt;"));
      bindings.put("serviceDescription", it.serviceDescription.replaceAll("<", "&lt;"));
      new File(targetToolsDir, "/${it.serviceId}.xml").write(
        jmte.transform(toolTempl, bindings),
        "UTF-8");
      services.add(it);
    }
  }
}


// generate content of tool_config.xml
println "generation completed. please add following settings to your tool_config.xml";
println "-----";
bindings = new HashMap();
bindings.put("toolId", toolId);
bindings.put("toolName", toolName);
bindings.put("services", services);
println jmte.transform(
	new File("templates/tool_config.xml.template.jmte").text,
	bindings);
println "-----";
println "paste code above to tool_config.xml to enable services.";

