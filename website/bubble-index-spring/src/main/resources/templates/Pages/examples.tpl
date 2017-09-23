<!doctype html>
<html class="" lang="en">

	<head>

		<meta charset="utf-8">
<style type="text/css">
.youtube {
    background-position: center;
    background-repeat: no-repeat;
    position: relative;
    display: inline-block;
    overflow: hidden;
    transition: all 200ms ease-out;
    cursor: pointer;
}

.youtube .play {
    background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAERklEQVR4nOWbTWhcVRTHb1IJVoxGtNCNdal2JYJReC6GWuO83PM/59yUS3FRFARdFlwYP1CfiojQWt36sRCUurRIdVFXIn41lAoVdRGrG1M01YpKrWjiYmaSl8ybZJL3cd+YA//NLObd3++eO8x79z5jSq5Gw+8kov0AP8vMR5l1BtBZQM4B8ks75wCdZdYZZj5qLZ4hov2Nht9Z9vhKKSIaB/gI4M4w62KeAO6Mte4lYOq20FxrlqqOibhHmeWbvNC9ZfDX1mLae391aN6limO/gwgvAPJbWeAZuSDingdwXTBw7/0IsyaA/Fkh+KqOkD+YNfHej1QKD+y7iVlOhgLvFqFfNJvNGyuBJ+KDAF8MDd0tgS8y64OlgSdJMsysL4cG7SOHkyQZLhTee7+d2R2rAVy/S+Jd7/32ouBHAP4gNNRGQyTHc/84NhqNywZp5rvjjnnvt21aABFeCQ+RLwAf2hQ8s7sv9OCLk6AHNgQvIrvbfzKCD76g/O6cu7lf/iER/aQGgy448pExZmhdegAPhR9sObFWH1gT3lp7DaA/5bkIgJhZPgsNmz02novj+KqeApj1ubwXWe4kdyeznAgNvTpE/HQmvKqOMeuFogTUVQSRno+iaLRLAJF7uIgL9O4ubgL8aWgB7S44mNX+35YpICUiAvS9sBLkq1WzT+NFffl6AuoiApi6NT37h6sWkBIRZGkQ8YtLgyji6e1mBYTqCEBPG2Naz+0BWQgtoGoRgCzEsd9hAN1X5BfnFZASUfrSAFQNsyZ1FJASUVpHiLinDJG8U2cBZYogkrcNs5waBAGdstbeU9zdqpw0gPwwSAI6VUxHyFlDpOcHUUBBIuYNs14aZAE5RVwyzPr3/0EAEY0TyfGNjBWQvwZ +CTSbehfAH29mrID8bET0+0EUkAd8WYDOmqJ3ecsG30yr9wqRfm6Y+a1BEFDEjHfHvWmY9ck6CygHvBVr8Xhtb4ZE5HZA3y8DvBNA1TjnrmXWf+sioMwZX5V/VHXMGGMMoKdDCxCRvRWBdzKzdHEO+EisilbPyopHYqp6S9UCAsz4iojI7hUDAtyXVQgIDd6KnOoaWNkbI6FaPSuZGyMArsi7MZoloB4zviI/Nhr3X95jltwTRQmoIfgisy5ai+me67OI7fE4nrqjrqfK1t0eby0FPRB6oGVlchL3rgnfrq19RKbVBdhV9IOSwJmfmJi4vi/4ThERitwyCxVAFqydshuCX5awhQ9KtmuIWd8IDZED/nXT77rvVVv6sHRKwjYi91poqP7Dr+Y6JJ1VSZIMA3wkPNy6bX+o8Bcm0sXMdwM8Fxo0A3xORPaWBp6uPXsmbxCRD0NDL0dOANhVCXy6iAjMcjbcrMt3RITKwdMVRdFo+y5yvkL4eWZ+zHt/ZVD4dEVRNGotpst+dZZZH8k86lqn2pIvT/eqrNfn2xuyqYPZ8mv7s8pfn/8Pybm4TIjanscAAAAASUVORK5CYII=") no-repeat center center;
    background-size: 64px 64px;
    position: absolute;
    height: 100%;
    width: 100%;
    opacity: .8;
    filter: alpha(opacity=80);
    transition: all 0.2s ease-out;
}

.youtube .play:hover {
    opacity: 1;
    filter: alpha(opacity=100);
}
</style>
		<title>The Bubble Index - Examples</title>

