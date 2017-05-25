/**
 * Module dependencies.
 */

var express = require('express')
  , routes = require('./routes')
  , user = require('./routes/user')
  , email = require('./routes/email')
  , http = require('http')
  , path = require('path')
  , friend = require('./routes/friend')
  , message = require('./routes/message')
  , feed = require('./routes/feed');
  // , message = require('./routes/message');

var app = express();

// all environments
app.set('port', process.env.PORT || 3000);
app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');
app.use(express.favicon());
app.use(express.logger('dev'));

// app.use(express.bodyParser());
app.use(express.bodyParser({
  keepExtensions: true, 
    uploadDir: __dirname + '/public/img',
    limit: '15mb'}));

app.use(express.methodOverride());
app.use(app.router);
app.use(express.static(path.join(__dirname, 'public')));

// development only
if ('development' == app.get('env')) {
  app.use(express.errorHandler());
}

app.get('/', routes.index);
// app.get('/users', user.list);

//Register user
app.post('/user/register', user.registerUser);

//Login user
app.post('/user/login', user.loginUser);

//Confirm user
app.get('/user/confirm', function(req, res){
	res.send("Ok");
});

//Confirm user
app.post('/user/confirmpin', user.confirmationUser);

//Get user's details
app.post('/user/info', user.userDetails);

app.post('/friend/search', friend.getUsers);

//Get All user's messages
// app.post('/message/allMessage', message.getMessage);

//Get user's details
app.post('/user/settings/info', user.userSettingDetails);

//Update user's information
app.post('/user/info/update', user.userDetailsUpdate);

//Upload user's photo
app.post('/user/image', user.userPhotoUpload);

//Update user's privacy
app.post('/user/privacy/update', user.userPrivacyUpdate);

//Update user's is send email
app.post('/user/isemail/update', user.userIsEmailUpdate);

//Get profile isFriend and isFollow information
app.post('/profile/friend/info', friend.profileFriendInfo);

//Get all sent friend request info
app.post('/friend/request/sent', friend.friendRequestSentInfo);

//Get all friend request info
app.post('/friend/request/get', friend.friendRequestInfo);



//Send friend request to a friend
app.post('/friend/request/send', friend.friendSendRequest);

//Cancel sent friend request to a friend
app.post('/friend/request/accept', friend.friendRequestAccept);

//Cancel sent friend request to a friend
app.post('/friend/request/cancel', friend.friendSentRequestCancel);

// //Unfriend to a friend
// app.post('/friend/request/unfriend', friend.friendUnfriend);

//Follow to a person
app.post('/friend/follow', friend.friendFollow);

//Unfollow to a person
app.post('/friend/unfollow', friend.friendUnfollow);

//get message list
app.post('/message/search', message.getMessages);

app.post('/message/email', message.getEmail);

//get message list
app.post('/message/send', message.createMessage);

//Create post
app.post('/feed/post', feed.feedCreatePost);

//Get post
app.get('/feed/get/:user_id', feed.feedGetPost);

//Get feeed
app.post('/feed/getfeed', feed.feedUpdate);

//Confirmed user redirect to app link
app.get('/user/confirmed', function(req, res){
	res.send("Ok");
});




/////// TEST ///////

//SEND EMAIL TEST
app.post('/user/email', email.sendForRegister);





/////// ERROR PAGE ///////

//ERROR URL
app.get('/error', function(req, res){
	res.send("Error");
});

http.createServer(app).listen(app.get('port'), function(){
  console.log('Express server listening on port ' + app.get('port'));
});