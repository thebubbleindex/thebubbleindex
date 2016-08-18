function displayLPPL() {
document.getElementById("plotarea").innerHTML = "";
var a = parseFloat(document.getElementById("A").value); 
var b = parseFloat(document.getElementById("B").value); 
var c = parseFloat(document.getElementById("C").value); 
var omega = parseFloat(document.getElementById("OMEGA").value); 
var m = parseFloat(document.getElementById("M").value); 
var pc = parseFloat(document.getElementById("TC").value);

var lppl = document.getElementById("lppl").checked;  


var data = [],
    n = 10000,
	increment = 0.01,
	startingValue = 1.0;
if (lppl) {
for (var k = 0; k < n; k++) {
	xvalue = pc - k * increment;
    data.push({x: xvalue, y: a - b * Math.pow(increment * k + startingValue, m) + c * Math.pow(increment * k + startingValue, m) * Math.cos(omega * Math.log(increment * k + startingValue))});
}
}
else {
for (var k = 0; k < n; k++) {
	xvalue = k * increment - pc;
    data.push({x: xvalue, y: a - b * Math.pow(increment * k + startingValue, m) + c * Math.pow(increment * k + startingValue, m) * Math.cos(omega * Math.log(increment * k + startingValue))});
}
}
var margin = {top: 20, right: 20, bottom: 30, left: 50},
	aspect = 1.92,
	areawidth=document.getElementById("plotarea").clientWidth,    
	width = areawidth - margin.left - margin.right,
    height = Math.round(areawidth / aspect) - margin.top - margin.bottom;

var x = d3.scale.linear()
    .range([0, width]);

var y = d3.scale.linear()
    .range([height, 0]);

var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom");

var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left");

var line = d3.svg.line()
    .x(function(d) { return x(d.x); })
    .y(function(d) { return y(d.y); })
	.interpolate("basis");

var svg = d3.select("#plotarea").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")")
    .attr("id", "svgMain");

  x.domain(d3.extent(data, function(d) { return d.x; }));
  y.domain(d3.extent(data, function(d) { return d.y; }));

  svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis);

  svg.append("g")
      .attr("class", "y axis")
      .call(yAxis)
    .append("text")
      .attr("transform", "rotate(-90)")
      .attr("y", 6)
      .attr("dy", ".71em")
      .style("text-anchor", "end")
      .text("y-axis");

  svg.append("path")
      .datum(data)
      .attr("class", "line")
      .attr("d", line);

document.getElementById("plotarea").scrollIntoView();
}
