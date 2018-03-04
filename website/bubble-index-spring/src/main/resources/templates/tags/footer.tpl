footer(class: 'section') {
	div(class: 'footer-wrapper') {
		div(class: 'zone-footer zone clearfix') {
			div(class: 'footer-container container-24') {
				div(class: 'website-short-description block') {
					i(class: 'sprite sprite-logo-small') {}
					div(class: 'description-text') {
								yield 'The Bubble Index is a non-profit, open-source tool designed to help investors, economists, and social scientists identify and diagnose asset bubbles in real time.'
					}
				}
				div(class: 'twitter-feed block') {
					h3(class: 'title', 'Recent Headlines')
					ul() {
						li(class: 'first') {
							a(class: 'text-colorful', href: 'http://thebubbleindex.lefora.com', 'Forum')
						}
						li() {
							a(class: 'text-colorful', href: 'https://github.com/thebubbleindex', 'Source Code')
						}
						li() {
							a(class: 'text-colorful', href: 'http://lpplmarketwatch.com', 'Blog')
						}
						li() {
							a(class: 'text-colorful', href: '/pages/legal', 'Legal')
						}
					}
				}
				div(class: 'recent-posts block') {
					h3(class: 'title', 'Recent Posts')
					ul() {
						li(class: 'first') {
							a(class: 'text-colorful', href: 'https://gateway.ipfs.io/ipns/QmPC1xcsMTDKFTWHLrGEaNTCEdceUf4wWRJhM5P4kDeUsv/post/2016/06/Mean Value Window Plot/', 'Mean Value vs. Window')
						}
						li() {
							a(class: 'text-colorful', href: 'https://gateway.ipfs.io/ipns/QmPC1xcsMTDKFTWHLrGEaNTCEdceUf4wWRJhM5P4kDeUsv/page/3dcontourexamples/', '3D Contour Examples')
						}							
						li() {
							a(class: 'text-colorful', href: 'https://www.scribd.com/user/224680756/thebubbleindex', 'Documents')
						}	
					}
				}
				div(class: 'flickr-feed block') {
					h3(class: 'title', 'Flickr Feed')
					div(id: 'flickr-feed') {}
				}
			}
		}
	}
	div(class: 'copyright-wrapper') {
		div(class: 'zone-copyright zone clearfix') {
			div(class: 'copyright-left-container container-12') {
				div(class: 'copyright block') { yield 'The Bubble Index. All Rights Reserved.'}
			}
			div(class: 'copyright-right-container container-12') {
				div(class: 'social-links block') {
					a(href: 'https://www.facebook.com/thebubbleindex') {
						i(class: 'sprite sprite-facebook-icon') {}
					}
					a(href: 'https://twitter.com/TheBubbleIndex') {
						i(class: 'sprite sprite-twitter-icon') {}
					}
					a(href: 'http://lpplmarketwatch.com') {
						i(class: 'sprite sprite-google-icon') {}
					}
					a(href: 'https://anautonomousagent.com') {
						i(class: 'sprite sprite-linkedin-icon') {}
					}
					a(href: 'https://github.com/thebubbleindex') {
						i(class: 'sprite sprite-pinterest-icon') {}
					}					
					a(href: 'http://thebubbleindex.lefora.com') {
						i(class: 'sprite sprite-dribbble-icon') {}
					}	
				}
			}
		}
	}
}
