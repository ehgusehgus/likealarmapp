var express = require('express');
var router = express.Router();
var db = require('../database')

const asyncMiddleware = fn => (req, res, next) => {
  Promise.resolve(fn(req, res, next))
    .catch(next);
  }

router.get('/', asyncMiddleware(async function(req, res, next) {
  var facebook_id = req.headers.facebook_id;
  console.log("ideal")	
  res.json(await db.getUserIdeal(facebook_id));
}));

router.post('/create',asyncMiddleware(async function(req, res, next){
  var facebook_id = req.body.facebook_id;
  var sex = req.body.sex;
  var age = req.body.age;
  var height = req.body.height;
  var personal = req.body.personal;
  var alcohol = req.body.alcohol;
  console.log(facebook_id+sex+age+height+alcohol+personal);
  res.json(await db.postUserIdeal(facebook_id, sex, age, height, personal, alcohol))

}));
module.exports = router;
