@Grab('org.langrid:jp.go.nict.langrid.client.soap:1.0.7')
@Grab('org.langrid:jp.go.nict.langrid.service.language_1_2:1.0.7')
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;

userEmail = args[0];
serviceId = args[1];
additionalUrlParam = args[2];
sourceLang = args[3];
targetLang = args[4];
sources = args[5];

config = new ConfigSlurper().parse(new File(
	new File(this.class.protectionDomain.codeSource.location.path).parentFile,
	'config.groovy').toURL());

client = new SoapClientFactory().create(
	TranslationService.class,
	new URL(config.langrid.invokerUrl + serviceId + additionalUrlParam),
	config.langrid.appAuthKey, userEmail);

new File(sources).eachLine("UTF-8"){
	println client.translate(sourceLang, targetLang, it);
}
