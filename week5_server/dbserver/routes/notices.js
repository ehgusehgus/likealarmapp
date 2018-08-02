var express = require('express');
var router = express.Router();
var db = require('../database');

const asyncMiddleware = fn => (req, res, next) => {
  Promise.resolve(fn(req, res, next))
    .catch(next);
};

router.get('/', asyncMiddleware(async function(req, res, next){
  res.json(await db.getNoticeList());
}))

router.post('/vote', asyncMiddleware(async function(req, res, next) {
  var keyword = req.body.keyword;
  var facebook_id = req.body.facebook_id;
  var agree = req.body.agree;
  console.log(agree)
  res.json(await db.postVote(keyword, facebook_id, agree));
}));

router.get('/check', asyncMiddleware(async function(req, res, next){
  var keyword = decodeURIComponent(req.headers.keyword);
  var facebook_id = req.headers.facebook_id;
  res.json(await db.checkVote(keyword,facebook_id));
}))


router.get('/detail', asyncMiddleware(async function(req, res, next) {
  var keyword = decodeURIComponent(req.headers.keyword);
  var result = await db.getNoticeDetail(keyword);
  var content = result.content;
  var content_edit = result.content_edit;
  var recipes = result.recipes;
  var recipes_edit = result.recipes_edit;
  var tags = result.tag;
  var tags_edit = result.tag_edit;
  var ret = {};

  ret['updated_at'] = { 'check' : true, 'before' : content.updated_at, 'after' : content_edit.updated_at }

  if(content.creater != content_edit.creater){
    ret['creater'] = { 'check' : true, 'before' : content.nickname, 'after' : content_edit.nickname }
  } else {
    ret['creater'] = { 'check' : false, 'before' : content.nickname }
  }

  if(content.ingredient != content_edit.ingredient) {
    ret['ingredient'] = { 'check' : true, 'before' : content.ingredient, 'after' : content_edit.ingredient }
  } else {
    ret['ingredient'] = { 'check' : false, 'before' : content.ingredient }
  }

  if(content.category_con != content_edit.category_con) {
    ret['category_con'] = { 'check' : true, 'before' : content.category_con, 'after' : content_edit.category_con }
  } else {
    ret['category_con'] = { 'check' : false, 'before' : content.category_con }
  }

  if(content.category_cooking != content_edit.category_cooking){
    ret['category_cooking'] = { 'check' : true, 'before' : content.category_cooking, 'after' : content_edit.category_cooking }
  } else {
    ret['category_cooking'] = { 'check' : false, 'before' : content.category_cooking }
  }

  var ck = false;
  for(var i=0;i<Math.max(recipes.length, recipes_edit.length);i++){
    if(recipes[i] == undefined){
      ck = true
      break;
    }
    if(recipes_edit[i] == undefined) {
      ck = true
      break;
    }
    if(recipes[i].descript != recipes_edit[i].descript) {
      ck = true
      break;
    }
  }
  if(ck){
    ret['recipes'] = { 'check' : ck, 'before' : recipes, 'after' : recipes_edit };
  } else {
    ret['recipes'] = { 'check' : ck, 'before' : recipes };
  }
  ret['tags'] = {'check' : true, 'before' : tags, 'after' : tags_edit }
  res.json(ret);
}))

module.exports = router;
