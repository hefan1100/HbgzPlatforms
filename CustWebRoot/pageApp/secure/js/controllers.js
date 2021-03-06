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
        $scope.goAllMan=function(){
            $state.go("allManager");
        };
        $scope.goAllUsers=function(){
            $state.go("allUsers");
        };
        $scope.goSalesReg=function(){
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
            if(data["name"]==""){
                $ionicPopup.alert({
                    title:'<b>错误</b>',
                    template:'请填写用户姓名！'
                })
            }else if(data["city"]==""){
                $ionicPopup.alert({
                    title:'<b>错误</b>',
                    template:'请填写城市！'
                })
            }else if(data["province"]==""){
                $ionicPopup.alert({
                    title:'<b>错误</b>',
                    template:'请填写省份！'
                })
            }else if(data["platenum"]==""){
                $ionicPopup.alert({
                    title:'<b>错误</b>',
                    template:'请填写车牌号码！'
                })
            }else if(data["platenum"].length!=6){
                $ionicPopup.alert({
                    title:'<b>错误</b>',
                    template:'请正确填写车牌号码！'
                })
            }else if(data["vin"]==""){
                $ionicPopup.alert({
                    title:'<b>错误</b>',
                    template:'请填写车架号！'
                })
            }else if( data["vin"].length!=17){
                $ionicPopup.alert({
                    title:'<b>错误</b>',
                    template:'请正确填写车架号！'
                })
            }else if(data["enginenum"]==""){
                $ionicPopup.alert({
                    title:'<b>错误</b>',
                    template:'请填写发动机号！'
                })
            }else if( data["enginenum"].length!=7){
                $ionicPopup.alert({
                    title:'<b>错误</b>',
                    template:'请正确填写发动机号！'
                })
            }else {
                listFactory.cusReg(data).then(function(response){
                        console.log(response);
                    }
                );
            }

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
    .controller('cusEditCtrl', function ($scope,$rootScope,$stateParams,$ionicHistory,$http,$ionicModal, $ionicPopup, $timeout,$state,$cacheFactory,listFactory) {
        $scope.screenHeight=window.screen.height;
        $scope.screenWidth=window.screen.width;
        $scope.citys = [
            {name:'请选择', cid:'0'},
            {name:'武汉', cid:'1'},
            {name:'宜昌', cid:'2'},
            {name:'襄阳', cid:'3'},
            {name:'荆州', cid:'4'},
            {name:'黄冈', cid:'5'},
            {name:'仙桃', cid:'6'},
            {name:'十堰', cid:'7'},
            {name:'恩施', cid:'8'},
            {name:'孝感', cid:'9'},
            {name:'咸宁', cid:'10'},
            {name:'随州', cid:'11'},
            {name:'神农架', cid:'12'},
            {name:'天门', cid:'13'},
            {name:'潜江', cid:'14'}
        ];
        $scope.provinces = [
            {name:'请选择', pid:'0'},
            {name:'湖北', pid:'1'}
        ];
        $scope.usertypes = [
            {name:'请选择', utid:'0'},
            {name:'新用户', utid:'1'},
            {name:'老用户', utid:'1'},
            {name:'潜在客户', utid:'1'}
        ];
        $scope.remindtimes = [
            {name:'请选择', utid:'0'},
            {name:'15天', utid:'1'},
            {name:'30天', utid:'1'},
            {name:'90天', utid:'1'}
        ];
        $scope.data = {'userid':'','city':'','enginenum':'','name':'','platenum':'','province':'','vin':'','startDate':'','endDate':'','branch':'','usertype':'','picurl':'','managerid':''};
        $scope.salesuccess = function() {
            // 自定义弹窗
            var myPopup = $ionicPopup.show({
                title: '编辑成功！',
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
        var did = $stateParams.did;
        listFactory.getUser(did).then(function(response){
                $scope.items =response[0];
                $scope.startDate = new Date(response[0].D_STARTDATE);
                $scope.endDate = new Date(response[0].D_ENDDATE);
                $scope.items.D_DETAILID=response[0].D_DETAILID;//如果想要第一个值
                $scope.items.D_CITY=response[0].D_CITY;//如果想要第一个值
                $scope.items.D_USERTYPE=response[0].D_USERTYPE;//如果想要第一个值
                $scope.items.D_PROVINCE=response[0].D_PROVINCE;//如果想要第一个值
                console.log($scope.items.D_NAME );
            }
        );
        $scope.cusEdit=function(data){
            $scope.data.managerid=QueryString();
            $scope.data.userid=$scope.items.D_DETAILID;
            $scope.data.name=$("#name").val();
            $scope.data.vin=$("#vin").val();
            $scope.data.platenum=$("#platenum").val();
            $scope.data.enginenum=$("#enginenum").val();
            $scope.data.city=$("#city").find("option:selected").text();
            $scope.data.usertype=$("#usertype").find("option:selected").text();
            $scope.data.province=$("#province").find("option:selected").text();
            $scope.data.startDate=startDate.innerText;
            $scope.data.endDate=endDate.innerText;
            listFactory.cusEdit(data).then(function(response){
                    console.log(response);
                    $scope.salesuccess();
                }
            );
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
        $scope.itemClicked = function(url){
            //url是从article.html页面中传递过来的参数;第一个参数是路由的路径，第二个参数是给属性赋值的值；
            $state.go('secureDetail',{sdid:url});
        }
        var sdid = $stateParams.sdid;
        listFactory.getSecureDtail(sdid).then(function(response){
                $scope.items =response;
            }
        );
        listFactory.getDetailItem(sdid).then(function(response){
                $scope.detailitems =response;
            }
        );
    })
    .controller('AllManCtrl', function($scope,$stateParams, $http, $ionicPopup, $state,$ionicLoading,listFactory) {

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
        listFactory.getUserList("").then(function(response){
                console.log(response);
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
        $scope.goAllMan=function(){
            $state.go("allManager");
        };
        var openid=QueryString();
        listFactory.getMangerList(openid).then(function(response){
                console.log(response);
                $scope.items =response;
            }
        );
    })
    .controller('UserCtrl', function($scope,$stateParams, $http, $ionicPopup, $state,$ionicLoading,listFactory) {
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
        $scope.goAllUsers=function(){
            $state.go("allUsers");
        };
        var openid=QueryString();
        listFactory.getUserInfo(QueryString(),'').then(function(response){
                console.log(response);
                $scope.items =response;
            }
        );
    })
    .controller('UserListCtrl', function($scope,$stateParams, $http, $ionicPopup, $state,$ionicLoading,listFactory) {
        $scope.screenHeight=window.screen.height;
        $scope.screenWidth=window.screen.width;
        $scope.data = {'openid':'','city':'','enginenum':'','name':'','platenum':'','province':'','vin':'','startDate':'','endDate':'','branch':'','password':'','picurl':''};

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
            $state.go('cusEdit',{did:url});
        };
        var openid=QueryString();
        listFactory.getUserList(openid).then(function(response){
                console.log(response);
                $scope.items =response;
            }
        );
    })
    .controller('AllUserCtrl', function($scope,$stateParams, $http, $ionicPopup, $state,$ionicLoading,listFactory) {
        $scope.data = {
            showDelete: false
        };
        $scope.items=[];
        $scope.col = 'D_NAME';//默认按name列排序
        $scope.desc = 0;//默认排序条件升序
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

        listFactory.queryUserByName("").then(function(response){
                console.log(response);
                $scope.items =response;

            }
        );

        $scope.bind=function(userid){
            var managerid= QueryString();
            listFactory.binduser(managerid,userid).then(function(response){
                    console.log(response);
                }
            );
        }
        console.log($scope.items);
    })
;