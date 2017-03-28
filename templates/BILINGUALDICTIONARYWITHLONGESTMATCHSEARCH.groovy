@Grab('net.arnx:jsonic:1.3.10')
@Grab('org.langrid:jp.go.nict.langrid.client.soap:1.0.10')
@Grab('org.langrid:jp.go.nict.langrid.service.language_1_2:1.0.10')
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryWithLongestMatchSearchService;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import net.arnx.jsonic.JSON;

userEmail = args[0];
serviceId = args[1];
additionalUrlParam = args[2];
sourceLang = args[3];
targetLang = args[4];
morphemes = args[5];

config = new ConfigSlurper().parse(new File(
	new File(this.class.protectionDomain.codeSource.location.path).parentFile,
	'config.groovy').toURL());

client = new SoapClientFactory().create(
	BilingualDictionaryWithLongestMatchSearchService.class,
	new URL(config.langrid.invokerUrl + serviceId + additionalUrlParam),
	config.langrid.appAuthKey, userEmail);

new File(morphemes).eachLine("UTF-8"){
	println JSON.encode(client.searchLongestMatchingTerms(
    	sourceLang, targetLang, JSON.decode(it, Morpheme[].class)
    ));
}
