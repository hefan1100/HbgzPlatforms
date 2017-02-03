angular.module("indexApp.controllers",[])
    .controller('mainCtrl', function ($scope,$rootScope,$ionicHistory,$http, $ionicPopup, $state,$cacheFactory,IndexFactory) {
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

        IndexFactory.getIndexList().then(function(response){
            var infolist=response.infolist;
            var districtlist=response.district;
            $scope.myName=districtlist[0];
            $scope.list=infolist;
            $scope.districtlist=districtlist;
        });

        $scope.goList=function(cid){
            $state.go("list",{cid:cid,cityname:$scope.myName.name});
        };

        $scope.$watch('myName',  function(newValue, oldValue) {
            if((newValue==null)||(oldValue==null)||(newValue === oldValue))
                return;

            IndexFactory.getIndexListByCity(newValue.id).then(function (response) {
                    $scope.list = response;
            });

        });

    })
    .controller('listCtrl', function ($scope,$rootScope,$ionicHistory,$http, $ionicPopup, $state,$cacheFactory,$stateParams,IndexFactory) {
        $scope.screenHeight=window.screen.availHeight;
        $scope.screenWidth=window.screen.availWidth;
        $scope.citytitle=$stateParams.cityname;
        IndexFactory.getArticleDetails($stateParams.cid).then(function(response){
           $scope.articlelist=response.articlelist;
            $scope.imginfo=response.img;
        });

        $scope.imgheaderStyle={
            "height":$scope.screenHeight*0.3+'px',
            "width":$scope.screenWidth+'px',
            "padding":"0px"
        };
        $scope.linkStyle={
            "height":$scope.screenHeight*0.15+'px',
            "width":$scope.screenWidth+'px'
        };
        $scope.itemStyle={
            "height":$scope.screenHeight*0.25+'px',
            'border':'none',
            "width":"100%"
        };

        $scope.goDetail=function(aid)
        {
            $state.go("detail",{aid:aid});
        };

        $scope.doLink=function(){
            IndexFactory.getLink($stateParams.cid).then(function(response){
                var link=response.stateurl;
                window.location.href=link;
            });
        };
    })
    .controller('detailCtrl', function ($scope,$rootScope,$ionicHistory,$http, $ionicPopup, $state,$cacheFactory,$stateParams,IndexFactory) {
        $scope.screenHeight=window.screen.height;
        $scope.screenWidth=window.screen.width;

        $scope.imgheaderStyle={
            "height":$scope.screenHeight*0.33+'px',
            "width":"100%",
            "padding":"0px"
        };


        IndexFactory.getArticleDetailsInfo($stateParams.aid).then(function(response){
            $scope.info=response;
        });
    })
;
