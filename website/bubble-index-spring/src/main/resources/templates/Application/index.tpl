yieldUnescaped '<!doctype html>'
html(lang:'en') {
	head {     
		title('The Bubble Index - Home')
		link(rel: 'stylesheet', href: '/css/allCss.css')
		link(rel: 'stylesheet', href: '/css/mastodon.widget.css')
		script(type: 'text/javascript', src: 'https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js', integrity: 'sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=', crossorigin: 'anonymous') {}
		script(type: 'text/javascript', src: '/scripts/mastodon.widget.js') {}
	
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
								li(class: 'empty neighbour-left') {
									div() {}
								}
								li(class: 'first active') {
									a(href: '/index', 'HOME')
								}
								li(class: 'neighbour-right') {
									a(href: 'pages/method', 'ABOUT')
								}
								li(class: '') {
									a(href: 'pages/examples', 'EXAMPLES')
								}
								li(class: '') {
									a(href: 'pages/links', 'LINKS')
								}							
								li(class: '') {
									a(href: 'pages/contact', 'CONTACT')
								}
								li(class: '') {
									a(href: '/browse', 'BROWSE')
								}
								li(class: 'last') {
									a(href: 'pages/search', 'SEARCH')
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
			div(class: 'search-wrapper clearfix') {
				div(class: 'zone-search zone clearfix') {
					div(class: 'search-container container-24') {
						div(class: 'search block') {
							form(id: 'default-search', class: 'default-search clearfix', method: 'get', action: '/searchResults') {
								input(type: 'text', name: 'search', id: 'search-what', class: 'text-input-black input-text', placeholder: 'Keyword/Ticker...')
							}
						}
					}
				}
			}
			div(class: 'map-wrapper') {
				div(class: 'map', id: 'map') {}
			}	
			div(class: 'content-wrapper') {
				div(class: 'zone-content equalize zone clearfix') {
					div(class: 'content-container container-16') {
						div(class: 'welcome block') {
							div(class: 'block-title') {
								h1() { yield 'Welcome' }
							}
							div(class: 'welcome-text') {
								yield 'Welcome to The Bubble Index, an index which measures Log-Periodic Power Law Oscillations in traded assets. Either select from a category below or enter a ticker symbol/keyword in the above search field.'
							}
							div(class: 'welcome-globe clearfix') {
								img(class: 'globe', src: '/images/globe.png', height: '244', width: '244')
								img(class: 'globe-background', src: '/images/globe-background.png', height: '201', width: '620')
								a(class: 'left edge top', href: 'browselist(Indices)', 'MARKETS')
								a(class: 'right edge top', href: 'browselist(Currencies)', 'CURRENCIES')
								a(class: 'left middle top', href: 'browselist(Commodities)', 'COMMODITIES')
								a(class: 'right middle top', href: 'browseAmericas', 'AMERICAS')
								a(class: 'left middle bottom', href: 'browseAsia', 'ASIA')
								a(class: 'right middle bottom', href: 'browseEurope', 'EUROPE')
								a(class: 'left edge bottom', href: 'browsePacific', 'PACIFIC')
								a(class: 'right edge bottom', href: 'browseComposite', 'COMPOSITE')
							}
						}
					}
					include template: 'tags/sidebar.tpl'
				}
			}
			div(class: 'partners-wrapper') {
				div(class: 'zone-partners zone clearfix') {
					div(class: 'partners-container container-24') {
						div(class: 'partners block') {
							div(class: 'block-title background-white') {
								h4() { yield 'LINKS' }
							}
							a(class: 'partner', href: 'https://github.com/bigttrott/thebubbleindex', 'Source Code / Download')
							a(class: 'partner', href: 'http://tasmania.ethz.ch/pubfco/fco.html', 'Financial Crisis Observatory')
							a(class: 'partner', href: 'http://lpplmarketwatch.com', 'LPPL Marketwatch')
							a(class: 'partner', href: 'https://anautonomousagent.com', 'An Autonomous Agent')
						}
					}
				}
			}
			div(class: 'interlayer') {}
		}
		include template: 'tags/footer.tpl'
		include template: 'tags/scripts.tpl'
		script(type: 'text/javascript', src: '/scripts/autocomplete.js') {}
	}
}
