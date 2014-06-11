var system = require('system');
var fs = require('fs');
var file_h = fs.open('website.csv', 'r');
var line = file_h.readLine();
var arr = [];
arr.push(line);
while(line) {
    //console.log(line);
	line = file_h.readLine();
	arr.push(line);
}
file_h.close();
var index=0;
myfun(index);

function myfun(ind){
	if (ind >= arr.length - 1){
		//phantom.exit();
		return;
	}
	//console.log(ind + ' >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ' + arr[ind]);
	var page = require('webpage').create();
	page.onResourceRequested = function (req) {
		console.log('REQ ' + page.url + ' ' +req.url);
		//console.log('page' + JSON.stringify(netreq, undefined, 4));
	};
	page.onResourceReceived = function (res) {
		console.log('REC ' + page.url + ' ' + res.url);
		//console.log('page' + JSON.stringify(netreq, undefined, 4));
	};
	page.open(arr[ind], function (status) {
	//	console.log('next '+ arr[ind+1]);
		myfun(ind+1);
	});
}


