angular.module("indexApp.controllers",[])
    .controller('mainCtrl', function ($scope,$rootScope,$ionicHistory,$http, $ionicPopup, $state,$cacheFactory) {
        $scope.screenHeight=window.screen.height;
        $scope.screenWidth=window.screen.width;
        $scope.allheight= $scope.screenHeight*0.35;
        $scope.itemStyle={
             "height":$scope.allheight+'px',
             "border":"none",
             "width":'100%',
             "background-color":"#ffffff",
             "padding":"0px"
        };
        $scope.itemtitleStyle={
            "height":$scope.allheight*0.15+'px',
            "width":$scope.screenWidth+'px',
            "padding-left":"5px"
        };
        $scope.imagelocationStyle={
            "height":$scope.allheight*0.15*0.6+'px'
        };

        $scope.itemavatarstyle={
            "height":$scope.allheight*0.5+'px',
            "width":$scope.screenWidth+'px'
        }

        $scope.itemdescriptionstyle={
            "padding":"5px",
            "height": $scope.allheight*0.25+'px',
            "width":$scope.screenWidth+'px'
        }
        $scope.itemdivider={
            "height": $scope.allheight*0.1+'px',
            "width":$scope.screenWidth+'px',
            "background-color":"#dcdcdc"
        }
    })

;
