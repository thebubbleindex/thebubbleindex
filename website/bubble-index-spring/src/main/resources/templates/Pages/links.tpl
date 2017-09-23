yieldUnescaped '<!doctype html>'
html(class: '', lang: 'en') {
	link(rel: 'stylesheet', href: '/css/allCss.css')
	head {
		title('The Bubble Index - Links')
		script(type: 'text/javascript', src: 'https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js', integrity: 'sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=', crossorigin: 'anonymous') {}
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
								li(class: 'neighbour-left') {
									a(href: '/pages/examples', 'EXAMPLES')
								}
								li(class: 'active') {
									a(href: '/pages/links', 'LINKS')
								}							
								li(class: 'neighbour-right') {
									a(href: '/pages/contact', 'CONTACT')
								}
								li(class: '') {
									a(href: '/browse', 'BROWSE')
								}
								li(class: 'last') {
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
				div(class: 'zone-content zone clearfix') {
					div(class: 'project-header-container container-24') {
						div(class: 'project-header block') {
							div(class: 'block-title') {
								h1() { yield 'Links'}
							}
						}
					
					}
					div(class: 'project-body-container container-24') {
						div(class: 'project-body block') {
							div(class: 'project-short-description') {
								yield 'To help understand the foundation of The Bubble Index, please visit the following links.'
							}
							div(class: 'project-description') {
								p() {
									a(href: 'http://tasmania.ethz.ch/pubfco/fco.html', 'Financial Crisis Observatory')
								}
								p() {
									a(href: '//amzn.com/0691118507', 'Why Stock Markets Crash by Didier Sornette')
								}
								p() {
									a(href: '//amzn.com/0465043577', 'The Misbehavior of Markets: A Fractal View of Financial Turbulence by Benoit Mandelbrot')
								}
								p() {
									a(href: '//amzn.com/0932750753', 'Elliott Wave Principle: Key To Market Behavior by Robert Prechter and A.J. Frost')
								}
								p() {
									a(href: '//arxiv.org/abs/cond-mat/0201458', 'Generalized q-Analysis of Log-Periodicity: Applications to Critical Ruptures by Wei-Xing Zhou and Didier Sornette')
								}
								p() {
									a(href: 'http://econpapers.repec.org/article/bxrbxrceb/2013_2f80942.htm', 'Shocks, Crashes and Bubbles in Financial Markets, by Didier Sornette and Anders Johansen')
								}
								p() {
									a(href: '//arxiv.org/abs/cond-mat/9903321', 'Predicting Financial Crashes Using Discrete Scale Invariance, by Anders Johansen, Didier Sornette, and Olivier Ledoit')
								}
								p() {
									a(href: '//arxiv.org/abs/1001.0265', 'Diagnosis and Prediction of Tipping Points in Financial Markets: Crashes and Rebounds, by Wanfeng Yan, Ryan Woodard, and Didier Sornette')
								}
								p() {
									a(href: '//www.tandfonline.com/doi/abs/10.1080/14697680701689620', 'Detecting Log-Periodicity in a Regime Switching Model of Stock Returns by George Chang and James Feigenbaum')
								}
								p() {
									a(href: '//papers.ssrn.com/sol3/papers.cfm?abstract_id=1752115', 'Everything You Always Wanted to Know about Log Periodic Power Laws for Bubble Modelling but Were Afraid to Ask by Petr Geraskin and Dean Fantazzini')
								}
								p() {
									a(href: '//arxiv.org/abs/1107.3171', 'Clarifications to Questions and Criticisms on the Johansen-Ledoit-Sornette Bubble Model by Didier Sornette, Ryan Woodard, Wanfeng Yan, Wei-Xing Zhou')
								}
								p() {
									a(href: '//www3.nd.edu/~meg/MEG2004/Chang-George.pdf', 'A Bayesian Analysis of Log-Periodic Precursors to Financial Crashes by George Chang and James Feigenbaum')
								}
								p() {
									a(href: 'http://www.pfr.com/index.htm', 'Parallax Financial Research, Inc.')
								}
								p() {
									a(href: 'http://hedcapital.com/index.php', 'HED Capital Management Ltd.')
								}
								p() {
									a(href: 'https://vimeo.com/66865737', 'Financial Seismology Video')
								}
								p() {
									a(href: '//arxiv.org/abs/1310.8431', "Multiagent's model of stock market with p-adic description of prices by Viktor Zharkov")
								}
								p() {
									a(href: '//arxiv.org/abs/1401.3281', 'A Creepy World by Didier Sornette and Peter Cauwels')
								}
								p() {
									a(href: '//arxiv.org/abs/1401.5314', 'Why free markets die: An evolutionary perspective by Eduardo Viegas, Stuart P. Cockburn, Henrik Jeldtoft Jensen, Geoffrey B. West')
								}
								p() {
									a(href: 'http://arrow.dit.ie/cgi/viewcontent.cgi?article=1046&amp;context=engscheleart2', 'Currency Trading using the Fractal Market Hypothesis by Jonathan Blackledge and Kieran Murphy')
								}
								p() {
									a(href: 'http://arrow.dit.ie/cgi/viewcontent.cgi?article=1016&amp;context=engscheleart2', 'Application of the Fractional Diffusion Equation for Predicting Market Behaviour by Jonathan Blackledge')
								}
								p() {
									a(href: 'http://socionomics.org/pdf/socionomics_pareto.pdf', 'The Socionomic Theory of Finance and the Institution of Social Mood:Pareto and the Sociology of Instinct and Rationalization by Wayne D. Parker and Robert R. Prechter')
								}
								p() {
									a(href: 'http://lpplmarketwatch.com', 'LPPL Market Watch')
								}
								p() {
									a(href: 'https://anautonomousagent.com', 'An Autonomous Agent')
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
