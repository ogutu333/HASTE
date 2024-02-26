var functions = require('firebase-functions');
var admin = require('firebase-admin');
var request = require('request');

var API_KEY = "AAAAty92hlo:APA91bGbHCOFvOX-cNR5zzYiNJ5V_Tl4VFMrVWIF78ii64l7jV0VTYs-Kl4BC8XjE6y9UiFN-VjBqr30dekcc1dMJTmYEz9ltlNhhTOjuUhJ1B-mvxtKxOiq6RF9JsKZMgJbUk7Jpqt7"; // Your Firebase Cloud Messaging Server API key

// Fetch the service account key JSON file contents
var serviceAccount = require("./serviceAccount.json");

// Initialize the app with a service account, granting admin privileges
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://haste-17bcd.firebaseio.com/"
});

ref = admin.database().ref();
var requests = ref.child('notificationRequests');
requests.on('child_added', function(requestSnapshot) {
	var request = requestSnapshot.val();
	sendNotificationToTopic(
		request.zipcode, 
		request.key,
		request.message,
		request.type,
		function() {
			//requestSnapshot.ref.remove();
		}
	);
}, function(error) {
	console.error(error);
});

function sendNotificationToTopic(zipcode, key, message, type, onSuccess) {
	request({
		url: 'https://fcm.googleapis.com/fcm/send',
		method: 'POST',
		headers: {
			'Content-Type' :' application/json',
			'Authorization': 'key='+API_KEY
	},
	body: JSON.stringify({
		/*
		notification: {
			title: "HASTE"
			body: message
		},*/
		data: {
			"key": key,
			"body": message,
			"zipcode": zipcode,
			"type": type
		}
		to : '/topics/'+zipcode
	})
	}, function(error, response, body) {
		if (error) { console.error(error); }
		else if (response.statusCode >= 400) { 
			console.error('HTTP Error: '+response.statusCode+' - '+response.statusMessage); 
		}
		else {
		 	onSuccess();
		 	console.log("Successfully sent message:", payload);
		}
	});
}
