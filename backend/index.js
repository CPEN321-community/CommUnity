var express = require('express');
var http = require('http');
var app = express();

app.get('/ip', (req, res) => {
    http.get({ 'host': 'api.ipify.org', 'port': 80, 'path': '/?format=json' }, (result) => {
        result.on("data", (ip) => {
            res.send(JSON.parse(ip).ip);
        })
    })
});

app.get('/time', (req, res) => {
    var serverTime = new Date(Date.now());
    res.send(serverTime.toTimeString().split(' ')[0]);
});

app.get('/name', (req, res) => {
    res.send("Joshua Park");
});

var server = app.listen(3000, () => {
    var host = server.address().address;
    var port = server.address().port;
    console.log("App listening at http://%s:%s", host, port);
})