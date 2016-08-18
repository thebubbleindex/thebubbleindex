+++
date = "2015-08-04T22:59:42-04:00"
title = "Three.js 3D Contour Plot - Part 2"
categories = ["code", "programming", "three.js", "website"]
+++
Once the Three.js JSON loader fetches the 3D contour data from the server, the code thread enters the modelLoadedCallback function. This function scales the raw JSON data, colors it, and transforms it into a nice looking visual graphic.
<pre><code>function modelLoadedCallback(geometry) {

    //coloring vertex code from https://stemkoski.github.io/Three.js/Graphulus-Function.html
    ///////////////////////////////////////////////
    // calculate vertex colors based on Z values //
    ///////////////////////////////////////////////
    geometry.computeBoundingBox();
    var zMin = geometry.boundingBox.min.z;
    var zMax = geometry.boundingBox.max.z;
    var zRange = zMax - zMin;
    var color, point, face, numberOfSides, vertexIndex;
    // faces are indexed using characters
    var faceIndices = ['a', 'b', 'c', 'd'];
    // first, assign colors to vertices as desired
    for (var i = 0; i &lt; geometry.vertices.length; i++) {
        point = geometry.vertices[i];         
        color = new THREE.Color(0x0000ff);
        var zvalueRel = (zMax - point.z) / zRange;
        if (zvalueRel &gt; 0.97) {
            color.setHSL(1.0, 1.0, 0.6);
        } else {
            color.setHSL(0.7 * zvalueRel, 1.0, 0.6);
        }
        geometry.colors[i] = color; // use this array for convenience
    }
    // copy the colors as necessary to the face's vertexColors array.
    for (var i = 0; i &lt; geometry.faces.length; i++) {
        face = geometry.faces[i];
        numberOfSides = (face instanceof THREE.Face3) ? 3 : 4;
        for (var j = 0; j &lt; numberOfSides; j++) {
            vertexIndex = face[faceIndices[j]];
            face.vertexColors[j] = geometry.colors[vertexIndex];
        }
    }
    ///////////////////////
    // end vertex colors //
    ///////////////////////

    var material = new THREE.MeshBasicMaterial({
        vertexColors: THREE.VertexColors,
        side: THREE.DoubleSide
    });
    var object = new THREE.Mesh(geometry, material);
    var boundingBoxMinimum = [8778.96907216, 21.6082474227, 1.32498653749];
    var boundingBoxMaximum = [10069.0309278, 3030.39175258, 3043.59064093];

    var centerX = (boundingBoxMinimum[0] + boundingBoxMaximum[0]) / 2;
    var centerY = (boundingBoxMinimum[1] + boundingBoxMaximum[1]) / 2;
    var centerZ = (boundingBoxMinimum[2] + boundingBoxMaximum[2]) / 2;

    var max = Math.max(centerX - boundingBoxMinimum[0], boundingBoxMaximum[0] - centerX);
    max = Math.max(max, Math.max(centerY - boundingBoxMinimum[1], boundingBoxMaximum[1] - centerY));
    max = Math.max(max, Math.max(centerZ - boundingBoxMinimum[2], boundingBoxMaximum[2] - centerZ));

    object.position.set(-boundingBoxMinimum[0], -boundingBoxMinimum[1], -boundingBoxMinimum[2]);
    var scale = 10 / max;

    model = new THREE.Object3D();
    model.add(object);
    model.scale.set(scale, scale, scale);
    rotateX = rotateY = 0;
    scene.add(model);
    render();
}</code></pre>
This code contains several parts. The first part takes the vertices of the JSON data and assigns a numeric color value to them -- based on their z-axis "height". Thus,
the greater the Relative Power of The Bubble Index, the closer the color comes to red. Also, the code adds red to the base ("floor") of the graph at values near zero for a better visual contrast. The code for coloring the faces is from <a href="https://stemkoski.github.io/Three.js/Graphulus-Function.html" target="_blank">Graphulus</a>.

Next, a Three.js material is created based on the colored faces. This material is then passed to the Three.js Mesh function along with the raw data. By the power of Three.js the 3D contour object takes shape. With various min and max operations, along with bounding boxes, the 3D contour is placed in the correct location and its size scaled to an appropriate amount for the a good display. For the curious reader, the bounding box is created by <a href="http://www.johannes-raida.de/jnetcad.htm" target="_blank">JNetCAD</a> along with the JSON data.

The last step adds the newly created object to a scene and then the <code>render()</code> function draws the scene.
<pre><code>function render() {
    renderer.render(scene, camera);
}</code></pre>
