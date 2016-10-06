@Grab('org.langrid:jp.go.nict.langrid.client.soap:1.0.7')
@Grab('org.langrid:jp.go.nict.langrid.service.language_1_2:1.0.7')
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;

config = new ConfigSlurper().parse(new File(
  new File(this.class.protectionDomain.codeSource.location.path).parentFile, 'config.groovy').toURL())
client = new SoapClientFactory().create(
  TranslationService.class,
  new URL(config.langrid.invokerUrl + args[3]),
  config.langrid.id,
  config.langrid.password)

new File(args[2]).eachLine{
  println client.translate(args[0], args[1], it)
}
