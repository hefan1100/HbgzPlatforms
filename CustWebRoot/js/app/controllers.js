angular.module("indexApp.controllers",[])
    .controller('mainCtrl', function ($scope, $http, $ionicPopup, $state,listFactory) {
        $scope.staff ="测试用户";
        $scope.dept ="ICT支撑中心";

        $scope.goToCheckList = function (o) {
            $state.go('checkList',{doIt:o});
        }

        $scope.alert = function(){
            $ionicPopup.alert({
                title:'<b>消息</b>',
                template:'hi！'
            })
        }
    })

    .controller('checkListCtrl', function ($scope, $http, $stateParams, $timeout, $state, $ionicPopup, listFactory) {
        $scope.showDetail = function (o) {
            var stateName = $stateParams.doIt + 'Detail';
            $state.go(stateName,{domain:stateName,demdId:o});
        }
        var JName = { "queryBidInfo":"投标","queryBidMoney":"保证金","queryCttInfo":"合同","queryPurcCon":"需求单","queryStoCk":"出库","queryIncRec":"列收","queryInvApp":"开票","queryPayApp":"付款" };
        $scope.domainName = eval('JName.'+$stateParams.doIt);
        var dataDirectory = "";
        document.addEventListener('deviceready', function () {
            dataDirectory = cordova.file.dataDirectory;
            $http.get(dataDirectory+'/login.json').success(function(response) {
                var userId = response[0].id;
                //下拉刷新
                $scope.doRefresh = function() {
                    listFactory.param.curPage = 10;
                    listFactory.getList($stateParams.doIt,userId).then(function(response){
                        $scope.items = response;
                        // 停止广播ion-refresher
                        $scope.$broadcast('scroll.refreshComplete');
                    });
                };
                $scope.items = [];
                $scope.ifIsNULL = false;
                $scope.ifIsError = false;
                listFactory.param.hasmore = true;
                //更多
                $scope.loadMore = function() {
                    $timeout(function() {
                        if(!listFactory.param.hasmore){
                            $scope.$broadcast('scroll.infiniteScrollComplete');
                            return;
                        }
                        listFactory.getList($stateParams.doIt,userId).then(function(response){
                            listFactory.param.hasmore = response.length>=listFactory.param.curPage ;
                            $scope.ifIsError = response == 'null';
                            $scope.ifIsNULL = response.length == 0;
                            $scope.items = response;
                            $scope.$broadcast('scroll.infiniteScrollComplete');
                            listFactory.param.curPage += 10;
                        });
                    },2000);
                };
                $scope.moreDataCanBeLoaded = function(){
                    return listFactory.param.hasmore;
                }
                $scope.$on('stateChangeSuccess', function() {
                    $scope.loadMore();
                });
            })
        });

        $scope.goToMain = function () {
            $state.go("tabs.main");
        }

    })

    .controller('queryDetailCtrl', function ($scope,$window,$http, $stateParams, $timeout, $state, $ionicPopup,$ionicModal,$cordovaToast, detailFactory) {
        //下拉刷新
        $scope.doRefresh = function() {
            detailFactory.param.curPage = 10;
            detailFactory.getList($stateParams.domain,$stateParams.demdId).then(function(response){
                $scope.items = response;
                // 停止广播ion-refresher
                $scope.$broadcast('scroll.refreshComplete');
            });
        };
        $scope.items = [];
        detailFactory.param.hasmore = true;
        //更多
        $scope.loadMore = function() {
            //这里使用定时器是为了缓存一下加载过程，防止加载过快
            $timeout(function() {
                if(!detailFactory.param.hasmore){
                    $scope.$broadcast('scroll.infiniteScrollComplete');
                    return;
                }
                detailFactory.getList($stateParams.domain,$stateParams.demdId).then(function(response){
                    detailFactory.param.hasmore = response.length>=detailFactory.param.curPage ;
                    $scope.items = response;
                    $scope.$broadcast('scroll.infiniteScrollComplete');
                    detailFactory.param.curPage += 10;
                });
            },2000);
        };
        $scope.moreDataCanBeLoaded = function(){
            return detailFactory.param.hasmore;
        }
        $scope.$on('stateChangeSuccess', function() {
            $scope.loadMore();
        });
        $scope.$window=$window;

        $scope.goToMain = function () {
            $window.history.back();
        }
        var modalURL = 'templates/'+$stateParams.domain + 'Work.html';
        $ionicModal.fromTemplateUrl('templates/workSheet.html', {// modal窗口选项
            scope: $scope,
            animation: 'slide-in-up'
        }).then(function(modal) {
                $scope.modal = modal;
            });
        $scope.openModal = function() {
            $scope.modal.show();
            $scope.yornSms = [
                {text:"短信通知此人",checked:true}
            ];
            $scope.toWhereSelectOptions = [
                {id:0, name:'上一步'},
                {id:1, name: '第一步'}
            ];
            $scope.yornToStart = {id:0};
            $scope.suggest = {inStr:''};

            detailFactory.getWorkRel($stateParams.domain,$stateParams.demdId).then(function(response){
                var workRelId = response.WORKFLOW_REL_ID;//obj:{"WORKFLOW_ID":2140,"STATE":"C0C","NODE_DOMAIN":"bid_info","OBJE_ID":122,"WORKFLOW_REL_ID":9581}
                if (typeof(workRelId) == "undefined") {
                    $cordovaToast.showShortCenter("查询失败!");
                    return;
                }
                $scope.workRelId = workRelId;
                detailFactory.getWorkList(workRelId).then(function(response){
                    $scope.checkLogs = response;//obj:[{"ACTION":"C0W-C0C","TARG_USER_ID":"232","CURR_NODE_ID":"node_1","POINTER":null,"STATE_DATE":"2015-12-14 16:23:04","REMARKS":"同意！","WORKFLOW_LOG_ID":25901,"DEAL_USER_ID":"232","DEAL_USER_NAME":"陈少蓉","LAST_NODE_ID":null,"TARG_USER_NAME":"陈少蓉","WORKFLOW_REL_ID":9581}]
                    var workLogId = response[response.length-1].WORKFLOW_LOG_ID;
                    if (typeof(workLogId) == "undefined") {
                        $cordovaToast.showShortCenter("查询失败!");
                        return;
                    }
                    $scope.workLogId = workLogId;
                    detailFactory.getCurrentWork(workRelId,workLogId).then(function(response){
                        //obj:{"ACTION":null,"TARG_USER_ID":"255","CURR_NODE_ID":"node_2","POINTER":"P","STATE_DATE":null,"REMARKS":null,"WORKFLOW_LOG_ID":25902,"DEAL_USER_ID":null,"DEAL_USER_NAME":null,"LAST_NODE_ID":"node_1","TARG_USER_NAME":"汪振华","WORKFLOW_REL_ID":9581}
                        if (typeof(response.CURR_NODE_ID) == "undefined") {
                            $cordovaToast.showShortCenter("查询失败!");
                            return;
                        }
                        var currNodeId = response.CURR_NODE_ID;
                        var lastNodeId = response.LAST_NODE_ID;
                        $scope.currNodeId  = currNodeId
                        $scope.lastNodeId  = lastNodeId
                        detailFactory.queryNextOp($stateParams.domain,$scope.items[0].LAN_ID,currNodeId,workLogId).then(function(response){
                            if (typeof(response.USER_ID) == "undefined") {
                                $cordovaToast.showShortCenter("查询失败!");
                                return;
                            }
                            $scope.receiverId =response.USER_ID;
                            $scope.receiverName =response.USER_NAME;
                        });
                    });
                });
            });
        };

        $scope.doWork = function (state) {
            var confirmPopup  = $ionicPopup.confirm({
                title: '操作确认',
                template: '是否执行工作流操作？',
                okText: '确定',
                cancelText: '取消'
            });
            confirmPopup.then(function(res) {
                if(res) {
                    $scope.countdown = "";
                    $scope.disableButton = true;
                    var countdownInt = 10;
                    var myTime = setInterval(function() {
                        countdownInt--;
                        $scope.countdown = '('+countdownInt+'s)';
                        $scope.$digest();
                    }, 1000);

                    setTimeout(function() {
                        $scope.disableButton=false;
                        $scope.countdown="";
                        $scope.$digest();
                        clearInterval(myTime);
                    }, 11000);

                    var dataDirectory = "";
                    document.addEventListener('deviceready', function () {
                        dataDirectory = cordova.file.dataDirectory;
                        $http.get(dataDirectory+'/login.json').success(function(response) {
                            var userId = response[0].id;
                            var lanId = $scope.items[0].LAN_ID;
                            var workRelId = $scope.workRelId;
                            var workLogId = $scope.workLogId;
                            var currNodeId = $scope.currNodeId;
                            var lastNodeId = $scope.lastNodeId;
                            var yornToStart = $scope.yornToStart.id;
                            var remarks = $scope.suggest.inStr;
                            var yornSms = $scope.yornSms[0].checked?1:0;
                            detailFactory.goNextWork(state, userId, currNodeId, workLogId, lastNodeId, $stateParams.domain, $stateParams.demdId, lanId, yornToStart, workRelId, remarks, yornSms).then(function (response) {
                                if (typeof(response.RESULT) == "undefined") {
                                    $cordovaToast.showShortCenter("操作失败！");
                                }else{
                                    $cordovaToast.showShortCenter(response.RESULT);
                                    $scope.modal.remove();
                                    $window.history.back();
                                }
                            })
                        })
                    });
                }
            });
        };
        $scope.closeModal = function() {
            $scope.modal.hide();
        };
        //当我们用完模型时，清除它！
        $scope.$on('$destroy', function() {
            $scope.modal.remove();
        });
        // 当隐藏模型时执行动作
        $scope.$on('modal.hide', function() {
            // 执行动作
        });
        // 当移动模型时执行动作
        $scope.$on('modal.removed', function() {
            // 执行动作
        });
    })

    .controller('newsCtrl', function ($scope) {

    })

    .controller('mailListCtrl', function ($scope, $http) {
        // 通讯录

        // 触摸变色
//        $scope.onTouch = function(index){
//            var array = [];
//            for (var i= 0;i<=$scope.contacts.length;i++){
//                array.push('item');
//            }
//            $scope.bg = array;
//            $scope.bg[index] = 'item item-energized';
//        }
//        $scope.onRelease = function(index){
//            var array = [];
//            for (var i= 0;i<=$scope.contacts.length;i++){
//                array.push('item');
//            }
//            $scope.bg = array;
//            $scope.bg[index] = 'item item-light';
//        }
    })

