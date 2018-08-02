var express = require('express');
var router = express.Router();
var db = require('../database')
/* GET home page. */

const asyncMiddleware = fn => (req, res, next) => {
  Promise.resolve(fn(req, res, next))
    .catch(next);
  }

router.get('/', asyncMiddleware(async function(req, res, next) {
  res.json(await db.getContentInMain(10));
}));

router.get('/category/country', asyncMiddleware(async function(req, res, next) {
  var category = req.headers.category;
  res.json(await db.getCountryCategoryContent(category));
}));
router.get('/category/cooking', asyncMiddleware(async function(req, res, next) {
  var category = decodeURIComponent(req.headers.category);
  res.json(await db.getCookingCategoryContent(category));
}));

router.post('/interest/on', asyncMiddleware(async function(req, res, next) {
  var keyword = req.body.keyword;
  var facebook_id = req.body.facebook_id;
  console.log(keyword)
  res.json(await db.onInterest(keyword, facebook_id));
}));

router.post('/interest/off', asyncMiddleware(async function(req, res, next) {
  var keyword = req.body.keyword;
  var facebook_id = req.body.facebook_id;
  res.json(await db.offInterest(keyword, facebook_id));
}));

router.get('/search', asyncMiddleware(async function(req, res, next) {
  var keyword = decodeURIComponent(req.headers.keyword);
  res.json(await db.Search(keyword));
}))

module.exports = router;
