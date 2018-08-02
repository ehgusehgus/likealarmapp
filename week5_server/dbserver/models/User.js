const mongoose = require('mongoose');

var userSchema = new mongoose.Schema({
  facebook_id : { type : String, required : true },
  nickname : { type : String, required : true },
},
  {
    timestamps : true
  })

module.exports =  mongoose.model('User', userSchema);
