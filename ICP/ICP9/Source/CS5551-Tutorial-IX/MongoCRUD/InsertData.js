/**
 * Created by Sneha mishra.
 * Creating REST service to insert data
 */
var MongoClient = require('mongodb').MongoClient;
var assert = require('assert');
var bodyParser = require("body-parser");
var express = require('express');
var cors = require('cors');
var app = express();
var url = 'mongodb://root:123456@ds023418.mlab.com:23418/aseicp9';

var insertDocument1 = function(db, callback) {
    db.collection('users').insertOne( {
        "UserID" : "Sneha",
        "fname" : "Sneha",
        "lname" : "Mishra",
        "Contact":"9137105238",
        "city":"Kansas City"
    }, function(err, result) {
        assert.equal(err, null);
        console.log("Inserted a document into the aseicp9 collection.");
        callback();
    });

};
MongoClient.connect(url, function(err, client) {
    assert.equal(null, err);
    var db = client.db("aseicp9");
    insertDocument1(db, function() {
        client.close();
    });
});

//Function to insert Data
var insertDocument = function(db, data, callback) {
    db.collection('users').insertOne( data, function(err, result) {
        if(err){
            res.write("Registration Unsuccessful.. Try again !!");
            res.end();

        }
        console.log("Inserted a document into the aseicp9 collection.");
        callback();
    });

};

//Creating  REST service  to insert  data
app.post('/register', function (req, res) {
    MongoClient.connect(url, function (err, db) {
        if(err){
            res.write("Error while connecting to Database!!");
            res.end();
        }
        insertDocument(db, req.body, function () {

            res.write("Successfully inserted!!");
            res.end();
        })
    })
    
})