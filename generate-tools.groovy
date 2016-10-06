/*
*/
@Grab('org.langrid:jp.go.nict.langrid.client.soap:1.0.7')
@Grab('org.langrid:jp.go.nict.langrid.service.management_1_2:1.0.7')
@Grab('com.floreysoft:jmte:3.1.1')
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.Order;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceManagementService;
import com.floreysoft.jmte.Engine;


if(args.length != 5){
  println "[usage] groovy generate-tools.groovy targetDir toolId toolName serviceGridUrl serviceGridUserId";
  return
}
print "Password: "
def dir = args[0]
def toolId = args[1]
def toolName = args[2]
def url = args[3]
def user = args[4]
def pass = new BufferedReader(new InputStreamReader(System.in)).readLine()

println ""
println "retrieving service list..."
def client = new SoapClientFactory().create(
  ServiceManagementService.class, new URL(url + "services/ServiceManagement"), user, pass)
def services = new ArrayList();
for(i in 0..10){
  result = client.searchServices(i * 100, 100, new MatchingCondition[0],
    [new Order("gridId", "ASCENDANT"), new Order("serviceId", "ASCENDANT")] as Order[], "ALL");
  if(result.elements.length == 0) break;
  result.elements.each{ 
    println it.serviceId;
    services.add(it);
  }
}

new File(dir + "/" + toolId).mkdirs()

Engine jmte = Engine.createNonCachingEngine()
String toolConfigTmpl = new File("templates/tool_config.xml.template.jmte").text
Map bindings = new HashMap();
bindings.put("toolId", toolId);
bindings.put("toolName", toolName);
bindings.put("services", services);
println jmte.transform(toolConfigTmpl, bindings);
