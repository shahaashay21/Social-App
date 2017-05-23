var bcrypt = require('bcrypt-nodejs');

//Files
var sendEmail = require('./email');
var db = require('./data_model');

// Variables
var Users = db.Users;
var Friend = db.Friend;
var IP = "http://54.183.170.253:3000";
var CONFIRMATION_URL = "/user/confirm";

Users.sync();



	

exports.getUsers = function(req, res){
	console.log("Class getUsers");

	var id = req.param('id');
	var search = req.param('search');



	//Users.findAll({where: {first_name:{$like: '%' + search + '%'} }}).then(function(userData){
	/*Users.findAll().then(function(userData){
		console.log(JSON.stringify(userData, 0, null));
		if(userData){
			console.log(userData.dataValues);
			res.json(userData.dataValues);
		}else{
			res.send("error");
		}
	});*/

	console.log("Search String: " + search);

	Users.findAll({where: {first_name:{$like: '%' + search + '%'} }}).then(function(userData){
		
		
		if(userData){
			console.log("userData Length: " + userData.length);
			console.log(JSON.stringify(userData, 0, null));
			//console.log(userData.dataValues);
			res.json(userData.dataValues);
		}else{
			console.log();
			res.send("error");
		}
	}).catch(function(error){
		console.log(JSON.stringify(error, 0, null));
	});
}
