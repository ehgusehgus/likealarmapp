var express = require('express');
var router = express.Router();
var db = require('../database');


const asyncMiddleware = fn => (req, res, next) => {
  Promise.resolve(fn(req, res, next))
    .catch(next);
  }

/* GET users listing. */
router.get('/', asyncMiddleware(async function(req, res, next) {
  var facebook_id = req.headers.facebook_id;
  res.json(await db.getUserData(facebook_id))
}));

router.post('/create', asyncMiddleware(async function(req, res, next) {
  var facebook_id = req.body.facebook_id
  var nickname = req.body.nickname
  var is_love = req.body.is_love
  var is_boring = req.body.is_boring
  var is_need = req.body.is_need

  res.json(await db.postUserData(facebook_id, nickname,is_love, is_boring, is_need));
}));

/* GET users listing. */
router.post('/is_love', asyncMiddleware(async function(req, res, next) {
  var facebook_id = req.body.facebook_id;
  var bool = req.body.is_love;
  res.json(await db.setUserData(facebook_id, "is_love", bool))
}));

/* GET users listing. */
router.post('/is_boring', asyncMiddleware(async function(req, res, next) {
  var facebook_id = req.body.facebook_id;
  var bool = req.body.is_boring;
  res.json(await db.setUserData(facebook_id, "is_boring", bool))
}));

/* GET users listing. */
router.post('/is_need', asyncMiddleware(async function(req, res, next) {
  var facebook_id = req.body.facebook_id;
  var bool = req.body.is_need;
  res.json(await db.setUserData(facebook_id, "is_need", bool))
}));

module.exports = router;
