/**
 * Module dependencies.
 */

var express = require('express')
  , routes = require('./routes')
  , user = require('./routes/user')
  , email = require('./routes/email')
  , http = require('http')
  , path = require('path');

var app = express();

// all environments
app.set('port', process.env.PORT || 3000);
app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.bodyParser());
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