// Setup basic express server
var express = require('express');
var app = express();
var path = require('path');
var server = require('http').createServer(app);
var io = require('socket.io').listen(server);
var port = 8070;

server.listen(port, () => {
  console.log('Server listening at port %d', port);
});

// Routing
app.use(express.static(path.join(__dirname, 'public')));

// Chatroom

var numUsers = 0;

io.on('connection', (socket) => {
  var addedUser = false;
  var room_name ="";

  // when the client emits 'new message', this listens and executes
  socket.on('new message', function(data, roomname) {
    console.log("new")
    // we tell the client to execute 'new message'
    socket.broadcast.to(roomname).emit('new message', {
      username: socket.username,
      message: data
    });
  });

  // when the client emits 'add user', this listens and executes
  socket.on('add user', function(username, roomname){
    if (addedUser) return;

    // we store the username in the socket session for this client
    socket.username = username;
    room_name = roomname;
    console.log(username + roomname)

    socket.join(roomname);

    ++numUsers;
    addedUser = true;
    // socket.emit('login', {
    //   numUsers: numUsers
    // });
    // echo globally (all clients) that a person has connected
    socket.to(roomname).emit('user joined', {
      username: socket.username,
      numUsers: numUsers
    });

  });

  // when the client emits 'typing', we broadcast it to others
  socket.on('typing', (roomname) => {
    console.log("typing")
    socket.broadcast.to(roomname).emit('typing', {
      username: socket.username
    });
  });

  // when the client emits 'stop typing', we broadcast it to others
  socket.on('stop typing', (roomname) => {
    socket.broadcast.to(roomname).emit('stop typing', {
      username: socket.username
    });
  });

  // when the user disconnects.. perform this
  socket.on('disconnect', () => {
    if (addedUser) {
      --numUsers;
      // echo globally that this client has left
      console.log("disconnect")

      socket.broadcast.to(room_name).emit('user left', {
        username: socket.username,
        numUsers: numUsers
      });
    }
  });

  socket.on('get users',(roomname)=>{
    socket.emit('get users',{
      numUsers: numUsers
    })
  })

});
