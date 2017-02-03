angular.module("indexApp.controllers",['ionic', 'ionic-datepicker'])
    .controller('mainCtrl', function ($scope,$rootScope,$ionicHistory,$http, $ionicPopup, $state,$cacheFactory,listFactory) {
        $scope.screenHeight=window.screen.height;
        $scope.screenWidth=window.screen.width;
        $scope.headerStyle={
            'height':$scope.screenHeight*0.2+'px'
        };
        $scope.titleStyle={
            'height':$scope.screenHeight*0.1+'px',
            'background-color':'red',
            'padding':'0px'
        };
        $scope.title2Style={
            'height':$scope.screenHeight*0.05+'px',
            'margin':'0px',
            'padding':'0px'
        };
        $scope.title2imgStyle={
            'height':$scope.screenHeight*0.05+'px',
            'width':$scope.screenWidth+'px'
        };
        $scope.itemStyle={
            'height':$scope.screenHeight*0.1+'px',
            "border":"none"
        };
        $scope.itemavatarstyle={
            'height':$scope.screenHeight*0.1+'px',
            "width":$scope.screenWidth*0.33+'px'
        };

        $scope.showPopup = function() {
            //       $scope.data = {}

            // 自定义弹窗

            var myPopup = $ionicPopup.show({
                title: '您尚未登陆/注册',
                scope: $scope,
                buttons: [
                    {
                        text: '<b>登录</b>',
                        type: 'button-positive',
                        onTap: function(e) {
                            return 'login';
                        }
                    },
                    {
                        text: '<b>注册</b>',
                        type: 'button-positive',
                        onTap: function(e) {
                            return 'reg';
                        }
                    }
                ]
            });
            myPopup.then(function(res) {
                console.log('Tapped!', res);
            });
            $timeout(function() {
                myPopup.close(); // 3秒后关闭弹窗
            }, 3000);
        };

        $scope.itemClick=function(){
            $scope.showPopup();
        };

        $scope.goSalesReg=function(){
            console.log(QueryString())
            listFactory.CheckReg(QueryString(),'1').then(function(response){
                    console.log(response+"asdasdsad");
                    if(response=="[]"){
                        $state.go("salesreg");
                    }else{
                        $state.go("salesreg");
                    }

                }
            );
        };
        $scope.goCusReg=function(){
//            $state.go("customereg");
//            判断是否已注册

            console.log(QueryString())
            listFactory.CheckReg(QueryString(),'2').then(function(response){
                    console.log(response+"asdasdsad");
                    if(response=="[]"){
                        $state.go("customereg");
                    }else{
                        $state.go("customereg");
                    }

                }
            );
        };
        $scope.goSecureDetail=function(url){
            $state.go('securedetail', {sdid: url});
        }


    })
    .controller('RegCtrl', function ($scope,$rootScope,$ionicHistory,$http,$ionicModal, $ionicPopup, $timeout,$state,$cacheFactory,listFactory) {
        $scope.screenHeight=window.screen.height;
        $scope.screenWidth=window.screen.width;
        $scope.startDate = new Date();
        $scope.endDate = new Date();
        $scope.data = {'openid':'','city':'','enginenum':'','name':'','platenum':'','province':'','vin':'','startDate':'','endDate':'','branch':'','password':'','picurl':''};
        $scope.salesuccess = function() {
            // 自定义弹窗

            var myPopup = $ionicPopup.show({
                title: '注册成功！',
                scope: $scope,
                buttons: [
                    {
                        text: '<b>确定</b>',
                        type: 'button-positive',
                        onTap: function(e) {
                            return 'reg';
                        }
                    }
                ]
            });
            myPopup.then(function(res) {
                console.log('Tapped!', res);
            });
            $timeout(function() {
                myPopup.close(); // 3秒后关闭弹窗
            }, 3000);
        };
        $scope.showPopup = function() {
            // 自定义弹窗

            var myPopup = $ionicPopup.show({
                title: '登录成功，请重新预约',
                scope: $scope,
                buttons: [
                    {
                        text: '<b>我要预约</b>',
                        type: 'button-positive',
                        onTap: function(e) {
                            return 'reg';
                        }
                    }
                ]
            });
            myPopup.then(function(res) {
                console.log('Tapped!', res);
            });
            $timeout(function() {
                myPopup.close(); // 3秒后关闭弹窗
            }, 3000);
        };
        $scope.doReg=function(data){
            $scope.data.openid=$("#openid").val();
            $scope.data.startDate=startDate.innerText;
            $scope.data.endDate=endDate.innerText;
            listFactory.cusReg(data).then(function(response){
                    console.log(response);
                    $scope.showPopup();
                }
            );
//            $scope.showPopup();
        };
        $scope.doSalesReg=function(data){
            $scope.data.openid=QueryString();;
            var sampleFile = document.getElementById("code").files[0];
            var formdata = new FormData();
            formdata.append("sampleFile", sampleFile);
            var xhr = new XMLHttpRequest();
            xhr.open("POST","/file", true);
            xhr.send(formdata);
            xhr.onload = function(e) {
                if (this.status == 200) {
                    $scope.data.picurl=this.responseText;
                    listFactory.cusSalesReg(data).then(function(response){
                            console.log(response);
                            $scope.showPopup();
                        }
                    );
                }
            };

//            $scope.showPopup();
        };

    })
    .controller('MyCtrl', function($scope, $http, $ionicPopup, $state,$ionicLoading,listFactory) {

        $scope.data = {
            showDelete: false
        };

        $scope.edit = function(item) {
            alert('Edit Item: ' + item.id);
        };
        $scope.share = function(item) {
            alert('Share Item: ' + item.id);
        };

        $scope.moveItem = function(item, fromIndex, toIndex) {
            $scope.items.splice(fromIndex, 1);
            $scope.items.splice(toIndex, 0, item);
        };

        $scope.onItemDelete = function(item) {
            $scope.items.splice($scope.items.indexOf(item), 1);
        };
        $scope.itemClicked = function(url){
            //url是从article.html页面中传递过来的参数;第一个参数是路由的路径，第二个参数是给属性赋值的值；
            $state.go('secureDetail',{sdid:url});
        };
        listFactory.getSecure().then(function(response){
                $scope.items =response;
            }
        );
    })
    .controller('SecureDetailCtrl', function($scope,$stateParams, $http, $ionicPopup, $state,$ionicLoading,listFactory) {

        $scope.data = {
            showDelete: false
        };

        $scope.edit = function(item) {
            alert('Edit Item: ' + item.id);
        };
        $scope.share = function(item) {
            alert('Share Item: ' + item.id);
        };

        $scope.moveItem = function(item, fromIndex, toIndex) {
            $scope.items.splice(fromIndex, 1);
            $scope.items.splice(toIndex, 0, item);
        };

        $scope.onItemDelete = function(item) {
            $scope.items.splice($scope.items.indexOf(item), 1);
        };
        var sdid = $stateParams.sdid;
        listFactory.getSecureDtail(sdid).then(function(response){
                $scope.items =response;
            }
        );
    })
    .controller('ManCusCtrl', function($scope,$stateParams, $http, $ionicPopup, $state,$ionicLoading,listFactory) {

        $scope.data = {
            showDelete: false
        };

        $scope.edit = function(item) {
            alert('Edit Item: ' + item.id);
        };
        $scope.share = function(item) {
            alert('Share Item: ' + item.id);
        };

        $scope.moveItem = function(item, fromIndex, toIndex) {
            $scope.items.splice(fromIndex, 1);
            $scope.items.splice(toIndex, 0, item);
        };

        $scope.onItemDelete = function(item) {
            $scope.items.splice($scope.items.indexOf(item), 1);
        };
        var openid=QueryString();
        listFactory.getMangerList(openid).then(function(response){
                console.log(response);
                $scope.items =response;
            }
        );
    })
;

