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



        $stateProvider
            .state('main', {
                url:"/main",
                cache:false,
                templateUrl:"main.html",
                controller: 'mainCtrl'
            })
            .state('list', {
                url:"/list/:cid/:cityname",
                cache:false,
                templateUrl:"list.html",
                controller: 'listCtrl'
            })
            .state('detail', {
                url:"/detail/:aid",
                cache:false,
                templateUrl:"detail.html",
                controller: 'detailCtrl'
            })
        ;
        //两个模块控制器不能一样，或者控制器有问题
        $urlRouterProvider.otherwise("/main");
    });
