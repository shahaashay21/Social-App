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
	console.log("Class users and function userCreateFeed");

	var fs = require("fs");

	var post_img = req.param("post_image");
	var user_id = req.param("user_id");
	var feed_text = req.param('feed_text');

	var postValue = {};
	postValue['user_id'] = user_id;
	postValue['image_url'] = null;
	postValue['user_name'] = feed_text;

	Feed.create({postValue}).then(post => {
		var post_id = post.feed_id;
		if(post_img!=null) 
		{
			var image = post_img;
			var bitmap = new Buffer(image, 'base64');//decode image i guess
			fs.writeFileSync("public/feed/"+post_id+".jpg", bitmap);//write image to this location
			var image_url = IP + "/feed/"+post_id+".jpg";//this is the location of the post image
		}
	})


}

