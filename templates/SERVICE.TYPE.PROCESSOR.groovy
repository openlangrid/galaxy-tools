@Grab('org.langrid:jp.go.nict.langrid.client.soap:1.0.8')
@Grab('org.langrid:org.langrid.service.api.lapps_nlp:1.0.8')
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import org.langrid.service.api.lapps_nlp.Processor;

serviceId = args[0];
requests = new File(args[1]).readLines("UTF-8");

config = new ConfigSlurper().parse(new File(
	new File(this.class.protectionDomain.codeSource.location.path).parentFile, 'config.groovy'
	).toURL())
client = new SoapClientFactory().create(
	Processor.class,
	new URL(config.langrid.invokerUrl + serviceId),
	config.langrid.id,
	config.langrid.password)

requests.each{
	println client.execute(it);
}
