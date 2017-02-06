angular.module("indexApp.services", [])
    .factory('listFactory', function($http,$stateParams, $q,$ionicPopup,$ionicLoading,$state){
        var param = {};
        param.curPage = 10;   // 每页的记录数
        param.hasmore = false;
        return {
            getSecureDtail: function getList(sdid){
                var deferred = $q.defer();
                var params="{\"sdid\":\""+sdid+"\"}";
                var url = '/car?domain=getSecureDetail&params='+encodeURIComponent(params);
                $http.post(url).success(function (response) {
                    deferred.resolve(response);
                }).error(function (data) {
                        console.log("getList网络联接失败!");
                        $ionicPopup.alert({
                            title:'<b>错误!</b>',
                            template:'网络联接失败!'
                        })
                        deferred.reject();
                    });
                return deferred.promise;
            },
            getUser: function getList(userid){
                var deferred = $q.defer();
                console.log(userid);
                var params="{\"userid\":\""+userid+"\"}";
                var url = '/car?domain=getUser&params='+encodeURIComponent(params);
                $http.post(url).success(function (response) {
                    deferred.resolve(response);
                }).error(function (data) {
                        console.log("getList网络联接失败!");
                        $ionicPopup.alert({
                            title:'<b>错误!</b>',
                            template:'网络联接失败!'
                        })
                        deferred.reject();
                    });
                return deferred.promise;
            },
            getSecure: function getList(){
                var deferred = $q.defer();
                var params="{\"sdid\":\"sdid\"}";
                var url = '/car?domain=getSecure&params='+encodeURIComponent(params);
                $http.post(url).success(function (response) {
                    deferred.resolve(response);
                }).error(function (data) {
                        console.log("getList网络联接失败!");
                        $ionicPopup.alert({
                            title:'<b>错误!</b>',
                            template:'网络联接失败!'
                        })
                        deferred.reject();
                    });
                return deferred.promise;
            },
            queryReg: function getList(openid,type){
                var deferred = $q.defer();
                console.log(openid);
                var params="{\"openid\":\""+openid+"\",\"type\":\""+type+"\"}";
                var url = '/car?domain=QueryReg&params='+encodeURIComponent(params);
                $http.post(url).success(function (response) {
                    deferred.resolve(response);
                }).error(function (data) {
                        console.log("getList网络联接失败!");
                        $ionicPopup.alert({
                            title:'<b>错误!</b>',
                            template:'网络联接失败!'
                        })
                        deferred.reject();
                    });
                return deferred.promise;
            },
            CheckReg: function getList(openid,type){
                var deferred = $q.defer();
                console.log(openid);
                var params="{\"openid\":\""+openid+"\",\"type\":\""+type+"\"}";
                var url = '/car?domain=CheckReg&params='+encodeURIComponent(params);
                $http.post(url).success(function (response) {
                    deferred.reject();
                    console.log(response.length);
                    if(response.length==0){
                        if(type==1){
                            $state.go("salesreg");
                        } else{
                            $state.go("customereg");
                        }
                    }  else{
                        if(type==1){
                            $state.go("userList");
                        }else{
                            $state.go("managerlist");
                        }
                    }

                }).error(function (data) {
                        console.log("getList网络联接失败!");
                        $ionicPopup.alert({
                            title:'<b>错误!</b>',
                            template:'网络联接失败!'
                        })
                        deferred.reject();
                    });
                return deferred.promise;
            },
            getUserInfo: function getList(openid,type){
                var deferred = $q.defer();
                console.log(openid);
                var params="{\"openid\":\""+openid+"\",\"type\":\""+type+"\"}";
                var url = '/car?domain=CheckReg&params='+encodeURIComponent(params);
                $http.post(url).success(function (response) {
                    console.log(response.length);
                    deferred.resolve(response);
                }).error(function (data) {
                        console.log("getList网络联接失败!");
                        $ionicPopup.alert({
                            title:'<b>错误!</b>',
                            template:'网络联接失败!'
                        })
                        deferred.reject();
                    });
                return deferred.promise;
            },
            cusEdit: function getList(data){
                var deferred = $q.defer();
                console.log(data);
                var params="{\"startDate\":\""+data["startDate"]+"\",\"endDate\":\""+data["endDate"]+"\",\"openid\":\""+data["openid"]+"\",\"name\":\""+data["name"]+"\",\"city\":\""+data["city"]+"\",\"province\":\""+data["province"]+"\",\"platenum\":\""+data["platenum"]+"\",\"vin\":\""+data["vin"]+"\",\"enginenum\":\""+data["enginenum"]+"\"}";
                var url = '/car?domain=cusReg&params='+encodeURIComponent(params);
                $http.post(url).success(function (response) {
                    if(response.trim()=="true"){
                        deferred.resolve(response);
                    } else{
                        $ionicPopup.alert({
                            title:'<b>错误!</b>',
                            template:'网络联接失败!'
                        })
                        deferred.reject();
                    }
                    deferred.resolve(response);
                }).error(function (data) {
                        console.log("getList网络联接失败!");
                        $ionicPopup.alert({
                            title:'<b>错误!</b>',
                            template:'网络联接失败!'
                        })
                        deferred.reject();
                    });
                return deferred.promise;
            },
            cusReg: function getList(data){
                var deferred = $q.defer();
                console.log(data);
                var params="{\"startDate\":\""+data["startDate"]+"\",\"endDate\":\""+data["endDate"]+"\",\"openid\":\""+data["openid"]+"\",\"name\":\""+data["name"]+"\",\"city\":\""+data["city"]+"\",\"province\":\""+data["province"]+"\",\"platenum\":\""+data["platenum"]+"\",\"vin\":\""+data["vin"]+"\",\"enginenum\":\""+data["enginenum"]+"\"}";
                var url = '/car?domain=cusReg&params='+encodeURIComponent(params);
                $http.post(url).success(function (response) {
                    if(response.trim()=="true"){
                        deferred.resolve(response);
                    } else{
                        $ionicPopup.alert({
                            title:'<b>错误!</b>',
                            template:'网络联接失败!'
                        })
                        deferred.reject();
                    }
                    deferred.resolve(response);
                }).error(function (data) {
                        console.log("getList网络联接失败!");
                        $ionicPopup.alert({
                            title:'<b>错误!</b>',
                            template:'网络联接失败!'
                        })
                        deferred.reject();
                    });
                return deferred.promise;
            },
            cusSalesReg: function getList(data){
                var deferred = $q.defer();
                console.log(data);
                var params="{\"name\":\""+data["name"]+"\",\"picurl\":\""+data["picurl"]+"\",\"openid\":\""+data["openid"]+"\",\"branch\":\""+data["branch"]+"\",\"password\":\""+data["password"]+"\",\"province\":\""+data["province"]+"\",\"city\":\""+data["city"]+"\"}";
                var url = '/car?domain=SalesReg&params='+encodeURIComponent(params);
                $http.post(url).success(function (response) {
                    if(response.trim()=="true"){
                        deferred.resolve(response);
                    } else{
                        $ionicPopup.alert({
                            title:'<b>错误!</b>',
                            template:'网络联接失败!'
                        })
                        deferred.reject();
                    }
                    deferred.resolve(response);
                }).error(function (data) {
                        console.log("getList网络联接失败!");
                        $ionicPopup.alert({
                            title:'<b>错误!</b>',
                            template:'网络联接失败!'
                        })
                        deferred.reject();
                    });
                return deferred.promise;
            },
            getMangerList:function getList(openid){
                var deferred = $q.defer();
                console.log(openid);
                var params="{\"openid\":\""+openid+"\"}";
                var url = '/car?domain=getMangerList&params='+encodeURIComponent(params);
                $http.post(url).success(function (response) {
                    deferred.resolve(response);
                }).error(function (data) {
                        console.log("getList网络联接失败!");
                        $ionicPopup.alert({
                            title:'<b>错误!</b>',
                            template:'网络联接失败!'
                        })
                        deferred.reject();
                    });
                return deferred.promise;
            },
            getUserList:function getList(openid){
                var deferred = $q.defer();
                var params="{\"openid\":\""+openid+"\"}";
                var url = '/car?domain=getUserList&params='+encodeURIComponent(params);
                $http.post(url).success(function (response) {
                    deferred.resolve(response);
                }).error(function (data) {
                        console.log("getList网络联接失败!");
                        $ionicPopup.alert({
                            title:'<b>错误!</b>',
                            template:'网络联接失败!'
                        })
                        deferred.reject();
                    });
                return deferred.promise;
            }
        }
    })
;