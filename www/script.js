$.getJSON("result.json", function(json) {
  console.log(json); // this will show the info it in firebug console
  let mymap = L.map('mapid').setView([45.755753, 4.8387673], 12);
  L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoiZG9tbWFydGoiLCJhIjoiY2pvNXFsZHBzMDl0azN2b2F0aG4wMmg2MyJ9.wSgUUIsVOaK8xlHHXrBuOQ', {
      // attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
      maxZoom: 18,
      id: 'mapbox.streets',
      accessToken: 'pk.eyJ1IjoiZG9tbWFydGoiLCJhIjoiY2pvNXFsZHBzMDl0azN2b2F0aG4wMmg2MyJ9.wSgUUIsVOaK8xlHHXrBuOQ'
  }).addTo(mymap);

  // Récupération des tournées
  const tournees = json.tournees;
  const points = tournees[0].clients;
  console.log(points);

  // Initialisation du tableau des points
  let latlngs = [];

  // Ajout des points avec des marqueurs sur la map
  points.forEach((point, i) => {
    let latlng = [point.latitude, point.longitude];
    if (i === 0) {
      var myIcon = L.icon({
        iconUrl: 'depot.png',
        iconSize: [38, 45],
      });
      L.marker(latlng, {
        title: "Dépôt",
        icon: myIcon
      }).addTo(mymap);
    } else {
      L.marker(latlng, {
        title: "Client " + i
      }).addTo(mymap);
    }
    latlngs.push(latlng);
  });
  latlngs.push([points[0].latitude, points[0].longitude]);

  // create a red polyline from an array of LatLng points
  var polyline = L.polyline(latlngs, {color: 'red'}).addTo(mymap);
  // zoom the map to the polyline
  mymap.fitBounds(polyline.getBounds());

});

