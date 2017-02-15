angular.module("indexApp.controllers",[])
    .controller('mainCtrl', function ($scope,$rootScope,$ionicHistory,$http, $ionicPopup, $state,$cacheFactory,IndexFactory) {
        $scope.screenHeight=window.screen.height;
        $scope.screenWidth=window.screen.width;
        $scope.allheight= $scope.screenHeight*0.25;
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

        $scope.itemdescriptionstyle={
            "padding":"5px",
            "height": $scope.allheight*0.35+'px',
            "width":$scope.screenWidth+'px'
        }
        $scope.itemdivider={
            "height": $scope.allheight*0.12+'px',
            "width":$scope.screenWidth+'px',
            "background-color":"#dcdcdc"
        }

        IndexFactory.getIndexList().then(function(response){
            var infolist=response.infolist;
            var districtlist=response.district;
            $scope.mycityid="";
            $scope.list=infolist;
            $scope.districtlist=districtlist;
        });

        $scope.goList=function(cid){
            var cityname="";
            for(var i=0;i<$scope.districtlist.length;i++)
            {
                var item=$scope.districtlist[i];
                if(item.id==cid) {
                    cityname = item.name;
                    break;
                }
            }
            $state.go("list",{cid:cid,cityname:cityname});
        };

        $scope.$watch('mycityid',  function(newValue, oldValue) {
            if((newValue==null)||(oldValue==null)||(newValue === oldValue))
                return;

            IndexFactory.getIndexListByCity(newValue).then(function (response) {
                $scope.list = response;
            });

        });

    })
    .controller('listCtrl', function ($scope,$rootScope,$ionicHistory,$http, $ionicPopup, $state,$cacheFactory,$stateParams,IndexFactory) {
        $scope.screenHeight=window.innerHeight;
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
            "height":$scope.screenHeight*0.18+'px',
            'border':'none',
            "width":"100%"
        };

        $scope.topdivStyle={
            "white-space":"normal",
            "width": "100%",
            "text-align": "left",
            "font-size": "1em",
            "height": $scope.screenHeight*0.2*0.3+'px'
        };


        $scope.pStyle={
            "width": "100%",
            "margin-top":$scope.screenHeight*0.2*0.3+'px'
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
        function noEscapeHtml(html) {
            return html.replace(/(\&|\&)gt;/g, ">")
                .replace(/(\&|\&)lt;/g, "<")
                .replace(/(\&|\&)quot;/g, "\"");
        }

        IndexFactory.getArticleDetailsInfo($stateParams.aid).then(function(response){
            $scope.info=response;
     //       $scope.info.CONTENT=noEscapeHtml($scope.info.CONTENT);
        });
    })
;
