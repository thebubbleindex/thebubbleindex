$(document).ready(function() {
// jQUERY is required!
	var mapi = new MastodonApi({
		target_selector  : '#myTimeline'
		,instance_uri    : 'https://mastodon.social'
		,access_token    : 'c951ba9bf4537b1bd3b3631a8e0db20ba902f9baffceb26c3cc10ad2dfd66c6e'
		,account_id      : '187876'
		// optional parameters
		// ===================
		// - status max
		//,toots_limit     : 5
		// - if you are using font-awesome:
		//,pic_icon        : '<i class="fa fa-picture-o"></i>'
		// or a picture
		//,pic_icon        : '<img src="mypicicon.gif" />'
	});
});