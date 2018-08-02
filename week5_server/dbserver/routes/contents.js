var express = require('express');
var router = express.Router();
var db = require('../database')

const asyncMiddleware = fn => (req, res, next) => {
  Promise.resolve(fn(req, res, next))
    .catch(next);
};

router.get('/', asyncMiddleware(async function(req, res, next) {
  var keyword = decodeURIComponent(req.headers.keyword);
  res.json(await db.getContentAndRecipes(keyword));
}));

router.get('/isInterest', asyncMiddleware(async function(req, res, next) {
  var keyword = decodeURIComponent(req.headers.keyword);
  var facebook_id = req.headers.facebook_id;

  res.json(await db.isInterest(keyword, facebook_id));
}))


router.post('/create', asyncMiddleware(async function(req, res, next){
  var keyword = req.body.keyword;
  var ingredient = req.body.ingredient;
  var creater = req.body.creater;
  var category_con = req.body.category_con;
  var category_cooking = req.body.category_cooking
  var tags = req.body.tags;
  var image = req.body.image;
  var recipes = JSON.parse(req.body.recipes);

  if(image != ""){
    require("fs").writeFile("public/images/" + keyword + ".jpg", image, 'base64', function(err) {
      console.log(err);
    })
  }
  for(var i in recipes) {
    var image1 = recipes[i].image;
    var j = parseInt(i)+parseInt(1);
    if(image1 == ""){
      continue;
    } else {
      require("fs").writeFile("public/images/" + keyword + "_" + j + ".jpg", image1, 'base64', function(err) {
        console.log(err);
      })
    }
  }
  tags = tags.replace(' ', '').split('$');

  res.json(await db.postContentAndRecipes(false, keyword,ingredient, creater, category_con, category_cooking, recipes, tags));
}))

router.post('/edit', asyncMiddleware(async function(req, res, next) {
    var keyword = req.body.keyword;
    var ingredient = req.body.ingredient;
    var creater = req.body.creater;
    var category_con = req.body.category_con;
    var category_cooking = req.body.category_cooking
    var tags = req.body.tags;
    var image = req.body.image;
    var recipes = JSON.parse(req.body.recipes);

    if(image != ""){
      require("fs").writeFile("public/images/" + keyword + "_edit.jpg", image, 'base64', function(err) {
        console.log(err);
      })
    }
    for(var i in recipes) {
      var j = parseInt(i)+parseInt(1);
      var image1 = recipes[i].image;
      if(image1 == ""){
        continue;
      } else {
        require("fs").writeFile("public/images/" + keyword + "_" + j + "_edit.jpg", image1, 'base64', function(err) {
          console.log(err);
        })
      }
    }
    tags = tags.replace(' ', '').split('$');

    var ret = await db.postContentAndRecipes(true, keyword,ingredient, creater, category_con, category_cooking, recipes, tags);

    setTimeout(async function() {
      console.log("IT THE TIME!!! IT ILL SELECT UPDATE OR NOT WHAT IS THE RESULT.. LET'S SEE!!!");
      var ck = await db.checkNoticeAgree(keyword);
      if(ck){
        await db.updateContent(keyword);
        await db.updateRecipes(keyword);
        await db.updateTags(keyword);
      } else {
        await db.doNotUpdate(keyword);
      }
    }, 60000);
    res.json(ret);
}))

router.use(function(err, req, res, next) {
  console.error(err);
  res.status(500).json({ message: 'an error occured'})
})


module.exports = router;
