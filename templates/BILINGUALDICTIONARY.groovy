@Grab('org.langrid:jp.go.nict.langrid.client.soap:1.0.7')
@Grab('org.langrid:jp.go.nict.langrid.service.language_1_2:1.0.7')
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryService;

config = new ConfigSlurper().parse(new File(
  new File(this.class.protectionDomain.codeSource.location.path).parentFile, 'config.groovy').toURL())
client = new SoapClientFactory().create(
  BilingualDictionaryService.class,
  new URL(config.langrid.invokerUrl + args[2]),
  config.langrid.id,
  config.langrid.password)

new File(args[2]).eachLine{
  client.search(args[0], args[1], it, args[3]).each{ println it.headWord + "\t" + it.targetWords[0] }
}
