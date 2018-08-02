var mysql_dbc = require('./db/db_con')();
var connection = mysql_dbc.init();
mysql_dbc.test_open(connection);

let getUserData = (facebook_id)   => {
  return new Promise((resolve, reject) => {
    var q = 'SELECT * FROM user WHERE facebook_id=' + facebook_id + ';'
    connection.query(q, function(err, result){
      if(err){
        console.error(err)
      } else {
        if(result.length == 0) {
          resolve({"messages" : "None"})
        } else {
          resolve(result[0])
        }
      }
    })
  })
}

exports.getUserData = async function getUser(facebook_id) {
  var result;
  await getUserData(facebook_id).then(function(data) {
    result = data;
  })
  return {'result' : result};
}


let postUserData = (facebook_id, name, is_love, is_boring, is_need) => {
  return new Promise((resolve, reject) => {
    var q = 'insert into user values(\"' + facebook_id + '\", \"' + name +'\",\"' + is_love +'\",\"' + is_boring +'\", \"' + is_need +'\"' + ')'
    connection.query(q, function(err, result) {
      if(err){
        console.error(err);
        resolve({"Messages" : "ERROR" });
      } else {
        console.log("Create Success");
        resolve({"Messages" : "GOOD" });
      }
    })
  })
};

exports.postUserData = async function postUser(facebook_id, name, is_love, is_boring, is_need) {
  var result;
  await postUserData(facebook_id, name, is_love, is_boring, is_need).then(function(data) {
    result = data;
  })
  return {'result' : result };
}

let setUserData = (facebook_id, value , bool) => {
  return new Promise((resolve, reject) => {
    var q = 'update user set ' + value + '=' + bool + ' WHERE facebook_id='+ facebook_id +';'
    connection.query(q, function(err, result) {
      if(err){
        console.error(err);
        resolve({"Messages" : "ERROR" });
      } else {
        console.log("Create Success");
        resolve({"Messages" : "GOOD" });
      }
    })
  })
};

exports.setUserData = async function setUser(facebook_id, value, bool) {
  var result;
  await setUserData(facebook_id, value, bool).then(function(data) {
    result = data;
  })
  return {'result' : result};
}


let postUserProfile = (facebook_id, name, sex, age, height, personal, alcohol) => {
  return new Promise((resolve, reject) => {
    var q = 'insert into profile values(\"' + facebook_id + '\", \"' + name +'\",\"' + sex +'\",\"' + age +'\", \"' + height + '\", \"' + personal + '\", \"' + alcohol +'\"' + ')'
    console.log(q);
    connection.query(q, function(err, result) {

      if(err){
        console.error(err);
        resolve({"Messages" : "ERROR" });
      } else {
        console.log("Create Success");
        resolve({"Messages" : "GOOD" });
      }
    })
  })
};


exports.postUserProfile = async function postProfile(facebook_id,  name, sex, age, height, personal, alcohol) {
  var result;
  await postUserProfile(facebook_id, name, sex, age, height, personal, alcohol).then(function(data) {
    result = data;
  })
  return {'result' : result };
}


let getUserProfile = (facebook_id)   => {
  return new Promise((resolve, reject) => {
    var q = 'SELECT * FROM profile WHERE facebook_id=' + facebook_id + ';'
    connection.query(q, function(err, result){
      if(err){
        console.error(err)
      } else {
        if(result.length == 0) {
          resolve({"messages" : "None"})
        } else {
          resolve(result[0])
        }
      }
    })
  })
}

exports.getUserProfile = async function getUser(facebook_id) {
  var result;
  await getUserProfile(facebook_id).then(function(data) {
    result = data;

  })
  return {'result' : result};
}

let postUserIdeal = (facebook_id, sex, age, height, personal, alcohol) => {
  return new Promise((resolve, reject) => {
    var q = 'insert into ideal values(\"' + facebook_id + '\", \"' + sex +'\",\"' + age +'\", \"' + height + '\", \"' + personal + '\", \"' + alcohol +'\"' + ')'
    console.log(q);
    connection.query(q, function(err, result) {

      if(err){
        console.error(err);
        resolve({"Messages" : "ERROR" });
      } else {
        console.log("Create Success");
        resolve({"Messages" : "GOOD" });
      }
    })
  })
};


exports.postUserIdeal = async function postIdeal(facebook_id, sex, age, height, personal, alcohol) {
  var result;
  await postUserIdeal(facebook_id, sex, age, height, personal, alcohol).then(function(data) {
    result = data;
  })
  return {'result' : result };
}



let getUserIdeal = (facebook_id)   => {
  return new Promise((resolve, reject) => {
    var q = 'SELECT * FROM ideal WHERE facebook_id=' + facebook_id + ';'
    connection.query(q, function(err, result){
      if(err){
        console.error(err)
      } else {
        if(result.length == 0) {
          resolve({"messages" : "None"})
        } else {
          resolve(result[0])
        }
      }
    })
  })
}

exports.getUserIdeal = async function getUser(facebook_id) {
  var result;
  await getUserIdeal(facebook_id).then(function(data) {
    result = data;
  })
  return {'result' : result};
}