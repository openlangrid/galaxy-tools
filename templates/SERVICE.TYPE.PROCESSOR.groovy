@Grab('org.langrid:jp.go.nict.langrid.client.soap:1.0.8')
@Grab('org.langrid:org.langrid.service.api.lapps_nlp:1.0.8')
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import org.langrid.service.api.lapps_nlp.Processor;

config = new ConfigSlurper().parse(new File(
  new File(this.class.protectionDomain.codeSource.location.path).parentFile, 'config.groovy').toURL())
client = new SoapClientFactory().create(
  Processor.class,
  new URL(config.langrid.invokerUrl + args[0]),
  config.langrid.id,
  config.langrid.password)

print client.execute(new File(args[1]).text)

