yieldUnescaped '<!doctype html>'
html(class: '', lang: 'en') {
	link(rel: 'stylesheet', href: '/css/allCss.css')
	link(rel: 'stylesheet', href: '/css/examples.css')
	head {
		title('The Bubble Index - Examples')
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
								li(class: 'neighbour-left') {
									a(href: '/pages/method', 'ABOUT')
								}
								li(class: 'active') {
									a(href: '/pages/examples', 'EXAMPLES')
								}
								li(class: 'neighbour-right') {
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
								h1() { yield 'Examples'}
							}
						}
					}
					div(class: 'project-body-container container-24') {
						div(class: 'project-body block') {
							div(class: 'project-short-description') {
								yieldUnescaped "To help understand The Bubble Index, ${$a(class: 'text-colorful', href:'https://www.scribd.com/user/224680756/thebubbleindex', "The Bubble Index: Dow Jones Industrial Average")} will be referred to in the following examples. Click on the title of each section for a more detailed PDF. By comparing the bubbles of 1929, 1987, 2000, and 2008 with the values of The Bubble Index, the following examples show the remarkable power of The Bubble Index. The entire contour map of the Dow Jones Industrial Average since 1871 can be viewed here: ${$a(class: 'text-colorful', href:'https://www.scribd.com/user/224680756/thebubbleindex', "3D Contour: DJIA (1871 - 2015)")}."
							}
							div(class: 'project-description') {
								p() {
									a(class: 'text-colorful', href: 'https://www.scribd.com/user/224680756/thebubbleindex', 'Theoretical LPPL Oscillations')
								}
								p('The theoretical output of The Bubble Index, if given a purely mathematical LPPL function.')
								p() {
									a(class: 'text-colorful', href: 'https://cdn.thebubbleindex.com/TheBubbleIndex/Indices/DJIA/DJIA.pdf', 'Black Thursday')
								}
								p('Wealthy tycoons and Wall Street businessmen on Black Thursday, October 24, 1929 witnessed one of the largest declines in the history of the DJIA. With the crash came the start of The Great Depression and economic hardship for millions of families. This was a terrible crisis of capital which precipitated the New Deal policies of FDR who saved capitalism from itself. The Bubble Index: 1,764 Day Window, which peaks on October 17, 1929, shows a rapid increase starting on September 22, 1927. Cautious elites, had they been aware of the growing LPPL oscillations would have anticipated the crash at least a year before the actual event. Imagine having this index in 1928. A trader would have seen the index break record highs on October 1, 1928. In other words, more than a year before the crash of Black Thursday, a quantitative warning sign was predicting disaster.')								
								p() {
									a(class: 'text-colorful', href: 'https://cdn.thebubbleindex.com/TheBubbleIndex/Indices/DJIA/DJIA.pdf', 'Black Monday')
								}
								p('Black Monday, October 19, 1987, can be considered a textbook example of an excellent prediction made by The Bubble Index: 1764 Day Window. On March 3, 1983 the index was hovering close to zero. Over the course of five years the index saw a massive and continuous rise to a peak on October 16, 1987. Wall Street firms could have easily avoided losses had they be aware of the LPPL oscillations. The crash of 1987 was an extreme event where bystanders witnessed one of the largest one day losses ever recorded.')
								p() {
									a(class: 'text-colorful', href: 'https://cdn.thebubbleindex.com/TheBubbleIndex/Indices/DJIA/DJIA.pdf', "Late 90's")
								}
								p("Emotions run high on Wall Street. Herd like behavior is the norm among certain subsets of the elite. And this can be clearly seen by looking at The Bubble Index during the Dot-Com Bubble. The Bubble Index (clearly seen in the 5,040, 10,080, and 20,160 Day Windows) rockets from near zero in the 1980's to a all-time highs in early 2000. Many common-sense people at the time knew that this was a massive bubble; evidenced was provided by some of the most extreme company valuations in recorded history. However, what they did not know was when the music was going to stop. With the help of The Bubble Index, a wise investor would have been out of the market.")
								p() {
									a(class: 'text-colorful', href: 'https://cdn.thebubbleindex.com/TheBubbleIndex/Indices/DJIA/DJIA.pdf', "The Great Recession")
								}
								p("As seen in The Bubble Index: DJIA (10,080 and 20,160 Day Windows), the crash appears to be the result of a process with long memory. What's striking is that this increase started in the early 1980's. These indices reached a peak in November 2008. The warning signs for the beginning of the Great Recession and the crash of 2008-9 couldn't have been more clear. But don't worry, central bankers know this and visit The Bubble Index website daily.")
								p('Predicting Anti-Bubbles')
								p('Anti-bubbles, also called market corrections, can be measured with The Bubble Index. A perfect analogy relies on the Elliott Wave concept of impulse waves which occur in both the positive and negative directions. For example, in the case of the 1930 correction, The Bubble Index: 1,764 Day Window peaks as the market finishes its collapse. This same type of spike occurs after the 2008-9 crash. Also, recently in stocks such as JCP and SNE, spikes occur just before a rebound in the price.')
								p() {
									a(class: 'text-colorful', href: 'https://lpplmarketwatch.com/3d-contour-examples/', '3D Contour')
								}
								p("All of these examples can be seen in their full expression as a 3D Contour Map ${$a(class: 'text-colorful', href:'https://lpplmarketwatch.com/3d-contour-examples/', "here")}")
								br()
								div(class: 'youtube', id: 'gfy9vJghAz0', style: 'width: 560px; height: 315px;') {}
								div(class: 'youtube', id: 'xX4SPU1_CVg', style: 'width: 560px; height: 315px;') {}
								br()
								br()
								div(id: 'disqus_thread') {}
							}
						}
					}
				}
			}
		}
		include template: 'tags/footer.tpl'
		include template: 'tags/scripts.tpl'
		script(type: 'text/javascript', src: '/scripts/youtubeScript.js') {}
		script(type: 'text/javascript', src: '/scripts/disqusExamples.js') {}
	}
}
