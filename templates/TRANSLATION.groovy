@Grab('org.langrid:jp.go.nict.langrid.client.soap:1.0.7')
@Grab('org.langrid:jp.go.nict.langrid.service.language_1_2:1.0.7')
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;

serviceId = args[0];
additionalUrlParam = args[1];
sourceLang = args[2];
targetLang = args[3];
sourceFile = args[4];

config = new ConfigSlurper().parse(new File(
	new File(this.class.protectionDomain.codeSource.location.path).parentFile, 'config.groovy').toURL())

client = new SoapClientFactory().create(
	TranslationService.class,
	new URL(config.langrid.invokerUrl + serviceId + additionalUrlParam),
	config.langrid.id,
	config.langrid.password)

new File(sourceFile).eachLine{
	println client.translate(sourceLang, targetLang, it);
}
