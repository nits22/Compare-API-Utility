package com.assignment.utils;

import org.custommonkey.xmlunit.DetailedDiff;
import org.xmlunit.diff.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.ElementSelectors;

import java.io.IOException;
import java.util.List;

public class CompareXML {

	public boolean compare(String xml1,String xml2) {

		if(xml1 == null && xml2 == null)
			return true;

		Diff diff = null;
		try {
			diff = DiffBuilder.compare(xml1).withTest(xml2)
					.ignoreWhitespace()
					.normalizeWhitespace().ignoreComments()
					.checkForSimilar() 						// a different order is always 'similar' not equals.
					.withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText))
					.build();
		}
		catch (Exception e) {
			return false;
		}
		return !diff.hasDifferences();
	}

	public static void main(String[] args) throws Exception {
		String result = "<geoPlugin>\n" +
				"<geoplugin_request>105.22.102.135</geoplugin_request>\n" +
				"<geoplugin_delay>2ms</geoplugin_delay>\n" +
				"<geoplugin_status>200</geoplugin_status>\n" +
				"   </geoPlugin>";
		String expected = "<geoPlugin>\n" +
				"<geoplugin_request>105.22.102.135</geoplugin_request>\n" +
				"<geoplugin_status>200</geoplugin_status>\n" +
				"<geoplugin_delay>2ms</geoplugin_delay>\n" +
				"</geoPlugin>";
		CompareXML xc = new CompareXML();
		//Assert.assertEquals(true, xc.compare(result, expected));
		//assertXMLEquals(expected, result);
	}

	public static boolean assertXMLEquals(String expectedXML, String actualXML)  {
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreAttributeOrder(true);

		DetailedDiff diff = null;
		try {
			diff = new DetailedDiff(XMLUnit.compareXML(expectedXML, actualXML));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<?> allDifferences = diff.getAllDifferences();
		//Assert.assertEquals(0, allDifferences.size(),"Differences found: "+ diff.toString());
		if(allDifferences.size() == 0)
			return true;
		else
			return false;
	}
}
