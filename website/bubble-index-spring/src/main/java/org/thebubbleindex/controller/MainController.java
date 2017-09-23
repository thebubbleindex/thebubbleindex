package org.thebubbleindex.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.thebubbleindex.model.*;
import org.thebubbleindex.repository.MainRepository;

@RestController
public class MainController {

	@Autowired
	MainRepository mainRepository;

	@RequestMapping("/index")
	public ModelAndView index() {
		return new ModelAndView("Application/index");
	}
	
	@RequestMapping("/pages/method")
	public ModelAndView about() {
		return new ModelAndView("Pages/about");
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

	@RequestMapping(value = "/browselist")
	public String browselist(final Model model, final String type) {

		final List<BubbleIndexTimeseries> list = mainRepository.findByTypeIgnoreCase(type);
		model.addAttribute("list", list);
		return "browselist";
	}

	public String browseEurope() {
		return "browseEurope";
	}

	public String browseMoreEurope() {
		return "browseMoreEurope";
	}

	public String browseAmericas() {
		return "browseAmericas";
	}

	public String browseAsia() {
		return "browseAsia";
	}

	public String browsePacific() {
		return "browsePacific";
	}

	public String browseComposite() {
		return "browseComposite";
	}

	public String searchResults(final Model model, final String search) {

		final List<BubbleIndex> stocksResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> indicesResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> currenciesResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> commoditiesResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> germanyResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> hongkongResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> unitedkingdomResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> indiaResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> brazilResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> chinaResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> japanResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> argentinaResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> southkoreaResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> australiaResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> israelResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> singaporeResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> italyResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> mexicoResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> compositefiftyResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> canadaResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> indonesiaResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> franceResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> taiwanResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> compositeeightyResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> compositeninetyResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> compositeninetyfiveResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> compositeninetynineResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> switzerlandResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> swedenResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> austriaResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> greeceResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> norwayResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> denmarkResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> newzealandResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> spainResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> netherlandsResults = new ArrayList<BubbleIndex>();
		final List<BubbleIndex> russiaResults = new ArrayList<BubbleIndex>();

		final String[] splitted = search.split("\\s+", 4);

		BubbleIndex index = new BubbleIndex();
		int totalFound = 0;

		for (int i = 0; i < splitted.length; i++) {
			final List<BubbleIndexTimeseries> temp = mainRepository.findBySymbolIgnoreCase("Stocks", splitted[i]).subList(0, 4);

			if (!temp.isEmpty()) {
				final int size = temp.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex stockResult = new BubbleIndex();
					stockResult.type = "Stocks";
					stockResult.symbol = temp.get(j).symbol;
					stockResult.name = temp.get(j).name;
					stocksResults.add(stockResult);
				}

				if (size == 1) {
					index = stocksResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwo = mainRepository.findByKeywordsIgnoreCase("Indices", splitted[i]).subList(0, 12);

			if (!temptwo.isEmpty()) {
				final int size = temptwo.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex indexResult = new BubbleIndex();
					indexResult.type = "Indices";
					indexResult.symbol = temptwo.get(j).symbol;
					indexResult.name = temptwo.get(j).name;
					indicesResults.add(indexResult);
				}

				if (size == 1) {
					index = indicesResults.get(0);
				}

				totalFound = totalFound + size;

			}

			final List<BubbleIndexTimeseries> tempthree = mainRepository.findByKeywordsIgnoreCase("Currencies", splitted[i]).subList(0, 12);

			if (!tempthree.isEmpty()) {
				int size = tempthree.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex currencyResult = new BubbleIndex();
					currencyResult.type = "Currencies";
					currencyResult.symbol = tempthree.get(j).symbol;
					currencyResult.name = tempthree.get(j).name;
					currenciesResults.add(currencyResult);
				}

				if (size == 1) {
					index = currenciesResults.get(0);
				}

				totalFound = totalFound + size;

			}

			final List<BubbleIndexTimeseries> tempfour = mainRepository.findByKeywordsIgnoreCase("Commodities", splitted[i]).subList(0, 12);

			if (!tempfour.isEmpty()) {
				final int size = tempfour.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex commodityResult = new BubbleIndex();
					commodityResult.type = "Commodities";
					commodityResult.symbol = tempfour.get(j).symbol;
					commodityResult.name = tempfour.get(j).name;
					commoditiesResults.add(commodityResult);
				}

				if (size == 1) {
					index = commoditiesResults.get(0);
				}

				totalFound = totalFound + size;

			}

			final List<BubbleIndexTimeseries> tempfive = mainRepository.findByKeywordsIgnoreCase("Germany", splitted[i]).subList(0, 4);

			if (!tempfive.isEmpty()) {
				final int size = tempfive.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex germanyResult = new BubbleIndex();
					germanyResult.type = "Germany";
					germanyResult.symbol = tempfive.get(j).symbol;
					germanyResult.name = tempfive.get(j).name;
					germanyResults.add(germanyResult);
				}

				if (size == 1) {
					index = germanyResults.get(0);
				}

				totalFound = totalFound + size;

			}

