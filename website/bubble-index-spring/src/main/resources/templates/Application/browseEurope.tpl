yieldUnescaped '<!doctype html>'
html(lang:'en') {
	head {     
		title('The Bubble Index - Browse Europe')
		link(rel: 'stylesheet', href: '/css/allCss.css')
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
								li(class: 'neighbour-left') {
									a(href: '/pages/contact', 'CONTACT')
								}
								li(class: 'active') {
									a(href: '/browse', 'BROWSE')
								}
								li(class: 'neighbour-right') {
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
								h1() { yield 'Browse Europe'}
							}
							div(class: 'zone-search clearfix') {}
						}
						div(class: 'project-body-container container-24') {
							div(class: 'welcome block') {
								div(class: 'welcome-globe clearfix') {
									img(class: 'globe', src: '/images/globe.png', height: '244', width: '244')
									img(class: 'globe-background', src: '/images/globe-background.png', height: '201', width: '620')
									a(class: 'left edge top', href: '/browselist/Germany', 'GERMANY')
									a(class: 'right edge top', href: '/browselist/UnitedKingdom', 'UNITED KINGDOM')
									a(class: 'left middle top', href: '/browselist/France', 'FRANCE')
									a(class: 'right middle top', href: '/browselist/Sweden', 'SWEDEN')
									a(class: 'left middle bottom', href: '/browselist/Italy', 'ITALY')
									a(class: 'right middle bottom', href: '/browselist/Greece', 'GREECE')
									a(class: 'left edge bottom', href: '/browselist/Switzerland', 'SWITZERLAND')
									a(class: 'right edge bottom', href: '/browseMoreEurope', 'MORE')
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
