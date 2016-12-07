@Grab('net.arnx:jsonic:1.3.10')
@Grab('org.langrid:jp.go.nict.langrid.wrapper.common:1.0.8')
@Grab('org.langrid:org.langrid.service.api.lapps_nlp:1.0.8')

import net.arnx.jsonic.JSON;
import org.langrid.service.api.lapps_nlp.Annotation;
import org.langrid.service.api.lapps_nlp.Request;
import org.langrid.service.api.lapps_nlp.Response;
import org.langrid.service.api.lapps_nlp.View;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.wrapper.common.pos.PennTreebank;

new File(args[0]).eachLine("UTF-8"){
	Response res = JSON.decode(it, Response.class);
	for(View v : res.getPayload().getViews()){
		if(!v.getMetadata().getContains().containsKey("http://vocab.lappsgrid.org/Token#pos")){
			continue;
		}
		Annotation[] annots = res.getPayload().getViews().get(1).getAnnotations();
		Morpheme[] ret = new Morpheme[annots.length];
		for(int i = 0; i < ret.length; i++){
			Annotation a = annots[i];
			String word = (String)a.getFeatures().get("word");
			String pos = (String)a.getFeatures().get("pos");
			ret[i] = new Morpheme(word, word, PennTreebank.ptToLg(pos).getExpression());
		}
		println JSON.encode(ret);
		return;
	}
	println "[]";
}