angular.module("indexApp.services", [])
    .factory('IndexFactory', function($http, $q,$ionicPopup,$ionicLoading){
        return {
            getIndexList: function getIndexList() {
                var deferred = $q.defer();
                var url = 'http://172.16.8.159:8080/city?domain=getIndexList';

                $ionicLoading.show({
                    template: 'Loading...'
                });

                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    var resultJson = response;
                    var districtJson=response;
                    var district=[];
                    var info=[];
                    for(var i=0;i<resultJson.length;i++)
                    {
                        district.push(resultJson[i]);
                        info.push(resultJson[i]);
                    }
                    var root={
                        infolist:info,
                        district:district
                    };
                    deferred.resolve(root);

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
                var url = 'http://172.16.8.159:8080/city?domain=getIndexListByCityName';
                if(cityid!=null)
                    url+='&cityid='+cityid;
                $ionicLoading.show({
                    template: 'Loading...'
                });

                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                //    var imgdetail=response.imginfo;
                //    var articlelist=response.articlelist;
                //    var listarray=[];
                //    for(var i=0;i<articlelist.length;i++)
                //    {
                //        listarray.push(articlelist[i]);
                //    }
                //    var jsonroot={
                //         img:imgdetail,
                //         articlelist:listarray
                //    };

                    deferred.resolve(response);
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
            getLink: function getLink(cityid) {
                var deferred = $q.defer();
                var url = 'http://172.16.8.159:8080/city?domain=getLink';
                if(cityid!=null)
                    url+='&cityid='+cityid;
                $ionicLoading.show({
                    template: 'Loading...'
                });

                $http.post(url).success(function (response) {
                    $ionicLoading.hide();

                    deferred.resolve(response);
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
            getArticleDetails: function getArticleDetails(cityid) {
                var deferred = $q.defer();
                var url = 'http://172.16.8.159:8080/city?domain=getArticleDetails';
                if(cityid!=null)
                    url+='&cityid='+cityid;
                $ionicLoading.show({
                    template: 'Loading...'
                });

                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    var articleJson=response.article;
                    var imgdetail=response.imginfo;
                        var articlelist=response.articlelist;
                        var listarray=[];
                        for(var i=0;i<articlelist.length;i++)
                        {
                            listarray.push(articlelist[i]);
                        }
                        var jsonroot={
                             img:imgdetail,
                             articlelist:listarray
                        };
                    deferred.resolve(jsonroot);
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
                var url = 'http://172.16.8.159:8080/city?domain=getArticleDetailsInfo';
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
            }
        }
    })
;