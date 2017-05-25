var bcrypt = require('bcrypt-nodejs');
var Sequelize = require('sequelize');

//Files
var sendEmail = require('./email');
var db = require('./data_model');

// Variables
var Users = db.Users;
var Friend = db.Friend;
var Message = db.Message;
var IP = "http://54.183.170.253:3000";



exports.getMessages = function(req, res){
	console.log("Class getMessages");

	var id = Number(req.param('user_id'));
	//dont reallly need this al i need is user id
	//var message = req.param('messages');


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

	console.log("Getting User Messages for user:"+id);

	Message.findAll({where: {user_id: id }}).then(function(messageData)
	{
		if(messageData)
		{
			//console.log("Message Data: " + userData.length);
			//print out list of messages this user has
			console.log(JSON.stringify(messageData, null, 1));
			res.json(messageData);
			//res.send({"userData": userData});
			console.log("Sent Message Data");
		}
		else
		{
			console.log("failed to receive message data");
			res.send("error");
		}
	})
	.catch(function(error)
	{
		console.log(JSON.stringify(error, 0, null));
	});
}

exports.getEmail = function(req, res){
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

	console.log("Search String For Email: " + search);

	Users.find({where: {email: search }}).then(function(userData){
		
		
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

exports.createMessage = function(req, res){
	console.log("Class createMessage");


	var fs = require("fs");

	var user_id = req.param("user_id");
	var search = req.param("search");
	var rec_id = req.param("rec_id");
	var first_name = req.param("first_name");
	var last_name = req.param("last_name");

	//still dunno what we will be sending back, ideally first name, last name, and recipient id
	//var recipient_id = req.param("recipient_id");
	var subject = req.param("subject");
	var body = req.param('body');

//we need to find recipient id, name, and last name
	// var first_name = "Hugo";
	// var last_name = "Quiroz";
	// var recipient_id = 50;
//

	var messageValue = {};
	messageValue['user_id'] = user_id;
	messageValue['rec_id'] = rec_id;
	messageValue['subject'] = subject;
	messageValue['body'] = body;
	messageValue['first_name'] = first_name;
	messageValue['last_name'] = last_name;





	Message.findOrCreate({where: {$and: [{rec_id: rec_id}, {body: body}]}, defaults: messageValue})
		.spread(function(message,created){
			// console.log("post: "+ post);
			// console.log("created: "+created);
			if(created)
			{
				res.send("success");
			}
			else
			{
				res.send("error")
				console.log("post was not created");
			}
		});



}