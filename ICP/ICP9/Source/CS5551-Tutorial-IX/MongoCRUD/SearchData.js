/*
 * Created by mnpw3d on 20/10/2016.
 */

var MongoClient = require('mongodb').MongoClient;
var assert = require('assert');
var url = 'mongodb://root:123456@ds023418.mlab.com:23418/aseicp9';
var findUser = function(db, callback) {
    var cursor =db.collection('users').find( );
    cursor.each(function(err, doc) {
        assert.equal(err, null);
        if (doc != null) {
            console.log(doc);
        } else {
            callback();
        }
    });
};
var findUserwithName = function(db,callback) {
    var cursor = db.collection('users').find({"fname":"Sneha"});
    cursor.each(function(err,doc) {
        assert.equal(err,null);
        if(doc != null)
        {
            console.log("First Name:" + doc.fname);
            console.log("Last Name:" + doc.lname);
            console.log("Contact:" + doc.Contact);
            console.log("city:" + doc.city);
        }
    });
}
var findUserwithContact = function(db, callback) {
    var cursor = db.collection('users').find({"Contact":"9137105238"});
    cursor.each(function(err,doc){
       assert.equal(err,null);
       if(doc != null)
       {
           console.log("First Name:" + doc.fname);
           console.log("Last Name:" + doc.lname);
           console.log("Contact:" + doc.Contact);
           console.log("city:" + doc.city);
       }
    });
}
/* MongoClient.connect(url, function(err, db) {
    assert.equal(null, err);
    findUserwithUniversity(db, function() {
        db.close();
    });
}); */


MongoClient.connect(url, function(err, client) {
    assert.equal(null, err);
    var db = client.db("aseicp9");
    findUserwithContact(db, function() {
        client.close();
    });
});