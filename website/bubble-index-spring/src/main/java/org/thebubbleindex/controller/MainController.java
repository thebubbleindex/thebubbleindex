package org.thebubbleindex.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.thebubbleindex.model.*;
import org.thebubbleindex.model.bubbleindex.BubbleIndex;
import org.thebubbleindex.repository.MainRepository;

@RestController
public class MainController {

	@Autowired
	MainRepository mainRepository;

	@RequestMapping("/")
	public ModelAndView root() {
		return index();
	}
	
	@RequestMapping("/index")
	public ModelAndView index() {
		return new ModelAndView("Application/index");
	}
	
	@RequestMapping("/pages/method")
	public ModelAndView about() {
		return new ModelAndView("Pages/about");
	}
	
	@RequestMapping("/pages/links")
	public ModelAndView links() {
		return new ModelAndView("Pages/links");
	}
	
	@RequestMapping("/pages/examples")
	public ModelAndView examples() {
		return new ModelAndView("Pages/examples");
	}
	
	@RequestMapping("/pages/contact")
	public ModelAndView contact() {
		return new ModelAndView("Pages/contact");
	}
	
	@RequestMapping("/pages/search")
	public ModelAndView search() {
		return new ModelAndView("Pages/search");
	}
	
	@RequestMapping("/pages/legal")
	public ModelAndView legal() {
		return new ModelAndView("Pages/legal");
	}

