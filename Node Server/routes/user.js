//Library
var bcrypt = require('bcrypt-nodejs');

//Files
var sendEmail = require('./email');
var db = require('./data_model');

// Variables
var Users = db.Users;
var IP = "http://54.183.170.253:3000";
var CONFIRMATION_URL = "/user/confirm";

exports.registerUser = function(req, res){

	console.log("Class users and function registerUser");

	var first_name = req.param('first_name');
	var last_name = req.param('last_name');
	var user_name = req.param('user_name');
	var email = req.param('email');
	var password = req.param('password');


//	GENERATE RANDOM STRING USING CRYPTO
	// require('crypto').randomBytes(48, function(err, buffer) {
		// var token = buffer.toString('hex');
		var token = Math.floor(10000 + Math.random() * 90000);
	//	ENCRYPT PASSWORD USING BCRYPT
		bcrypt.hash(password, null, null, function(err, hash) {
	//		GET NEXT USER ID FROM MYSQL FUNCTION

			var userValue = {};
			userValue['first_name'] = first_name;
			userValue['last_name'] = last_name;
			userValue['user_name'] = user_name;
			userValue['email'] = email;
			userValue['password'] = hash;
			userValue['verification_code'] = token;
			userValue['avatar'] = "http://54.183.170.253:3000/img/default_user.jpg";

			var URL = IP+CONFIRMATION_URL+"?email="+email;

	//			CHECK USER IS ALREADY AVAILABLE OR NOT IF NOT THEN CREATE USER
			Users.findOrCreate({where: {$or: [{user_name: user_name}, {email: email}]}, defaults: userValue})
				.spread(function(user,created){
					console.log("user: "+user);
					console.log("created: "+created);
					if(created){
						sendEmail.sendForRegister(token, email, URL, function(emailStatus){
							// if(emailStatus == "success"){
							// 	res.send("registered");
							// }else{
							// 	res.send("error");
							// }
						});
						res.send("registered");
					}else{
						res.send("available");
					}
			});
			
		});
	// });
}


exports.loginUser = function(req, res){

	console.log("Class users and function loginUser");

	var email = req.param('email');
	var password = req.param('password');

	Users.find({where: {email: email}, attributes: ['user_id', 'password']}).then(function(returnData){
		// console.log("Return DATA: "+ JSON.stringify(returnData));
		if(returnData){
			Users.find({where: {email: email, verified: "1"}, attributes: ['user_id', 'password']}).then(function(userData){
				if(userData){
					bcrypt.compare(password, userData.password, function(err, ans){
						// console.log("PASSWORD IS "+ JSON.stringify(ans));
						if(ans){
							retdata = {
											'user_id': userData.user_id,
											'ret': 'true'
									}
							res.send(JSON.stringify(retdata));
						}else{
							res.send("wrong password");
						}
					});
				}else{
					res.send("email is not verified");
				}
			});
		}else{
			res.send("email is not available");
		}
	});
}

exports.confirmationUser = function(req, res){

	console.log("Class users and function confirmationUser");

	var email = req.param('email');
	var token = req.param('token');

	console.log("TOKEN PIN:"+token);	

	Users.findOne({where: {email: email}}).then(function(userValue){
		// console.log(userValue.dataValues);
		// console.log(userValue.dataValues.verified);
		if(userValue.dataValues.verified == "0"){
			Users.findOne({where: {email: email, verification_code: token}}).then(function(id){
				// console.log(id);
				if(id){
					Users.update({
						verification_code: null,
						verified: 1
					}, {
						where: {
							email: email, 
							verification_code: token
						}
					}).then(function(update){
						if(update[0] == 1){
							res.send("success");
						}
					})
				}else{
					res.send("error");
				}
			});
		}else{
			res.send("verified");
		}
	});
}

exports.userDetails = function(req, res){
	console.log("Class users and function userDetails");

	var id = req.param('id');

	Users.findOne({where: {user_id: id}}).then(function(userData){
		if(userData){
			console.log(userData.dataValues);
			res.json(userData.dataValues);
		}else{
			res.send("error");
		}
	});
}

exports.userSettingDetails = function(req, res){
	console.log("Class users and function userSettingDetails");

	var id = req.param('id');

	Users.findOne({
		where: {
			user_id: id
		},
		attributes: [
			'privacy', 'isEmail', 'user_id'
		]
	}).then(function(userData){
		if(userData){
			console.log(userData.dataValues);
			res.json(userData.dataValues);
		}else{
			res.send("error");
		}
	});
}


exports.userDetailsUpdate = function(req, res){
	console.log("Class users and function userDetailsUpdate");

	var id = req.param('user_id');
	var email = req.param('email');
	var city = req.param('city');
	var state = req.param('state');
	var country = req.param('country');
	var profession = req.param('profession');
	var about_me = req.param('about_me');
	var interests = req.param('interests');

	Users.findOne({where: {user_id: id, email: email}}).then(function(userData){
		if(userData){
			Users.update({
				city: city,
				state: state,
				country: country,
				profession: profession,
				about_me: about_me,
				interests: interests
			}, {
				where: {
					email: email,
					user_id: id
				}
			}).then(function(updated){
				if(updated[0] == 1){
					res.send("success");
				}else{
					res.send("error");
				}
			});
		}else{
			res.send("error");
		}
	});
}

exports.userPhotoUpload = function(req, res){
	console.log("Class users and function userPhotoUpload");

	var user_img = req.param("user_image");
	var user_id = req.param("user_id");

	var fs = require("fs");
	var image = user_img;
	var bitmap = new Buffer(image, 'base64');
	fs.writeFileSync("public/img/"+user_id+".jpg", bitmap);

	var avatar = IP + "/img/" + user_id+".jpg";

	Users.update({
		avatar: avatar
	}, {
		where: {
			user_id: user_id
		}
	}).then(function(updated){
		if(updated[0] == 1){
			retdata = {
						'avatar': avatar,
						'ret': 'true'
				}
			res.json(retdata);
			// res.send("success");
		}else{
			res.send("error");
		}
	});
}

exports.userPrivacyUpdate = function(req, res){
	console.log("Class users and function userPrivacyUpdate");

	var privacy = req.param("privacy");
	var user_id = req.param("user_id");

	Users.update({
		privacy: privacy
	}, {
		where: {
			user_id: user_id
		}
	}).then(function(updated){
		if(updated[0] == 1){
			res.send("success");
		}else{
			res.send("error");
		}
	});
}

exports.userIsEmailUpdate = function(req, res){
	console.log("Class users and function userIsEmailUpdate");

	var isEmail = req.param("isEmail");
	var user_id = req.param("user_id");

	Users.update({
		isEmail: isEmail
	}, {
		where: {
			user_id: user_id
		}
	}).then(function(updated){
		if(updated[0] == 1){
			res.send("success");
		}else{
			res.send("error");
		}
	});
}

// exports.list = function(req, res){
//   res.send("respond with a resource");
// };