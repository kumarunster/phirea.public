'use strict';

/* Controllers */

//Declare app level module which depends on filters, and services
angular.module('FileUploadApp', [
                         'ngRoute',
                         'angularFileUpload',
                         'FileUploadApp.controllers'
                         ]).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.otherwise({redirectTo: '/fileupload'});
  }]);


angular.module('FileUploadApp.services', [])
.service("uploadService", function ($rootScope, $q /*, eventBusService*/) {
//	TODO make working over the event bus
//	
//	var that = this;
//	
//	that.upload = function (dto) {
//		
//		var deferred = $q.defer();
//        
//        var msg = {
//             action : 'fileUpload',
//             data: dto.file,
//             fileName: dto.fileName
//        };
//        
//        console.log("sockjs: sending request with " + msg.action);
//        /* */
//        var ebPromise = eventBusService.getEventBus();
//        
//        ebPromise.then(function(eb) {
//            eb.send('fileupload.handler', msg, function(reply) {
//                console.log("sockjs: received " + reply.answer);
//                
//            });
//        });
//        
//        
//        return deferred.promise;
//		
//	};
	
});

angular.module('FileUploadApp.controllers', ['ngCookies', 'ngSanitize'])
  .controller("FileUploadCtrl", function($scope, $rootScope, $upload/*, uploadService*/) {
	  
	  $scope.onFileSelect = function($files) {
		    //$files: an array of files selected, each file has name, size, and type.
		    for (var i = 0; i < $files.length; i++) {
		      var file = $files[i];
		      
//		      
//		      TODO make fileupload working over event bus
//		      
//		      uploadService.upload({
//		    	  file: file,
//	              fileName: file.name,
//	              size: file.size
//		      });
		      
		      $scope.upload = $upload.upload({
		        url: 'fileupload/process', //upload.php script, node.js route, or servlet url
		        method: 'POST', // or PUT,
		        // headers: {'headerKey': 'headerValue'},
		        // withCredentials: true,
		        data: {myObj: $scope.myModelObj},
		        file: file,
		        // file: $files, //upload multiple files, this feature only works in HTML5 FromData browsers
		        /* set file formData name for 'Content-Desposition' header. Default: 'file' */
		        //fileFormDataName: myFile, //OR for HTML5 multiple upload only a list: ['name1', 'name2', ...]
		        /* customize how data is added to formData. See #40#issuecomment-28612000 for example */
		        //formDataAppender: function(formData, key, val){} //#40#issuecomment-28612000
		      }).progress(function(evt) {
		        console.log('percent: ' + parseInt(100.0 * evt.loaded / evt.total));
		      }).success(function(data, status, headers, config) {
		        // file is uploaded successfully
		        console.log(data);
		      });
		      //.error(...)
		      //.then(success, error, progress); 
		    }
		    // $scope.upload = $upload.upload({...}) alternative way of uploading, sends the the file content directly with the same content-type of the file. Could be used to upload files to CouchDB, imgur, etc... for HTML5 FileReader browsers. 
		  };
	  
  });
