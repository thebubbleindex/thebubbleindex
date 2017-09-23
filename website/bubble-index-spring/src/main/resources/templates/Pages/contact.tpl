yieldUnescaped '<!doctype html>'
html(class: '', lang: 'en') {
	link(rel: 'stylesheet', href: '/css/allCss.css')
	head {
		title('The Bubble Index - Contact')
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
								li(class: 'neighbour-left') {
									a(href: '/pages/links', 'LINKS')
								}							
								li(class: 'active') {
									a(href: '/pages/contact', 'CONTACT')
								}
								li(class: 'neighbour-right') {
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
				div(class: 'zone-content equalize zone clearfix') {
					div(class: 'content-container container-24') {
						div(class: 'about-us block') {
							div(class: 'block-title') {
								h1() { yield 'Contact Us' }
							}
						}
						div(class: 'employee-info clearfix') {
							div(class: 'employee-info-social') {
								div(class: 'employee-photo') {
									img(src: '/images/content/binary.jpg', height: '249', width: '250')
								}
								a(class: 'facebook', href: '//www.facebook.com/thebubbleindex') {}
								a(class: 'twitter', href: '//mastodon.social/@TheBubbleIndex') {}
								a(class: 'google', href: 'https://github.com/bigttrott/thebubbleindex') {}
							}
							div(class: 'employee-info-description') {
								div(class: 'name') {
									yield 'Donation'
								}
								div(class: 'position') {
									yield 'Support the Site'
								}
								div(class: 'description') {
									yieldUnescaped "<a href='https://www.coinbase.com/checkouts/877a83366d0f3e5a19731467f5e7bb70' class='coinbase-button' data-code='877a83366d0f3e5a19731467f5e7bb70' data-button-style='custom_small'>Donate Bitcoins</a>"
								}
							}
						}
						div(class: 'employee-info clearfix') {
							div(class: 'employee-info-social') {
								div(class: 'employee-photo') {
									img(src: '/images/content/HTML.jpg', height: '250', width: '250')
								}
								a(class: 'facebook', href: '//www.facebook.com/thebubbleindex') {}
								a(class: 'twitter', href: '//mastodon.social/@TheBubbleIndex') {}
								a(class: 'google', href: 'https://github.com/bigttrott/thebubbleindex') {}
							}
							div(class: 'employee-info-description') {
								div(class: 'name') {
									yield 'IT'
								}
								div(class: 'position') {
									yield 'Website/Database Services'
								}
								div(class: 'description') {
									yield 'I manage the website and the functionality of its services.'
								}
							}
						}
						div(class: 'employee-info clearfix') {
							div(class: 'employee-info-social') {
								div(class: 'employee-photo') {
									img(src: '/images/content/monkey-business.jpg', height: '500', width: '500')
								}
								a(class: 'facebook', href: '//www.facebook.com/thebubbleindex') {}
								a(class: 'twitter', href: '//mastodon.social/@TheBubbleIndex') {}
								a(class: 'google', href: 'https://github.com/bigttrott/thebubbleindex') {}
							}
							div(class: 'employee-info-description') {
								div(class: 'name') {
									yield 'Mr. Market'
								}
								div(class: 'position') {
									yield 'Chief of Marketing'
								}
								div(class: 'description') {
									yield 'I manage the advertising. Follow us on Mastodon.social @TheBubbleIndex'
								}
							}
						}
						div(class: 'separator') {}
						div(class: 'company-details block') {
							div(class: 'company-address') {
								div(class: 'details-title') {
									yield 'Address Details:'
								}
								div(class: 'detail address') {
									yield 'Street'
									br()
									yield 'Canada'
								}
								div(class: 'detail phone') {
									yield 'Phone:'
								}
								div(class: 'detail email') {
									a(href: 'https://anautonomousagent.com/2016/04/nerdy-method-to-encode-email-address-to-avoid-spam/', 'Contact Me')
								}
							}
						}
					}
				}	
			}
		}
		include template: 'tags/footer.tpl'
		include template: 'tags/scripts.tpl'
		script(type: 'text/javascript', src: 'https://www.coinbase.com/assets/button.js') {}
	}
}
