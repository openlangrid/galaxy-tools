@Grab('org.langrid:jp.go.nict.langrid.client.soap:1.0.8')
@Grab('org.langrid:jp.go.nict.langrid.service.language_1_2:1.0.8')
@Grab('net.arnx:jsonic:1.3.10')
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.MorphologicalAnalysisService;
import net.arnx.jsonic.JSON;

userEmail = args[0];
serviceId = args[1];
additionalUrlParam = args[2];
language = args[3];
texts = args[4];

config = new ConfigSlurper().parse(new File(
	new File(this.class.protectionDomain.codeSource.location.path).parentFile,
	'config.groovy').toURL());

client = new SoapClientFactory().create(
	MorphologicalAnalysisService.class,
	new URL(config.langrid.invokerUrl + serviceId + additionalUrlParam),
	config.langrid.appAuthKey, userEmail);

new File(texts).eachLine("UTF-8"){
	println JSON.encode(client.analyze(language, it));
}
