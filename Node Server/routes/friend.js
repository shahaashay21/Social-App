var bcrypt = require('bcrypt-nodejs');
var Sequelize = require('sequelize');

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
			user_id: user_id,
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

//Sent the request to the friend
exports.friendSendRequest = function(req, res){
	console.log("Class friend and function friendSendRequest");

	var user_id = req.param('user_id');
	var friend_id = req.param('friend_id');

	var setFriendRequestValue = {};
	setFriendRequestValue['user_id'] = user_id;
	setFriendRequestValue['friend_id'] = friend_id;
	setFriendRequestValue['request'] = "1";

	Friend.findOrCreate({
		where: {
			user_id: user_id,
			friend_id: friend_id
		}, defaults: setFriendRequestValue
	}).spread(function(user,created){
		if(created){
			res.send("success");
		}else{
			res.send("error");
		}
	})
}

//Reject friend reuqest
exports.friendRejectRequest = function(req, res){
	console.log("Class friend and function friendRejectRequest");

	var user_id = req.param('user_id');
	var friend_id = req.param('friend_id');

	Friend.destroy({
		where: {
			user_id: user_id,
			friend_id: friend_id
		}
	}).then(function(del){
		console.log(del);
		if(del){
			res.send("success");
		}else{
			res.send("error");
		}
	})
}

//Get all sent requests
exports.friendRequestSentInfo = function(req, res){
	console.log("Class friend and function friendRequestSentInfo");

	var user_id = req.param('user_id');

	var q = "select u.user_id, u.user_name, u.email, u.avatar, u.first_name, u.last_name from friend as f left join users as u ON f.friend_id = u.user_id where f.user_id="+user_id+" and f.request='1'";
	

	db.sequelize.query(q).spread((results, metadata) => {
		res.json(results);
	});

}

//Get all frient request
exports.friendRequestInfo = function(req, res){
	console.log("Class friend and function friendRequestInfo");

	var user_id = req.param('user_id');

	var q = "select u.user_id, u.user_name, u.email, u.avatar, u.first_name, u.last_name from friend as f left join users as u ON f.friend_id = u.user_id where f.friend_id="+user_id+" and f.request='1'";

	db.sequelize.query(q).spread((results, metadata) => {
		res.json(results);
	});
}

