angular.module("indexApp", ["ionic","ui.router","indexApp.controllers","indexApp.services"])
    .run(['$ionicPlatform','$location','$rootScope','$ionicHistory',
        function ($ionicPlatform,$location,$rootScope,$ionicHistory) {


            /********************双击退出start********************/
            $ionicPlatform.registerBackButtonAction(function (e) {
                //判断处于哪个页面时双击退出
                if ($location.path() == '/main' ) {
                    if ($rootScope.backButtonPressedOnceToExit) {
                        ionic.Platform.exitApp();
                    } else {
                        $rootScope.backButtonPressedOnceToExit = true;
                        //$cordovaToast.showShortBottom('再按一次退出系统');
                        setTimeout(function () {
                            $rootScope.backButtonPressedOnceToExit = false;
                        }, 2000);
                    }
                }
                else if ($ionicHistory.backView()) {
                    $ionicHistory.goBack();
                } else {
                    $rootScope.backButtonPressedOnceToExit = true;
                    //$cordovaToast.showShortTop('再按一次退出系统')
                    //    .then(function(success) {
                    //        // success
                    //        alert("'success");
                    //    }, function (error) {
                    //        // error
                    //        alert("error");
                    //    });
                    setTimeout(function () {
                        $rootScope.backButtonPressedOnceToExit = false;
                    }, 2000);
                }
                e.preventDefault();
                return false;
            }, 101)
        }])
    .config(function ($stateProvider, $urlRouterProvider,$ionicConfigProvider,$httpProvider) {
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

        $httpProvider.defaults.headers.post = { 'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8' };

        $stateProvider
            .state('main', {
                url:"/main",
                cache:false,
                templateUrl:"main.html",
                controller: 'mainCtrl'
            })
            .state('yxlist', {
                url:"/yxlist",
                cache:false,
                templateUrl:"yxlist.html",
                controller: 'YxlistCtrl'
            })
            .state('chats', {
                url:"/chats",
                cache:false,
                templateUrl:"chats.html",
                controller: 'chatsCtrl'
            })
            .state('advise', {
                url:"/advise",
                cache:false,
                templateUrl:"advise.html",
                controller: 'adviseCtrl'
            })
            .state('phone', {
                url:"/phone",
                cache:false,
                templateUrl:"phone.html",
                controller: 'phoneCtrl'
            })
        ;
        //两个模块控制器不能一样，或者控制器有问题
        $urlRouterProvider.otherwise("/main");
    });
