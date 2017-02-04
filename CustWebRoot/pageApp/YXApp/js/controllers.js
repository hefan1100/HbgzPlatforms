angular.module("indexApp.controllers",[])
    .controller('mainCtrl', function ($scope,$rootScope,$ionicHistory,$http, $ionicPopup, $state,$cacheFactory,$ionicSlideBoxDelegate,IndexFactory) {
        $scope.screenHeight=window.innerHeight;
        $scope.screenWidth=window.screen.availWidth;
        $scope.itemStyle={
             "height":$scope.screenHeight*0.15+'px',
             "border":"none",
             "width":'100%',
             "background-color":"#ffffff",
            "padding-left":"3%",
            "padding-right":"3%"
        };

        $("#img").lazyload({
            placeholder : "img/loading.gif",
            effect: "fadeIn"
        });

        $scope.rowStyle={
            "height":$scope.screenHeight*0.13+'px',
            "border":"none",
            "width":'100%',
            "background-color":"#ffffff"
        };

        $scope.listStyle={
            "height":$scope.screenHeight*0.3+'px',
            "width":'100%'
        };

        $scope.itemSingleStyle={
            "height":$scope.screenHeight*0.1+'px',
            "width":'100%'
        };


        $scope.itembottomimageStyle={
            "height":($scope.screenHeight-$scope.screenHeight*0.7)+'px',
            "width":$scope.screenWidth+'px'
        };

        $scope.itemtitleimageStyle={
            "height":$scope.screenHeight*0.3+'px',
            "width":$scope.screenWidth+'px'
        };

        IndexFactory.getIndexList().then(function(response){
            $scope.articlelist=response.articlelist;
            $scope.imagelist=response.imagelist;
            $ionicSlideBoxDelegate.update();
            $ionicSlideBoxDelegate.$getByHandle("slideboximgs").loop(true);
        });


        $scope.model = {
            activeIndex:0
        };

        $scope.pageClick = function(index){
           $scope.model.activeIndex = index;
        };
        $scope.slideHasChanged = function($index){

        };
        $scope.delegateHandle = $ionicSlideBoxDelegate;

        $scope.goDetail=function(aid){
            $state.go("detail",{aid:aid});
        };

        $scope.goListMain=function(){
            $state.go("yxlist");
        };
    })
    .controller('yxslistCtrl', function ($scope,$rootScope,$ionicHistory,$http, $ionicPopup, $state,$cacheFactory,$stateParams,IndexFactory) {
        $scope.screenHeight=window.screen.availHeight;
        $scope.screenWidth=window.screen.availWidth;
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

        $scope.itemavatarstyle={
            "height":$scope.allheight*0.6+'px',
            "width":$scope.screenWidth+'px'
        }

        $scope.itemdescriptionstyle={
            "padding":"5px",
            "height": $scope.allheight*0.3+'px',
            "width":$scope.screenWidth+'px'
        }
        $scope.itemdivider={
            "height": $scope.allheight*0.1+'px',
            "width":$scope.screenWidth+'px',
            "background-color":"#dcdcdc"
        }

        IndexFactory.getIndexMain().then(function(response){
            var infolist=response.infolist;
            var districtlist=response.district;
            $scope.myName="";
            $scope.list=infolist;
            $scope.districtlist=districtlist;
        });
        $scope.goList=function(cid){

            //$state.go("zixun",{cid:cid,cityname:$scope.myName.name});
            IndexFactory.getLink(cid).then(function(response){
                var link=response.mainurl;
                if(link!=null&&link!='null')
                    window.location.href=link;
            });

        };

        $scope.$watch('myName',  function(newValue, oldValue) {

            if((newValue==null)||(oldValue==null)||(newValue === oldValue))
                return;
            IndexFactory.getIndexListByCity(newValue).then(function(response){
                $scope.list = response;
            });
        });

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
