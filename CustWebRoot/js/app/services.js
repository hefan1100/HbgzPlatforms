angular.module("indexApp.services", [])
    .factory('listFactory', function($http, $q,$ionicPopup){
        var param = {};
        param.curPage = 10;   // 每页的记录数
        param.hasmore = false;

        return {
            getCount: function getCount(userId){
                var deferred = $q.defer();
                var url = 'http://192.168.10.100:8118/service?domain=queryAllCount&userId='+userId;
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
            getList: function getList(domain,userId){
                var deferred = $q.defer();
                var url = 'http://192.168.10.100:8118/service?domain='+domain+'&userId='+userId;
                $http.post(url).success(function (response) {
                    var resultJson = response;
                    if(resultJson.toString().trim() == 'null') {
                        deferred.resolve('null');
                    }else{
                        var array = [];
                        for (var i = 0; i < (resultJson.length<=param.curPage?resultJson.length:param.curPage) ; i++) {
                            array.push(resultJson[i]);
                        }
                        deferred.resolve(array);
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
            param:param
        };
    })
    .factory('detailFactory', function($http, $q){
        var param = {};
        param.curPage = 10;   // 每页的记录数
        param.hasmore = false;
        return {
            getList: function getList(domain,demdId){
                var deferred = $q.defer();
                var url = 'http://192.168.10.100:8118/service?domain='+domain+'&demdId='+demdId;
                $http.post(url).success(function (response) {
                    var resultJson = response;
                    var array = [];
                    array.push(resultJson);
                    deferred.resolve(array);
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
            getWorkRel: function getWorkRel(name,demdId){
                var deferred = $q.defer();
                var JName = { "queryBidInfoDetail":"bid_info","queryBidMoneyDetail":"bid_money_info","queryCttInfoDetail":"ctt_info","queryPurcConDetail":"purc_order_info","queryStoCkDetail":"sto_ck_info","queryIndecDetail":"inc_rec_billing","queryInvAppDetail":"invoice_apply_info","queryPayAppDetail":"pay_apply_info" };
                var DName = eval('JName.'+name);
                var url = 'http://192.168.10.100:8118/service?domain=queryWorkRel&name='+DName+'&demdId='+demdId;
                $http.post(url).success(function (response) {
                    var resultJson = response;//return like {"WORKFLOW_ID":2140,"STATE":"C0C","NODE_DOMAIN":"bid_info","OBJE_ID":122,"WORKFLOW_REL_ID":9581}
                    deferred.resolve(resultJson);
                }).error(function (data) {
                        console.log("getWorkRel网络联接失败!");
                        $ionicPopup.alert({
                            title:'<b>错误!</b>',
                            template:'网络联接失败!'
                        })
                        deferred.reject();
                    });
                return deferred.promise;
            },
            getWorkList: function getWorkList(demdId){
                var deferred = $q.defer();
                var url = 'http://192.168.10.100:8118/service?domain=queryWorkDetail&demdId='+demdId;
                $http.post(url).success(function (response) {
                    var resultJson = response;//return like [{"ACTION":"C0W-C0C","TARG_USER_ID":"232","CURR_NODE_ID":"node_1","POINTER":null,"STATE_DATE":"2015-12-14 16:23:04","REMARKS":"同意！","WORKFLOW_LOG_ID":25901,"DEAL_USER_ID":"232","DEAL_USER_NAME":"陈少蓉","LAST_NODE_ID":null,"TARG_USER_NAME":"陈少蓉","WORKFLOW_REL_ID":9581}]
                    deferred.resolve(resultJson);
                }).error(function (data) {
                        console.log("getWorkList网络联接失败!");
                        $ionicPopup.alert({
                            title:'<b>错误!</b>',
                            template:'网络联接失败!'
                        })
                        deferred.reject();
                    });
                return deferred.promise;
            },
            getCurrentWork: function getCurrentWork(name,demdId){
                var deferred = $q.defer();
                var url = 'http://192.168.10.100:8118/service?domain=queryCurWorkDetail&name=' + name + '&demdId=' + demdId;
                $http.post(url).success(function (response) {
                    var resultJson = response;//return like {"ACTION":null,"TARG_USER_ID":"255","CURR_NODE_ID":"node_2","POINTER":"P","STATE_DATE":null,"REMARKS":null,"WORKFLOW_LOG_ID":25902,"DEAL_USER_ID":null,"DEAL_USER_NAME":null,"LAST_NODE_ID":"node_1","TARG_USER_NAME":"汪振华","WORKFLOW_REL_ID":9581}
                    deferred.resolve(resultJson);
                }).error(function (data) {
                        console.log("getCurrentWork网络联接失败!");
                        $ionicPopup.alert({
                            title:'<b>错误!</b>',
                            template:'网络联接失败!'
                        })
                        deferred.reject();
                    });
                return deferred.promise;
            },
            queryNextOp:function queryNextOp(name,lanId,currNodeid,workLogId){
                var deferred = $q.defer();
                var JName = { "queryBidInfoDetail":"bid_info","queryBidMoneyDetail":"bid_money_info","queryCttInfoDetail":"ctt_info","queryPurcConDetail":"purc_order_info","queryStoCkDetail":"sto_ck_info","queryIncRecDetail":"inc_rec_billing","queryInvAppDetail":"invoice_apply_info","queryPayAppDetail":"pay_apply_info" };
                var DName = eval('JName.'+name);
                var url = 'http://192.168.10.100:8118/service?domain=queryNextOp&name='+DName+'&lanId='+lanId+'&userId='+currNodeid+'&demdId='+workLogId;
                alert(url)
                $http.post(url).success(function (response) {
                    var resultJson = response;//return like {"CONTACT":"18907181688","USER_ID":613,"USER_NAME":"闵春林"}
                    deferred.resolve(resultJson);
                }).error(function (data) {
                        console.log("queryNextOp网络联接失败!");
                        $ionicPopup.alert({
                            title:'<b>错误!</b>',
                            template:'网络联接失败!'
                        })
                        deferred.reject();
                    });
                return deferred.promise;
            },
            goNextWork: function goNextWork(state, userId, currNodeId, workLogId, lastNodeId, doWhat,doId, lanId, yornToStart, workRelId, remarks, yornSms){
                var deferred = $q.defer();
                var JName = { "queryBidInfoDetail":"bid_info","queryBidMoneyDetail":"bid_money_info","queryCttInfoDetail":"ctt_info","queryPurcConDetail":"purc_order_info","queryStoCkDetail":"sto_ck_info","queryIncRecDetail":"inc_rec_billing","queryInvAppDetail":"invoice_apply_info","queryPayAppDetail":"pay_apply_info" };
                var DName = eval('JName.'+doWhat);
                var url = 'http://192.168.10.100:8118/service?domain=doWork&state=' + state + '&userId=' + userId + '&currNodeId=' + currNodeId + '&workLogId=' + workLogId + '&lastNodeId=' + lastNodeId + '&name=' + DName + '&demdId=' + doId + '&lanId=' + lanId + '&yornToStart=' + yornToStart + '&workRelId=' + workRelId + '&remarks=' + remarks + '&yornSms=' + yornSms;
                $http.post(url).success(function (response) {
                    var resultJson = response;//return like
                    deferred.resolve(resultJson);
                }).error(function (data) {
                        console.log("goNextWork网络联接失败!");
                        $ionicPopup.alert({
                            title:'<b>错误!</b>',
                            template:'网络联接失败!'
                        })
                        deferred.reject();
                    });
                return deferred.promise;
            },
            param:param
        };
    })
    .factory('doDemdEventFactory', function($http, $q){
        return {
            getList: function getList(mobileNumber,demdId,doWhat,nodeId,nextNodeId,nextDealManager,textarea,evaluate,manager_1,number_1,
                                      manager_2,number_2,manager_3,number_3,manager_4,number_4,manager_5,number_5){
                var deferred = $q.defer();
                var url = '/service?domain=doDemdEvent&mobileNumber='+mobileNumber+'&demdId='+demdId+'&doWhat='+doWhat+'&nodeId='+nodeId
                    +'&nextNodeId='+nextNodeId+'&nextDealManager='+nextDealManager+'&textarea='+encodeURIComponent(textarea)+'&evaluate='+evaluate
                    +'&manager_1='+manager_1+'&number_1='+number_1+'&manager_2='+manager_2+'&number_2='+number_2+'&manager_3='+manager_3
                    +'&number_3='+number_3+'&manager_4='+manager_4+'&number_4='+number_4+'&manager_5='+manager_5+'&number_5='+number_5;
                $http.post(url).success(function (response) {
                    deferred.resolve(response);
                }).error(function (data) {
                        console.log("doDemdEvent网络联接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            }
        };
    })