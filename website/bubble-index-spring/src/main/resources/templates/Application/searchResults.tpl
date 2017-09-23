<!doctype html>
<html class="" lang="en">

	<head>

		<meta charset="utf-8">
		<title>The Bubble Index - Search Results</title>

#{header /}
			<div class="main-menu-wrapper">
				<div class="zone-main-menu zone clearfix">

					<div class="main-menu-container container-24">

						<div class="main-menu block">
							<ul id="sf-menu">
								<li class="empty">
									<div></div>
								</li>
								<li class="first">
									<a href="@{Application.index()}">HOME</a>
									<!--<ul>
										<li class="first">
											<a href="#">Home (Street View)</a>
										</li>
										<li class="">
											<a href="#">Home (Slideshow)</a>
										</li>
										<li class="">
											<a href="#">Home (Header 2)</a>
										</li>
										<li class="">
											<a href="#">Home (Header 3)</a>
										</li>
										<li class="">
											<a href="#">Home (Header 4)</a>
										</li>
										<li class="">
											<a href="#">Home (Header 5)</a>
										</li>
										<li class="">
											<a href="#">Home (Header 6)</a>
										</li>
										<li class="last">
											<a href="#">Home (Header 7)</a>
										</li>
									</ul>-->
								</li>
								<li class="">
									<a href="@{Pages.method()}">ABOUT</a>
								</li>
								<li class="">
									<a href="@{Pages.examples()}">EXAMPLES</a>
								</li>
								<li class="">
									<a href="@{Pages.links()}">LINKS</a>
								</li>

								<li class="">
									<a href="@{Pages.contact()}">CONTACT</a>
								</li>
								<li class="neighbour-left">
									<a href="@{Application.browse()}">BROWSE</a>
								</li>
								<li class="last active">
									<a href="@{Pages.search()}">SEARCH</a>
								</li>
								<li class="empty">
									<div></div>
								</li>
							</ul>
						</div>

					</div>

				</div><!-- end of .zone-main-menu -->
			</div><!-- end of .main-menu-wrapper -->

		</header>

<section class="section content">

			
			<div class="content-wrapper">
				<div class="zone-content equalize zone clearfix">

					<div class="content-container container-24">

						<div class="companies-listings block">
							<div class="block-title">
								<h1>Search Results</h1>
							</div>

							<div class="project-header block" align="center">
								<p>Found ${totalFound} Results</p>
							</div>
							<!--<div class="pagination">
								<div class="pagination-buttons">
									<a href="#" class="current-page">1</a>
									<a href="#">2</a>
									<a href="#">3</a>
								</div>
							</div>-->

<div class="company-listing clearfix">
								<a href="#" class="listing-image">
									<img src="@{'/public/images/content/markets.png'}" alt="" />
								</a>
								<div class="listing-body">
									<div class="listing-text">
				
				#{list items:indicesResults, as:'indicesresult'} 
					<a href="/application/plot?type=Indices&name=${indicesresult.name}&symbol=${indicesresult.symbol}" class="listing-read-more">${indicesresult.name}</a><br>
				#{/list}
				
</div>
								</div>
							</div>

<div class="company-listing clearfix">
								<a href="#" class="listing-image">
									<img src="@{'/public/images/content/commodities.png'}" alt="" />
								</a>
								<div class="listing-body">
									<div class="listing-text">
				
				#{list items:commoditiesResults, as:'commoditiesresult'} 
					<a href="/application/plot?type=Commodities&name=${commoditiesresult.name}&symbol=${commoditiesresult.symbol}" class="listing-read-more">${commoditiesresult.name}</a><br>
				#{/list}
				

</div>

								</div>
							</div>
							<div class="company-listing clearfix">
								<a href="#" class="listing-image">
									<img src="@{'/public/images/content/currencies.png'}" alt="" />
								</a>
								<div class="listing-body">
									<div class="listing-text">
				
				#{list items:currenciesResults, as:'currenciesresult'} 
					<a href="/application/plot?type=Currencies&name=${currenciesresult.name}&symbol=${currenciesresult.symbol}" class="listing-read-more">${currenciesresult.name}</a><br>
				#{/list}
				
</div>
								</div>
							</div>

							
							

<div class="company-listing clearfix">
								<a href="#" class="listing-image">
									<img src="@{'/public/images/content/composite.png'}" alt="" />
								</a>
								<div class="listing-body">
									<div class="listing-text">
				
				#{list items:compositefiftyResults, as:'compositefiftyresult'} 
					<a href="/application/plot?type=CompositeFifty&name=${compositefiftyresult.name}&symbol=${compositefiftyresult.symbol}" class="listing-read-more">${compositefiftyresult.name}</a><br>
				#{/list}

				#{list items:compositeeightyResults, as:'compositeeightyresult'} 
					<a href="/application/plot?type=CompositeEighty&name=${compositeeightyresult.name}&symbol=${compositeeightyresult.symbol}" class="listing-read-more">${compositeeightyresult.name}</a><br>
				#{/list}

				#{list items:compositeninetyResults, as:'compositeninetyresult'} 
					<a href="/application/plot?type=CompositeNinety&name=${compositeninetyresult.name}&symbol=${compositeninetyresult.symbol}" class="listing-read-more">${compositeninetyresult.name}</a><br>
				#{/list}

				#{list items:compositeninetyfiveResults, as:'compositeninetyfiveresult'} 
					<a href="/application/plot?type=CompositeNinetyFive&name=${compositeninetyfiveresult.name}&symbol=${compositeninetyfiveresult.symbol}" class="listing-read-more">${compositeninetyfiveresult.name}</a><br>
				#{/list}

				#{list items:compositeninetynineResults, as:'compositeninetynineresult'} 
					<a href="/application/plot?type=CompositeNinetyNine&name=${compositeninetynineresult.name}&symbol=${compositeninetynineresult.symbol}" class="listing-read-more">${compositeninetynineresult.name}</a><br>
				#{/list}
				
