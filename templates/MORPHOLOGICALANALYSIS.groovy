@Grab('org.langrid:jp.go.nict.langrid.client.soap:1.0.8')
@Grab('org.langrid:jp.go.nict.langrid.service.language_1_2:1.0.8')
@Grab('net.arnx:jsonic:1.3.10')
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.MorphologicalAnalysisService;
import net.arnx.jsonic.JSON;

serviceId = args[0];
language = args[1];
sources = new File(args[2]).readLines("UTF-8");

config = new ConfigSlurper().parse(new File(
	new File(this.class.protectionDomain.codeSource.location.path).parentFile, 'config.groovy').toURL())

client = new SoapClientFactory().create(
	MorphologicalAnalysisService.class,
	new URL(config.langrid.invokerUrl + serviceId),
	config.langrid.id,
	config.langrid.password)

sources.each{
	println JSON.encode(client.analyze(language, it));
}