			final List<BubbleIndexTimeseries> tempsix = mainRepository.findByDualKeywordsIgnoreCase("HongKong", splitted[i], splitted[i] + ".HK").subList(0, 4);

			if (!tempsix.isEmpty()) {
				int size = tempsix.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex hongkongResult = new BubbleIndex();
					hongkongResult.type = "HongKong";
					hongkongResult.symbol = tempsix.get(j).symbol;
					hongkongResult.name = tempsix.get(j).name;
					hongkongResults.add(hongkongResult);
				}

				if (size == 1) {
					index = hongkongResults.get(0);
				}

				totalFound = totalFound + size;

			}

			final List<BubbleIndexTimeseries> tempseven = mainRepository.findByDualKeywordsIgnoreCase("UnitedKingdom", splitted[i], splitted[i] + ".L").subList(0, 4);

			if (!tempseven.isEmpty()) {
				final int size = tempseven.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex unitedkingdomResult = new BubbleIndex();
					unitedkingdomResult.type = "UnitedKingdom";
					unitedkingdomResult.symbol = tempseven.get(j).symbol;
					unitedkingdomResult.name = tempseven.get(j).name;
					unitedkingdomResults.add(unitedkingdomResult);
				}

				if (size == 1) {
					index = unitedkingdomResults.get(0);
				}

				totalFound = totalFound + size;

			}

			final List<BubbleIndexTimeseries> tempeight = mainRepository.findByDualKeywordsIgnoreCase("India", splitted[i], splitted[i] + ".BO").subList(0, 4);

			if (!tempeight.isEmpty()) {
				final int size = tempeight.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex indiaResult = new BubbleIndex();
					indiaResult.type = "India";
					indiaResult.symbol = tempeight.get(j).symbol;
					indiaResult.name = tempeight.get(j).name;
					indiaResults.add(indiaResult);
				}

				if (size == 1) {
					index = indiaResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempnine = mainRepository.findByDualKeywordsIgnoreCase("Brazil", splitted[i], splitted[i] + ".SA").subList(0, 4);

			if (!tempnine.isEmpty()) {
				final int size = tempnine.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex brazilResult = new BubbleIndex();
					brazilResult.type = "Brazil";
					brazilResult.symbol = tempnine.get(j).symbol;
					brazilResult.name = tempnine.get(j).name;
					brazilResults.add(brazilResult);
				}

				if (size == 1) {
					index = brazilResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempten = mainRepository.findByTriKeywordsIgnoreCase("China", splitted[i], splitted[i] + ".SZ",  splitted[i] + ".SS").subList(0, 4);

			if (!tempten.isEmpty()) {
				final int size = tempten.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex chinaResult = new BubbleIndex();
					chinaResult.type = "China";
					chinaResult.symbol = tempten.get(j).symbol;
					chinaResult.name = tempten.get(j).name;
					chinaResults.add(chinaResult);
				}

				if (size == 1) {
					index = chinaResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempeleven = mainRepository.findByKeywordsIgnoreCase("Japan", splitted[i]).subList(0, 4);

			if (!tempeleven.isEmpty()) {
				final int size = tempeleven.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex japanResult = new BubbleIndex();
					japanResult.type = "Japan";
					japanResult.symbol = tempeleven.get(j).symbol;
					japanResult.name = tempeleven.get(j).name;
					japanResults.add(japanResult);
				}

				if (size == 1) {
					index = japanResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwelve = mainRepository.findByDualKeywordsIgnoreCase("Argentina", splitted[i], splitted[i] + ".BA").subList(0, 4);

			if (!temptwelve.isEmpty()) {
				final int size = temptwelve.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex argentinaResult = new BubbleIndex();
					argentinaResult.type = "Argentina";
					argentinaResult.symbol = temptwelve.get(j).symbol;
					argentinaResult.name = temptwelve.get(j).name;
					argentinaResults.add(argentinaResult);
				}

				if (size == 1) {
					index = argentinaResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempthirteen = mainRepository.findByDualKeywordsIgnoreCase("SouthKorea", splitted[i], splitted[i] + ".KS").subList(0, 4);

			if (!tempthirteen.isEmpty()) {
				final int size = tempthirteen.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex southkoreaResult = new BubbleIndex();
					southkoreaResult.type = "SouthKorea";
					southkoreaResult.symbol = tempthirteen.get(j).symbol;
					southkoreaResult.name = tempthirteen.get(j).name;
					southkoreaResults.add(southkoreaResult);
				}

				if (size == 1) {
					index = southkoreaResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempfourteen = mainRepository.findByDualKeywordsIgnoreCase("Australia", splitted[i], splitted[i] + ".AX").subList(0, 4);

			if (!tempfourteen.isEmpty()) {
				final int size = tempfourteen.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex australiaResult = new BubbleIndex();
					australiaResult.type = "Australia";
					australiaResult.symbol = tempfourteen.get(j).symbol;
					australiaResult.name = tempfourteen.get(j).name;
					australiaResults.add(australiaResult);
				}

				if (size == 1) {
					index = australiaResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempfifteen = mainRepository.findByDualKeywordsIgnoreCase("Israel", splitted[i], splitted[i] + ".TA").subList(0, 4);

			if (!tempfifteen.isEmpty()) {
				final int size = tempfifteen.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex israelResult = new BubbleIndex();
					israelResult.type = "Israel";
					israelResult.symbol = tempfifteen.get(j).symbol;
					israelResult.name = tempfifteen.get(j).name;
					israelResults.add(israelResult);
				}

				if (size == 1) {
					index = israelResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempsixteen = mainRepository.findByDualKeywordsIgnoreCase("Singapore", splitted[i], splitted[i] + ".SI").subList(0, 4);

			if (!tempsixteen.isEmpty()) {
				final int size = tempsixteen.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex singaporeResult = new BubbleIndex();
					singaporeResult.type = "Singapore";
					singaporeResult.symbol = tempsixteen.get(j).symbol;
					singaporeResult.name = tempsixteen.get(j).name;
					singaporeResults.add(singaporeResult);
				}

				if (size == 1) {
					index = singaporeResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempseventeen = mainRepository.findByKeywordsIgnoreCase("CompositeFifty", splitted[i]).subList(0, 4);

			if (!tempseventeen.isEmpty()) {
				final int size = tempseventeen.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex compositefiftyResult = new BubbleIndex();
					compositefiftyResult.type = "CompositeFifty";
					compositefiftyResult.symbol = tempseventeen.get(j).symbol;
					compositefiftyResult.name = tempseventeen.get(j).name;
					compositefiftyResults.add(compositefiftyResult);
				}

				if (size == 1) {
					index = compositefiftyResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempeighteen = mainRepository.findByDualKeywordsIgnoreCase("Italy", splitted[i], splitted[i] + ".MI").subList(0, 4);

			if (!tempeighteen.isEmpty()) {
				final int size = tempeighteen.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex italyResult = new BubbleIndex();
					italyResult.type = "Italy";
					italyResult.symbol = tempeighteen.get(j).symbol;
					italyResult.name = tempeighteen.get(j).name;
					italyResults.add(italyResult);
				}

				if (size == 1) {
					index = italyResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempnineteen = mainRepository.findByDualKeywordsIgnoreCase("Mexico", splitted[i], splitted[i] + ".MX").subList(0, 4);

			if (!tempnineteen.isEmpty()) {
				final int size = tempnineteen.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex mexicoResult = new BubbleIndex();
					mexicoResult.type = "Mexico";
					mexicoResult.symbol = tempnineteen.get(j).symbol;
					mexicoResult.name = tempnineteen.get(j).name;
					mexicoResults.add(mexicoResult);
				}

				if (size == 1) {
					index = mexicoResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwenty = mainRepository.findByTriKeywordsIgnoreCase("Canada", splitted[i], splitted[i] + ".V", splitted[i] + ".TO").subList(0, 4);
			
			if (!temptwenty.isEmpty()) {
				final int size = temptwenty.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex canadaResult = new BubbleIndex();
					canadaResult.type = "Canada";
					canadaResult.symbol = temptwenty.get(j).symbol;
					canadaResult.name = temptwenty.get(j).name;
					canadaResults.add(canadaResult);
				}

				if (size == 1) {
					index = canadaResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwentyone = mainRepository.findByDualKeywordsIgnoreCase("France", splitted[i], splitted[i] + ".PA").subList(0, 4);

			if (!temptwentyone.isEmpty()) {
				final int size = temptwentyone.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex franceResult = new BubbleIndex();
					franceResult.type = "France";
					franceResult.symbol = temptwentyone.get(j).symbol;
					franceResult.name = temptwentyone.get(j).name;
					franceResults.add(franceResult);
				}

				if (size == 1) {
					index = franceResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwentytwo = mainRepository.findByDualKeywordsIgnoreCase("Indonesia", splitted[i], splitted[i] + ".JK").subList(0, 4);

			if (!temptwentytwo.isEmpty()) {
				final int size = temptwentytwo.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex indonesiaResult = new BubbleIndex();
					indonesiaResult.type = "Indonesia";
					indonesiaResult.symbol = temptwentytwo.get(j).symbol;
					indonesiaResult.name = temptwentytwo.get(j).name;
					indonesiaResults.add(indonesiaResult);
				}

				if (size == 1) {
					index = indonesiaResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwentythree = mainRepository.findByDualKeywordsIgnoreCase("Taiwan", splitted[i], splitted[i] + ".TW").subList(0, 4);

			if (!temptwentythree.isEmpty()) {
				final int size = temptwentythree.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex taiwanResult = new BubbleIndex();
					taiwanResult.type = "Taiwan";
					taiwanResult.symbol = temptwentythree.get(j).symbol;
					taiwanResult.name = temptwentythree.get(j).name;
					taiwanResults.add(taiwanResult);
				}

				if (size == 1) {
					index = taiwanResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwentyfour = mainRepository.findByKeywordsIgnoreCase("CompositeEighty", splitted[i]).subList(0, 4);

			if (!temptwentyfour.isEmpty()) {
				final int size = temptwentyfour.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex compositeeightyResult = new BubbleIndex();
					compositeeightyResult.type = "CompositeEighty";
					compositeeightyResult.symbol = temptwentyfour.get(j).symbol;
					compositeeightyResult.name = temptwentyfour.get(j).name;
					compositeeightyResults.add(compositeeightyResult);
				}

				if (size == 1) {
					index = compositeeightyResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwentyfive = mainRepository.findByKeywordsIgnoreCase("CompositeNinety", splitted[i]).subList(0, 4);

			if (!temptwentyfive.isEmpty()) {
				final int size = temptwentyfive.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex compositeninetyResult = new BubbleIndex();
					compositeninetyResult.type = "CompositeNinety";
					compositeninetyResult.symbol = temptwentyfive.get(j).symbol;
					compositeninetyResult.name = temptwentyfive.get(j).name;
					compositeninetyResults.add(compositeninetyResult);
				}

				if (size == 1) {
					index = compositeninetyResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwentysix = mainRepository.findByKeywordsIgnoreCase("CompositeNinetyFive", splitted[i]).subList(0, 4);

			if (!temptwentysix.isEmpty()) {
				final int size = temptwentysix.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex compositeninetyfiveResult = new BubbleIndex();
					compositeninetyfiveResult.type = "CompositeNinetyFive";
					compositeninetyfiveResult.symbol = temptwentysix.get(j).symbol;
					compositeninetyfiveResult.name = temptwentysix.get(j).name;
					compositeninetyfiveResults.add(compositeninetyfiveResult);
				}

				if (size == 1) {
					index = compositeninetyfiveResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwentyseven = mainRepository.findByKeywordsIgnoreCase("CompositeNinetyNine", splitted[i]).subList(0, 4);

			if (!temptwentyseven.isEmpty()) {
				final int size = temptwentyseven.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex compositeninetynineResult = new BubbleIndex();
					compositeninetynineResult.type = "CompositeNinetyNine";
					compositeninetynineResult.symbol = temptwentyseven.get(j).symbol;
					compositeninetynineResult.name = temptwentyseven.get(j).name;
					compositeninetynineResults.add(compositeninetynineResult);
				}

				if (size == 1) {
					index = compositeninetynineResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwentyeight = mainRepository.findByDualKeywordsIgnoreCase("Switzerland", splitted[i], splitted[i] + ".SW").subList(0, 4);

			if (!temptwentyeight.isEmpty()) {
				final int size = temptwentyeight.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex switzerlandResult = new BubbleIndex();
					switzerlandResult.type = "Switzerland";
					switzerlandResult.symbol = temptwentyeight.get(j).symbol;
					switzerlandResult.name = temptwentyeight.get(j).name;
					switzerlandResults.add(switzerlandResult);
				}

				if (size == 1) {
					index = switzerlandResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> temptwentynine = mainRepository.findByDualKeywordsIgnoreCase("Sweden", splitted[i], splitted[i] + ".ST").subList(0, 4);

			if (!temptwentynine.isEmpty()) {
				final int size = temptwentynine.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex swedenResult = new BubbleIndex();
					swedenResult.type = "Sweden";
					swedenResult.symbol = temptwentynine.get(j).symbol;
					swedenResult.name = temptwentynine.get(j).name;
					swedenResults.add(swedenResult);
				}

				if (size == 1) {
					index = swedenResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempthirty = mainRepository.findByDualKeywordsIgnoreCase("Austria", splitted[i], splitted[i] + ".VI").subList(0, 4);

			if (!tempthirty.isEmpty()) {
				final int size = tempthirty.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex austriaResult = new BubbleIndex();
					austriaResult.type = "Austria";
					austriaResult.symbol = tempthirty.get(j).symbol;
					austriaResult.name = tempthirty.get(j).name;
					austriaResults.add(austriaResult);
				}

				if (size == 1) {
					index = austriaResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempthirtyone = mainRepository.findByDualKeywordsIgnoreCase("Greece", splitted[i], splitted[i] + ".AT").subList(0, 4);

			if (!tempthirtyone.isEmpty()) {
				final int size = tempthirtyone.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex greeceResult = new BubbleIndex();
					greeceResult.type = "Greece";
					greeceResult.symbol = tempthirtyone.get(j).symbol;
					greeceResult.name = tempthirtyone.get(j).name;
					greeceResults.add(greeceResult);
				}

				if (size == 1) {
					index = greeceResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempthirtytwo = mainRepository.findByDualKeywordsIgnoreCase("Norway", splitted[i], splitted[i] + ".OL").subList(0, 4);

			if (!tempthirtytwo.isEmpty()) {
				final int size = tempthirtytwo.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex norwayResult = new BubbleIndex();
					norwayResult.type = "Norway";
					norwayResult.symbol = tempthirtytwo.get(j).symbol;
					norwayResult.name = tempthirtytwo.get(j).name;
					norwayResults.add(norwayResult);
				}

				if (size == 1) {
					index = norwayResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempthirtythree = mainRepository.findByDualKeywordsIgnoreCase("Denmark", splitted[i], splitted[i] + ".CO").subList(0, 4);

			if (!tempthirtythree.isEmpty()) {
				final int size = tempthirtythree.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex denmarkResult = new BubbleIndex();
					denmarkResult.type = "Denmark";
					denmarkResult.symbol = tempthirtythree.get(j).symbol;
					denmarkResult.name = tempthirtythree.get(j).name;
					denmarkResults.add(denmarkResult);
				}

				if (size == 1) {
					index = denmarkResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempthirtyfour = mainRepository.findByDualKeywordsIgnoreCase("NewZealand", splitted[i], splitted[i] + ".NZ").subList(0, 4);

			if (!tempthirtyfour.isEmpty()) {
				final int size = tempthirtyfour.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex newzealandResult = new BubbleIndex();
					newzealandResult.type = "NewZealand";
					newzealandResult.symbol = tempthirtyfour.get(j).symbol;
					newzealandResult.name = tempthirtyfour.get(j).name;
					newzealandResults.add(newzealandResult);
				}

				if (size == 1) {
					index = newzealandResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempthirtyfive = mainRepository.findByDualKeywordsIgnoreCase("Spain", splitted[i], splitted[i] + ".MC").subList(0, 4);

			if (!tempthirtyfive.isEmpty()) {
				final int size = tempthirtyfive.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex spainResult = new BubbleIndex();
					spainResult.type = "Spain";
					spainResult.symbol = tempthirtyfive.get(j).symbol;
					spainResult.name = tempthirtyfive.get(j).name;
					spainResults.add(spainResult);
				}

				if (size == 1) {
					index = spainResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempthirtysix = mainRepository.findByDualKeywordsIgnoreCase("Brazil", splitted[i], splitted[i] + ".AS").subList(0, 4);

			if (!tempthirtysix.isEmpty()) {
				final int size = tempthirtysix.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex netherlandsResult = new BubbleIndex();
					netherlandsResult.type = "Netherlands";
					netherlandsResult.symbol = tempthirtysix.get(j).symbol;
					netherlandsResult.name = tempthirtysix.get(j).name;
					netherlandsResults.add(netherlandsResult);
				}

				if (size == 1) {
					index = netherlandsResults.get(0);
				}

				totalFound = totalFound + size;
			}

			final List<BubbleIndexTimeseries> tempthirtyseven = mainRepository.findByDualKeywordsIgnoreCase("Russia", splitted[i], splitted[i] + ".ME").subList(0, 4);

			if (!tempthirtyseven.isEmpty()) {
				final int size = tempthirtyseven.size();
				for (int j = 0; j < size; j++) {
					final BubbleIndex russiaResult = new BubbleIndex();
					russiaResult.type = "Russia";
					russiaResult.symbol = tempthirtyseven.get(j).symbol;
					russiaResult.name = tempthirtyseven.get(j).name;
					russiaResults.add(russiaResult);
				}

				if (size == 1) {
					index = russiaResults.get(0);
				}

				totalFound = totalFound + size;
			}
		}

		if (totalFound == 0) {
			noResults();
		} else if (totalFound == 1) {
			index.findLocation();
			index.findPlotLocation();
			plot(index.type, index.name, index.symbol);
			// render("Application/plot.html", index);
		} else {
			model.addAttribute("stocksResults", stocksResults);
			model.addAttribute("indicesResults", indicesResults);
			model.addAttribute("currenciesResults", currenciesResults);
			model.addAttribute("commoditiesResults", commoditiesResults);
			model.addAttribute("germanyResults", germanyResults);
			model.addAttribute("hongkongResults", hongkongResults);
			model.addAttribute("unitedkingdomResults", unitedkingdomResults);
			model.addAttribute("indiaResults", indiaResults);
			model.addAttribute("brazilResults", brazilResults);
			model.addAttribute("chinaResults", chinaResults);
			model.addAttribute("japanResults", japanResults);
			model.addAttribute("argentinaResults", argentinaResults);
			model.addAttribute("southkoreaResults", southkoreaResults);
			model.addAttribute("australiaResults", australiaResults);
			model.addAttribute("israelResults", israelResults);
			model.addAttribute("singaporeResults", singaporeResults);
			model.addAttribute("compositefiftyResults", compositefiftyResults);
			model.addAttribute("italyResults", italyResults);
			model.addAttribute("mexicoResults", mexicoResults);
			model.addAttribute("canadaResults", canadaResults);
			model.addAttribute("franceResults", franceResults);
			model.addAttribute("indonesiaResults", indonesiaResults);
			model.addAttribute("taiwanResults", taiwanResults);
			model.addAttribute("compositeeightyResults", compositeeightyResults);
			model.addAttribute("compositeninetyResults", compositeninetyResults);
			model.addAttribute("compositeninetyfiveResults", compositeninetyfiveResults);
			model.addAttribute("compositeninetynineResults", compositeninetynineResults);
			model.addAttribute("switzerlandResults", switzerlandResults);
			model.addAttribute("swedenResults", swedenResults);
			model.addAttribute("austriaResults", austriaResults);
			model.addAttribute("denmarkResults", denmarkResults);
			model.addAttribute("greeceResults", greeceResults);
			model.addAttribute("norwayResults", norwayResults);
			model.addAttribute("newzealandResults", newzealandResults);
			model.addAttribute("spainResults", spainResults);
			model.addAttribute("netherlandsResults", netherlandsResults);
			model.addAttribute("russiaResults", russiaResults);
			model.addAttribute("totalFound", totalFound);

			return "results";
		}
		
		return noResults();
	}

	/*
	 * public static void plot(String type, String name, String symbol) {
	 * BubbleIndex index = new BubbleIndex(); index.type = type; index.symbol =
	 * symbol; index.name = name; index.findLocation();
	 * index.findPlotLocation(); render(index); }
	 * 
	 * public static void fullPlot(String type, String name, String symbol) {
	 * BubbleIndex index = new BubbleIndex(); index.type = type; index.symbol =
	 * symbol; index.name = name; index.findLocation();
	 * index.findPlotLocation(); render(index); }
	 */

	public String plot(final String type, final String name, final String symbol) {
		return "redirect:https://bigttrott-thebubbleindex.netdna-ssl.com/TheBubbleIndex/" + type + "/" + symbol + "/"
				+ "plot.html";
	}

	public String fullPlot(final String type, final String name, final String symbol) {
		return "redirect:https://bigttrott-thebubbleindex.netdna-ssl.com/TheBubbleIndex/" + type + "/" + symbol + "/"
				+ "fullPlot.html";
	}

	public String noResults() {
		return "noResults";
	}

}
