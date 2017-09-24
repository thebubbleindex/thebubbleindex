yieldUnescaped '<!doctype html>'
html(class: '', lang: 'en') {
	link(rel: 'stylesheet', href: '/css/allCss.css')
	head {
		title("The Bubble Index - Search Results")
		script(type: 'text/javascript', src: 'https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js', integrity: 'sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=', crossorigin: 'anonymous') {}
		script(type: 'text/javascript', src: '/scripts/browselistScript.js') {}
		div(class: 'header section') {
			div(class: 'header-wrapper') {
				div(class: 'zone-header zone clearfix') {
					div(class: 'header-lef container-24') {
						div(class: 'logo block') {
							a(href: '/index') {
								img(src: '/images/logo.png', height: '80', width: '680')
							}
						}
					}
				}
			}
			div(class: 'main-menu-wrapper') {
				div(class: 'zone-main-menu zone clearfix') {
					div(class: 'main-menu-container container-24') {
						div(class: 'main-menu block') {
							ul(id: 'sf-menu') {
								li(class: 'empty') {
									div() {}
								}
								li(class: 'first') {
									a(href: '/index', 'HOME')
								}
								li(class: '') {
									a(href: '/pages/method', 'ABOUT')
								}
								li(class: '') {
									a(href: '/pages/examples', 'EXAMPLES')
								}
								li(class: '') {
									a(href: '/pages/links', 'LINKS')
								}							
								li(class: '') {
									a(href: '/pages/contact', 'CONTACT')
								}
								li(class: 'neighbour-left') {
									a(href: '/browse', 'BROWSE')
								}
								li(class: 'last active') {
									a(href: '/pages/search', 'SEARCH')
								}
								li(class: 'empty') {
									div() {}
								}
							}
						}
					}
				}
			}
		}
	}
	body {
		section(class: 'section content') {
			div(class: 'content-wrapper') {
				div(class: 'zone-content equalize zone clearfix') {
					div(class: 'content-container container-24') {
						div(class: 'companies-listings block') {
							div(class: 'block-title') {
								h1() {
									yield 'Search Results'
								}
							}
							div(class: 'project-header block', align: 'center') {
								p("Found ${totalFound} results.")
							}
							div(class: 'company-listing clearfix') {
								a(href: '#', class: 'listing-image') {
									img(src: '/images/content/markets.png')
								}
								div(class: 'listing-body') {
									div(class: 'listing-text') {
										ul() {
											indicesResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
									}
								}
							}
							div(class: 'company-listing clearfix') {
								a(href: '#', class: 'listing-image') {
									img(src: '/images/content/commodities.png')
								}
								div(class: 'listing-body') {
									div(class: 'listing-text') {
										ul() {
											commoditiesResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
									}
								}
							}
							div(class: 'company-listing clearfix') {
								a(href: '#', class: 'listing-image') {
									img(src: '/images/content/currencies.png')
								}
								div(class: 'listing-body') {
									div(class: 'listing-text') {
										ul() {
											currenciesResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
									}
								}
							}
							div(class: 'company-listing clearfix') {
								a(href: '#', class: 'listing-image') {
									img(src: '/images/content/composite.png')
								}
								div(class: 'listing-body') {
									div(class: 'listing-text') {
										ul() {
											compositefiftyResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											compositeeightyResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											compositeninetyResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											compositeninetyfiveResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											compositeninetynineResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
									}
								}
							}
							div(class: 'company-listing clearfix') {
								a(href: '#', class: 'listing-image') {
									img(src: '/images/content/americas.png')
								}
								div(class: 'listing-body') {
									div(class: 'listing-text') {
										ul() {
											stocksResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											canadaResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											brazilResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											argentinaResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											mexicoResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
									}
								}
							}
							div(class: 'company-listing clearfix') {
								a(href: '#', class: 'listing-image') {
									img(src: '/images/content/asia.png')
								}
								div(class: 'listing-body') {
									div(class: 'listing-text') {
										ul() {
											hongkongResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											russiaResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											indiaResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											chinaResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											southkoreaResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											singaporeResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											israelResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
									}
								}
							}
							div(class: 'company-listing clearfix') {
								a(href: '#', class: 'listing-image') {
									img(src: '/images/content/europe.png')
								}
								div(class: 'listing-body') {
									div(class: 'listing-text') {
										ul() {
											unitedkingdomResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											franceResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											germanyResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											italyResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											switzerlandResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											swedenResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											austriaResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											greeceResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											norwayResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											denmarkResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											spainResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											netherlandsResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
									}
								}
							}
							div(class: 'company-listing clearfix') {
								a(href: '#', class: 'listing-image') {
									img(src: '/images/content/pacific.png')
								}
								div(class: 'listing-body') {
									div(class: 'listing-text') {
										ul() {
											japanResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											southkoreaResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											indonesiaResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											taiwanResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											australiaResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											singaporeResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
										ul() {
											newzealandResults.each {
												fragment "li(line) { a(href: link, line) }", line: it.name, link: it.plotLocation
												br()
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		include template: 'tags/footer.tpl'
		include template: 'tags/scripts.tpl'		
	}
}