#{header /}
			<div class="main-menu-wrapper">
				<div class="zone-main-menu zone clearfix">

					<div class="main-menu-container container-24">

						<div class="main-menu block">
							<ul id="sf-menu">
								<li class="empty">
									<div></div>
								</li>
																<li class="first">
									<a href="@{Application.index()}">HOME</a>
									<!--<ul>
										<li class="first">
											<a href="#">Home (Street View)</a>
										</li>
										<li class="">
											<a href="#">Home (Slideshow)</a>
										</li>
										<li class="">
											<a href="#">Home (Header 2)</a>
										</li>
										<li class="">
											<a href="#">Home (Header 3)</a>
										</li>
										<li class="">
											<a href="#">Home (Header 4)</a>
										</li>
										<li class="">
											<a href="#">Home (Header 5)</a>
										</li>
										<li class="">
											<a href="#">Home (Header 6)</a>
										</li>
										<li class="last">
											<a href="#">Home (Header 7)</a>
										</li>
									</ul>-->
								</li>
								<li class="neighbour-left">
									<a href="@{Pages.method()}">ABOUT</a>
								</li>
								<li class="active">
									<a href="@{Pages.examples()}">EXAMPLES</a>
								</li>
								<li class="neighbour-right">
									<a href="@{Pages.links()}">LINKS</a>
								</li>

								<li class="">
									<a href="@{Pages.contact()}">CONTACT</a>
								</li>
								<li class="">
									<a href="@{Application.browse()}">BROWSE</a>
								</li>
								<li class="last">
									<a href="@{Pages.search()}">SEARCH</a>
								</li>
								<li class="empty">
									<div></div>
								</li>
							</ul>
						</div>

					</div>

				</div><!-- end of .zone-main-menu -->
			</div><!-- end of .main-menu-wrapper -->

		</header>

		<section class="section content">

			<div class="content-wrapper">
				<div class="zone-content zone clearfix">

					<div class="project-header-container container-24">

						<div class="project-header block">
							<div class="block-title">
								<h1>Examples</h1>
							</div>
						</div>

					</div>

					<div class="project-body-container container-24">

						<div class="project-body block">
							<div class="project-short-description">
								To help understand The Bubble Index, <a href="https://www.scribd.com/user/224680756/thebubbleindex" class="text-colorful">The Bubble Index: Dow Jones Industrial Average</a> will be referred to in the following examples. Click on the title of each section for a more detailed PDF. By comparing the bubbles of 1929, 1987, 2000, and 2008 with the values of The Bubble Index, the following examples show the remarkable power of The Bubble Index. The entire contour map of the Dow Jones Industrial Average since 1871 can be viewed here: <a href="https://www.scribd.com/user/224680756/thebubbleindex" class="text-colorful">3D Contour: DJIA (1871 - 2015)</a>.
							</div>
							<div class="project-description">
								<p>
									<a href="https://www.scribd.com/user/224680756/thebubbleindex" class="text-colorful">Theoretical LPPL Oscillations</a>
								</p><p>
									The theoretical output of The Bubble Index, if given a purely mathematical LPPL function. 
								</p>
								<p>
									<a href="https://www.scribd.com/user/224680756/thebubbleindex" class="text-colorful">Black Thursday</a>
								</p><p>
									Wealthy tycoons and Wall Street businessmen on Black Thursday, October 24, 1929 witnessed one of the largest declines in the history of the DJIA. With the crash came the start of The Great Depression and economic hardship for millions of families. This was a terrible crisis of capital which precipitated the New Deal policies of FDR who saved capitalism from itself. The Bubble Index: 1,764 Day Window, which peaks on October 17, 1929, shows a rapid increase starting on September 22, 1927. Cautious elites, had they been aware of the growing LPPL oscillations would have anticipated the crash at least a year before the actual event. Imagine having this index in 1928. A trader would have seen the index break record highs on October 1, 1928. In other words, more than a year before the crash of Black Thursday, a quantitative warning sign was predicting disaster.
								</p><p>
									<a href="https://www.scribd.com/user/224680756/thebubbleindex" class="text-colorful">Black Monday</a>
								</p><p>
									Black Monday, October 19, 1987, can be considered a textbook example of an excellent prediction made by The Bubble Index: 1764 Day Window. On March 3, 1983 the index was hovering close to zero. Over the course of five years the index saw a massive and continuous rise to a peak on October 16, 1987. Wall Street firms could have easily avoided losses had they be aware of the LPPL oscillations. The crash of 1987 was an extreme event where bystanders witnessed one of the largest one day losses ever recorded.
								</p><p>
									<a href="https://www.scribd.com/user/224680756/thebubbleindex" class="text-colorful">Late 90's</a>
								</p><p>
									Emotions run high on Wall Street. Herd like behavior is the norm among certain subsets of the elite. And this can be clearly seen by looking at The Bubble Index during the Dot-Com Bubble. The Bubble Index (clearly seen in the 5,040, 10,080, and 20,160 Day Windows) rockets from near zero in the 1980's to a all-time highs in early 2000. Many common-sense people at the time knew that this was a massive bubble; evidenced was provided by some of the most extreme company valuations in recorded history. However, what they did not know was when the music was going to stop. With the help of The Bubble Index, a wise investor would have been out of the market.
								</p><p>
									
