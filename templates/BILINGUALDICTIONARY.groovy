@Grab('org.langrid:jp.go.nict.langrid.client.soap:1.0.10')
@Grab('org.langrid:jp.go.nict.langrid.service.language_1_2:1.0.10')
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryService;

userEmail = args[0];
serviceId = args[1];
additionalUrlParam = args[2];
sourceLang = args[3];
targetLang = args[4];
headWords = args[5];
matchingMethod = args[6];

config = new ConfigSlurper().parse(new File(
	new File(this.class.protectionDomain.codeSource.location.path).parentFile,
	'config.groovy').toURL());

client = new SoapClientFactory().create(
	BilingualDictionaryService.class,
	new URL(config.langrid.invokerUrl + serviceId + additionalUrlParam),
	config.langrid.appAuthKey, userEmail);

new File(headWords).eachLine("UTF-8"){
	client.search(sourceLang, targetLang, it, matchingMethod).each{
		println it.headWord + "\t" + it.targetWords[0]
	}
}
