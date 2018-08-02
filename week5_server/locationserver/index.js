// Setup basic express server
var express = require('express');
var app = express();
var path = require('path');
var server = require('http').createServer(app);
var io = require('socket.io').listen(server);
var port = process.env.PORT || 8080;

server.listen(port, () => {
  console.log('Server listening at port %d', port);
});

// Routing
app.use(express.static(path.join(__dirname, 'public')));
// Chatroom

io.on('connection', (socket) => {
	console.log("connected")
  // when the client emits 'new message', this listens and executes
  socket.on('new location', function(longitude, latitude, altitude, facebook_id) {
  	console.log("new location")
    // we tell the client to execute 'new message'
    socket.broadcast.emit('new location', {
      username: socket.username,
      longitude: longitude,
      latitude: latitude,
      altitude: altitude,
      facebook_id: facebook_id
    });
  	console.log("new location")

  });
});
