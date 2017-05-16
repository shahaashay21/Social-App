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
	require('crypto').randomBytes(48, function(err, buffer) {
		var token = buffer.toString('hex');
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

			var URL = IP+CONFIRMATION_URL+"?email="+email+"&token="+token;

	//			CHECK USER IS ALREADY AVAILABLE OR NOT IF NOT THEN CREATE USER
			Users.findOrCreate({where: {$or: [{user_name: user_name}, {email: email}]}, defaults: userValue})
				.spread(function(user,created){
					console.log("user: "+user);
					console.log("created: "+created);
					if(created){
						sendEmail.sendForRegister(email, URL, function(emailStatus){
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
	});
}


exports.loginUser = function(req, res){

	console.log("Class users and function loginUser");

	var email = req.param('email');
	var password = req.param('password');

	Users.find({where: {email: email}, attributes: ['user_id', 'password']}).then(function(returnData){
		// console.log("Return DATA: "+ JSON.stringify(returnData));
		if(returnData){
			bcrypt.compare(password, returnData.password, function(err, ans){
				// console.log("PASSWORD IS "+ JSON.stringify(ans));
				if(ans){
					retdata = {
									'user_id': returnData.user_id,
									'ret': 'true'
							}
					res.send(JSON.stringify(retdata));
				}else{
					res.send("wrong password");
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

	Users.findOne({where: {email: email, verification_code: token}}).then(function(id){
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
					//DO REDIRECT FOR RIGHT TOKEN
					res.redirect("/user/confirmed");
				}
			})
		}else{
			//REDIRECT ON ERROR
			res.redirect("/error");
		}
	});
}


// exports.list = function(req, res){
//   res.send("respond with a resource");
// };