	@RequestMapping("/greeting")
	public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name,
			Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}

	@RequestMapping(value = "/sitemap.xml", method = RequestMethod.GET)
	public String getSiteMap() {
		return "sitemap.xml";
	}

	@RequestMapping(value = "/favicon.ico", method = RequestMethod.GET)
	public String getFavicon() {
		return "favicon.ico";
	}

	@RequestMapping(value = "/robots.txt", method = RequestMethod.GET)
	public String getRobots() {
		return "robots.txt";
	}

	@RequestMapping(value = "/browse")
	public ModelAndView browse() {
		return new ModelAndView("Application/browse");
	}
	
	@RequestMapping(value = "/browseEurope")
	public ModelAndView browseEurope() {
		return new ModelAndView("Application/browseEurope");
	}

	@RequestMapping(value = "/browseMoreEurope")
	public ModelAndView browseMoreEurope() {
		return new ModelAndView("Application/browseMoreEurope");
	}
	
	@RequestMapping(value = "/browseAmericas")
	public ModelAndView browseAmericas() {
		return new ModelAndView("Application/browseAmericas");
	}

	@RequestMapping(value = "/browseAsia")
	public ModelAndView browseAsia() {
		return new ModelAndView("Application/browseAsia");
	}

	@RequestMapping(value = "/browsePacific")
	public ModelAndView browsePacific() {
		return new ModelAndView("Application/browsePacific");
	}
	
	@RequestMapping(value = "/browseComposite")
	public ModelAndView browseComposite() {
		return new ModelAndView("Application/browseComposite");
	}
	
	@RequestMapping(value = "/browselist/{type}", method = RequestMethod.GET)
	public ModelAndView browselist(@PathVariable final String type) {

		final Map<String, Object> model = new HashMap<String, Object>(2);
		final List<BubbleIndexTimeseries> list = mainRepository.findByType(type);
		if (list != null && !list.isEmpty())
			model.put("list", list);
		
		model.put("Type", type);
		
		return new ModelAndView("Application/browselist", model);
	}

	@RequestMapping(value = "/searchResults", method = RequestMethod.GET)
	public ModelAndView searchResults(@RequestParam("search") final String search) {

		final List<BubbleIndex> stocksResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> indicesResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> currenciesResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> commoditiesResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> germanyResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> hongkongResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> unitedkingdomResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> indiaResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> brazilResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> chinaResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> japanResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> argentinaResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> southkoreaResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> australiaResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> israelResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> singaporeResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> italyResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> mexicoResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> compositefiftyResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> canadaResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> indonesiaResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> franceResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> taiwanResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> compositeeightyResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> compositeninetyResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> compositeninetyfiveResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> compositeninetynineResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> switzerlandResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> swedenResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> austriaResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> greeceResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> norwayResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> denmarkResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> newzealandResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> spainResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> netherlandsResults = new ArrayList<BubbleIndex>(5);
		final List<BubbleIndex> russiaResults = new ArrayList<BubbleIndex>(5);

		final String[] splitted = search.toUpperCase().split("\\s+", 4);

		BubbleIndex index = new BubbleIndex();
		int totalFound = 0;

		for (int i = 0; i < splitted.length; i++) {
			final List<BubbleIndexTimeseries> temp = mainRepository.findBySymbol("Stocks", splitted[i]);

			if (!temp.isEmpty()) {
				final int size = Math.min(temp.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex stockResult = new BubbleIndex();
					stockResult.dtype = "Stocks";
					stockResult.symbol = temp.get(j).symbol;
					stockResult.name = temp.get(j).name;
					stockResult.findPlotLocation();
					stocksResults.add(stockResult);
				}

				if (size == 1) {
					index = stocksResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwo = mainRepository.findByKeywords("Indices", splitted[i]);

			if (!temptwo.isEmpty()) {
				final int size = Math.min(temptwo.size(), 12);
				for (int j = 0; j < size; j++) {
					final BubbleIndex indexResult = new BubbleIndex();
					indexResult.dtype = "Indices";
					indexResult.symbol = temptwo.get(j).symbol;
					indexResult.name = temptwo.get(j).name;
					indexResult.findPlotLocation();
					indicesResults.add(indexResult);
				}

				if (size == 1) {
					index = indicesResults.get(0);
				}

				totalFound = totalFound + size;

			}

			final List<BubbleIndexTimeseries> tempthree = mainRepository.findByKeywords("Currencies", splitted[i]);

			if (!tempthree.isEmpty()) {
				int size = Math.min(tempthree.size(), 12);
				for (int j = 0; j < size; j++) {
					final BubbleIndex currencyResult = new BubbleIndex();
					currencyResult.dtype = "Currencies";
					currencyResult.symbol = tempthree.get(j).symbol;
					currencyResult.name = tempthree.get(j).name;
					currencyResult.findPlotLocation();
					currenciesResults.add(currencyResult);
				}

				if (size == 1) {
					index = currenciesResults.get(0);
				}

				totalFound = totalFound + size;

			}

			final List<BubbleIndexTimeseries> tempfour = mainRepository.findByKeywords("Commodities", splitted[i]);

			if (!tempfour.isEmpty()) {
				final int size = Math.min(tempfour.size(), 12);
				for (int j = 0; j < size; j++) {
					final BubbleIndex commodityResult = new BubbleIndex();
					commodityResult.dtype = "Commodities";
					commodityResult.symbol = tempfour.get(j).symbol;
					commodityResult.name = tempfour.get(j).name;
					commodityResult.findPlotLocation();
					commoditiesResults.add(commodityResult);
				}

				if (size == 1) {
					index = commoditiesResults.get(0);
				}

				totalFound = totalFound + size;

			}

			final List<BubbleIndexTimeseries> tempfive = mainRepository.findByKeywords("Germany", splitted[i]);

			if (!tempfive.isEmpty()) {
				final int size = Math.min(tempfive.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex germanyResult = new BubbleIndex();
					germanyResult.dtype = "Germany";
					germanyResult.symbol = tempfive.get(j).symbol;
					germanyResult.name = tempfive.get(j).name;
					germanyResult.findPlotLocation();
					germanyResults.add(germanyResult);
				}

				if (size == 1) {
					index = germanyResults.get(0);
				}

				totalFound = totalFound + size;

			}

			final List<BubbleIndexTimeseries> tempsix = mainRepository.findByDualKeywords("HongKong", splitted[i], splitted[i] + ".HK");

			if (!tempsix.isEmpty()) {
				int size = Math.min(tempsix.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex hongkongResult = new BubbleIndex();
					hongkongResult.dtype = "HongKong";
					hongkongResult.symbol = tempsix.get(j).symbol;
					hongkongResult.name = tempsix.get(j).name;
					hongkongResult.findPlotLocation();
					hongkongResults.add(hongkongResult);
				}

				if (size == 1) {
					index = hongkongResults.get(0);
				}

				totalFound = totalFound + size;

			}

			final List<BubbleIndexTimeseries> tempseven = mainRepository.findByDualKeywords("UnitedKingdom", splitted[i], splitted[i] + ".L");

			if (!tempseven.isEmpty()) {
				final int size = Math.min(tempseven.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex unitedkingdomResult = new BubbleIndex();
					unitedkingdomResult.dtype = "UnitedKingdom";
					unitedkingdomResult.symbol = tempseven.get(j).symbol;
					unitedkingdomResult.name = tempseven.get(j).name;
					unitedkingdomResult.findPlotLocation();
					unitedkingdomResults.add(unitedkingdomResult);
				}

				if (size == 1) {
					index = unitedkingdomResults.get(0);
				}

				totalFound = totalFound + size;

			}

			final List<BubbleIndexTimeseries> tempeight = mainRepository.findByDualKeywords("India", splitted[i], splitted[i] + ".BO");

			if (!tempeight.isEmpty()) {
				final int size = Math.min(tempeight.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex indiaResult = new BubbleIndex();
					indiaResult.dtype = "India";
					indiaResult.symbol = tempeight.get(j).symbol;
					indiaResult.name = tempeight.get(j).name;
					indiaResult.findPlotLocation();
					indiaResults.add(indiaResult);
				}

				if (size == 1) {
					index = indiaResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempnine = mainRepository.findByDualKeywords("Brazil", splitted[i], splitted[i] + ".SA");

			if (!tempnine.isEmpty()) {
				final int size = Math.min(tempnine.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex brazilResult = new BubbleIndex();
					brazilResult.dtype = "Brazil";
					brazilResult.symbol = tempnine.get(j).symbol;
					brazilResult.name = tempnine.get(j).name;
					brazilResult.findPlotLocation();
					brazilResults.add(brazilResult);
				}

				if (size == 1) {
					index = brazilResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempten = mainRepository.findByTriKeywords("China", splitted[i], splitted[i] + ".SZ",  splitted[i] + ".SS");

			if (!tempten.isEmpty()) {
				final int size = Math.min(tempten.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex chinaResult = new BubbleIndex();
					chinaResult.dtype = "China";
					chinaResult.symbol = tempten.get(j).symbol;
					chinaResult.name = tempten.get(j).name;
					chinaResult.findPlotLocation();
					chinaResults.add(chinaResult);
				}

				if (size == 1) {
					index = chinaResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempeleven = mainRepository.findByKeywords("Japan", splitted[i]);

			if (!tempeleven.isEmpty()) {
				final int size = Math.min(tempeleven.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex japanResult = new BubbleIndex();
					japanResult.dtype = "Japan";
					japanResult.symbol = tempeleven.get(j).symbol;
					japanResult.name = tempeleven.get(j).name;
					japanResult.findPlotLocation();
					japanResults.add(japanResult);
				}

				if (size == 1) {
					index = japanResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwelve = mainRepository.findByDualKeywords("Argentina", splitted[i], splitted[i] + ".BA");

			if (!temptwelve.isEmpty()) {
				final int size = Math.min(temptwelve.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex argentinaResult = new BubbleIndex();
					argentinaResult.dtype = "Argentina";
					argentinaResult.symbol = temptwelve.get(j).symbol;
					argentinaResult.name = temptwelve.get(j).name;
					argentinaResult.findPlotLocation();
					argentinaResults.add(argentinaResult);
				}

				if (size == 1) {
					index = argentinaResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempthirteen = mainRepository.findByDualKeywords("SouthKorea", splitted[i], splitted[i] + ".KS");

			if (!tempthirteen.isEmpty()) {
				final int size = Math.min(tempthirteen.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex southkoreaResult = new BubbleIndex();
					southkoreaResult.dtype = "SouthKorea";
					southkoreaResult.symbol = tempthirteen.get(j).symbol;
					southkoreaResult.name = tempthirteen.get(j).name;
					southkoreaResult.findPlotLocation();
					southkoreaResults.add(southkoreaResult);
				}

				if (size == 1) {
					index = southkoreaResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempfourteen = mainRepository.findByDualKeywords("Australia", splitted[i], splitted[i] + ".AX");

			if (!tempfourteen.isEmpty()) {
				final int size = Math.min(tempfourteen.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex australiaResult = new BubbleIndex();
					australiaResult.dtype = "Australia";
					australiaResult.symbol = tempfourteen.get(j).symbol;
					australiaResult.name = tempfourteen.get(j).name;
					australiaResult.findPlotLocation();
					australiaResults.add(australiaResult);
				}

				if (size == 1) {
					index = australiaResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempfifteen = mainRepository.findByDualKeywords("Israel", splitted[i], splitted[i] + ".TA");

			if (!tempfifteen.isEmpty()) {
				final int size = Math.min(tempfifteen.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex israelResult = new BubbleIndex();
					israelResult.dtype = "Israel";
					israelResult.symbol = tempfifteen.get(j).symbol;
					israelResult.name = tempfifteen.get(j).name;
					israelResult.findPlotLocation();
					israelResults.add(israelResult);
				}

				if (size == 1) {
					index = israelResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempsixteen = mainRepository.findByDualKeywords("Singapore", splitted[i], splitted[i] + ".SI");

			if (!tempsixteen.isEmpty()) {
				final int size = Math.min(tempsixteen.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex singaporeResult = new BubbleIndex();
					singaporeResult.dtype = "Singapore";
					singaporeResult.symbol = tempsixteen.get(j).symbol;
					singaporeResult.name = tempsixteen.get(j).name;
					singaporeResult.findPlotLocation();
					singaporeResults.add(singaporeResult);
				}

				if (size == 1) {
					index = singaporeResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempseventeen = mainRepository.findByKeywords("CompositeFifty", splitted[i]);

			if (!tempseventeen.isEmpty()) {
				final int size = Math.min(tempseventeen.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex compositefiftyResult = new BubbleIndex();
					compositefiftyResult.dtype = "CompositeFifty";
					compositefiftyResult.symbol = tempseventeen.get(j).symbol;
					compositefiftyResult.name = tempseventeen.get(j).name;
					compositefiftyResult.findPlotLocation();
					compositefiftyResults.add(compositefiftyResult);
				}

				if (size == 1) {
					index = compositefiftyResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempeighteen = mainRepository.findByDualKeywords("Italy", splitted[i], splitted[i] + ".MI");

			if (!tempeighteen.isEmpty()) {
				final int size = Math.min(tempeighteen.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex italyResult = new BubbleIndex();
					italyResult.dtype = "Italy";
					italyResult.symbol = tempeighteen.get(j).symbol;
					italyResult.name = tempeighteen.get(j).name;
					italyResult.findPlotLocation();
					italyResults.add(italyResult);
				}

				if (size == 1) {
					index = italyResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempnineteen = mainRepository.findByDualKeywords("Mexico", splitted[i], splitted[i] + ".MX");

			if (!tempnineteen.isEmpty()) {
				final int size = Math.min(tempnineteen.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex mexicoResult = new BubbleIndex();
					mexicoResult.dtype = "Mexico";
					mexicoResult.symbol = tempnineteen.get(j).symbol;
					mexicoResult.name = tempnineteen.get(j).name;
					mexicoResult.findPlotLocation();
					mexicoResults.add(mexicoResult);
				}

				if (size == 1) {
					index = mexicoResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwenty = mainRepository.findByTriKeywords("Canada", splitted[i], splitted[i] + ".V", splitted[i] + ".TO");
			
			if (!temptwenty.isEmpty()) {
				final int size = Math.min(temptwenty.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex canadaResult = new BubbleIndex();
					canadaResult.dtype = "Canada";
					canadaResult.symbol = temptwenty.get(j).symbol;
					canadaResult.name = temptwenty.get(j).name;
					canadaResult.findPlotLocation();
					canadaResults.add(canadaResult);
				}

				if (size == 1) {
					index = canadaResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwentyone = mainRepository.findByDualKeywords("France", splitted[i], splitted[i] + ".PA");

			if (!temptwentyone.isEmpty()) {
				final int size = Math.min(temptwentyone.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex franceResult = new BubbleIndex();
					franceResult.dtype = "France";
					franceResult.symbol = temptwentyone.get(j).symbol;
					franceResult.name = temptwentyone.get(j).name;
					franceResult.findPlotLocation();
					franceResults.add(franceResult);
				}

				if (size == 1) {
					index = franceResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwentytwo = mainRepository.findByDualKeywords("Indonesia", splitted[i], splitted[i] + ".JK");

			if (!temptwentytwo.isEmpty()) {
				final int size = Math.min(temptwentytwo.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex indonesiaResult = new BubbleIndex();
					indonesiaResult.dtype = "Indonesia";
					indonesiaResult.symbol = temptwentytwo.get(j).symbol;
					indonesiaResult.name = temptwentytwo.get(j).name;
					indonesiaResult.findPlotLocation();
					indonesiaResults.add(indonesiaResult);
				}

				if (size == 1) {
					index = indonesiaResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwentythree = mainRepository.findByDualKeywords("Taiwan", splitted[i], splitted[i] + ".TW");

			if (!temptwentythree.isEmpty()) {
				final int size = Math.min(temptwentythree.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex taiwanResult = new BubbleIndex();
					taiwanResult.dtype = "Taiwan";
					taiwanResult.symbol = temptwentythree.get(j).symbol;
					taiwanResult.name = temptwentythree.get(j).name;
					taiwanResult.findPlotLocation();
					taiwanResults.add(taiwanResult);
				}

				if (size == 1) {
					index = taiwanResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwentyfour = mainRepository.findByKeywords("CompositeEighty", splitted[i]);

			if (!temptwentyfour.isEmpty()) {
				final int size = Math.min(temptwentyfour.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex compositeeightyResult = new BubbleIndex();
					compositeeightyResult.dtype = "CompositeEighty";
					compositeeightyResult.symbol = temptwentyfour.get(j).symbol;
					compositeeightyResult.name = temptwentyfour.get(j).name;
					compositeeightyResult.findPlotLocation();
					compositeeightyResults.add(compositeeightyResult);
				}

				if (size == 1) {
					index = compositeeightyResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwentyfive = mainRepository.findByKeywords("CompositeNinety", splitted[i]);

			if (!temptwentyfive.isEmpty()) {
				final int size = Math.min(temptwentyfive.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex compositeninetyResult = new BubbleIndex();
					compositeninetyResult.dtype = "CompositeNinety";
					compositeninetyResult.symbol = temptwentyfive.get(j).symbol;
					compositeninetyResult.name = temptwentyfive.get(j).name;
					compositeninetyResult.findPlotLocation();
					compositeninetyResults.add(compositeninetyResult);
				}

				if (size == 1) {
					index = compositeninetyResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwentysix = mainRepository.findByKeywords("CompositeNinetyFive", splitted[i]);

			if (!temptwentysix.isEmpty()) {
				final int size = Math.min(temptwentysix.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex compositeninetyfiveResult = new BubbleIndex();
					compositeninetyfiveResult.dtype = "CompositeNinetyFive";
					compositeninetyfiveResult.symbol = temptwentysix.get(j).symbol;
					compositeninetyfiveResult.name = temptwentysix.get(j).name;
					compositeninetyfiveResult.findPlotLocation();
					compositeninetyfiveResults.add(compositeninetyfiveResult);
				}

				if (size == 1) {
					index = compositeninetyfiveResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwentyseven = mainRepository.findByKeywords("CompositeNinetyNine", splitted[i]);

			if (!temptwentyseven.isEmpty()) {
				final int size = Math.min(temptwentyseven.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex compositeninetynineResult = new BubbleIndex();
					compositeninetynineResult.dtype = "CompositeNinetyNine";
					compositeninetynineResult.symbol = temptwentyseven.get(j).symbol;
					compositeninetynineResult.name = temptwentyseven.get(j).name;
					compositeninetynineResult.findPlotLocation();
					compositeninetynineResults.add(compositeninetynineResult);
				}

				if (size == 1) {
					index = compositeninetynineResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwentyeight = mainRepository.findByDualKeywords("Switzerland", splitted[i], splitted[i] + ".SW");

			if (!temptwentyeight.isEmpty()) {
				final int size = Math.min(temptwentyeight.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex switzerlandResult = new BubbleIndex();
					switzerlandResult.dtype = "Switzerland";
					switzerlandResult.symbol = temptwentyeight.get(j).symbol;
					switzerlandResult.name = temptwentyeight.get(j).name;
					switzerlandResult.findPlotLocation();
					switzerlandResults.add(switzerlandResult);
				}

				if (size == 1) {
					index = switzerlandResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwentynine = mainRepository.findByDualKeywords("Sweden", splitted[i], splitted[i] + ".ST");

			if (!temptwentynine.isEmpty()) {
				final int size = Math.min(temptwentynine.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex swedenResult = new BubbleIndex();
					swedenResult.dtype = "Sweden";
					swedenResult.symbol = temptwentynine.get(j).symbol;
					swedenResult.name = temptwentynine.get(j).name;
					swedenResult.findPlotLocation();
					swedenResults.add(swedenResult);
				}

				if (size == 1) {
					index = swedenResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempthirty = mainRepository.findByDualKeywords("Austria", splitted[i], splitted[i] + ".VI");

			if (!tempthirty.isEmpty()) {
				final int size = Math.min(tempthirty.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex austriaResult = new BubbleIndex();
					austriaResult.dtype = "Austria";
					austriaResult.symbol = tempthirty.get(j).symbol;
					austriaResult.name = tempthirty.get(j).name;
					austriaResult.findPlotLocation();
					austriaResults.add(austriaResult);
				}

				if (size == 1) {
					index = austriaResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempthirtyone = mainRepository.findByDualKeywords("Greece", splitted[i], splitted[i] + ".AT");

			if (!tempthirtyone.isEmpty()) {
				final int size = Math.min(tempthirtyone.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex greeceResult = new BubbleIndex();
					greeceResult.dtype = "Greece";
					greeceResult.symbol = tempthirtyone.get(j).symbol;
					greeceResult.name = tempthirtyone.get(j).name;
					greeceResult.findPlotLocation();
					greeceResults.add(greeceResult);
				}

				if (size == 1) {
					index = greeceResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempthirtytwo = mainRepository.findByDualKeywords("Norway", splitted[i], splitted[i] + ".OL");

			if (!tempthirtytwo.isEmpty()) {
				final int size = Math.min(tempthirtytwo.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex norwayResult = new BubbleIndex();
					norwayResult.dtype = "Norway";
					norwayResult.symbol = tempthirtytwo.get(j).symbol;
					norwayResult.name = tempthirtytwo.get(j).name;
					norwayResult.findPlotLocation();
					norwayResults.add(norwayResult);
				}

				if (size == 1) {
					index = norwayResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempthirtythree = mainRepository.findByDualKeywords("Denmark", splitted[i], splitted[i] + ".CO");

			if (!tempthirtythree.isEmpty()) {
				final int size = Math.min(tempthirtythree.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex denmarkResult = new BubbleIndex();
					denmarkResult.dtype = "Denmark";
					denmarkResult.symbol = tempthirtythree.get(j).symbol;
					denmarkResult.name = tempthirtythree.get(j).name;
					denmarkResult.findPlotLocation();
					denmarkResults.add(denmarkResult);
				}

				if (size == 1) {
					index = denmarkResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempthirtyfour = mainRepository.findByDualKeywords("NewZealand", splitted[i], splitted[i] + ".NZ");

			if (!tempthirtyfour.isEmpty()) {
				final int size = Math.min(tempthirtyfour.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex newzealandResult = new BubbleIndex();
					newzealandResult.dtype = "NewZealand";
					newzealandResult.symbol = tempthirtyfour.get(j).symbol;
					newzealandResult.name = tempthirtyfour.get(j).name;
					newzealandResult.findPlotLocation();
					newzealandResults.add(newzealandResult);
				}

				if (size == 1) {
					index = newzealandResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempthirtyfive = mainRepository.findByDualKeywords("Spain", splitted[i], splitted[i] + ".MC");

			if (!tempthirtyfive.isEmpty()) {
				final int size = Math.min(tempthirtyfive.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex spainResult = new BubbleIndex();
					spainResult.dtype = "Spain";
					spainResult.symbol = tempthirtyfive.get(j).symbol;
					spainResult.name = tempthirtyfive.get(j).name;
					spainResult.findPlotLocation();
					spainResults.add(spainResult);
				}

				if (size == 1) {
					index = spainResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempthirtysix = mainRepository.findByDualKeywords("Brazil", splitted[i], splitted[i] + ".AS");

			if (!tempthirtysix.isEmpty()) {
				final int size = Math.min(tempthirtysix.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex netherlandsResult = new BubbleIndex();
					netherlandsResult.dtype = "Netherlands";
					netherlandsResult.symbol = tempthirtysix.get(j).symbol;
					netherlandsResult.name = tempthirtysix.get(j).name;
					netherlandsResult.findPlotLocation();
					netherlandsResults.add(netherlandsResult);
				}

				if (size == 1) {
					index = netherlandsResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempthirtyseven = mainRepository.findByDualKeywords("Russia", splitted[i], splitted[i] + ".ME");

			if (!tempthirtyseven.isEmpty()) {
				final int size = Math.min(tempthirtyseven.size(), 4);
				for (int j = 0; j < size; j++) {
					final BubbleIndex russiaResult = new BubbleIndex();
					russiaResult.dtype = "Russia";
					russiaResult.symbol = tempthirtyseven.get(j).symbol;
					russiaResult.name = tempthirtyseven.get(j).name;
					russiaResult.findPlotLocation();
					russiaResults.add(russiaResult);
				}

				if (size == 1) {
					index = russiaResults.get(0);
				}

				totalFound = totalFound + size;
			}
		}

		if (totalFound == 0) {
			return noResults();
		} else if (totalFound == 1) {
			index.findLocation();
			index.findPlotLocation();
			return plot(index.dtype, index.name, index.symbol);
		} else {
			final Map<String, Object> model = new HashMap<String, Object>(30);
			model.put("stocksResults", stocksResults);
			model.put("indicesResults", indicesResults);
			model.put("currenciesResults", currenciesResults);
			model.put("commoditiesResults", commoditiesResults);
			model.put("germanyResults", germanyResults);
			model.put("hongkongResults", hongkongResults);
			model.put("unitedkingdomResults", unitedkingdomResults);
			model.put("indiaResults", indiaResults);
			model.put("brazilResults", brazilResults);
			model.put("chinaResults", chinaResults);
			model.put("japanResults", japanResults);
			model.put("argentinaResults", argentinaResults);
			model.put("southkoreaResults", southkoreaResults);
			model.put("australiaResults", australiaResults);
			model.put("israelResults", israelResults);
			model.put("singaporeResults", singaporeResults);
			model.put("compositefiftyResults", compositefiftyResults);
			model.put("italyResults", italyResults);
			model.put("mexicoResults", mexicoResults);
			model.put("canadaResults", canadaResults);
			model.put("franceResults", franceResults);
			model.put("indonesiaResults", indonesiaResults);
			model.put("taiwanResults", taiwanResults);
			model.put("compositeeightyResults", compositeeightyResults);
			model.put("compositeninetyResults", compositeninetyResults);
			model.put("compositeninetyfiveResults", compositeninetyfiveResults);
			model.put("compositeninetynineResults", compositeninetynineResults);
			model.put("switzerlandResults", switzerlandResults);
			model.put("swedenResults", swedenResults);
			model.put("austriaResults", austriaResults);
			model.put("denmarkResults", denmarkResults);
			model.put("greeceResults", greeceResults);
			model.put("norwayResults", norwayResults);
			model.put("newzealandResults", newzealandResults);
			model.put("spainResults", spainResults);
			model.put("netherlandsResults", netherlandsResults);
			model.put("russiaResults", russiaResults);
			model.put("totalFound", totalFound);

			return new ModelAndView("Application/searchResults", model);
		}		
	}

	public ModelAndView plot(final String type, final String name, final String symbol) {
		return new ModelAndView("redirect:https://bigttrott-thebubbleindex.netdna-ssl.com/TheBubbleIndex/" + type + "/" + symbol + "/"
				+ "plot.html");
	}
	
	@RequestMapping(value = "/plot", method = RequestMethod.GET)
	public ModelAndView plotEndpoint(@RequestParam("type") final String type, @RequestParam(value = "name", required = false) final String name, @RequestParam("symbol") final String symbol) {
		return new ModelAndView("redirect:https://bigttrott-thebubbleindex.netdna-ssl.com/TheBubbleIndex/" + type + "/" + symbol + "/"
				+ "plot.html");
	}

	public ModelAndView fullPlot(final String type, final String name, final String symbol) {
		return new ModelAndView("redirect:https://bigttrott-thebubbleindex.netdna-ssl.com/TheBubbleIndex/" + type + "/" + symbol + "/"
				+ "fullPlot.html");
	}

	@RequestMapping(value = "/noResults")
	public ModelAndView noResults() {
		return new ModelAndView("Application/noResults");
	}

}
