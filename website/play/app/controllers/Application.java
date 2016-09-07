package controllers;

import play.*;
import play.mvc.*;
import play.vfs.VirtualFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

import models.*;
import java.io.InputStream;
import java.net.SocketException;

public class Application extends Controller {

	public static void index() {
		render();
	}

	public static void getSiteMap() {
		String location = "/public/sitemap.xml";
		VirtualFile vf = VirtualFile.fromRelativePath(location);
		File realFile = vf.getRealFile();
		renderBinary(realFile);
	}

	public static void getFavicon() {
		String location = "/public/favicon.ico";
		VirtualFile vf = VirtualFile.fromRelativePath(location);
		File realFile = vf.getRealFile();
		renderBinary(realFile);
	}

	public static void verification() {
		String location = "/public/googlec5ff56e922eed948.html";
		VirtualFile vf = VirtualFile.fromRelativePath(location);
		File realFile = vf.getRealFile();
		renderBinary(realFile);
	}

	public static void getrobots() {
		String location = "/public/robots.txt";
		VirtualFile vf = VirtualFile.fromRelativePath(location);
		File realFile = vf.getRealFile();
		renderBinary(realFile);
	}

	public static void browse() {
		render();
	}

	public static void browselist(String Type) {

		String Types[] = { "Indices", "Commodities", "Currencies", "CompositeFifty", "Stocks", "Germany", "China",
				"Japan", "HongKong", "UnitedKingdom", "Australia", "Israel", "Singapore", "Mexico", "Italy", "India",
				"Brazil", "Argentina", "Canada", "France", "Indonesia", "Taiwan", "Switzerland", "Sweden", "Austria",
				"Greece", "Norway", "Denmark", "NewZealand", "CompositeEighty", "CompositeNinety",
				"CompositeNinetyFive", "CompositeNinetyNine", "SouthKorea", "Spain", "Netherlands", "Russia" };

		if (Type.equals(Types[0])) {
			List<Indices> list = Indices.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[1])) {
			List<Commodities> list = Commodities.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[2])) {
			List<Currencies> list = Currencies.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[3])) {
			List<CompositeFifty> list = CompositeFifty.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[4])) {
			List<Stocks> list = Stocks.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[5])) {
			List<Germany> list = Germany.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[6])) {
			List<China> list = China.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[7])) {
			List<Japan> list = Japan.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[8])) {
			List<HongKong> list = HongKong.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[9])) {
			List<UnitedKingdom> list = UnitedKingdom.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[10])) {
			List<Australia> list = Australia.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[11])) {
			List<Israel> list = Israel.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[12])) {
			List<Singapore> list = Singapore.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[13])) {
			List<Mexico> list = Mexico.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[14])) {
			List<Italy> list = Italy.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[15])) {
			List<India> list = India.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[16])) {
			List<Brazil> list = Brazil.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[17])) {
			List<Argentina> list = Argentina.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[18])) {
			List<Canada> list = Canada.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[19])) {
			List<France> list = France.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[20])) {
			List<Indonesia> list = Indonesia.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[21])) {
			List<Taiwan> list = Taiwan.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[22])) {
			List<Switzerland> list = Switzerland.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[23])) {
			List<Sweden> list = Sweden.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[24])) {
			List<Austria> list = Austria.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[25])) {
			List<Greece> list = Greece.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[26])) {
			List<Norway> list = Norway.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[27])) {
			List<Denmark> list = Denmark.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[28])) {
			List<NewZealand> list = NewZealand.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[29])) {
			List<CompositeEighty> list = CompositeEighty.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[30])) {
			List<CompositeNinety> list = CompositeNinety.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[31])) {
			List<CompositeNinetyFive> list = CompositeNinetyFive.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[32])) {
			List<CompositeNinetyNine> list = CompositeNinetyNine.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[33])) {
			List<SouthKorea> list = SouthKorea.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[34])) {
			List<Spain> list = Spain.findAll();
			render(Type, list);
		}

		else if (Type.equals(Types[35])) {
			List<Netherlands> list = Netherlands.findAll();
			render(Type, list);
		}

		else {
			List<Russia> list = Russia.findAll();
			render(Type, list);
		}

	}

	public static void browseEurope() {
		render();
	}

	public static void browseMoreEurope() {
		render();
	}

	public static void browseAmericas() {
		render();
	}

	public static void browseAsia() {
		render();
	}

	public static void browsePacific() {
		render();
	}

	public static void browseComposite() {
		render();
	}

	public static void searchResults(String search) {

		List<BubbleIndex> stocksResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> indicesResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> currenciesResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> commoditiesResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> germanyResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> hongkongResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> unitedkingdomResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> indiaResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> brazilResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> chinaResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> japanResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> argentinaResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> southkoreaResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> australiaResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> israelResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> singaporeResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> italyResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> mexicoResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> compositefiftyResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> canadaResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> indonesiaResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> franceResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> taiwanResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> compositeeightyResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> compositeninetyResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> compositeninetyfiveResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> compositeninetynineResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> switzerlandResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> swedenResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> austriaResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> greeceResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> norwayResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> denmarkResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> newzealandResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> spainResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> netherlandsResults = new ArrayList<BubbleIndex>();
		List<BubbleIndex> russiaResults = new ArrayList<BubbleIndex>();

		String[] splitted = search.split("\\s+", 4);

		BubbleIndex index = new BubbleIndex();
		int totalFound = 0;

		for (int i = 0; i < splitted.length; i++) {
			List<Stocks> temp = Stocks.find("symbol like ?", splitted[i].toUpperCase()).fetch(4);

			if (!temp.isEmpty()) {
				int size = temp.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex stockResult = new BubbleIndex();
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

			List<Indices> temptwo = Indices.find("keywords like ?", "%" + splitted[i].toUpperCase() + "%").fetch(12);

			if (!temptwo.isEmpty()) {
				int size = temptwo.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex indexResult = new BubbleIndex();
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

			List<Currencies> tempthree = Currencies.find("keywords like ?", "%" + splitted[i].toUpperCase() + "%")
					.fetch(12);

			if (!tempthree.isEmpty()) {
				int size = tempthree.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex currencyResult = new BubbleIndex();
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

			List<Commodities> tempfour = Commodities.find("keywords like ?", "%" + splitted[i].toUpperCase() + "%")
					.fetch(12);

			if (!tempfour.isEmpty()) {
				int size = tempfour.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex commodityResult = new BubbleIndex();
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

			List<Germany> tempfive = Germany.find("keywords like ? or keywords like ?", splitted[i].toUpperCase(),
					splitted[i].toUpperCase() + ".F").fetch(4);

			if (!tempfive.isEmpty()) {
				int size = tempfive.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex germanyResult = new BubbleIndex();
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

			List<HongKong> tempsix = HongKong.find("keywords like ? or keywords like ?", splitted[i].toUpperCase(),
					splitted[i].toUpperCase() + ".HK").fetch(4);

			if (!tempsix.isEmpty()) {
				int size = tempsix.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex hongkongResult = new BubbleIndex();
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

			List<UnitedKingdom> tempseven = UnitedKingdom.find("keywords like ? or keywords like ?",
					splitted[i].toUpperCase(), splitted[i].toUpperCase() + ".L").fetch(4);

			if (!tempseven.isEmpty()) {
				int size = tempseven.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex unitedkingdomResult = new BubbleIndex();
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

			List<India> tempeight = India.find("keywords like ? or keywords like ?", splitted[i].toUpperCase(),
					splitted[i].toUpperCase() + ".BO").fetch(4);

			if (!tempeight.isEmpty()) {
				int size = tempeight.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex indiaResult = new BubbleIndex();
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

			List<Brazil> tempnine = Brazil.find("keywords like ? or keywords like ?", splitted[i].toUpperCase(),
					splitted[i].toUpperCase() + ".SA").fetch(4);

			if (!tempnine.isEmpty()) {
				int size = tempnine.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex brazilResult = new BubbleIndex();
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

			List<China> tempten = China.find("keywords like ? or keywords like ? or keywords like ?",
					splitted[i].toUpperCase(), splitted[i].toUpperCase() + ".SS", splitted[i].toUpperCase() + ".SZ")
					.fetch(4);

			if (!tempten.isEmpty()) {
				int size = tempten.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex chinaResult = new BubbleIndex();
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

			List<Japan> tempeleven = Japan.find("keywords like ?", splitted[i].toUpperCase()).fetch(4);

			if (!tempeleven.isEmpty()) {
				int size = tempeleven.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex japanResult = new BubbleIndex();
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

			List<Argentina> temptwelve = Argentina.find("keywords like ? or keywords like ?", splitted[i].toUpperCase(),
					splitted[i].toUpperCase() + ".BA").fetch(4);

			if (!temptwelve.isEmpty()) {
				int size = temptwelve.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex argentinaResult = new BubbleIndex();
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

			List<SouthKorea> tempthirteen = SouthKorea.find("keywords like ? or keywords like ?",
					splitted[i].toUpperCase(), splitted[i].toUpperCase() + ".KS").fetch(4);

			if (!tempthirteen.isEmpty()) {
				int size = tempthirteen.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex southkoreaResult = new BubbleIndex();
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

			List<Australia> tempfourteen = Australia.find("keywords like ? or keywords like ?",
					splitted[i].toUpperCase(), splitted[i].toUpperCase() + ".AX").fetch(4);

			if (!tempfourteen.isEmpty()) {
				int size = tempfourteen.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex australiaResult = new BubbleIndex();
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

			List<Israel> tempfifteen = Israel.find("keywords like ? or keywords like ?", splitted[i].toUpperCase(),
					splitted[i].toUpperCase() + ".TA").fetch(4);

			if (!tempfifteen.isEmpty()) {
				int size = tempfifteen.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex israelResult = new BubbleIndex();
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

			List<Singapore> tempsixteen = Singapore.find("keywords like ? or keywords like ?",
					splitted[i].toUpperCase(), splitted[i].toUpperCase() + ".SI").fetch(4);

			if (!tempsixteen.isEmpty()) {
				int size = tempsixteen.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex singaporeResult = new BubbleIndex();
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

			List<CompositeFifty> tempseventeen = CompositeFifty
					.find("keywords like ?", "%" + splitted[i].toUpperCase() + "%").fetch(4);

			if (!tempseventeen.isEmpty()) {
				int size = tempseventeen.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex compositefiftyResult = new BubbleIndex();
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

			List<Italy> tempeighteen = Italy.find("keywords like ? or keywords like ?", splitted[i].toUpperCase(),
					splitted[i].toUpperCase() + ".MI").fetch(4);

			if (!tempeighteen.isEmpty()) {
				int size = tempeighteen.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex italyResult = new BubbleIndex();
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

			List<Mexico> tempnineteen = Mexico.find("keywords like ? or keywords like ?", splitted[i].toUpperCase(),
					splitted[i].toUpperCase() + ".MX").fetch(4);

			if (!tempnineteen.isEmpty()) {
				int size = tempnineteen.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex mexicoResult = new BubbleIndex();
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

			List<Canada> temptwenty = Canada.find("keywords like ? or keywords like ? or keywords like ?",
					splitted[i].toUpperCase(), splitted[i].toUpperCase() + ".TO", splitted[i].toUpperCase() + ".V")
					.fetch(4);

			if (!temptwenty.isEmpty()) {
				int size = temptwenty.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex canadaResult = new BubbleIndex();
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

			List<France> temptwentyone = France.find("keywords like ? or keywords like ?", splitted[i].toUpperCase(),
					splitted[i].toUpperCase() + ".PA").fetch(4);

			if (!temptwentyone.isEmpty()) {
				int size = temptwentyone.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex franceResult = new BubbleIndex();
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

			List<Indonesia> temptwentytwo = Indonesia.find("keywords like ? or keywords like ?",
					splitted[i].toUpperCase(), splitted[i].toUpperCase() + ".JK").fetch(4);

			if (!temptwentytwo.isEmpty()) {
				int size = temptwentytwo.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex indonesiaResult = new BubbleIndex();
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

			List<Taiwan> temptwentythree = Taiwan.find("keywords like ? or keywords like ?", splitted[i].toUpperCase(),
					splitted[i].toUpperCase() + ".TW").fetch(4);

			if (!temptwentythree.isEmpty()) {
				int size = temptwentythree.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex taiwanResult = new BubbleIndex();
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

			List<CompositeEighty> temptwentyfour = CompositeEighty
					.find("keywords like ?", "%" + splitted[i].toUpperCase() + "%").fetch(4);

			if (!temptwentyfour.isEmpty()) {
				int size = temptwentyfour.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex compositeeightyResult = new BubbleIndex();
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

			List<CompositeNinety> temptwentyfive = CompositeNinety
					.find("keywords like ?", "%" + splitted[i].toUpperCase() + "%").fetch(4);

			if (!temptwentyfive.isEmpty()) {
				int size = temptwentyfive.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex compositeninetyResult = new BubbleIndex();
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

			List<CompositeNinetyFive> temptwentysix = CompositeNinetyFive
					.find("keywords like ?", "%" + splitted[i].toUpperCase() + "%").fetch(4);

			if (!temptwentysix.isEmpty()) {
				int size = temptwentysix.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex compositeninetyfiveResult = new BubbleIndex();
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

			List<CompositeNinetyNine> temptwentyseven = CompositeNinetyNine
					.find("keywords like ?", "%" + splitted[i].toUpperCase() + "%").fetch(4);

			if (!temptwentyseven.isEmpty()) {
				int size = temptwentyseven.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex compositeninetynineResult = new BubbleIndex();
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

			List<Switzerland> temptwentyeight = Switzerland.find("keywords like ? or keywords like ?",
					splitted[i].toUpperCase(), splitted[i].toUpperCase() + ".SW").fetch(4);

			if (!temptwentyeight.isEmpty()) {
				int size = temptwentyeight.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex switzerlandResult = new BubbleIndex();
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

			List<Sweden> temptwentynine = Sweden.find("keywords like ? or keywords like ?", splitted[i].toUpperCase(),
					splitted[i].toUpperCase() + ".ST").fetch(4);

			if (!temptwentynine.isEmpty()) {
				int size = temptwentynine.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex swedenResult = new BubbleIndex();
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

			List<Austria> tempthirty = Austria.find("keywords like ? or keywords like ?", splitted[i].toUpperCase(),
					splitted[i].toUpperCase() + ".VI").fetch(4);

			if (!tempthirty.isEmpty()) {
				int size = tempthirty.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex austriaResult = new BubbleIndex();
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

			List<Greece> tempthirtyone = Greece.find("keywords like ? or keywords like ?", splitted[i].toUpperCase(),
					splitted[i].toUpperCase() + ".AT").fetch(4);

			if (!tempthirtyone.isEmpty()) {
				int size = tempthirtyone.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex greeceResult = new BubbleIndex();
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

			List<Norway> tempthirtytwo = Norway.find("keywords like ? or keywords like ?", splitted[i].toUpperCase(),
					splitted[i].toUpperCase() + ".OL").fetch(4);

			if (!tempthirtytwo.isEmpty()) {
				int size = tempthirtytwo.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex norwayResult = new BubbleIndex();
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

			List<Denmark> tempthirtythree = Denmark.find("keywords like ? or keywords like ?",
					splitted[i].toUpperCase(), splitted[i].toUpperCase() + ".CO").fetch(4);

			if (!tempthirtythree.isEmpty()) {
				int size = tempthirtythree.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex denmarkResult = new BubbleIndex();
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

			List<NewZealand> tempthirtyfour = NewZealand.find("keywords like ? or keywords like ?",
					splitted[i].toUpperCase(), splitted[i].toUpperCase() + ".NZ").fetch(4);

			if (!tempthirtyfour.isEmpty()) {
				int size = tempthirtyfour.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex newzealandResult = new BubbleIndex();
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

			List<Spain> tempthirtyfive = Spain.find("keywords like ? or keywords like ?", splitted[i].toUpperCase(),
					splitted[i].toUpperCase() + ".MC").fetch(4);

			if (!tempthirtyfive.isEmpty()) {
				int size = tempthirtyfive.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex spainResult = new BubbleIndex();
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

			List<Netherlands> tempthirtysix = Netherlands.find("keywords like ? or keywords like ?",
					splitted[i].toUpperCase(), splitted[i].toUpperCase() + ".AS").fetch(4);

			if (!tempthirtysix.isEmpty()) {
				int size = tempthirtysix.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex netherlandsResult = new BubbleIndex();
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

			List<Russia> tempthirtyseven = Russia.find("keywords like ? or keywords like ?", splitted[i].toUpperCase(),
					splitted[i].toUpperCase() + ".ME").fetch(4);

			if (!tempthirtyseven.isEmpty()) {
				int size = tempthirtyseven.size();
				for (int j = 0; j < size; j++) {
					BubbleIndex russiaResult = new BubbleIndex();
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
		}

		else if (totalFound == 1) {
			index.findLocation();
			index.findPlotLocation();

			render("Application/plot.html", index);
		}

		else {
			render(stocksResults, indicesResults, currenciesResults, commoditiesResults, germanyResults,
					hongkongResults, unitedkingdomResults, indiaResults, brazilResults, chinaResults, japanResults,
					argentinaResults, southkoreaResults, australiaResults, israelResults, singaporeResults,
					compositefiftyResults, italyResults, mexicoResults, canadaResults, franceResults, indonesiaResults,
					taiwanResults, compositeeightyResults, compositeninetyResults, compositeninetyfiveResults,
					compositeninetynineResults, switzerlandResults, swedenResults, austriaResults, greeceResults,
					denmarkResults, norwayResults, newzealandResults, spainResults, netherlandsResults, russiaResults,
					totalFound);
		}
	}

	public static void plot(String type, String name, String symbol) {
		BubbleIndex index = new BubbleIndex();
		index.type = type;
		index.symbol = symbol;
		index.name = name;
		index.findLocation();
		index.findPlotLocation();
		render(index);
	}

	public static void fullPlot(String type, String name, String symbol) {
		BubbleIndex index = new BubbleIndex();
		index.type = type;
		index.symbol = symbol;
		index.name = name;
		index.findLocation();
		index.findPlotLocation();
		render(index);
	}

	public static void noResults() {
		render();
	}

}
