+++
date = "2015-07-31T10:23:58-04:00"
title = "Three.js 3D Contour Plot - Part 1"
categories = ["code", "programming", "three.js", "website"]
+++
Three.js is a powerful tool for visualizing a wide variety of objects including mathematical data. The JavaScript based API greatly simplifies the creation and manipulation of WebGL graphic objects. This post is the first in a series which describe the creation of <a href="https://lpplmarketwatch.com/3d-contour-examples/" target="_blank">The Bubble Index 3D Contours</a>. The first step is to check if the user's device supports WebGL. The <code>Detector</code> variable comes from <a href="http://alteredqualia.com/" target="_blank">AlteredQualia</a> and <a href="http://mrdoob.com/" target="_blank">Mr.Doob</a>. If the device supports WebGL, it is time to initialize variables:
<pre><code>if ( ! Detector.webgl ) Detector.addGetWebGLMessage();
var renderer; 
var texture; 
var scene;
var camera;
var controls;
var axes;
var model; 
var container;
var rotateX = 0;
var rotateY = 0;

init();</code></pre>
The first important code is called by the <code>init()</code> function. This code creates the director's camera; the user is like a film director. The camera is the amount, perspective, and angle of the 3D scene displayed on the screen. The camera is controlled by a control object which allows the user to interact via the keyboard and mouse. To display anything, a scene needs to be created. Objects need to be added to the scene in order for them to become visible. The renderer does all the work to actually turn the code objects, numbers, and interactions into colors and shapes displayed on the screen via the device's graphics card and WebGL.

In order to draw a 3D contour, a set of geometric data needs to be created. The data defines vertices and faces of polygons which tell Three.js how to draw a shape. The Bubble Index 3D Contour displays parallel time-series by first turning them into a set of X,Y,Z coordinates. It then sends these coordinates to <a href="http://3dfmaps.com/index.htm" target="_blank">3DFieldPro</a> which outputs a dxf AutoCAD file. The dxf file is then converted into a Three.js JSON 3D data structure via the <a href="http://www.johannes-raida.de/jnetcad.htm" target="_blank">JNetCAD</a> program. Thus, with the JSON data created, the <code>THREE.JSONLoader()</code> provides a convenient way to load and display the 3D contour data.

In future posts, the <code>addAxesAndText()</code> and <code>addTimeSeries()</code> functions will be discussed. They simply add the axes and the price data time-series to the display.

Finally, the renderer needs to be added to the HTML DOM. A window listener function is created to provide a means to redraw the display in the case of a resize of the browser.
<pre><code>function init() {
    camera = new THREE.PerspectiveCamera(50, window.innerWidth / window.innerHeight, 0.1, 1000);
    camera.position.z = 35;
    camera.up.set( 0, 0, 1 );
    controls = new THREE.OrbitControls( camera );
    controls.damping = 0.5;
    controls.addEventListener( 'change', render );
    scene = new THREE.Scene();
    renderer = new THREE.WebGLRenderer( {  antialias: true   } );
    renderer.setPixelRatio( window.devicePixelRatio );
    renderer.setSize( window.innerWidth, window.innerHeight );
    renderer.setClearColor(0);
    var loader = new THREE.JSONLoader();
    loader.load('AA.BA.json', modelLoadedCallback);
    addAxesAndText();
    addTimeSeries();
    container = document.getElementById( 'container' );
    container.appendChild( renderer.domElement );
    window.addEventListener( 'resize', onWindowResize, false );
    animate();
 }</code></pre>
