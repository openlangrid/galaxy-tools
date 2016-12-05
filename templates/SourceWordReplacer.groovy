@Grab('net.arnx:jsonic:1.3.10')
@Grab('org.langrid:jp.go.nict.langrid.wrapper.workflowsupport:1.0.8')
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.wrapper.workflowsupport.ConstructSourceAndMorphemesAndCodes;
import net.arnx.jsonic.JSON;

lang = args[0];
morphs = new File(args[1]).readLines("UTF-8");
trans = new File(args[2]).readLines("UTF-8");

s = new File(args[3]);
m = new File(args[4]);
c = new File(args[5]);
w = new File(args[6]);

csmc = new ConstructSourceAndMorphemesAndCodes();

i = 0;
morphs.each{
	if(it.length() == 0){
		i++;
		return;
	}
	morphs = JSON.decode(it, Morpheme[].class);
	translations = JSON.decode(trans[i++], TranslationWithPosition[].class);
	ret = csmc.constructSMC(lang, morphs, translations);
	s.write(ret.getSource() + "\n", "UTF-8");
	m.write(JSON.encode(ret.getMorphemes()) + "\n", "UTF-8");
	c.write(JSON.encode(ret.getCodes()) + "\n", "UTF-8");
	w.write(JSON.encode(ret.getTargetWords()) + "\n", "UTF-8");
}
