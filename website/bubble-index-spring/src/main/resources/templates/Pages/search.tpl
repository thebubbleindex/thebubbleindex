yieldUnescaped '<!doctype html>'
html(class: '', lang: 'en') {
	link(rel: 'stylesheet', href: '/css/allCss.css')
	head {
		title('The Bubble Index - Search')
		script(type: 'text/javascript', src: 'https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js', integrity: 'sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=', crossorigin: 'anonymous') {}
		script(type: 'text/javascript', src: '/scripts/jquery-migrate-3.0.0.js') {}
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
								li(class: 'active last') {
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
								h1() { yield 'Search'}
							}
							div(class: 'zone-search clearfix') {
								div(class: 'search-container container-24') {
									div(class: 'search block') {
										form(id: 'default-search', class: 'default-search clearfix', method: 'get', action: 'searchResults()') {
											input(type: 'text', name: 'search', id: 'search-what', class: 'text-input-black input-text', placeholder: 'Keywords/Ticker...', required: '')
										}
									}
								}
							}
						}
					}
					div(class: 'project-body-container container-24') {
						div(class: 'project-body block') {
							div(class: 'project-short-description') {
								yield 'Enter a ticker symbol or a keyword to search.'
							}
						}
					}
				}
			}
		}
		include template: 'tags/footer.tpl'
		include template: 'tags/scripts.tpl'
		script(type: 'text/javascript', src: '/scripts/autocomplete.js') {}
	}
}
