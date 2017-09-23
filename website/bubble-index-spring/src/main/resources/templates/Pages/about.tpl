yieldUnescaped '<!doctype html>'
html(class: '', lang: 'en') {
	link(rel: 'stylesheet', href: '/css/allCss.css')
	link(rel: 'stylesheet', href: '/css/aboutCustom.css')
	head {
		title('The Bubble Index - About')
		script(type: 'text/x-mathjax-config', src: '/scripts/mathjaxCustom.js') {}
		script(src: 'https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.1/MathJax.js?config=TeX-MML-AM_CHTML', integrity: 'sha256-SDRP1VVYu+tgAGKhddBSl5+ezofHKZeI+OzxakbIe/Y=', crossorigin: 'anonymous') {}
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
								li(class: 'first neighbour-left') {
									a(href: '/index', 'HOME')
								}
								li(class: 'active') {
									a(href: '/pages/method', 'ABOUT')
								}
								li(class: 'neighbour-right') {
									a(href: '/pages/examples', 'EXAMPLES')
								}
								li(class: '') {
									a(href: '/pages/links', 'LINKS')
								}							
								li(class: '') {
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
								h1() { yield 'About'}
							}
						}
					
					}
					div(class: 'project-body-container container-24') {
						div(class: 'project-body block') {
							div(class: 'project-short-description') {
								yield 'To help understand The Bubble Index, a brief summary is given:'
							}
							div(class: 'project-description') {
								p('The pupose of The Bubble Index is to provide investors, traders, economists, and social scientists with a tool to detect and monitor asset bubbles in real time.')
								p('The Bubble Index, with its 3D contour graphs, attempts to display the Log-Periodic Power Law (LPPL) Oscillation signature of traded stocks, indices, and commodities on all time scales in a straight-forward and simple manner. The idea relies on the emergent properties of the human societal organism. The growth and contraction of a society occurs in a fractal process which has long term memory, also know as hysteresis. In other words, an event which occurred several decades in the past may have a strong influence on current events. This is taken for granted in historical studies of the world and not considered in many modern economic theories. In other words, modern economics tends to ignore political science with various un-realistic assumptions, such as rational investors, perfect competition, friction-less markets, stability of the environment, and the infallibility of ruling elites. It would be interesting to see a theory of capital which predicts depressions, wars, fascism, dictatorships, dark ages, etc...')
								p('Take for example the horrific wars of the 20th century; today their outcomes currently shape the world. The creation of the state of Israel, for example, shapes current events in the Middle East; The Cold War shapes the ideology of modern generations and national conflicts more than 20 years after its end. The invention of the steam engine, AC electricity, the internet and many other similiar events still influence the present and future. Therefore, to understand the development of an economy in the time dimension, it must be decomposed into various time scales -- from several days to decades to centuries; a Fourier Transform in the language of Mathematics.')
								p("Interestingly, The Bubble Index has a subtle connection with Isaac Asimov's Foundation Series. In this series a mathematician, Hari Seldon, develops ${$a(href:'http://en.wikipedia.org/wiki/Psychohistory_%28fictional%29', "psychohistory")}. What is psychohistory?")
								blockquote("Using the laws of ${$a(href:'http://en.wikipedia.org/wiki/Mass_action_%28sociology%29', "mass action")}, it can predict the future, but only on a large scale; it is error-prone on a small scale. It works on the principle that the behaviour of a mass of people is predictable if the quantity of this mass is very large (equal to the population of the galaxy, which has a population of quadrillions of humans, inhabiting millions of star systems). The larger the number, the more predictable is the future.")
								p('Isaac Asimov was years ahead of his generation. Predicting the future based on mass action is perhaps possible. Perhaps in the near future we will have this ability refined. We will be able to make decisions which take into account the fractal nature of history.')
								p('The Bubble Index has attempted to make the detection of market bubbles simple and easy for the general public. The goal is to give the common investor confidence and independence by providing him/her with a powerful open-source tool. Please enjoy The Bubble Index and spread the word if it has helped you to make wise financial decisions.')
								p('The Elliott Wave Principle was influential in the foundation of The Bubble Index. According to the <a href="http://en.wikipedia.org/wiki/Elliott_wave_principle">Elliott Wave Principle</a>: <q>market prices alternate between an impulsive, or motive phase, and a corrective phase on all time scales of trend.</q> Commonly called a robust fractal, this fractal alternation provides the framework with which to understand the output of The Bubble Index. Underlying the Elliott Wave Principle is a fundamental Fibonacci sequence. The Fibonacci sequence, universal in biological growth processes, is a log-periodic sequence. The impulsive phase of an Elliott Wave can also be considered a "bubble" and its corrective phase a "crash." Hence, an "anti-bubble" is merely an impulse in the downward price direction. The bubbles and crashes mentioned in the press are simply impulse and corrective waves of the longer period Elliott Wave cycles (see <a href="http://en.wikipedia.org/wiki/Kondratiev_wave">Kondratiev wave</a> and <a href="http://en.wikipedia.org/wiki/Kuznets_swing">Kuznets Swing</a>). These large corrections are more directly felt by the majority of society. Recall that fractals are scale-invariant, thus further implying that bubbles and anti-bubbles occur on all time scales.')
								p('Financial bubbles, aka impulses, exhibit log-periodic precursory patterns prior to their correction. The goal of The Bubble Index is to identify and display these patterns in order to prepare investors for the correction.')
								p('The Bubble Index algorithm, based the ideas and research of Didier Sornette and colleagues, searches for LPPL Oscillations and displays their strength on various time scales. Central to the development of their idea were the subjects of networks, fractals, and self-organization. Indeed Sornette has a background in geophysics and earthquake prediction. His website, <a href="http://tasmania.ethz.ch/pubfco/fco.html">The Financial Crisis Observatory</a> has been reporting bubbles and predicting crashes and should be considered as an extra tool. The Bubble Index seeks to display similar information in an alternative format.')
								p('Given a time series of daily price data, The Bubble Index can easily be created. The algorithm determines the strength of the LPPL oscillations in the time series data. For example, consider the time series of daily data for the Dow Jones Industrial Average. This can be used to create The Bubble Index which predicts corrections on various time scales. The longer the window (52, 104, 256, ..., 10080 days), the further in the future the forcast and the larger the cycle being displayed.')
								p("The algorithm merges ideas found in various papers by Sornette. In most of these papers the time series is fitted with the LPPL formula. This formula requires a t${$sub("c")}, or critical time, which must be estimated through statistical methods. If the fit's statistical properties fit certain criteria, then a bubble is predicted with a most probable time of crash, t${$sub("c")}. However, this requires heavy computation and does not provide an easy way to provide a non-mathematician with an understandable analysis for thousands of assets.")
								p('By making certain assumptions, a computationally quicker and alternate approach was developed which takes the (H,Q)–Derivative of the LPPL formula. Now, instead of estimating the critical time, the critical time is now assumed to be one month in the future. With this assumption, the power of LPPL oscillations can be determined with minimal computation and Fast Fourier Transform.')
								p("The following formulas are found in the paper entitled, ${$a(href:'arxiv.org/abs/cond-mat/0201458', "Generalized q-Analysis of Log-Periodicity: Applications to Critical Ruptures")}, by Wei-Xing Zhou and Didier Sornette. The simple log-periodic power law (LPPL) function which forms the basis of the algorithm used by The Bubble Index is:")
								include unescaped: 'formulas/first.txt'
								p('The Bubble Index calculates the H,Q derivative and then performs a search for the best parameters H and q which give the strongest periodogram signal in a specific frequency range.')
								p('The Bubble Index attempts to create a daily index by comparing the current market-price time series with the LPPL function above.')
								p('Since the majority of all bubbles, where a bubble is here defined by a common term applied to a specific historical event such as the Internet Bubble or the 1920s American stock bubble, contain a price time series which exhibits the LPPL oscillation, the term "Bubble Index" is suitable to describe such a daily index. Since human behavior and social interactions change infinitesimally over time periods of 10 or 100 years, these LPPL can be found in price time series as far back as historical records allow. I think one of the biggest unanswered questions is: will LPPL oscillations continue to exist if all market participants recognize their signal?')
								h2() { yield 'Plot the LPPL Curve'}
								div(class: 'container') {
									form(action: '', onsubmit: 'displayLPPL();return false;') {
										fieldset() {
											label(for: 'A') { yield 'A:' }
											input(id: 'A', type: 'textarea', name: 'A', value: '10.0')
											br()
											br()
											label(for: 'B') { yield 'B:' }
											input(id: 'B', type: 'textarea', name: 'B', value: '5.0')
											br()
											br()
											label(for: 'C') { yield 'C:' }										
											input(id: 'C', type: 'textarea', name: 'C', value: '2.0')
											br()
											br()
											label(for: 'Omega') { yield 'Omega:' }																				
											input(id: 'OMEGA', type: 'textarea', name: 'Omega', value: '6.28')
											br()
											br()
											label(for: 'M') { yield 'm:' }																				
											input(id: 'M', type: 'textarea', name: 'M', value: '0.37')
											br()
											br()
											label(for: 'T-c') {
												yield 't'
												sub() { 'c' }
												yield ':'
											}																				
											input(id: 'TC', type: 'textarea', name: 'T-c', value: '100')
											br()
											br()
											label(for: 'lppl') { yield 'LPPL' }																				
											input(id: 'lppl', type: 'radio', name: 'lppl', checked: '')
											br()
											br()
											label(for: 'lppl') { yield 'Reverse-LPPL' }																				
											input(id: 'lppl_rev', type: 'radio', name: 'lppl')
											br()
											br()
											input(type: 'submit', value: 'Plot')
										}
									}
								}
								div(class: 'plot', id: 'plotarea', width: '100%') {}
							}
						}
					}
				}
			}
		}
		include template: 'tags/footer.tpl'
		include template: 'tags/scripts.tpl'
		script(src: 'https://cdnjs.cloudflare.com/ajax/libs/d3/4.9.1/d3.min.js', integrity: 'sha256-jJlsVPlSwQyGokYi0JgmrFRihc7ILE3Id8AB0dpr0JA=', crossorigin: 'anonymous') {}
		script(src: '/scripts/methodD3Script.js') {}
	}
}
