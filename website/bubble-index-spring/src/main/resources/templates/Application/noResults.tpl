yieldUnescaped '<!doctype html>'
html(class: '', lang: 'en') {
	link(rel: 'stylesheet', href: '/css/allCss.css')
	head {
		title('The Bubble Index - Search Results')
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
							p('Sorry! The Bubble Index can not find your query. Go to the Forum to request your desired stock or index. Or send me an email.')
						}
					}
				}
			}
		}
		include template: 'tags/footer.tpl'
		include template: 'tags/scripts.tpl'
	}
}
