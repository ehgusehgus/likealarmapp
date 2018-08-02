const mongoose = require('mongoose');


var recipeSchema = new Schema({ index : NUMBER, descript : String,
                        editer : String}, { noId: true});

var contentSchema = new mongoose.Schema({
  keyword : { type : String, required : true },
  creater : { type : String, required : true },
  category : { type : String, enum : ['KOR', 'WEST', 'CHN', 'JPN', 'ETC']},
  recipes : [recipeSchema],
  createdAt: {type: Date, default: Date.now },
  updatedAt: {type: Date, default: Date.now }
})

module.exports =  mongoose.model('Content', contentSchema);