<a href="https://www.scribd.com/user/224680756/thebubbleindex" class="text-colorful">The Great Recession</a>
								</p><p>
									As seen in The Bubble Index: DJIA (10,080 and 20,160 Day Windows), the crash appears to be the result of a process with long memory. What's striking is that this increase started in the early 1980's. These indices reached a peak in November 2008. The warning signs for the beginning of the "Great Recession" and the crash of 2008-9 couldn't have been more clear. But don't worry, central bankers know this and visit The Bubble Index website daily.</p><p>
									Predicting Anti-Bubbles
								</p><p>
									Anti-bubbles, also called market corrections, can be measured with The Bubble Index. A perfect analogy relies on the Elliott Wave concept of impulse waves which occur in both the positive and negative directions. For example, in the case of the 1930 correction, The Bubble Index: 1,764 Day Window peaks as the market finishes its collapse. This same type of spike occurs after the 2008-9 crash. Also, recently in stocks such as JCP and SNE, spikes occur just before a rebound in the price.
								</p><p>
									
<a href="https://gateway.ipfs.io/ipns/QmPC1xcsMTDKFTWHLrGEaNTCEdceUf4wWRJhM5P4kDeUsv/page/3dcontourexamples" class="text-colorful">3D Contour </a>
								</p><p>
									All of these examples can be seen in their full expression as a 3D Contour Map <a href="https://lpplmarketwatch.com/3d-contour-examples/" class="text-colorful">here</a>.
								</p>
								<br />
<div class="youtube"
     id="gfy9vJghAz0" 
     style="width: 560px; height: 315px;">
</div>
								<br />
<div class="youtube"
     id="xX4SPU1_CVg" 
     style="width: 560px; height: 315px;">
</div>
							</div>
						</div>

					</div>
				</div><!-- end of .zone-content -->
			</div><!-- end of .content-wrapper -->

		</section>

		#{footer /}

#{scripts /}
<script>
'use strict';
function r(f){/in/.test(document.readyState)?setTimeout('r('+f+')',9):f()}
r(function(){
    if (!document.getElementsByClassName) {
        // IE8 support
        var getElementsByClassName = function(node, classname) {
            var a = [];
            var re = new RegExp('(^| )'+classname+'( |$)');
            var els = node.getElementsByTagName("*");
            for(var i=0,j=els.length; i<j; i++)
                if(re.test(els[i].className))a.push(els[i]);
            return a;
        }
        var videos = getElementsByClassName(document.body,"youtube");
    } else {
        var videos = document.getElementsByClassName("youtube");
    }

    var nb_videos = videos.length;
    for (var i=0; i<nb_videos; i++) {
        // Based on the YouTube ID, we can easily find the thumbnail image
        videos[i].style.backgroundImage = 'url(https://i.ytimg.com/vi/' + videos[i].id + '/sddefault.jpg)';

        // Overlay the Play icon to make it look like a video player
        var play = document.createElement("div");
        play.setAttribute("class","play");
        videos[i].appendChild(play);

        videos[i].onclick = function() {
            // Create an iFrame with autoplay set to true
            var iframe = document.createElement("iframe");
            var iframe_url = "https://www.youtube.com/embed/" + this.id + "?autoplay=1&autohide=1";
            if (this.getAttribute("data-params")) iframe_url+='&'+this.getAttribute("data-params");
            iframe.setAttribute("src",iframe_url);
            iframe.setAttribute("frameborder",'0');

            // The height and width of the iFrame should be the same as parent
            iframe.style.width  = this.style.width;
            iframe.style.height = this.style.height;

            // Replace the YouTube thumbnail with YouTube Player
            this.parentNode.replaceChild(iframe, this);
        }
    }
});
</script>

	</body>

</html>
