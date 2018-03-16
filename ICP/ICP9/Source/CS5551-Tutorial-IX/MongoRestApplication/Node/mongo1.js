/**
 * Created by Sneha Mishra on 15/03/2018.
 */
var MongoClient = require('mongodb').MongoClient;
var assert = require('assert');
var bodyParser = require("body-parser");
var express = require('express');
var cors = require('cors');
var app = express();
var db;
var url = 'mongodb://root:123456@ds023418.mlab.com:23418/aseicp9';
app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.post('/search', function (req, res) {
    MongoClient.connect(url, function(err, client) {
        console.log(JSON.stringify(req.body))
        db=client.db('aseicp9');
        if(err)
        {
            res.write("Failed, Error while connecting to Database");
            res.end();
        }
        searchDocument(db, req.body.contact, function() {
            console.log(req.body.contact)
            res.write("Successfully inserted");
            res.end();
        });
    });
})
var searchDocument = function(db, data, callback) {
    var cursor = db.collection('users').find("Contact:"+data);
    cursor.each(function(err,doc){
        assert.equal(err,null);
        if(doc != null)
        {
            console.log("First Name:" + doc.fname);
            console.log("Last Name:" + doc.lname);
            console.log("city:" + doc.city);
        }
    });

};

var server = app.listen(8081,function () {
    var host = server.address().address
    var port = server.address().port

    console.log("Example app listening at http://%s:%s", host, port)
})