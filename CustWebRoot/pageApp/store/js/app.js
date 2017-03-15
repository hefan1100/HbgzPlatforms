angular.module("indexApp", ["ionic","ui.router","indexApp.controllers","indexApp.services"])
    .config(function ($stateProvider, $urlRouterProvider,$ionicConfigProvider) {
        $ionicConfigProvider.platform.ios.tabs.style('standard');
        $ionicConfigProvider.platform.ios.tabs.position('bottom');
        $ionicConfigProvider.platform.android.tabs.style('standard');
        $ionicConfigProvider.platform.android.tabs.position('bottom');

        $ionicConfigProvider.platform.ios.navBar.alignTitle('center');
        $ionicConfigProvider.platform.android.navBar.alignTitle('center');

        $ionicConfigProvider.platform.ios.backButton.previousTitleText('').icon('ion-ios-arrow-thin-left');
        $ionicConfigProvider.platform.android.backButton.previousTitleText('').icon('ion-android-arrow-back');

        $ionicConfigProvider.platform.ios.views.transition('ios');
        $ionicConfigProvider.platform.android.views.transition('android');
        //允许开启滑动，ion下拉加载更多时
        $ionicConfigProvider.scrolling.jsScrolling(true);

        $stateProvider
            .state('tabs', {
                url:"/tab",
                abstract:true,
                templateUrl:"tabs.html"
            })
            .state('tabs.main', {
                url:"/main",
                cache:false,
                views:{
                    'main-tab':{
                        templateUrl:"main.html",
                        controller: 'mainCtrl'
                    }
                }
            })
            .state('tabs.mine', {
                url:"/mine",
                cache:false,
                views:{
                    'mine-tab':{
                        templateUrl:"mine.html",
                        controller: 'mineCtrl'
                    }
                }
            })

            .state('tabs.clarify', {
                url:"/clarify",
                cache:false,
                views:{
                    'clarify-tab':{
                        templateUrl:"clarify.html",
                        controller: 'clarifyCtrl'
                    }
                }
            })
            .state('tabs.cart', {
                url:"/cart",
                cache:false,
                views:{
                    'cart-tab':{
                        templateUrl:"cart.html",
                        controller: 'cartCtrl'
                    }
                }
            })
            .state('tabs.may', {
                url:"/may",
                cache:false,
                views:{
                    'may-tab':{
                        templateUrl:"date.html",
                        controller: 'dateCtrl'
                    }
                }
            })

            .state('goodslist', {
                cache:false,
                url:"/goodslist/:clarifyId",
                templateUrl:"goodslist.html",
                controller: 'goodslistCtrl'
              })

            .state('orderslist', {
                url:"/orderslist",
                cache:false,
                templateUrl:"myorder.html",
                controller: 'orderslistCtrl'
            })

            .state('setting', {
                url:"/setting",
                cache:false,
                templateUrl:"setting.html",
                controller: 'settingCtrl'
            })

            .state('accountadminister', {
                url:"/accountadminister",
                cache:false,
                templateUrl:"accountadminister.html",
                controller: 'accountadministerCtrl'
            })

            .state('personalinfo', {
                url:"/personalinfo",
                cache:false,
                templateUrl:"personalinfodetail.html",
                controller: 'personalinfoCtrl'
            })

            .state('goodsdetail', {
                url:"/goodsdetail/:gid",
                cache:false,
                templateUrl:"goodsdetail.html",
                controller: 'goodsdetailCtrl'
            })

            .state('confirmorder', {
                url:"/confirmorder",
                cache:false,
                templateUrl:"confirmorder.html",
                controller: 'orderconfirmCtrl'
            })
            //立即购买
            .state('buyinstance', {
                url:"/buyinstance/:gid",
                cache:false,
                templateUrl:"confirmorder.html",
                controller: 'buyinstanceCtrl'
            })

            .state('cart', {
                url:"/cart",
                cache:false,
                templateUrl:"cart.html",
                controller: 'cartCtrl'
            })

            .state('discount', {
                url:"/discount",
                cache:false,
                templateUrl:"discount.html",
                controller: 'discountCtrl'
            })

            .state('pay', {
                url:"/pay",
                cache:false,
                templateUrl:"pay.html",
                controller: 'payCtrl'
            })

            .state('paysuccess', {
                url:"/paysuccess",
                cache:false,
                templateUrl:"paysuccess.html",
                controller: 'paysuccessCtrl'
            })

            .state('orderdetail', {
                url:"/orderdetail/:oid",
                cache:false,
                templateUrl:"myorderdetail.html",
                controller: 'orderdetailCtrl'
            })

            .state('eval', {
                url:"/evaluate/:oid",
                cache:false,
                templateUrl:"evaluate.html",
                controller: 'evaluateCtrl'
            })

            .state('addresslist', {
                url:"/addresslist",
                cache:false,
                templateUrl:"addressadminister.html",
                controller: 'addressadministerCtrl'
            })

            .state('modifyname', {
                url:"/modifyname",
                cache:false,
                templateUrl:"modifyUserName.html",
                controller: 'modifyusernameCtrl'
            })

            .state('modifymobilephone', {
                url:"/modifymobilephone",
                cache:false,
                templateUrl:"modifyMobilePhone.html",
                controller: 'modifymobilephoneCtrl'
            })

            .state('addaddress', {
                url:"/addaddress",
                cache:false,
                templateUrl:"addaddress.html",
                controller: 'addaddressCtrl'
            })

            .state('editaddress', {
                url:"/editaddress/:aid",
                cache:false,
                templateUrl:"editaddress.html",
                controller: 'editaddressCtrl'
            })
            .state('storedianpu', {
                url:"/storedianpu/:supid",
                cache:false,
                templateUrl:"storedianpu.html",
                controller: 'dianpuCtrl'
            })
        ;
        //两个模块控制器不能一样，或者控制器有问题

        $urlRouterProvider.otherwise("/tab/main");
    });
