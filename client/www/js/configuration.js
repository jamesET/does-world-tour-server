"use strict";

 angular.module('config', [])

.constant('ENV', {name:'development',apiEndpoint:'http://localhost:8080/'})
//.constant('ENV', {name:'production',apiEndpoint:'http://beerlist-env.us-west-2.elasticbeanstalk.com/'})

;