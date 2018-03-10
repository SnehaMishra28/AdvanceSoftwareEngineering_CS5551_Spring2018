'use strict';

// Declare app level module which depends on views, and components
angular.module('myApp', [])


    .controller('View1Ctrl', function ($scope, $http) {
        $scope.clothesList = new Array();
       // $scope.mostRecentReview;
        $scope.getClothes = function () {
            var clothesTypeEntered = document.getElementById("txt_dressType").value;
            if (clothesTypeEntered != null && clothesTypeEntered != "" ) {
                //This is the API used in our project to fetch the clothes type.
                var handler = $http.get("https://api.indix.com/v2/summary/products?countryCode=US&q=" +
                    clothesTypeEntered + "&app_key=w2xqtl4uBXLJnCk0zscGrt86TEh80bmx");

                alert("After getting the request");

                handler.success(function (data) {

                    if(data == null || data==""){

                        alert("Data is null inside success");
                    }

                    if (data != null && data.result != null && data.result.products != undefined && data.result.products != null) {
                        for (var i = 0; i < data.result.products.length; i++) {
                            $scope.clothesList[i] = {
                                "categoryName": data.result.products[i].categoryName,
                                "brandName": data.result.products[i].brandName
                            };
                        }
                    }

                })
                handler.error(function (data) {
                    alert("There was some error processing your request. Please try after some time.");
                });
            }else {

                alert("Enter some type of clothes you want to seach for.");
            }
        }
    });


$http({url:"http://localhost:8081/api/" + clothesEntered, method: 'POST'}).then(function(data, status) {
    $scope.venue = data.data.title;
    alert(data.data.title);
}