</div>
								</div>
							</div>



							<div class="company-listing clearfix">
								<a href="#" class="listing-image">
									<img src="@{'/public/images/content/americas.png'}" alt="" />
								</a>

								<div class="listing-body">
									<div class="listing-text">
				
				#{list items:stocksResults, as:'stocksresult'} 
					<a href="/application/plot?type=Stocks&name=${stocksresult.name}&symbol=${stocksresult.symbol}" class="listing-read-more">${stocksresult.name}</a><br>
				#{/list}

				#{list items:canadaResults, as:'canadaresult'} 
					<a href="/application/plot?type=Canada&name=${canadaresult.name}&symbol=${canadaresult.symbol}" class="listing-read-more">${canadaresult.name}</a><br>
				#{/list}

				#{list items:brazilResults, as:'brazilresult'} 
					<a href="/application/plot?type=Brazil&name=${brazilresult.name}&symbol=${brazilresult.symbol}" class="listing-read-more">${brazilresult.name}</a><br>
				#{/list}

				#{list items:argentinaResults, as:'argentinaresult'} 
					<a href="/application/plot?type=Argentina&name=${argentinaresult.name}&symbol=${argentinaresult.symbol}" class="listing-read-more">${argentinaresult.name}</a><br>
				#{/list}
				
				#{list items:mexicoResults, as:'mexicoresult'} 
					<a href="/application/plot?type=Mexico&name=${mexicoresult.name}&symbol=${mexicoresult.symbol}" class="listing-read-more">${mexicoresult.name}</a><br>
				#{/list}

</div>
								</div>
							</div>

<div class="company-listing clearfix">
								<a href="#" class="listing-image">
									<img src="@{'/public/images/content/asia.png'}" alt="" />
								</a>
								<div class="listing-body">
									<div class="listing-text">
				
				#{list items:hongkongResults, as:'hongkongresult'} 
					<a href="/application/plot?type=HongKong&name=${hongkongresult.name}&symbol=${hongkongresult.symbol}" class="listing-read-more">${hongkongresult.name}</a><br>
				#{/list}

				#{list items:russiaResults, as:'russiaresult'} 
					<a href="/application/plot?type=Russia&name=${russiaresult.name}&symbol=${russiaresult.symbol}" class="listing-read-more">${russiaresult.name}</a><br>
				#{/list}
				
				#{list items:indiaResults, as:'indiaresult'} 
					<a href="/application/plot?type=India&name=${indiaresult.name}&symbol=${indiaresult.symbol}" class="listing-read-more">${indiaresult.name}</a><br>
				#{/list}

				#{list items:chinaResults, as:'chinaresult'} 
					<a href="/application/plot?type=China&name=${chinaresult.name}&symbol=${chinaresult.symbol}" class="listing-read-more">${chinaresult.name}</a><br>
				#{/list}

				#{list items:southkoreaResults, as:'southkorearesult'} 
					<a href="/application/plot?type=SouthKorea&name=${southkorearesult.name}&symbol=${southkorearesult.symbol}" class="listing-read-more">${southkorearesult.name}</a><br>
				#{/list}

				#{list items:singaporeResults, as:'singaporeresult'} 
					<a href="/application/plot?type=Singapore&name=${singaporeresult.name}&symbol=${singaporeresult.symbol}" class="listing-read-more">${singaporeresult.name}</a><br>
				#{/list}

				#{list items:israelResults, as:'israelresult'} 
					<a href="/application/plot?type=Israel&name=${israelresult.name}&symbol=${israelresult.symbol}" class="listing-read-more">${israelresult.name}</a><br>
				#{/list}

</div>
								</div>
							</div>

