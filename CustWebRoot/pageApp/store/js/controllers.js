angular.module("indexApp.controllers",[])
    .controller('mainCtrl', function ($scope,$rootScope,$ionicHistory,$http, $ionicPopup, $state,$cacheFactory,listFactory,commendlistFactory,CookieFactory,sqlFactory) {
        $scope.staff ="测试用户";
        $scope.dept ="ICT支撑中心";
        $scope.outline=":";
        $scope.goToCheckList = function (o) {
            $state.go('checkList',{doIt:o});
        };
        $rootScope.httpback=function(){
            $ionicHistory.goBack();
        };


        $scope.getCommendGoodsList=function(){
            commendlistFactory.getCommendlist().then(function(response){
                console.log("response:"+response);

                $scope.commendlist=response.list;
             });
        };

        $scope.alert = function(){
            $ionicPopup.alert({
                title:'<b>消息</b>',
                template:'hi！'
            })
        };
        $scope.entergoodsdetail=function(goodsid){
            $state.go('goodsdetail', {gid: goodsid});
        }
        $scope.getCommendGoodsList();
  //      sqlFactory.test1();
    })
    .controller('clarifyCtrl', function ($scope, $http, $ionicPopup, $state,$ionicLoading,listFactory,categorylistFactory) {
        //     var url="http://192.168.15.52:7001/serv?domain=getClassifyTpye";
        var domain="getClassifyTpye";
//        $ionicLoading.show({
//            template: 'Loading...'
//        });
        var selectstyle_parent={
            "padding-right":"0px",
            "padding-left":"0px",
            "margin-top":"0px",
            "margin-bottom":"0px",
            "text-align":"center",
            "background-color":"#dcdcdc"
        };
        var unselectstyle_parent={
            "padding-right":"0px",
            "padding-left":"0px",
            "margin-top":"0px",
            "margin-bottom":"0px",
            "text-align":"center",
            "background-color":"white"
        };

        var selectstyle_font={
            "font-size":"16px",
            "color":"#87C644"
        };

        var unselectstyle_font={
            "font-size":"16px",
            "color":"#000000"
        };

        $scope.lastpfoffset=0;

        listFactory.getListByDomain(domain).then(function(response){
            console.log("response:"+response);

            var pfarray=new Array();
            for(var i=0;i<response.length;i++)
            {
                var pfobject;
                if(i==0)
                {
                     pfobject={
                         offset:i,
                         sparent:selectstyle_parent,
                         f:selectstyle_font
                     };
                }
                else
                {
                    pfobject={
                        offset:i,
                        sparent:unselectstyle_parent,
                        f:unselectstyle_font
                    };
                }
                response[i].pfstyleobj=pfobject;
            }
            $scope.items=response;
     //       $scope.pfstyleitems=pfarray;
            $scope.getCategorylistByPId($scope.items[0].DIC_ID,0);
        });

       $scope.selectpfItems=function(offset){
            $scope.items[$scope.lastpfoffset].pfstyleobj.sparent=unselectstyle_parent;
            $scope.items[$scope.lastpfoffset].pfstyleobj.f=unselectstyle_font;
            $scope.items[offset].pfstyleobj.sparent=selectstyle_parent;
            $scope.items[offset].pfstyleobj.f=selectstyle_font;
            $scope.lastpfoffset=offset;
        };

        $scope.getCategorylistByPId=function(did,offset){

            categorylistFactory.getCategorylist(did).then(function(response){
                console.log("response:"+response);
                var itemarray=[];//重组数组，两个，两个一输出
                var rows;
      //           alert(1);
                if(response.length%3)
                    rows=parseInt(response.length/3)+1;
                else
                    rows=response.length/3;
      //          alert("rows:"+rows);
                for(var i=0;i<rows;i++)
                {
                   var obj=new Object();
                   var currentamount=response.length-(i+1)*3;
                   if(currentamount==-2)
                   {
                       obj.size=1;
                       obj.once=response[i*3];
                   }
                   else if(currentamount==-1)
                   {
                       obj.size=2;
                       obj.once=response[i*3];
                       obj.twice=response[i*3+1];
                   }
                   else
                   {
                       obj.size=3;
                       obj.once=response[i*3];
                       obj.twice=response[i*3+1];
                       obj.third=response[i*3+2];
                   }
                   itemarray.push(obj);
                }
                $scope.categorylist=itemarray;
                $scope.selectpfItems(offset);
            });
        };

        $scope.goDetail=function(clarifyId){
            console.log("goDetail",clarifyId);
            $state.go('goodslist', {clarifyId: clarifyId});
        };
    })
    .controller('cartCtrl', function ($scope,$rootScope,$cacheFactory,$http, $ionicPopup, $state,$ionicLoading,$ionicHistory,loginFactory,listFactory,cartFactory) {
        //数量弹窗没做
        cartFactory.setCartidlist(null);
        $scope.httpback=function(){
            $ionicHistory.goBack();
        };
        $scope.formData = {'username':'zhang','password':'wang'};

        var allselect={
            checked:false
        };

        $scope.havingVar=true;
        $scope.noVar=false;



        $scope.cartidarray=[];

        $scope.doRefresh=function(){
            var cartarray=[];
            listFactory.getcartList("getCartList").then(function(response){
                var i=0;
                var allcount=0;
                var allprice=0;
                if(response.length==0)
                {
                    $scope.havingVar=true;
                    $scope.noVar=false;
                }
                else
                {
                    $scope.havingVar=false;
                    $scope.noVar=true;
                    for(;i<response.length;i++)
                    {
                        var cartitem={};
                        cartitem.check=false;
                        cartitem.obj=response[i];
                        var price=parseFloat(response[i].G_PRICE);
                        allprice=price*response[i].COUNT+allprice;
                        allcount+=response[i].COUNT;
                        cartarray.push(cartitem);
                    }
                    $scope.allprice=allprice.toFixed(2);
                    $scope.allcount=allcount;
                    $scope.items=cartarray;
                    $scope.lastSelectItem=null;
                    $scope.lastcheckall=allselect;
                    $scope.$broadcast('scroll.refreshComplete');


                }
            });
        };

        /**
         * 全选按钮
         */
         $scope.selectAll=function(){
             if($scope.lastcheckall.checked)
             {
                 var scopeitems=$scope.items;
                 for(var i=0;i<scopeitems.length;i++)
                    scopeitems[i].check=true;
             }else
             {
                 var scopeitems=$scope.items;
                 for(var i=0;i<scopeitems.length;i++)
                     scopeitems[i].check=false;
             }
          };
        $scope.checkItem=function(index)      //由于已经绑定了每个item的check属性，每次点击的时候都要检查一遍
        {
            var itemarrays=$scope.items;
            if(itemarrays[index].check)
                itemarrays[index].check=false;
            else
                itemarrays[index].check=true;
            var ischeckall=true; //初始化全选为真
            var notcheckall=true;//初始化没有全选为真
   //         var collection=$scope.items;   //获取所有数据
            //检查有没有全部选中
             for(var i=0;i<itemarrays.length;i++)
             {
                 if(!itemarrays[i].check)
                 {
                     ischeckall=false;     // 没有全部选中
                     break;
                 }
             }
            if(ischeckall)
                $scope.lastcheckall.checked=true;
            else if(notcheckall)
                $scope.lastcheckall.checked=false;


        }
        //点击结算再更改,点击结算之后再
        $scope.delCart= function(gid){
//            cartFactory.delCart(gid).then(function(response){
//                $scope.doRefresh();
//            });
            var confirmPopup = $ionicPopup.confirm({
                title: '删除商品',
                template: '你确定要删除该商品吗?'
            });
            confirmPopup.then(function(res) {
                if(res) {

                    //        $scope.delCart(cartitem.obj.G_ID);
                    cartFactory.delCart(gid).then(function(response){
                        $scope.doRefresh();
                    });
                }
            });
        };

        $scope.addGoodsNumber=function(cartitem)
        {
            cartFactory.addCartNumber(cartitem.obj.G_ID).then(function(response){
                cartitem.obj.COUNT++;
                $scope.allprice+=cartitem.obj.G_PRICE.toFixed(2);
                $scope.allcount++;
            });
        }

        $scope.minusGoodsNumber=function(cartitem)
        {
            cartitem.obj.COUNT--;
            if(cartitem.obj.COUNT<1)
            {
                cartitem.obj.COUNT=1;
                $scope.delCart(cartitem.obj.G_ID);
            }else
            {
                cartFactory.minusCartNumber(cartitem.obj.G_ID).then(function(response){
                    $scope.allprice=($scope.allprice-cartitem.obj.G_PRICE).toFixed(2);
                    $scope.allcount--;
                });
            }

        }

        $scope.goConfirm=function(){         //看有那几个选中的
            var collection=$scope.items;
            var cartarray=[];
            var cartstr='';
            for(var i=0;i<collection.length;i++)
            {
                if(collection[i].check)
                {
                    cartarray.push(collection[i].obj.S_ID);
                    cartstr=cartstr+"'"+collection[i].obj.S_ID+"',"; //'1','2','3'
                }
            }
            cartstr=cartstr.substring(0,cartstr.length-1);


            //确认订单controller里面需要
            cartFactory.setCartidlist(cartstr);
     //       $rootScope.cartidlist=cartstr;
            $state.go("confirmorder");
        };

        $scope.doRefresh();
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
                    }, 1000);

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
    .controller('mailListCtrl', function ($scope, $http) {

    })
    .controller('scrollindex', function($scope, $ionicSlideBoxDelegate) {
        //为了验证属性active-slide定义的模型，angularjs是mvc模式
        $scope.model = {
            activeIndex:0
        };

//此事件对应的是pager-click属性，当显示图片是有对应数量的小圆点，这是小圆点的点击事件
        $scope.pageClick = function(index){
            //         alert(index);

            $scope.model.activeIndex = index;
        };

//当图片切换后，触发此事件，注意参数


        $scope.slideHasChanged = function($index){
            //     alert($index);

        };
        //这是属性delegate-handle的验证使用的，其实没必要重定义，直接使用$ionicSlideBoxDelegate就可以


        $scope.delegateHandle = $ionicSlideBoxDelegate;
    })
    .controller('goodslistCtrl', function ($scope,$http, $ionicPopup,$stateParams, $state,$ionicLoading,$ionicSideMenuDelegate,$ionicHistory,$cacheFactory,$timeout,goodslistFactory) {
    //数量弹窗没做
        var param={
            curPage:5,
            loadPage:0,   //加载了多少条记录
            hasMore:true,  //有没有更多数据
            orderparam:null     //按照什么排序
        };
        $scope.parameter=param;
        $scope.alertLoadMore = function(){
            $ionicPopup.alert({
                title:'<b>已经没有更多数据加载了</b>',
                template:''
            });
        }; //$都是要依赖注入的变量
        $scope.goodsitems=[];

        $scope.toggleMenu = function() {
            $ionicSideMenuDelegate.toggleRight();
        };

        goodslistFactory.getGoodslist($stateParams.clarifyId,$scope.parameter.loadPage,$scope.parameter.curPage).then(function(response){
            $scope.parameter.loadPage += response.length;
            $scope.parameter.hasMore = $scope.parameter.loadPage>= $scope.parameter.curPage;
            $scope.goodsitems = $scope.goodsitems.concat(response);
            $scope.parameter.curPage += 5;     //原本这样的方法肯定是不对的，通过curPage往上加
        });

        $scope.getGoodslistBySort=function(orderparam)
        {
            $scope.parameter.loadPage=0;
            $scope.parameter.hasMore=true;
            $scope.parameter.curPage=5;
            $scope.parameter.orderparam=orderparam;
            $scope.goodsitems=[];

            goodslistFactory.getGoodslist($stateParams.clarifyId,$scope.parameter.loadPage,$scope.parameter.curPage,$scope.parameter.orderparam).then(function(response){
                $scope.parameter.loadPage += response.length;
                $scope.parameter.hasMore = $scope.parameter.loadPage>= $scope.parameter.curPage;
                $scope.goodsitems = $scope.goodsitems.concat(response);
                $scope.parameter.curPage += 5;     //原本这样的方法肯定是不对的，通过curPage往上加
                $scope.$broadcast('scroll.infiniteScrollComplete');
            });
        }

        var titlearray=["默认","销量","价格","筛选"];
        var paramarray=["default","salesamount","salesprice","default"];

        var unselectPstyle={
            "color":"#000000",
            "font-size": "16px",
            "display": "inline"
        };

        var selectPstyle={
            "color": "#87C644",
            "font-size": "16px",
            "display": "inline"
        };

        var colselectstyle={
            "border-bottom": "1px",
            "border-bottom-color": "#87C644",
            "text-align": "center"
        };

        var colunselectstyle={
            "text-align": "center"
        };

        var stylearray=[];
        /**
         * 选中标题
         */

        for(var i=0;i<titlearray.length;i++)
        {
            var styleitemobj;
            if(i==0)
            {
                styleitemobj={
                  text:titlearray[i],
                  style:selectPstyle,
                  colstyle:colselectstyle
                };
            }
            else
            {
                styleitemobj={
                    text:titlearray[i],
                    style:unselectPstyle,
                    colstyle:colunselectstyle
                };
            }
            styleitemobj.orderparam=paramarray[i];
            stylearray.push(styleitemobj);
        }

        $scope.stylelist=stylearray;

        $scope.laststyleobj=$scope.stylelist[0];

        $scope.selectOptionTitle=function(styleitemobj)
        {
            $scope.laststyleobj.style=unselectPstyle;
            styleitemobj.style=selectPstyle;
            $scope.laststyleobj=styleitemobj;
            if(styleitemobj!=$scope.stylelist[3])
                 $scope.getGoodslistBySort(styleitemobj.orderparam);
            else
            {
                $ionicSideMenuDelegate.toggleRight();
            }
        };

        $scope.loadMore = function() {
            //这里使用定时器是为了缓存一下加载过程，防止加载过快
            if(!$scope.parameter.hasMore)
            {
                $timeout(function() {
                    $scope.$broadcast('scroll.infiniteScrollComplete');
                },1000);
            }
            else
            {
                $timeout(function() {
                    goodslistFactory.getGoodslist($stateParams.clarifyId,$scope.parameter.loadPage,$scope.parameter.curPage,$scope.parameter.orderparam).then(function(response){
                        $scope.parameter.loadPage += response.length;
                        $scope.parameter.hasMore = $scope.parameter.loadPage>= $scope.parameter.curPage;
                        $scope.goodsitems = $scope.goodsitems.concat(response);
                        $scope.parameter.curPage += 5;     //原本这样的方法肯定是不对的，通过curPage往上加
                        $scope.$broadcast('scroll.infiniteScrollComplete');
                    });
                },1000);
            }
        };

        $scope.$on('stateChangeSuccess', function() {
            $scope.loadMore();
        });
        $scope.entergoodsdetail=function(goodsid){
     //       $state.go("goodsdetail");
            $state.go('goodsdetail', {gid: goodsid});
        }
    })
    .controller('orderslistCtrl', function ($scope,$http, $ionicPopup,$stateParams, $state,$ionicLoading,$ionicHistory,$cacheFactory,$ionicSlideBoxDelegate,OrderFactory) {
        //数量弹窗没做
        $scope.getOrderStatus = function(index){
            var status=null;
            //全部略去
            if(index==1)
                status='0';       //待付款
            else if(index==2)
                status='1';       //已付款
            else if(index==3)
                status='2';       //已发货
            else if(index==4)
                status='8';       //已完成
            OrderFactory.getPayOrderByStatus(status).then(function(response){
                $scope.orderlist=response;
            });
        };

        $scope.goEvaluate=function(oid){
            $state.go("eval",{oid:oid});
        };

        $scope.myActiveSlide = 0;
        var selectColstyle={
             "border-bottom":"1px solid orange",
             "text-align":"center"
        };
        var unselectColstyle={
            "border":"none",
            "text-align":"center"
        };
        var selectPStyle = {
             "color" : "orange"
         };
        var unselectPStyle = {
            "color" : "#000000"
        };

        $scope.allstyle= {
            colstyle:selectColstyle,
            pstyle:selectPStyle
        };
        $scope.Fkstyle={
            colstyle:unselectColstyle,
            pstyle:unselectPStyle
        };
        $scope.Fhstyle={
            colstyle:unselectColstyle,
            pstyle:unselectPStyle
        };
        $scope.Shstyle={
            colstyle:unselectColstyle,
            pstyle:unselectPStyle
        };
        $scope.Pjstyle={
            colstyle:unselectColstyle,
            pstyle:unselectPStyle
        };
        $scope.laststyle=$scope.allstyle;
        /**
         * 换标题样式
         * @param obj   选中的那个状态对象，全部对应allstyle,待付款对应Fkstyle.......
         * @param i     tab选中的索引，选中第几页
         */
         $scope.changestyle=function(obj,i)
        {
            $scope.laststyle.colstyle= unselectColstyle;
            $scope.laststyle.pstyle= unselectPStyle;
            obj.colstyle= selectColstyle;
            obj.pstyle= selectPStyle;
            $scope.laststyle=obj;
            $scope.selectedTab(i);
            $scope.getOrderStatus(i);
        };
        $scope.lastindex=0;

        /**
         * 滑屏切换，调整标题
         * @param index
         */
        $scope.slideHasChanged=function(index)
        {
            if($scope.lastindex!=index) {
                $scope.myActiveSlide = index;

                if (index==0)
                    $scope.changestyle($scope.allstyle,index);
                else if(index==1)
                    $scope.changestyle($scope.Fkstyle,index);
                else if(index==2)
                    $scope.changestyle($scope.Fhstyle,index);
                else if(index==3)
                    $scope.changestyle($scope.Shstyle,index);
                else if(index==4)
                    $scope.changestyle($scope.Pjstyle,index);
                else
                {
                    if($scope.lastindex==0)
                        $scope.changestyle($scope.allstyle,0);
                    else if($scope.lastindex==4)
                        $scope.changestyle($scope.allstyle,4);

                }
            }

            $scope.lastindex=index;
        };
        $scope.selectedTab = function (index) {
            $ionicSlideBoxDelegate.slide(index);
        };
         //进入订单详情
        $scope.goOrderdetail=function(oid){
            OrderFactory.setDetailOrderid(oid);
            $state.go("orderdetail",{oid:oid});
        };

        $scope.selectedTab(0);
        $scope.changestyle($scope.allstyle,0);    // 默认会调用getOrderStatus方法

       //付款
        $scope.itemgopay=function(oid,orderprice){
            OrderFactory.setOrderId(oid);
            OrderFactory.setOrderprice(orderprice);
            $state.go("pay");
        };
       //取消订单       未付款   改成    已取消  5          已付款     改成      待取消         11
        /**
         *
         * @param oid           订单id
         * @param prestatus     当前的状态status
         */
        $scope.cancelorder=function(oid,curstatus){
            var status;
            if(curstatus=='0')
                status='5';
            else if(curstatus=='1')
                status='11';
            else
                $ionicPopup.alert({
                    title:'<b>参数错误</b>'
                });
            OrderFactory.modifyPayOrder(oid,status,null).then(function(response){
                $ionicPopup.alert({
                    title:'<b>'+response.message+'</b>'
                });
         //       $scope.getOrderStatus(0);
                 $scope.changestyle($scope.allstyle,0);    // 默认会调用getOrderStatus方法
            });
        };
        //删除订单
        $scope.delorder=function(oid){
            OrderFactory.modifyPayOrder(oid,'10',null).then(function(response){
                $ionicPopup.alert({
                    title:'<b>订单已被成功删除</b>'
                });
      //          $scope.getOrderStatus(0);
                $scope.changestyle($scope.allstyle,0);    // 默认会调用getOrderStatus方法
            });
        };
       //提醒发货
        $scope.tiporder=function(oid){
            OrderFactory.modifyPayOrder(oid,'12',null).then(function(response){
                $ionicPopup.alert({
                    title:'<b>提醒发货成功</b>'
                });
          //      $scope.getOrderStatus(0);
                $scope.changestyle($scope.allstyle,0);    // 默认会调用getOrderStatus方法
            });
        };

        //确认收货
        $scope.confirmordertip=function(oid)
        {
            var myPopup = $ionicPopup.show({
                <!--         template: '<input type="password" ng-model="data.wifi">',   -->
                title: '订单消息',
                subTitle: '确认收货吗,对话框将在3秒钟后消失',
                scope: $scope,
                buttons: [
                    { text: '取消' },
                    {
                        text: '<b>确定</b>',
                        type: 'button-positive',
                        onTap: function(e) {
                            OrderFactory.modifyPayOrder(oid,'8',null).then(function(response){
                                $ionicPopup.alert({
                                    title:'<b>'+response.message+'</b>'
                                });
                 //               $scope.getOrderStatus(0);
                                $scope.changestyle($scope.allstyle,4);    // 默认会调用getOrderStatus方法
                            });
                        }
                    },
                ]
            });
            myPopup.then(function(res) {
                console.log('Tapped!', res);
            });
            $timeout(function() {
                myPopup.close(); // 3秒后关闭弹窗
            }, 3000);
        };




    })
   .controller('mineCtrl', function ($scope,$http, $ionicPopup,$stateParams, $state,$ionicLoading,$ionicHistory,$cacheFactory) {
    //数量弹窗没做
        $scope.entermyorder=function(){
            $state.go("orderslist");
        }
        $scope.entersetting=function(){
           $state.go("setting");
        }
        $scope.enterpersonalinfo=function(){
            $state.go("personalinfo");
        }
       
        $scope.enterdiscount=function () {
            $state.go('discount');
        }
        $scope.init=function(){
            var screenHeight= window.screen.height;
            $scope.gainheight={
                'height':screenHeight*0.4+'px'
            };
            $scope.gain={
                "height": screenHeight*0.6-50,//-document.getElementById("nav").offsetHeight,
                "background-color":"white",
                "padding-top":"5%",
                "padding-bottom":"5%",
                "width":"100%"
            };
        }

        $scope.goAddress=function(){
            $state.go("addresslist");
        };

        $scope.init();

    })
    .controller('settingCtrl', function ($scope,$http, $ionicPopup,$stateParams, $state,$ionicLoading,$ionicHistory,$cacheFactory) {
        //数量弹窗没做

    })
    .controller('accountadministerCtrl', function ($scope,$http, $ionicPopup,$stateParams, $state,$ionicLoading,$ionicHistory,$cacheFactory) {
        //数量弹窗没做

    })
    .controller('personalinfoCtrl', function ($scope,$http, $ionicPopup,$stateParams, $state,$ionicLoading,$ionicHistory,$cacheFactory,$ionicPopover) {
        $scope.screenHeight=window.screen.height;
        $scope.avatarheight={
          "background-color":"white",
           "height": $scope.screenHeight*0.2+"px"
        };
        $scope.nichengheight={
            "background-color":"white",
            "height": $scope.screenHeight*0.1+"px"
        };
        $scope.sexheight={
            "background-color":"white",
            "height": $scope.screenHeight*0.1+"px"
        };
        $scope.birthheight={
            "background-color":"white",
            "height": $scope.screenHeight*0.1+"px"
        };



    }) //一旦退回到购物车页面的时候，购物车id列表清空
    .controller('orderconfirmCtrl', function ($scope,$rootScope,$http, $ionicPopup,$stateParams, $state,$ionicLoading,$ionicHistory,$cacheFactory,ConfirmOrderFactory,DiscountFactory,cartFactory,OrderFactory) {
        $scope.showPopup = function(text) {

            // 自定义弹窗
            var myPopup = $ionicPopup.show({
                title: '您还没有收货地址,请立即到个人中心那儿设置',
                scope: $scope,
                buttons: [
                    {
                        text: '<b>'+text+'</b>',
                        type: 'button-positive',
                        onTap: function(e) {
                            $state.go("personalinfo");
                        }
                    }
                ]
            });

        };
        $scope.goDiscount=function(){
            $state.go("discount");
        };
        $scope.acquirePersonalInfo=function(){
            var cartidlist=cartFactory.getCartidlist();
            OrderFactory.setOrderId(null);//每次进来之前都清一下缓存
            OrderFactory.setOrderprice(null);  //清空订单总价缓存
            ConfirmOrderFactory.acquirePersonalInfo(cartidlist).then(function(response){
                  if(response.code=='NoAddress')
                  {
                      $scope.showPopup('去设置收货地址');
                  }
                  else if(response.code=="success")
                  {
                      var allprice=0;
                      var cartalllist=response.cartlist;
                      var paramlist="";
                      for(var i=0;i<cartalllist.length;i++)
                      {
                         var price=parseFloat(cartalllist[i].G_PRICE);
                         var singleallprice=price*(cartalllist[i].COUNT);
                         allprice+=cartalllist[i].COUNT*price;
                          //gid商品id-单个商品总价-数量
                          paramlist+="'"+cartalllist[i].G_ID+"'-'"+singleallprice+"'-'"+cartalllist[i].COUNT+"',";
                      }
                      $scope.detaillist=paramlist.substring(0,paramlist.length-1);
                      allprice=allprice.toFixed(2);
                      $scope.allprice=allprice;
                      OrderFactory.setOrderprice($scope.allprice);
                      $scope.cartlistresult=response.cartlist;
                      $scope.addressresult=response.addresslist;
                  }
            });
        };

        $scope.enterPay=function(){
    //        $state.go("pay");
            var discount=DiscountFactory.getCoupon();

            if(discount!=null)
            {
               OrderFactory.produceNotPayOrder(cartFactory.getCartidlist(),$scope.allprice,discount.UC_ID,$scope.detaillist,discount.C_ID).then(function(response){
                     $state.go("pay");
                     cartFactory.setCartidlist(null);   //清除缓存
                     OrderFactory.setOrderId(response.data.O_ORDERID);
               //      OrderFactory.setOrderprice(null);  //清空订单总价缓存
               });
            }
            else
            {
                OrderFactory.produceNotPayOrder(cartFactory.getCartidlist(),$scope.allprice,null,$scope.detaillist,null).then(function(response){
                    $state.go("pay");
                    cartFactory.setCartidlist(null);   //清除缓存          //需要解决回退问题
                    OrderFactory.setOrderId(response.data.O_ORDERID);
                });
            }
        };

        $scope.acquirePersonalInfo();
    })
    .controller('goodsdetailCtrl', function ($scope,$http, $ionicPopup,$stateParams, $state,$ionicLoading,$ionicHistory,$cacheFactory,$ionicSlideBoxDelegate,goodsDetailFactory,cartFactory) {
        $scope.myActiveSlide = 0;

        var selectPStyle = {
            "color" : "orange",
            "margin-left":"10px"
        };
        var unselectPStyle = {
            "color" : "white",
            "margin-left":"10px"
        };

        $scope.allstyle= {
            pstyle:selectPStyle
        };
        $scope.Fkstyle={
            pstyle:unselectPStyle
        };
        $scope.Fhstyle={
            pstyle:unselectPStyle
        };

        $scope.laststyle=$scope.allstyle;

        $scope.changestyle=function(obj,i)
        {
    //      $scope.laststyle.colstyle= unselectColstyle;
            $scope.laststyle.pstyle= unselectPStyle;
    //      obj.colstyle= selectColstyle;
            obj.pstyle= selectPStyle;
            $scope.laststyle=obj;
            $scope.selectedTab(i);
        };
        $scope.lastindex=0;
        $scope.slidHasChanged=function(index)
        {
            if($scope.lastindex!=index) {
                $scope.myActiveSlide = index;
                if (index==0)
                    $scope.changestyle($scope.allstyle,index);
                else if(index==1)
                    $scope.changestyle($scope.Fkstyle,index);
                else if(index==2)
                    $scope.changestyle($scope.Fhstyle,index);
             }
            $scope.lastindex=index;
        };
        $scope.selectedTab = function (index) {
            $ionicSlideBoxDelegate.slide(index);
        };
        $scope.selectedTab(0);

        $scope.goOrder=function(){
             $state.go("confirmorder");
        };
        $scope.goCart=function(){
            $state.go("cart");
        };

        $scope.addCart =function(){
            cartFactory.addCart($stateParams.gid).then(function(response){
            });
        };


        goodsDetailFactory.getGoodsdetail($stateParams.gid).then(function(response){
              $scope.goodsdetail = response;

        });


    })
    .controller('discountCtrl', function ($scope,$http, $ionicPopup,$stateParams, $state,$ionicLoading,$ionicHistory,$cacheFactory,$ionicSlideBoxDelegate,DiscountFactory) {

        $scope.myActiveSlide = 0;
        var selectColstyle={
            "border-bottom":"1px solid green",
            "text-align":"center"
        };
        var unselectColstyle={
            "border":"none",
            "text-align":"center"
        };
        var selectPStyle = {
            "color" : "green"
        };
        var unselectPStyle = {
            "color" : "#000000"
        };
        //未使用
        $scope.WSYstyle= {
            colstyle:selectColstyle,
            pstyle:selectPStyle
        };
        //已使用
        $scope.YSYstyle={
            colstyle:unselectColstyle,
            pstyle:unselectPStyle
        };
        //已过期
        $scope.YGQstyle={
            colstyle:unselectColstyle,
            pstyle:unselectPStyle
        };

        $scope.laststyle=$scope.WSYstyle;

        $scope.changestyle=function(obj,i)
        {
            $scope.laststyle.colstyle= unselectColstyle;
            $scope.laststyle.pstyle= unselectPStyle;
            obj.colstyle= selectColstyle;
            obj.pstyle= selectPStyle;
            $scope.laststyle=obj;
            $scope.selectedTab(i);
            $scope.doselectCondition(i);
        };
        $scope.lastindex=0;
        $scope.slideHasChanged=function(index)
        {
            if($scope.lastindex!=index) {
                $scope.myActiveSlide = index;
                if (index==0)
                    $scope.changestyle($scope.WSYstyle,index);
                else if(index==1)
                    $scope.changestyle($scope.YSYstyle,index);
                else if(index==2)
                    $scope.changestyle($scope.YGQstyle,index);
            }
            $scope.lastindex=index;
            $scope.getCouponlist(index);
        };

         //使用优惠券去支付
        $scope.goPayByDiscount=function(discountobj){
       //    $scope.discountobj=discountobj;
            DiscountFactory.setCoupon(discountobj);
            $ionicHistory.goBack();
        };

        //已使用优惠券
        $scope.unableAlreadyUse=function(){
            $ionicPopup.alert({
                title:'<b>该优惠券已使用，不能投入使用</b>'
            });
            $ionicHistory.goBack();
        };

        //已过期优惠券
        $scope.unableDecline=function(){
            $ionicPopup.alert({
                title:'<b>该优惠券已过期，不能投入使用</b>'
            });
            $ionicHistory.goBack();
        };

        $scope.selectedTab = function (index) {
            $ionicSlideBoxDelegate.slide(index);
        };
        $scope.selectedTab(0);

        //获取优惠券列表，status：三种状态的标识码
        $scope.getCouponlist=function(status){
            DiscountFactory.getCouponlist(status).then(function(response){
               $scope.discountlist=response;
            });
        };
        $scope.getCouponlist();


        //三个tab点击时候相互切换
        $scope.doselectCondition=function(status){
            $scope.getCouponlist(status);
        };
    })
    .controller('payCtrl', function ($scope,$http, $ionicPopup,$stateParams, $state,$ionicLoading,$ionicHistory,$cacheFactory,$ionicSlideBoxDelegate,DiscountFactory,OrderFactory) {
         $scope.allorderprice=  OrderFactory.getOrderprice();
         var O_ORDERID=OrderFactory.getOrderId();
         $scope.payOrder=function(){
             OrderFactory.modifyPayOrder(O_ORDERID,1,$scope.allorderprice).then(function(response){
                 //已付款   1    待付款   0     已发货    2    已签收    3     已评论     4      已退款     7      已完成     8
                 OrderFactory.setOrderprice(null);  //清空订单总价缓存
                 OrderFactory.setOrderId(null);     //清空订单ID缓存
                 $state.go("paysuccess");
             });
         };
    })
    .controller('paysuccessCtrl', function ($scope,$http, $ionicPopup,$stateParams, $state,$ionicLoading,$ionicHistory,$cacheFactory,$ionicSlideBoxDelegate,DiscountFactory,OrderFactory) {
        $scope.goOrderPage=function(){
            $state.go("orderslist");
        };
        $scope.goMain=function(){
            $state.go("tabs.main");
        };
    })

    .controller('evaluateCtrl', function ($scope,$http, $ionicPopup,$stateParams, $state,$ionicLoading,$ionicHistory,$cacheFactory,$ionicSlideBoxDelegate,DiscountFactory,OrderFactory) {
        $scope.description=[false,false,false,false,false];
        $scope.wuliu=[false,false,false,false,false];
        $scope.fuwu=[false,false,false,false,false];

       //点击一个，看他是否被选中，如果他被选中，他上面也被选中，那么就把上面的全去掉，如果上面的没选中，那就把之前的全去掉
       //点击一个，如果他没被选中，那就到他这为止都选中
        $scope.chooseDescription=function(index)
        {
            if(index==4)//选中最后一个
            {
               if($scope.description[index])
               {
                   for(var i=0;i<5;i++)
                        $scope.description[i]=false;
               }
               else     //如果没有选中
               {
                   for(var i=0;i<5;i++)
                       $scope.description[i]=true;
               }
            }
            else
            {
                if($scope.description[index])   //当前选中
                {
                   if($scope.description[index+1]) //如果后面是选中的话
                   {
                       for(var i=index+1;i<5;i++)
                            $scope.description[i]=false;    //往更高的地方全部不选中
                   }
                   else                             //如果后面没有选中
                   {
                       for(var i=0;i<=index;i++)
                           $scope.description[i]=false;
                   }
                }
                else
                {
                    for(var i=0;i<=index;i++)
                        $scope.description[i]=true;
                }
            }
        };

        $scope.chooseWuliu=function(index)
        {
            if(index==4)//选中最后一个
            {
                if($scope.wuliu[index])
                {
                    for(var i=0;i<5;i++)
                        $scope.wuliu[i]=false;
                }
                else     //如果没有选中
                {
                    for(var i=0;i<5;i++)
                        $scope.wuliu[i]=true;
                }
            }
            else
            {
                if($scope.wuliu[index])   //当前选中
                {
                    if($scope.wuliu[index+1]) //如果后面是选中的话
                    {
                        for(var i=index+1;i<5;i++)
                            $scope.wuliu[i]=false;    //往更高的地方全部不选中
                    }
                    else                             //如果后面没有选中
                    {
                        for(var i=0;i<=index;i++)
                            $scope.wuliu[i]=false;
                    }
                }
                else
                {
                    for(var i=0;i<=index;i++)
                        $scope.wuliu[i]=true;
                }
            }
        };

        $scope.chooseFuwu=function(index)
        {
            if(index==4)//选中最后一个
            {
                if($scope.fuwu[index])
                {
                    for(var i=0;i<5;i++)
                        $scope.fuwu[i]=false;
                }
                else     //如果没有选中
                {
                    for(var i=0;i<5;i++)
                        $scope.fuwu[i]=true;
                }
            }
            else
            {
                if($scope.fuwu[index])   //当前选中
                {
                    if($scope.fuwu[index+1]) //如果后面是选中的话
                    {
                        for(var i=index+1;i<5;i++)
                            $scope.fuwu[i]=false;    //往更高的地方全部不选中
                    }
                    else                             //如果后面没有选中
                    {
                        for(var i=0;i<=index;i++)
                            $scope.fuwu[i]=false;
                    }
                }
                else
                {
                    for(var i=0;i<=index;i++)
                        $scope.fuwu[i]=true;
                }
            }
        };

    })
    .controller('orderdetailCtrl', function ($scope,$http, $ionicPopup,$stateParams, $state,$ionicLoading,$ionicHistory,$cacheFactory,$ionicSlideBoxDelegate,DiscountFactory,OrderFactory) {
        $scope.init=function(){
   //        var orderid=OrderFactory.getDetailOrderid();
   //         alert(orderid);$stateParams传过来的就是一个javascript对象
            OrderFactory.getOrderDetail($stateParams.oid).then(function(response){
                //已付款   1    待付款   0     已发货    2    已签收    3     已评论     4      已退款     7      已完成     8
                $scope.detailinfo=response;
            });
  //          OrderFactory.setDetailOrderid(null);
        };

        //付款
        $scope.itemgopay=function(oid,orderprice){
            OrderFactory.setOrderId(oid);
            OrderFactory.setOrderprice(orderprice);
            $state.go("pay");
        };
        //取消订单       未付款   改成    已取消  5          已付款     改成      待取消         11
        /**
         *
         * @param oid           订单id
         * @param prestatus     当前的状态status
         */
        $scope.cancelorder=function(oid,curstatus){
            var status;
            if(curstatus=='0')
                status='5';
            else if(curstatus=='1')
                status='11';
            else
                $ionicPopup.alert({
                    title:'<b>参数错误</b>'
                });
            OrderFactory.modifyPayOrder(oid,status,null).then(function(response){
                $ionicPopup.alert({
                    title:'<b>'+response.message+'</b>'
                });
                $state.go('orderlist');
                //       $scope.getOrderStatus(0);
   //             $scope.changestyle($scope.allstyle,0);    // 默认会调用getOrderStatus方法
            });
        };
        //删除订单
        $scope.delorder=function(oid){
            OrderFactory.modifyPayOrder(oid,'10',null).then(function(response){
                $ionicPopup.alert({
                    title:'<b>订单已被成功删除</b>'
                });
                $state.go('orderlist');
                //          $scope.getOrderStatus(0);
       //         $scope.changestyle($scope.allstyle,0);    // 默认会调用getOrderStatus方法
            });
        };
        //提醒发货
        $scope.tiporder=function(oid){
            OrderFactory.modifyPayOrder(oid,'12',null).then(function(response){
                $ionicPopup.alert({
                    title:'<b>提醒发货成功</b>'
                });
                $state.go('orderlist');
                //      $scope.getOrderStatus(0);
      //          $scope.changestyle($scope.allstyle,0);    // 默认会调用getOrderStatus方法
            });
        };

        //确认收货
        $scope.confirmordertip=function(oid)
        {
            var myPopup = $ionicPopup.show({
                <!--         template: '<input type="password" ng-model="data.wifi">',   -->
                title: '订单消息',
                subTitle: '确认收货吗,对话框将在3秒钟后消失',
                scope: $scope,
                buttons: [
                    { text: '取消' },
                    {
                        text: '<b>确定</b>',
                        type: 'button-positive',
                        onTap: function(e) {
                            OrderFactory.modifyPayOrder(oid,'8',null).then(function(response){
                                $ionicPopup.alert({
                                    title:'<b>'+response.message+'</b>'
                                });
                                //               $scope.getOrderStatus(0);
     //                           $scope.changestyle($scope.allstyle,4);    // 默认会调用getOrderStatus方法
                                $state.go('orderlist');
                            });
                        }
                    },
                ]
            });
            myPopup.then(function(res) {
                console.log('Tapped!', res);
            });
            $timeout(function() {
                myPopup.close(); // 3秒后关闭弹窗
            }, 3000);
        };

        $scope.goEvaluate=function(oid){
            $state.go("eval",{oid:oid});
        };

        $scope.init();
    })
    .controller('addressadministerCtrl', function ($scope,$http, $ionicPopup,$stateParams, $state,$ionicLoading,$ionicHistory,$cacheFactory,$ionicSlideBoxDelegate,DiscountFactory,OrderFactory) {
       $scope.screenHeight=window.screen.height;
       $scope.liststyle={
          "height":$scope.screenHeight*0.2+'px',
          'border':'none',
          'padding':'5px',
          'background-color':'white'
       };


    })
;
