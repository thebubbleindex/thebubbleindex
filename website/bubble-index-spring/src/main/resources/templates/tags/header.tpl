layout 'tags/meta.tpl' 
	head {     
		title('The Bubble Index - Browse')
		link(rel: 'stylesheet', href: '/css/allCss.css')
		link(rel: 'stylesheet', href: '/css/mastodon.widget.css')
		script(type: 'text/javascript', src: 'https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js', integrity: 'sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=', crossorigin: 'anonymous')
		script(type: 'text/javascript', src: '/scripts/mastodon.widget.js')
	
		header(class: 'section') {
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
									a(href: 'pages/method', 'ABOUT')
								}
								li(class: '') {
									a(href: 'pages/examples', 'EXAMPLES')
								}
								li(class: '') {
									a(href: 'pages/links', 'LINKS')
								}							
								li(class: 'neighbour-left') {
									a(href: 'pages/contact', 'CONTACT')
								}
								li(class: 'active') {
									a(href: '/browse', 'BROWSE')
								}
								li(class: 'neighbour-right') {
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