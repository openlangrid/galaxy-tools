@Grab('net.arnx:jsonic:1.3.10')
@Grab('org.langrid:org.langrid.service.api.lapps_nlp:1.0.8')

import net.arnx.jsonic.JSON;
import org.langrid.service.api.lapps_nlp.Request;

print JSON.encode(new Request(
					"http://vocab.lappsgrid.org/ns/media/text",
					new File(args[0]).text
					));
