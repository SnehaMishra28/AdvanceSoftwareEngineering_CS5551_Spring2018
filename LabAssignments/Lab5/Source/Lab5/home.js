var app1= angular.module('weatherapp',[]);

app1.controller('WeatherCntrl', function ($scope,$http) {


    $scope.myfunction= function (my) {

        var temp, weather, wind, pressure, humidity, currenticon;
        var temp1, temp2, temp3;
        var statecode = document.getElementById("state").value;
        var citycode = document.getElementById("city").value;

        if(statecode == "" || citycode == "")
        {
            alert("Oops!!! Looks like you have not entered the State Code or City Code");

        }


        $http.get("http://api.wunderground.com/api/4bbbc25f4f5946dd/conditions/q/" + statecode +"/"+ citycode +".json").then(function (response) {

            var data1 = response.data;
            temp = response.data.current_observation.temp_f;
            weather = response.data.current_observation.weather;
            wind = response.data.current_observation.wind_mph;
            pressure = response.data.current_observation.pressure_mb;
            humidity = response.data.current_observation.relative_humidity;
            var icon = response.data.current_observation.icon_url;
            currenticon="<img src='" + icon + "'/>"

            temp1 = "Currently the temperature is " + temp +  "\u00B0" + "F with a wind speed of " + wind + "mph";
            temp2 = "The barometric pressure is " + pressure + "mb with a relative humidity of " + humidity;
            temp3 = "The weather is " + weather + " " + currenticon;
            //alert(temp3)
            document.getElementById("temp").innerHTML = temp1
            document.getElementById("press").innerHTML = temp2
            document.getElementById("weather").innerHTML = "The weather is currently " + weather + currenticon


        });

    };

});