<div class="company-listing clearfix">
								<a href="#" class="listing-image">
									<img src="@{'/public/images/content/europe.png'}" alt="" />
								</a>
								<div class="listing-body">
									<div class="listing-text">
				
				#{list items:unitedkingdomResults, as:'unitedkingdomresult'} 
					<a href="/application/plot?type=UnitedKingdom&name=${unitedkingdomresult.name}&symbol=${unitedkingdomresult.symbol}" class="listing-read-more">${unitedkingdomresult.name}</a><br>
				#{/list}

				#{list items:franceResults, as:'franceresult'} 
					<a href="/application/plot?type=France&name=${franceresult.name}&symbol=${franceresult.symbol}" class="listing-read-more">${franceresult.name}</a><br>
				#{/list}

				#{list items:germanyResults, as:'germanyresult'} 
					<a href="/application/plot?type=Germany&name=${germanyresult.name}&symbol=${germanyresult.symbol}" class="listing-read-more">${germanyresult.name}</a><br>
				#{/list}

				#{list items:italyResults, as:'italyresult'} 
					<a href="/application/plot?type=Italy&name=${italyresult.name}&symbol=${italyresult.symbol}" class="listing-read-more">${italyresult.name}</a><br>
				#{/list}

				#{list items:switzerlandResults, as:'switzerlandresult'} 
					<a href="/application/plot?type=Switzerland&name=${switzerlandresult.name}&symbol=${switzerlandresult.symbol}" class="listing-read-more">${switzerlandresult.name}</a><br>
				#{/list}

				#{list items:swedenResults, as:'swedenresult'} 
					<a href="/application/plot?type=Sweden&name=${swedenresult.name}&symbol=${swedenresult.symbol}" class="listing-read-more">${swedenresult.name}</a><br>
				#{/list}


				#{list items:austriaResults, as:'austriaresult'} 
					<a href="/application/plot?type=Austria&name=${austriaresult.name}&symbol=${austriaresult.symbol}" class="listing-read-more">${austriaresult.name}</a><br>
				#{/list}
				

				#{list items:greeceResults, as:'greeceresult'} 
					<a href="/application/plot?type=Greece&name=${greeceresult.name}&symbol=${greeceresult.symbol}" class="listing-read-more">${greeceresult.name}</a><br>
				#{/list}				


				#{list items:norwayResults, as:'norwayresult'} 
					<a href="/application/plot?type=Norway&name=${norwayresult.name}&symbol=${norwayresult.symbol}" class="listing-read-more">${norwayresult.name}</a><br>
				#{/list}	


				#{list items:denmarkResults, as:'denmarkresult'} 
					<a href="/application/plot?type=Denmark&name=${denmarkresult.name}&symbol=${denmarkresult.symbol}" class="listing-read-more">${denmarkresult.name}</a><br>
				#{/list}

				#{list items:spainResults, as:'spainresult'} 
					<a href="/application/plot?type=Spain&name=${spainresult.name}&symbol=${spainresult.symbol}" class="listing-read-more">${spainresult.name}</a><br>
				#{/list}

				#{list items:netherlandsResults, as:'netherlandsresult'} 
					<a href="/application/plot?type=Netherlands&name=${netherlandsresult.name}&symbol=${netherlandsresult.symbol}" class="listing-read-more">${netherlandsresult.name}</a><br>
				#{/list}
</div>
								</div>
							</div>

<div class="company-listing clearfix">
								<a href="#" class="listing-image">
									<img src="@{'/public/images/content/pacific.png'}" alt="" />
								</a>
								<div class="listing-body">
									<div class="listing-text">
				
				#{list items:japanResults, as:'japanresult'} 
					<a href="/application/plot?type=Japan&name=${japanresult.name}&symbol=${japanresult.symbol}" class="listing-read-more">${japanresult.name}</a><br>
				#{/list}
				
				#{list items:southkoreaResults, as:'southkorearesult'} 
					<a href="/application/plot?type=SouthKorea&name=${southkorearesult.name}&symbol=${southkorearesult.symbol}" class="listing-read-more">${southkorearesult.name}</a><br>
				#{/list}

				#{list items:indonesiaResults, as:'indonesiaresult'} 
					<a href="/application/plot?type=Indonesia&name=${indonesiaresult.name}&symbol=${indonesiaresult.symbol}" class="listing-read-more">${indonesiaresult.name}</a><br>
				#{/list}

				#{list items:taiwanResults, as:'taiwanresult'} 
					<a href="/application/plot?type=Taiwan&name=${taiwanresult.name}&symbol=${taiwanresult.symbol}" class="listing-read-more">${taiwanresult.name}</a><br>
				#{/list}

				#{list items:australiaResults, as:'australiaresult'} 
					<a href="/application/plot?type=Australia&name=${australiaresult.name}&symbol=${australiaresult.symbol}" class="listing-read-more">${australiaresult.name}</a><br>
				#{/list}

				#{list items:singaporeResults, as:'singaporeresult'} 
					<a href="/application/plot?type=Singapore&name=${singaporeresult.name}&symbol=${singaporeresult.symbol}" class="listing-read-more">${singaporeresult.name}</a><br>
				#{/list}

				#{list items:newzealandResults, as:'newzealandresult'} 
					<a href="/application/plot?type=NewZealand&name=${newzealandresult.name}&symbol=${newzealandresult.symbol}" class="listing-read-more">${newzealandresult.name}</a><br>
				#{/list}

</div>
								</div>
							</div>
						</div>

					</div><!-- end of .content-container -->
			</div><!-- end of .zone-content -->
			</div><!-- end of .content-wrapper -->

						<div class="interlayer"></div>

		</section>

#{footer /}

#{scripts /}

	</body>

</html>
