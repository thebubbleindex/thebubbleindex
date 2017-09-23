div(class: 'sidebar-container container-8') {
	div(class: 'recently-added block') {
		div(class: 'block-title') {
			h3() { yield 'Data Status' }
		}
		ul(class: 'entries-list') {
			li(class: 'clearfix') {
				a(class: 'entry-title', href: '#', 'Last Update')
				div(class: 'entry-excerpt') { yield 'July 22, 2017' }
			}
		}
	}
	div(class: 'latest-news block') {
		div(class: 'block-title') {
			h3() { yield 'Updates' }
		}
		ul(class: 'entries-list') {
			li(class: 'clearfix') {
				div(id: 'myTimeline', class: 'mastodon-timeline mastodon-timeline-dark') {}
				script(type: 'text/javascript', src: '/scripts/mastodonCustom.js') {}
			}
		}
	}
}
