/* global document */
jQuery(document).ready(function(){

	/***
	Adding Google Map.
	***/

	/* Calling goMap() function, initializing map and adding markers. */
	jQuery('#map').goMap({
		maptype: 'ROADMAP',
        latitude: 40.760651, 
        longitude: -73.930635, 
        zoom: 13,
        scaleControl: true,
        scrollwheel: false,
		markers: [
			{latitude: 40.739323, longitude: -73.993807, group: 'airport', icon: 'images/marker-airport.png', html: {
				content: 'Lorem ipsum dolor sit amet.<br /><a href="company-page.html">Read More</a>'
			}},
			{latitude: 40.759090, longitude: -73.918619, group: 'airport', icon: 'images/marker-airport.png', html: {
				content: 'Lorem ipsum dolor sit amet.<br /><a href="company-page.html">Read More</a>'
			}},
			{latitude: 40.750898, longitude: -74.004278, group: 'airport', icon: 'images/marker-airport.png', html: {
				content: 'Lorem ipsum dolor sit amet.<br /><a href="company-page.html">Read More</a>'
			}},
			{latitude: 40.733210, longitude: -74.062300, group: 'restaurant', icon: 'images/marker-restaurant.png', html: {
				content: 'Lorem ipsum dolor sit amet.<br /><a href="company-page.html">Read More</a>'
			}},
			{latitude: 40.716818, longitude: -73.983164, group: 'restaurant', icon: 'images/marker-restaurant.png', html: {
				content: 'Lorem ipsum dolor sit amet.<br /><a href="company-page.html">Read More</a>'
			}},
			{latitude: 40.714216, longitude: -73.948317, group: 'restaurant', icon: 'images/marker-restaurant.png', html: {
				content: 'Lorem ipsum dolor sit amet.<br /><a href="company-page.html">Read More</a>'
			}},
			{latitude: 40.739323, longitude: -73.993807, group: 'shop', icon: 'images/marker-shop.png', html: {
				content: 'Lorem ipsum dolor sit amet.<br /><a href="company-page.html">Read More</a>'
			}},
			{latitude: 40.759090, longitude: -73.918619, group: 'shop', icon: 'images/marker-shop.png', html: {
				content: 'Lorem ipsum dolor sit amet.<br /><a href="company-page.html">Read More</a>'
			}},
			{latitude: 40.750898, longitude: -74.004278, group: 'entertainment', icon: 'images/marker-entertainment.png', html: {
				content: 'Lorem ipsum dolor sit amet.<br /><a href="company-page.html">Read More</a>'
			}},
			{latitude: 40.733210, longitude: -74.062300, group: 'realestate', icon: 'images/marker-realestate.png', html: {
				content: 'Lorem ipsum dolor sit amet.<br /><a href="company-page.html">Read More</a>'
			}},
			{latitude: 40.716818, longitude: -73.983164, group: 'sports', icon: 'images/marker-sports.png', html: {
				content: 'Lorem ipsum dolor sit amet.<br /><a href="company-page.html">Read More</a>'
			}},
			{latitude: 40.739323, longitude: -73.993807, group: 'cars', icon: 'images/marker-cars.png', html: {
				content: 'Lorem ipsum dolor sit amet.<br /><a href="company-page.html">Read More</a>'
			}},
			{latitude: 40.759090, longitude: -73.918619, group: 'education', icon: 'images/marker-education.png', html: {
				content: 'Lorem ipsum dolor sit amet.<br /><a href="company-page.html">Read More</a>'
			}},
			{latitude: 40.714216, longitude: -73.948317, group: 'garden', icon: 'images/marker-garden.png', html: {
				content: 'Lorem ipsum dolor sit amet.<br /><a href="company-page.html">Read More</a>'
			}},
			{latitude: 40.750898, longitude: -74.004278, group: 'mechanic', icon: 'images/marker-mechanic.png', html: {
				content: 'Lorem ipsum dolor sit amet.<br /><a href="company-page.html">Read More</a>'
			}},
			{latitude: 40.714216, longitude: -73.948317, group: 'offices', icon: 'images/marker-offices.png', html: {
				content: 'Lorem ipsum dolor sit amet.<br /><a href="company-page.html">Read More</a>'
			}},
			{latitude: 40.733210, longitude: -74.062300, group: 'advertising', icon: 'images/marker-advertising.png', html: {
				content: 'Lorem ipsum dolor sit amet.<br /><a href="company-page.html">Read More</a>'
			}},
			{latitude: 40.714216, longitude: -73.948317, group: 'industry', icon: 'images/marker-industry.png', html: {
				content: 'Lorem ipsum dolor sit amet.<br /><a href="company-page.html">Read More</a>'
			}},
			{latitude: 40.716818, longitude: -73.983164, group: 'postal', icon: 'images/marker-postal.png', html: {
				content: 'Lorem ipsum dolor sit amet.<br /><a href="company-page.html">Read More</a>'
			}},
			{latitude: 40.714216, longitude: -73.948317, group: 'libraries', icon: 'images/marker-libraries.png', html: {
				content: 'Lorem ipsum dolor sit amet.<br /><a href="company-page.html">Read More</a>'
			}}
		]
	});

	/* Hiding all the markers on the map. */
	for (var i in $.goMap.markers) {
		if (this[i] !== 0) {
			$.goMap.showHideMarker(jQuery.goMap.markers[i], false);
		}
	}

	/* Revealing markers from the first group - 'airport' */
	$.goMap.showHideMarkerByGroup('airport', true);

	/* Processing clicks on the tabs under the map. Revealing corresponding to each tab markers. */
	jQuery('#industries-tabs ul li a').click(function(event) {
		/* Preventing default link action */
		event.preventDefault();
		/* Getting current marker group name. Link ID's and marker group names must coincide. */
		var markerGroup = jQuery(this).attr('id');
		/* Changing current active tab. */
		jQuery('#industries-tabs ul li').removeClass('active');
		jQuery(this).parent().addClass('active');
		/* Hiding all the markers on the map. */
		for (var i in jQuery.goMap.markers) {
			if (this[i] !== 0) {
				jQuery.goMap.showHideMarker(jQuery.goMap.markers[i], false);
			}
		}
		/* Revealing markers from the corresponding group. */
		jQuery.goMap.showHideMarkerByGroup(markerGroup, true);
	});

});