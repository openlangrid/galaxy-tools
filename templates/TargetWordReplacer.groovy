@Grab('net.arnx:jsonic:1.3.10')
@Grab('org.langrid:jp.go.nict.langrid.wrapper.workflowsupport:1.0.8')
import jp.go.nict.langrid.wrapper.workflowsupport.ReplacementTerm;
import net.arnx.jsonic.JSON;

config = new ConfigSlurper().parse(new File(
	new File(this.class.protectionDomain.codeSource.location.path).parentFile, 'config.groovy').toURL())

targetLang = args[0];
transresults = new File(args[1]).readLines("UTF-8");
codes = new File(args[2]).readLines("UTF-8");
words = new File(args[3]).readLines("UTF-8");

rt = new ReplacementTerm();

i = 0;
transresults.each{
	if(it.length() == 0){
		i++;
		return;
	}
	println rt.replace(targetLang, it,
		JSON.decode(codes[i], String[].class),
		JSON.decode(words[i], String[].class)
		);
	i++;
}
