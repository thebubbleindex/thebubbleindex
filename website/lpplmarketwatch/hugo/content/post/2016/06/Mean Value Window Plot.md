+++
date = "2016-06-19T22:48:45-04:00"
title = "Mean Value vs. Window - Plot"
categories = ["bubble", "index", "summary"]
+++
There are now 17,330 individual timeseries monitored by The Bubble Index; each with a full range of windows. The windows range from 52 to 35,500 days. Below is plotted the natural log of the mean value (y-axis) versus the window (x-axis). Each window's mean value represents the mean calculated over the entire historical record for all 17,330 timeseries.

<a href="../../../../2016/06/log.png"><img src="../../../../2016/06/logsmall.png" alt="Natural Log" /></a>

The curve of best fit displayed above is:

<code>ln(y) = a + b * ln(x)</code>, where y represents the mean value outputted by The Bubble Index software and x represents the window.

a = -9.746393

b = 3.613444

Taking this function and rearranging the terms produces:

<code>y = exp(a) * x ^ b</code>, where y represents the mean value outputted by The Bubble Index software and x represents the window. (constants a and b are as above).

<a href="../../../../2016/06/exp.png"><img src="../../../../2016/06/expsmall.png" alt="Exponential" /></a>

To incorporate this information into the plots, the mean value for a given window will have Relative Power = 50.0
In other words, the output to the previous equation, which calculates the mean value for a given window, will be displayed on the website's plots as 50.0

The next step will be to calculate an empirical distribution for each window.

Download the pdfs:

<a href="https://cdn.thebubbleindex.com/Blog/log.pdf">Natural Log Plot</a>

<a href="https://cdn.thebubbleindex.com/Blog/exp.pdf">Exponential Plot</a>
