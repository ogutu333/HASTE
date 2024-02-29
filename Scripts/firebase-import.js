var admin = require("firebase-admin");
var json = require("./"+process.argv[2]);

var serviceAccount = require("./haste-service-account.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://eager-621db.firebaseio.com"
});

var dbRef = admin.database().ref('Reports');

for(var i = 0; i < json.length; i++){
	var key = dbRef.push().key;
	dbRef.child(key).set(json[i]);
	//break at first node for testing purpose
}
