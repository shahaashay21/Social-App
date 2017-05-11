var db = require('./data_model');
var Users = db.Users;
var bcrypt = require('bcrypt-nodejs');

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
	//			CHECK USER IS ALREADY AVAILABLE OR NOT IF NOT THEN CREATE USER
			Users.findOrCreate({where: {$or: [{user_name: user_name}, {email: email}]}, defaults: userValue})
				.spread(function(user,created){
					console.log("user: "+user);
					console.log("created: "+created);
					if(created){
						res.end(JSON.stringify('Registered'));
					}else{
						res.end(JSON.stringify('available'));
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
		console.log("Return DATA: "+ JSON.stringify(returnData));
		if(returnData){
			bcrypt.compare(password, returnData.password, function(err, ans){
				console.log("PASSWORD IS "+ JSON.stringify(ans));
				if(ans){
					retdata = {
									'user_id': returnData.user_id,
									'ret': 'true'
							}
					res.end(JSON.stringify(retdata));
				}else{
					res.end(JSON.stringify('wrong password'));
				}
			});
		}else{
			res.end(JSON.stringify('email is not available'));
		}
	});
}


// exports.list = function(req, res){
//   res.send("respond with a resource");
// };