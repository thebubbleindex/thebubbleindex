yieldUnescaped '<!doctype html>'
html(class: '', lang: 'en') {
	link(rel: 'stylesheet', href: '/css/allCss.css')
	head {
		title('The Bubble Index - Legal Notice')
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
								h1() { yield 'Legal Notice'}
							}
						}
					}
					div(class: 'project-body-container container-24') {
						div(class: 'project-body block') {
							div(class: 'project-short-description') {
								p("1. This is an Agreement between you and The Bubble Index (“Company”). This Agreement governs your use of this Web site, www.thebubbleindex.com (the “Site”). THE COMPANY OFFERS THE SITE TO YOU CONDITIONED ON YOUR ACCEPTANCE WITHOUT MODIFICATION OF THIS AGREEMENT. YOUR USE OF THE SITE CONSTITUTES YOUR ACCEPTANCE OF THIS AGREEMENT. THIS AGREEMENT CONTAINS DISCLAIMERS OF WARRANTIES AND LIABILITY (SEE SECTIONS 4 AND 5) AND AN EXCLUSIVE REMEDY (SEE SECTION 6). THESE PROVISIONS FORM AN ESSENTIAL BASIS FOR YOUR USE OF THE SITE. You agree not to use the Site in any way that is unlawful.")
								p("2. The Company reserves the right to modify the terms, conditions and notices under which it offers the Site without notice. Your continued use of the Site after any such changes constitutes your agreement to such changes. The Company further reserves the right to change prices and other information on the Site at any time without notice. The posting of prices and other terms of sale shall not constitute a binding order to sell product on such terms.")
								p("3. All content, including without limitation graphics, logos, text, images and other features, appearing on the Site, are the copyrights, trademarks and other intellectual property owned, controlled or licensed by the Company or third parties. This content is protected by copyright separately and as a collective work or compilation under U.S. and international copyright law and is the property of the Company, its licensors, or the party credited as the provider of the content or other third-party owners of the content, as the case may be.")
								p("4. THIS SITE IS PROVIDED BY THE COMPANY ON AN “AS IS” BASIS. THE COMPANY, TO THE MAXIMUM EXTENT PERMITTED BY APPLICABLE LAW, MAKES NO REPRESENTATIONS OR WARRANTIES OF ANY KIND, EXPRESS OR IMPLIED, AS TO THE OPERATION OF THE SITE OR THE INFORMATION, CONTENT, MATERIALS, OR PRODUCTS INCLUDED ON THIS SITE. THE COMPANY DISCLAIMS ALL WARRANTIES, EXPRESS, STATUTORY OR IMPLIED, INCLUDING WITHOUT LIMITATION IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE; WARRANTIES OR CONDITIONS OF WORKMANLIKE EFFORT, ACCURACY, TITLE, QUIET ENJOYMENT, NO ENCUMBRANCES, NO LIENS AND NON-INFRINGEMENT; WARRANTIES OR CONDITIONS ARISING THROUGH COURSE OF DEALING OR USAGE OF TRADE; AND WARRANTIES OR CONDITIONS THAT ACCESS TO OR USE OF THE SITE WILL BE UNINTERRUPTED OR ERROR FREE.")
								p("6. THE COMPANY WILL NOT BE LIABILE FOR ANY DAMAGES OF ANY KIND ARISING FROM THE USE OF THIS SITE, INCLUDING WITHOUT LIMITATION DIRECT, INDIRECT, INCIDENTAL, PUNITIVE AND CONSEQUENTIAL DAMAGES, EVEN IF THE COMPANY OR AN AUTHORIZED REPRESENTATIVE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES. APPLICABLE LAW MAY NOT PERMIT THE LIMITATION OR EXCLUSION OF LIABILITY OR INCIDENTAL OR CONSEQUENTIAL DAMAGES, SO THE ABOVE LIMITATION OR EXCLUSION MAY NOT APPLY TO YOU. THIS EXCLUSION OF DAMAGES IS INDEPENDENT OF THE EXCLUSIVE REMEDY DESCRIBED BELOW AND SHALL SURVIVE IN THE EVENT THAT SUCH REMEDY FAILS OF ITS ESSENTIAL PURPOSE OR IS OTHERWISE DEEMED UNENFORCEABLE.")
								p("7. IN NO EVENT SHALL THE COMPANY’S TOTAL LIABILITY TO YOU FOR ALL DAMAGES, LOSSES AND CAUSES OF ACTION (WHETHER OR NOT IN CONTRACT, TORT (INCLUDING WITHOUT LIMITATION NEGLIGENCE), OR OTHERWISE) EXCEED THE AMOUNT PAID BY YOU, IF ANY, FOR ACCESSING THIS OR ANY OTHER COMPANY SITE.")
								p("8. This Site contains links to additional resources. The Company does not have any control over, and, therefore, is not responsible for, the content or availability of these other resources. The Company is not affiliated in any way with Didier Sornette, the Financial Crisis Observatory, Parallax Financial, or HED Capital Management.")
								p("9. This Agreement constitutes the entire agreement of the parties with respect to its subject matter, and supersedes all previous written or oral agreements of the parties with respect to such subject matter. No waiver by either party of any breach or default by the other shall be deemed to be a waiver of any preceding or subsequent breach or default. This Agreement shall be governed by, and construed in accordance with, the laws of Canada, without regard to its conflict of laws rules.")
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
