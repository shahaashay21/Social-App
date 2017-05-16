var nodemailer = require("nodemailer");

// create reusable transporter object using the default SMTP transport
	var transporter = nodemailer.createTransport({
	    service: 'gmail',
	    auth: {
	        user: 'apt140021@gmail.com',
	        pass: 'aM1111012'
	    }
	});

exports.sendForRegister = function(token, to, url, callback){

	console.log("Class email and function sendForRegister");

	var registerFrom = '"Social App 👻" <apt140021@gmail.com>';
	var registerSubject = 'Social App Verification ✔';
	var registerBody = '<h2>Social App Confirmation</h2><p>We have received a request to authorize this email address for use with Android Social App. If you requested this verification, please go to the following Button or URL to confirm that you are authorized to use this email address:</p><h3>PIN: '+token+'</h3><a href="'+url+'"><button style="background-color: #4CAF50; border: none;color: white;padding: 15px 32px;text-align: center;text-decoration: none;display: inline-block;border-radius: 8px;font-size: 16px;margin: 4px 2px;cursor: pointer;">Confirm me</button></a>' // html body
	sendEmail(registerFrom, to, registerSubject, registerBody, function(emailStatus){
		if(emailStatus == "Sent"){
			callback("success");
		}else{
			callback("error");
		}
	});	
}


function sendEmail(from, to, subject, body, callback){

	console.log("Class email and function sendEmail");

	// setup email data with unicode symbols
	// var mailOptions = {
	//     from: '"Social App 👻" <aashay@socialapp.com>', // sender address
	//     to: 'shah.aashay21@gmail.com', // list of receivers
	//     subject: 'Social App Verification ✔', // Subject line
	//     // text: 'Hello world ?', // plain text body
	//     // html: '<b>Hello world ?</b>' // html body




	//     html: '<h2>Social App Confirmation</h2><p>We have received a request to authorize this email address for use with Android Social App. If you requested this verification, please go to the following Button or URL to confirm that you are authorized to use this email address:</p><a href="http://www.foodfire.in/"><button style="background-color: #4CAF50; border: none;color: white;padding: 15px 32px;text-align: center;text-decoration: none;display: inline-block;border-radius: 8px;font-size: 16px;margin: 4px 2px;cursor: pointer;">Confirm me</button></a>' // html body
	// };

	var mailOptions = {
	    from: from, // sender address
	    to: to, // list of receivers
	    subject: subject, // Subject line
	    html: body
	};


	// send mail with defined transport object
	transporter.sendMail(mailOptions, (error, info) => {
	    if (error) {
	        console.log(error);
	        callback("Error");
	    }else{
	    	console.log('Message %s sent: %s', info.messageId, info.response);
	    	callback("Sent");
	    }
	});	
}