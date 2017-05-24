var bcrypt = require('bcrypt-nodejs');

//Files
var sendEmail = require('./email');
var db = require('./data_model');

// Variables
var Users = db.Users;
var Friend = db.Friend;
var IP = "http://54.183.170.253:3000";



	

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
			console.log(JSON.stringify(userData, null, 1));
			res.json(userData);
			//res.send({"userData": userData});
		}else{
			console.log();
			res.send("error");
		}
	}).catch(function(error){
		console.log(JSON.stringify(error, 0, null));
	});
}

//Get friend information
exports.profileFriendInfo = function(req, res){
	console.log("Class friend and function profileFriendInfo");

	var user_id = req.param('user_id');
	var friend_id = req.param('friend_id');

	Friend.findOne({
		where: {
			user_idd: user_id,
			friend_id: friend_id
		}
	}).then(function(friendInfo){
		if(friendInfo){
			console.log(friendInfo.dataValues);
			res.json(friendInfo.dataValues);
		}else{
			res.send("no friend");
		}
	})
}


