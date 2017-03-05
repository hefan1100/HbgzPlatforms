angular.module("indexApp.services", [])
    .factory('listFactory', function($http, $q,$ionicPopup,$ionicLoading){
        var param = {};
        param.curPage = 10;   // 每页的记录数
        param.hasmore = false;

        return {
            getCount: function getCount(userId){
                var deferred = $q.defer();
                var url = 'http://192.168.15.20:8118/service?domain=queryAllCount&userId='+userId;
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
                var url = 'http://192.168.15.20:8118/service?domain='+domain+'&userId='+userId;
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
            getListByDomain: function getListByDomain(domain,uid){
                var deferred = $q.defer();
                var url = 'http://192.168.15.42:8080/store?domain='+domain;
                if(uid!=null)
                     url=url+"&uid="+uid;
                $ionicLoading.show({
                      template: 'Loading...'
                });
                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    console.log("http response:"+response);
                    var resultJson = response;
                    if(resultJson.toString().trim() == 'null') {
                        deferred.resolve('null');
                    }else if(resultJson.code!=null)
                    {
                        alert(resultJson.message);
                        deferred.resolve('null');
                    }else{
                        var array = [];
                        for (var i = 0; i < resultJson.length ; i++) {
                            array.push(resultJson[i]);
                        }
                        deferred.resolve(array);
                    }
                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("getListByDomain网络联接失败!");
                        $ionicPopup.alert({
                            title:'<b>错误!</b>',
                            template:'网络联接失败!'
                        })
                        deferred.reject();
                    });
                return deferred.promise;
            },
            getcartList: function getcartList(domain){
                var deferred = $q.defer();
                var url = 'http://192.168.15.42:8080/store?domain='+domain;
                $ionicLoading.show({
                    template: 'Loading...'
                });

                $http.post(url).success(function (response) {
                    console.log("http response:"+response);
                    var resultJson = response;
                    $ionicLoading.hide();
                    if(resultJson.toString().trim() == 'null') {
                        deferred.resolve('null');
                    }else if(resultJson.code!=null)
                    {
                        alert(resultJson.message);
                        deferred.resolve('null');
                    }else{
                        var array = [];
                        for (var i = 0; i < resultJson.length ; i++) {
                            array.push(resultJson[i]);
                        }
                        deferred.resolve(array);
                    }
                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("getListByDomain网络联接失败!");
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
                var url = 'http://192.168.15.20:8118/service?domain='+domain+'&demdId='+demdId;
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
                var url = 'http://192.168.15.20:8118/service?domain=queryWorkRel&name='+DName+'&demdId='+demdId;
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
                var url = 'http://192.168.15.20:8118/service?domain=queryWorkDetail&demdId='+demdId;
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
                var url = 'http://192.168.15.20:8118/service?domain=queryCurWorkDetail&name=' + name + '&demdId=' + demdId;
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
                var url = 'http://192.168.15.20:8118/service?domain=queryNextOp&name='+DName+'&lanId='+lanId+'&userId='+currNodeid+'&demdId='+workLogId;
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
                var url = 'http://192.168.15.20:8118/service?domain=doWork&state=' + state + '&userId=' + userId + '&currNodeId=' + currNodeId + '&workLogId=' + workLogId + '&lastNodeId=' + lastNodeId + '&name=' + DName + '&demdId=' + doId + '&lanId=' + lanId + '&yornToStart=' + yornToStart + '&workRelId=' + workRelId + '&remarks=' + remarks + '&yornSms=' + yornSms;
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
    .factory('loginFactory', function($http, $q,$ionicLoading){
        return {
            doLogin: function doLogin(username,password){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=doLogin&username='+username+'&password='+password;
                $http.post(url).success(function (response) {
                    $ionicLoading.hide();

                    deferred.resolve(response);
                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("doLogin网络联接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            }
        };
    })
    .factory('categorylistFactory', function($http, $q,$ionicLoading){
        return {
            getCategorylist: function getCategorylist(dicid){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=getCategorylist&dicid='+dicid;
                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    var resultJson = response;
                    var array = [];
                    for (var i = 0; i < resultJson.length ; i++) {
                        array.push(resultJson[i]);
                    }
                    deferred.resolve(array);
                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("doLogin网络联接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            }
        };
    })
    .factory('commendlistFactory', function($http, $q,$ionicLoading){
        return {
            getCommendlist: function getCommendlist(){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=getMain';
                $http.post(url).success(function (response) {
                    $ionicLoading.hide();

                    var result=new Object();
                    var resultJson = response.commendlist;
                    var array = [];
                    for (var i = 0; i < resultJson.length ; i++) {
                        array.push(resultJson[i]);
                    }

                    result.list=array;
                    result.uinfo=response.userinfo;

                    deferred.resolve(result);
                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("doLogin网络联接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            }
        };
    })
    .factory('goodslistFactory', function($http, $q,$ionicPopup,$ionicLoading){

        return {
            getGoodslist: function getGoodslist(clarifyId,startpage,curpage,param){
                var deferred = $q.defer();
                console.log("curpage","curpage页数:"+curpage);
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=getGoodslist&clarifyId='+clarifyId+'&startpage='+startpage+'&endpage='+curpage;
                if(param!=null)
                    url=url+"&orderparam="+param;
                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    var resultJson = response;
                    var array = [];
                    for (var i = 0; i < resultJson.length ; i++) {
                        array.push(resultJson[i]);
                    }
                    deferred.resolve(array);
                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("getGoodslist连接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            }
        };
    })

  //购物车service
    .factory('cartFactory', function($http, $q,$ionicPopup,$ionicLoading){

        return {
            cartidlist:null,
            setCartidlist:function(idlist)
            {
                this.cartidlist=idlist;
            },
            getCartidlist:function(){
                return this.cartidlist;
            },
            addCart: function addCart(gid){
                var deferred = $q.defer();
                console.log("gid","商品id:"+gid);
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=addCart&gid='+gid;

                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    if(response.code=='success')
                    {
                        $ionicPopup.alert({
                            title:'<b>加入购物车成功</b>',
                            template:''
                        });
                    }
                    else
                    {
                        var message=response.message;
                        $ionicPopup.alert({
                            title:'<b>加入购物车失败</b>',
                            template:message
                        });
                    }
                    deferred.resolve(response);
                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("addCart连接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            },
            addCartNumber: function addCartNumber(gid){
                var deferred = $q.defer();
                console.log("gid","商品id:"+gid);
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=addCartNumber&gid='+gid;

                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    var message=response.message;
                    if(response.code=='success')
                    {
                        $ionicPopup.alert({
                            title:message,
                            template:''
                        });
                    }
                    else
                    {

                        $ionicPopup.alert({
                            title:'',
                            template:message
                        });
                    }
                    deferred.resolve(response);
                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("addCart连接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            },
            minusCartNumber: function minusCartNumber(gid){
                var deferred = $q.defer();
                console.log("gid","商品id:"+gid);
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=minusCartNumber&gid='+gid;

                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    var message=response.message;
                    if(response.code=='success')
                    {
                        $ionicPopup.alert({
                            title:message,
                            template:''
                        });
                    }
                    else
                    {

                        $ionicPopup.alert({
                            title:'<b>加入购物车失败</b>',
                            template:message
                        });
                    }
                    deferred.resolve(response);
                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("addCart连接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            },
          delCart: function delCart(gid){
              var deferred = $q.defer();
              console.log("gid","商品id:"+gid);
              $ionicLoading.show({
                  template: 'Loading...'
              });
              var url = 'http://192.168.15.42:8080/store?domain=delCart&gid='+gid;

              $http.post(url).success(function (response) {
                  $ionicLoading.hide();
                  if(response.code=='success')
                  {
                      $ionicPopup.alert({
                          title:'<b>删除购物车成功</b>',
                          template:''
                      });
                  }
                  else
                  {
                      var message=response.message;
                      $ionicPopup.alert({
                          title:'<b>删除购物车失败</b>',
                          template:message
                      });
                  }
                  deferred.resolve(response);
              }).error(function (data) {
                      $ionicLoading.hide();
                      console.log("delCart连接失败!");
                      deferred.reject();
                  });
              return deferred.promise;
          }
        };
    })
    .factory('goodsDetailFactory', function($http, $q,$ionicPopup,$ionicLoading){

        return {
            getGoodsdetail: function getGoodsdetail(gid){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=getGoodsdetail&gid='+gid;
//                if(param!=null)
//                    url=url+"&orderparam="+param;
                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    var resultJson = response;
             //       var array = [];
//                    for (var i = 0; i < resultJson.length ; i++) {
//                        array.push(resultJson[i]);
//                    }
                    deferred.resolve(resultJson);
                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("getGoodslist连接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            }
        };
    })                      //确认订单
    .factory('ConfirmOrderFactory', function($http, $q,$ionicPopup,$ionicLoading){

        return {
            //把所有购物车的id用''括起来拼接成字符串传到后台去
            acquirePersonalInfo: function acquirePersonalInfo(cartidlist){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=acquireConfirmOrderInfo&idlist='+cartidlist;
//                if(param!=null)
//                    url=url+"&orderparam="+param;
                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    if(response.code=='NoLogin')
                    {
                        //            deferred.resolve(rootresult);
                        $ionicPopup.alert({
                            title:'<b>用户信息获取不到，请重新登录微信</b>',
                            template:''
                        });
                        deferred.reject();
                    }
                    else if(response.code=='NoAddress')
                    {
                        deferred.resolve(response);
                    }
                    else if(response.code=="success")
                    {
                        var addressJson = response.addresslist;
                        var cartJson=response.cartlist;
                        var cartarray = [];
                        var addressarray = [];
                        for (var i = 0; i < addressJson.length ; i++) {
                            addressarray.push(addressJson[i]);
                        }
                        for (var i = 0; i < cartJson.length ; i++) {
                            cartarray.push(cartJson[i]);
                        }
                        console.log("addresslist",addressarray);
                        console.log("cartlist",cartarray);
                        var rootresult=new Object();
                        rootresult.addresslist=addressarray;
                        rootresult.cartlist=cartarray;
                        rootresult.code='success';
                        deferred.resolve(rootresult);   //接收字段的是then方法回调
                    }
                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("acquirePersonalInfo连接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            }
        };
    })
    //优惠券


    .factory('DiscountFactory', function($http, $q,$ionicPopup,$ionicLoading){

        return {
            //之间的传值，优惠券，优惠券列表和确认订单列表之间的传值


            discount:null,
            //把所有购物车的id用''括起来拼接成字符串传到后台去
            setCoupon:function setCoupon(coupon)
            {
                this.discount=coupon;
            },
            getCoupon:function getCoupon(coupon)
            {
                return this.discount;
            },

            getCouponlist: function getCouponlist(status){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=getDiscountByUser';
                if(status!=null)
                     status=status+'&status='+status;
//                if(param!=null)
//                    url=url+"&orderparam="+param;
                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    if(response.code=='NoLogin')
                    {
                        //            deferred.resolve(rootresult);
                        $ionicPopup.alert({
                            title:'<b>用户信息获取不到，请重新登录微信</b>',
                            template:''
                        });
                        deferred.reject();
                    }

                    else if(response.code=="success")
                    {
                        var couponlistJson = response.data;
                        var couponarray = [];
                        for (var i = 0; i < couponlistJson.length ; i++) {
                            couponarray.push(couponlistJson[i]);
                        }
                        console.log("couponlist",couponarray);
                        deferred.resolve(couponarray);   //接收字段的是then方法回调
                    }
                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("getCouponlist连接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            }
        };
    })
    //个人中心
    .factory('UserCenterFactory', function($http, $q,$ionicPopup,$ionicLoading){

        return {
            //之间的传值，优惠券，优惠券列表和确认订单列表之间的传值
             getUserInfo: function getUserInfo(){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=getUserDetailinfo';

                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    deferred.resolve(response);
                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("getUserInfo连接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            } ,
            modifyAvatar: function modifyAvatar(avatar){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=modifyUserDetailinfo&param=avatar';
                if(avatar!=null)
                {
                    url+='&imgurl='+avatar;
                }
                else
                {
                    alert('系统错误');
                }
                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    deferred.resolve(response);
                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("modifyAvatar连接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            },
            modifySex: function modifySex(sex){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=modifyUserDetailinfo&param=gendor';
                if(sex!=null)
                {
                    url+='&sex='+sex;
                }
                else
                {
                    alert('系统错误');
                }
                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    deferred.resolve(response);
                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("modifySex连接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            },
            modifyName: function modifyName(name){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=modifyUserDetailinfo&param=name';
                if(name!=null)
                {
                    url+='&name='+name;
                }
                else
                {
                    alert('系统错误');
                }
                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    deferred.resolve(response);
                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("modifyName连接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            } ,
            modifyMobilephone: function modifyMobilephone(mobilephone){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=modifyUserDetailinfo&param=mobile';
                if(mobilephone!=null)
                {
                    url+='&mobile='+mobilephone;
                }
                else
                {
                    alert('系统错误');
                }
                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    deferred.resolve(response);
                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("modifyMobilephone连接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            },
            modifyBirthdate: function modifyBirthdate(birthdate){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=modifyUserDetailinfo&param=birthdate';
                if(birthdate!=null)
                {
                    url+='&birthdate='+birthdate;
                }
                else
                {
                    alert('系统错误');
                }
                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    deferred.resolve(response);
                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("modifyBirthdate连接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            }
        };
    })
    .factory('AddressFactory', function($http, $q,$ionicPopup,$ionicLoading){

        return {
            //把所有购物车的id用''括起来拼接成字符串传到后台去
            getAllAddress: function getAllAddress(){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=getAllAddress';
//                if(param!=null)
//                    url=url+"&orderparam="+param;
                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    var addressarray=[];
                    var addressJson=response;
                    for (var i = 0; i < addressJson.length ; i++) {
                      addressarray.push(addressJson[i]);
                    }
                    deferred.resolve(addressarray);   //接收字段的是then方法回调

                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("acquirePersonalInfo连接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            } ,
            aquireAddressByAid: function aquireAddressByAid(aid){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=aquireAddressByAid';
                if(aid!=null)
                    url=url+"&aid="+aid;
                else
                    alert('系统错误');
                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    deferred.resolve(response);   //接收字段的是then方法回调

                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("acquirePersonalInfo连接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            } ,
            updateDefaultAddress: function updateDefaultAddress(lid,aid){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=updateDefaultAddress';
                if(aid!=null)
                    url=url+"&aid="+aid;
                else
                    alert('系统错误');
                if(lid!=null)
                    url=url+"&lid="+lid;
                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    deferred.resolve(response);   //接收字段的是then方法回调

                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("acquirePersonalInfo连接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            } ,
            addAddress: function addAddress(contactmobile,receivename,province,city,detailaddress){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=addAddress';
                if(contactmobile!=null)
                    url=url+'&contactmobile='+contactmobile;
                if(receivename!=null)
                    url=url+'&receivename='+receivename;
                if(province!=null)
                    url=url+'&province='+province;
                if(city!=null)
                    url=url+'&city='+city;
                if(detailaddress!=null)
                    url=url+'&detailaddress='+detailaddress;
                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    deferred.resolve(response);   //接收字段的是then方法回调

                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("acquirePersonalInfo连接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            },
            editAddress: function editAddress(aid,contactmobile,receivename,province,city,detailaddress){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=editAddress';
                if(contactmobile!=null)
                    url=url+'&contactmobile='+contactmobile;
                if(receivename!=null)
                    url=url+'&receivename='+receivename;
                if(province!=null)
                    url=url+'&province='+province;
                if(city!=null)
                    url=url+'&city='+city;
                if(detailaddress!=null)
                    url=url+'&detailaddress='+detailaddress;
                if(aid!=null)
                    url=url+"&aid="+aid;
                else
                    alert('系统错误');
                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    deferred.resolve(response);   //接收字段的是then方法回调

                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("acquirePersonalInfo连接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            },
            delAddress: function delAddress(aid){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=delAddress';

                if(aid!=null)
                    url=url+"&aid="+aid;
                else
                    alert('系统错误');
                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    deferred.resolve(response);   //接收字段的是then方法回调

                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("delAddress连接失败!");
                        deferred.reject();
                    });
                return deferred.promise;
            }
        };
    })
    .factory('CookieFactory', function(){

        return {
            setCookie:function setCookie(name,value)
            {
                var Days = 30;
                var exp = new Date();
                exp.setTime(exp.getTime() + Days*24*60*60*1000);
                document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
            },
            getCookie:function getCookie(name)
            {
                var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
                if(arr=document.cookie.match(reg))
                    return unescape(arr[2]);
                else
                    return null;
            },
            setCookieByTime: function setCookieByTime(name,value,time)
            {
                var strsec;
                var str1=time.substring(1,time.length)*1;
                var str2=time.substring(0,1);
                if (str2=="s")
                {
                    strsec=str1*1000;
                }
                else if (str2=="h")
                {
                    strsec=str1*60*60*1000;
                }
                else if (str2=="d")
                {
                    strsec=str1*24*60*60*1000;
                }

    //            var strsec = getsec(time);
                var exp = new Date();
                exp.setTime(exp.getTime() + strsec*1);
                document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
            },
            delCookie:function delCookie(name)
            {
                var exp = new Date();
                exp.setTime(exp.getTime() - 1);
                var cval=getCookie(name);
                if(cval!=null)
                    document.cookie= name + "="+cval+";expires="+exp.toGMTString();
            }
         };
    })
    .factory('OrderFactory', function($http, $q,$ionicPopup,$ionicLoading){

        return {
            //生成为支付订单


            orderprice:null,//实际付的金额
            orderid:null,   //orderid ID        正在支付的订单id,金额
            detailorderid:null,     //当前进入订单详情的orderid
            //给正要付款的订单赋值id
            setOrderId:function setOrderId(orderid){
              this.orderid=orderid;
            },
            getOrderId:function getOrderId(){
              return this.orderid;
            },
            setDetailOrderid:function(orderid){
              this.detailorderid=orderid;
            },
            getDetailOrderid:function(){
              return this.detailorderid;
            },
            setOrderprice:function setOrderprice(orderprice){
              this.orderprice=orderprice;
            },
            getOrderprice:function getOrderprice(){
              return this.orderprice;
            },
            produceNotPayOrder:function produceNotPayOrder(cartidlist,cartallprice,UC_ID,paramlist,couponid){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=produceNotPayOrder';
                if(cartidlist!=null)
                    url=url+"&cartidlist="+cartidlist;
                if(cartallprice!=null)
                    url=url+"&cartallprice="+cartallprice;
                if(UC_ID!=null)
                    url=url+"&UC_ID="+UC_ID;

                if(paramlist!=null)
                    url=url+"&paramlist="+paramlist;
                if(couponid!=null)
                    url=url+"&couponid="+couponid;
                $http.post(url).success(function (response) {
                    $ionicLoading.hide();
                    if(response.code=='NoLogin')
                    {
                        //            deferred.resolve(rootresult);
                        $ionicPopup.alert({
                            title:'<b>用户信息获取不到，请重新登录微信</b>',
                            template:''
                        });
                        deferred.reject();
                    }
                    else if(response.code=='NoNumber')
                    {
                        var jsonarray=response.data;
                        var resultmessage='';
                        for(var i=0;i<jsonarray.length;i++) {
                             resultmessage=resultmessage+" "+jsonarray[i].G_NAME+" 商品库存不足,仅有"+jsonarray[i].G_AMOUNT+"件/个";
                        }
                        $ionicPopup.alert({
                            title:response.message,
                            template:resultmessage
                        });
                    }
                    else if(response.code=="success")
                    {
                        $ionicPopup.alert({
                             template:response.message
                        }); //过滤传到controller then方法的数据，此处为允许这个数据传递到controller里面
                        deferred.resolve(response);
                    }
                }).error(function (data) {
                        $ionicLoading.hide();
                        console.log("OrderFactory连接失败!");
                        deferred.reject();
                    });
                return deferred.promise;

            },
            //改变订单状态


            getPayOrderByStatus:function getPayOrderByStatus(orderstatus){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=getPayOrderByStatus';
                if(orderstatus!=null)
                     url=url+"&orderstatus="+orderstatus;
                    $http.post(url).success(function (response) {
                        $ionicLoading.hide();
                        if(response.code=='NoLogin')
                        {
                            $ionicPopup.alert({
                                title:'<b>用户信息获取不到，请重新登录微信</b>',
                                template:''
                            });
                            deferred.reject();
                        }
                        else if(response.code=="success")
                        {
                            var orderlistJson = response.data;
                            var orderarray = [];

                            for (var i = 0; i < orderlistJson.length ; i++) {
                                var listjson=orderlistJson[i];
                                var count=parseInt(listjson.INFO_AMOUNT);
                                var price=parseFloat(listjson.G_PRICE);
                                var allprice=count*price;
                                listjson.allprice=allprice;
                                orderarray.push(listjson);
                            }
                            deferred.resolve(orderarray);
                        }

                    }).error(function (data) {
                            $ionicLoading.hide();
                            console.log("getPayOrderByStatus连接失败!");
                            deferred.reject();
                        });
                    return deferred.promise;        //defered.promise    有then方法


            },
           //改变订单状态


            /**
             *
             * @param O_ORDERID             订单ID
             * @param orderstatus           新的状态


             * @param allprice              需要付款总价参数
             * @return {*}
             */
            modifyPayOrder:function modifyPayOrder(O_ORDERID,orderstatus,allprice){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=modifyPayOrder';
                if(O_ORDERID==null||orderstatus==null)
                {
                    alert("系统错误");
                    return;
                }
                else
                {
                      url=url+"&O_ORDERID="+O_ORDERID+"&orderstatus="+orderstatus;
                      if(allprice!=null)
                          url=url+"&allprice="+allprice;

                      $http.post(url).success(function (response) {
                         $ionicLoading.hide();
                         if(response.code=='NoLogin')
                         {
                              $ionicPopup.alert({
                                    title:'<b>用户信息获取不到，请重新登录微信</b>',
                                    template:''
                              });
                              deferred.reject();
                         }
                         else if(response.code=='NoParam')
                         {
                             $ionicPopup.alert({
                                 title:'<b>访问失败</b>',
                                 template:response.message
                             });
                             deferred.reject();
                         }
                         else if(response.code=="success")
                              deferred.resolve(response);
                     }).error(function (data) {
                             $ionicLoading.hide();
                             console.log("modifyPayOrder连接失败!");
                             deferred.reject();
                     });
                     return deferred.promise;

                    }
                },
            getOrderDetail:function getOrderDetail(O_ORDERID){
                var deferred = $q.defer();
                $ionicLoading.show({
                    template: 'Loading...'
                });
                var url = 'http://192.168.15.42:8080/store?domain=getOrderDetailById';
                if(O_ORDERID==null)
                {
                    alert("系统错误");
                    deferred.reject();
                }
                else
                {
                    url=url+"&O_ORDERID="+O_ORDERID;
                    $http.post(url).success(function (response) {
                        $ionicLoading.hide();
                        if(response.code=='NoLogin')
                        {
                            $ionicPopup.alert({
                                title:'<b>用户信息获取不到，请重新登录微信</b>',
                                template:''
                            });
                            deferred.reject();
                        }
                        else if(response.code=='NoParam')
                        {
                            $ionicPopup.alert({
                                title:'<b>访问失败</b>',
                                template:response.message
                            });
                            deferred.reject();
                        }
                        else if(response.code=="success")
                        {
                            var array=[];
                            var result=response.data.listjson;    //listjson商品列表信息      infojson基本信息
                            for(var i=0;i<result.length;i++)
                            {
                                array.push(result[i]);
                            }
                            var detailinfo={
                                basicinfo:response.data.infojson,
                                goodslistinfo:response.data.listjson
                            };
                            deferred.resolve(detailinfo); //向then传递数据，deferred.promise传过去用他的then方法，他的做法就是把jsonarray对象转化成javascript数组对象，前端只识别javascript数组对象
                        }

                    }).error(function (data) {
                            $ionicLoading.hide();
                            console.log("getOrderDetail连接失败!");
                            deferred.reject();
                        });
                    return deferred.promise;

                }
            }


        };
    })

    .factory('sqlFactory', function(){

        return {
           test1:function test1(){
               var db = new SQL.Database();
               // Run a query without reading the results
               db.run("CREATE TABLE test (col1, col2);");
               // Insert two rows: (1,111) and (2,222)
               db.run("INSERT INTO test VALUES (?,?), (?,?)", [1,111,2,222]);

               // Prepare a statement
               var stmt = db.prepare("SELECT * FROM test WHERE col1 BETWEEN $start AND $end");
               stmt.getAsObject({$start:1, $end:1}); // {col1:1, col2:111}

               // Bind new values
               stmt.bind({$start:1, $end:2});
               while(stmt.step()) { //
                   var row = stmt.getAsObject();
                   console.log("row:"+row.col1);
                   // [...] do something with the row of result
               }
               db.close();
           }
        };
    })

;
