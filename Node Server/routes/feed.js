var bcrypt = require('bcrypt-nodejs');

//Files
var sendEmail = require('./email');
var db = require('./data_model');

// Variables
var Users = db.Users;
var Friend = db.Friend;
var Feed = db.Feed;
var IP = "http://54.183.170.253:3000";


exports.feedCreatePost = function(req, res)
{
	console.log("Class users and function feedCreatePost");

	var fs = require("fs");

	var post_img = req.param("post_image");
	var user_id = req.param("user_id");
	var feed_text = req.param('feed_text');


	var postValue = {};
	postValue['user_id'] = user_id;
	postValue['image_url'] = post_img;
	postValue['feed_text'] = feed_text;


	console.log("got parameters and put in object");
	// Feed.Create({postValue})
	// .then(post => {
		//var post_id = post.feed_id;
		// if(post_img!=null) 
		// {
		// 	var image = post_img;
		// 	var bitmap = new Buffer(image, 'base64');//decode image i guess
		// 	fs.writeFileSync("public/feed/"+post_id+".jpg", bitmap);//write image to this location
		// 	var image_url = IP + "/feed/"+post_id+".jpg";//this is the location of the post image
		// }
	// })

	Feed.findOrCreate({where: {$and: [{user_id: user_id}, {feed_text: feed_text}]}, defaults: postValue})
	.spread(function(post,created){
		// console.log("post: "+ post);
		// console.log("created: "+created);
		if(created)
		{
			res.send("success");
			Feed.findOne({where: {user_id: user_id, image_url: post_img}}).then(function(postData){
				if(postData)
				{
					if(post_img!=0) 
					{
						var image = post_img;
						var bitmap = new Buffer(image, 'base64');//decode image i guess
						fs.writeFileSync("public/feed/"+postData.user_id+postData.feed_id+".jpg", bitmap);//write image to this location
						var image_url = IP + "/feed/"+postData.user_id+postData.feed_id+".jpg";//this is the location of the post image
						//var temp_test = "I am  test";
						Feed.update({ image_url: image_url}, {where: {feed_text:feed_text,	user_id: user_id}})
						.then(function(updated)
						{
						if(updated[0] == 1)
						{
							console.log('updated picture url');
						}
						else
						{
							console.log("dint do it");
						}
					});
					}
				}
				else
				{
					console.log("post was not found");
				}
			});
		}
		else
		{
			res.send("failure")
			console.log("post was not created");
		}
	});


}

exports.feedUpdate = function(req, res)
{
	console.log("Class users and function userCreateFeed");

	var fs = require("fs");

	var post_img = req.param("post_image");
	var user_id = req.param("user_id");
	var feed_text = req.param('feed_text');

	var postValue = {};
	postValue['user_id'] = user_id;
	postValue['image_url'] = post_img;
	postValue['feed_text'] = feed_text;

	console.log(postValue);
	// Feed.create({postValue}).then(post => {
	// 	//var post_id = post.feed_id;
	// 	// if(post_img!=null) 
	// 	// {
	// 	// 	var image = post_img;
	// 	// 	var bitmap = new Buffer(image, 'base64');//decode image i guess
	// 	// 	fs.writeFileSync("public/feed/"+post_id+".jpg", bitmap);//write image to this location
	// 	// 	var image_url = IP + "/feed/"+post_id+".jpg";//this is the location of the post image
	// 	// }
	// })


}


exports.feedGetPost = function(req, res)
{
	console.log("Class users and function feedGetPost");

	var user_id = req.param("user_id");

	var q = "select fe.feed_id as id, concat(u.first_name, '  ',u.last_name) as name, fe.image_url as image, fe.feed_text as status, fe.createdAt as timeStamp, u.avatar as profilePic from friend as fr left join users as u on fr.friend_id = u.user_id left join feed as fe on u.user_id = fe.user_id where fr.user_id = "+user_id+" and (fr.request = '2' or fr.follow = '1') and u.privacy = '0' UNION select fe.feed_id as id, concat(u.first_name, ' ', u.last_name) as name, fe.image_url as image, fe.feed_text as status, fe.createdAt as timeStamp, u.avatar as profilePic from feed as fe left join users as u on fe.user_id = u.user_id;";
	db.sequelize.query(q).spread((results, metadata) => {
		res.send({"feed": results});
		//res.json(results);
	});
}