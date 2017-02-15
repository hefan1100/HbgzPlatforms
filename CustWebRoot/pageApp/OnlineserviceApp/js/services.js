angular.module("indexApp.services", [])
    .factory('IndexFactory', function($http, $q,$ionicPopup,$ionicLoading){
        return {
            getIndexList: function getIndexList() {
                var deferred = $q.defer();
                var url = 'http://120.27.221.0/yxs?domain=getIndexList';

                $ionicLoading.show({
                    template: 'Loading...'
                });

                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    var resultJson=response;
                    var objarray = [];
                    for (var i = 0; i < resultJson.length; i++) {
                        objarray.push(resultJson[i]);
                    }
                    deferred.resolve(objarray);
                }).error(function (data) {
                    $ionicLoading.hide();
                    console.log("getList网络联接失败!");
                    $ionicPopup.alert({
                        title: '<b>错误!</b>',
                        template: '网络联接失败!'
                    })
                    deferred.reject();
                });
                return deferred.promise;
            },

            getIndexMain: function getIndexMain() {
                var deferred = $q.defer();
                var url = 'http://120.27.221.0/yxs?domain=getListMain';

                $ionicLoading.show({
                    template: 'Loading...'
                });

                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    var districtJson = response.district;
                    var articleJson=response.article;
                    if (districtJson.toString().trim() == 'null'&&articleJson.toString().trim() == 'null') {
                        deferred.resolve('null');
                    } else {
                        var districtarray = [];
                        for (var i = 0; i < districtJson.length; i++) {
                            districtarray.push(districtJson[i]);
                        }
                        var articlearray = [];
                        for (var i = 0; i < articleJson.length; i++) {
                            articlearray.push(articleJson[i]);
                        }
                        var root={
                            district:districtarray,
                            article:articlearray
                        }
                        deferred.resolve(root);
                    }
                }).error(function (data) {
                    $ionicLoading.hide();
                    console.log("getList网络联接失败!");
                    $ionicPopup.alert({
                        title: '<b>错误!</b>',
                        template: '网络联接失败!'
                    })
                    deferred.reject();
                });
                return deferred.promise;
            },
            getIndexListByCity: function getIndexListByCity(cityid) {
                var deferred = $q.defer();
                var url = 'http://120.27.221.0/yxs?domain=getIndexListByCityName';
                if(cityid!=null)
                    url+='&cityid='+cityid;
                $ionicLoading.show({
                    template: 'Loading...'
                });

                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    var articleJson=response.article;
                    if (articleJson.toString().trim() == 'null') {
                        deferred.resolve('null');
                    } else {
                        var articlearray = [];
                        for (var i = 0; i < articleJson.length; i++) {
                            articlearray.push(articleJson[i]);
                        }
                        deferred.resolve(articlearray);
                    }
                }).error(function (data) {
                    $ionicLoading.hide();
                    console.log("getList网络联接失败!");
                    $ionicPopup.alert({
                        title: '<b>错误!</b>',
                        template: '网络联接失败!'
                    })
                    deferred.reject();
                });
                return deferred.promise;
            },
            getArticleDetails: function getArticleDetails(yxsid) {
                var deferred = $q.defer();
                var url = 'http://120.27.221.0/yxs?domain=getArticleDetails';
                if(yxsid!=null)
                    url+='&yxsid='+yxsid;
                $ionicLoading.show({
                    template: 'Loading...'
                });

                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    var articleJson=response.article;
                    if (articleJson.toString().trim() == 'null') {
                        deferred.resolve('null');
                    } else {
                        var articlearray = [];
                        for (var i = 0; i < articleJson.length; i++) {
                            articlearray.push(articleJson[i]);
                        }
                        deferred.resolve(articlearray);
                    }
                }).error(function (data) {
                    $ionicLoading.hide();
                    console.log("getArticleDetails网络联接失败!");
                    $ionicPopup.alert({
                        title: '<b>错误!</b>',
                        template: '网络联接失败!'
                    })
                    deferred.reject();
                });
                return deferred.promise;
            },
            getArticleDetailsInfo: function getArticleDetailsInfo(aid) {
                var deferred = $q.defer();
                var url = 'http://120.27.221.0/yxs?domain=getArticleDetailsInfo';
                if(aid!=null)
                    url+='&aid='+aid;
                $ionicLoading.show({
                    template: 'Loading...'
                });

                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    deferred.resolve(response);
                }).error(function (data) {
                    $ionicLoading.hide();
                    console.log("getArticleDetailsInfo网络联接失败!");
                    $ionicPopup.alert({
                        title: '<b>错误!</b>',
                        template: '网络联接失败!'
                    })
                    deferred.reject();
                });
                return deferred.promise;
            },

            submitForAdvise: function submitForAdvise(adviseinfo) {
                var deferred = $q.defer();
                var params="{\"name\":\""+adviseinfo.name+"\",\"sex\":\""+adviseinfo.sex+"\",\"advisecontent\":\""+adviseinfo.advisecontent+"\",\"ordernumber\":\""+adviseinfo.ordernumber+"\",\"phonenumber\":\""+adviseinfo.phonenumber+"\"}";
                var url = 'http://192.168.1.106:8080/chats?domain=submitForAdvise&params='+encodeURIComponent(params);
                $ionicLoading.show({
                    template: 'Loading...'
                });

                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    deferred.resolve(response);
                }).error(function (data) {
                    $ionicLoading.hide();
                    console.log("submitForAdvise网络联接失败!");
                    $ionicPopup.alert({
                        title: '<b>错误!</b>',
                        template: '网络联接失败!'
                    })
                    deferred.reject();
                });
                return deferred.promise;
            },
            getChatsReply: function getChatsReply(sendMessage) {
                var deferred = $q.defer();
                var url = 'http://120.27.221.0/chats?domain=getChatsReply';
                if(sendMessage!=null)
                    url+='&send='+sendMessage;
                $ionicLoading.show({
                    template: 'Loading...'
                });

                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    deferred.resolve(response);
                }).error(function (data) {
                    $ionicLoading.hide();
                    console.log("getArticleDetailsInfo网络联接失败!");
                    $ionicPopup.alert({
                        title: '<b>错误!</b>',
                        template: '网络联接失败!'
                    })
                    deferred.reject();
                });
                return deferred.promise;
            }
        }
    })
;