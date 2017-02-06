angular.module("indexApp", ["ionic","ui.router","ionic-datepicker","indexApp.controllers","indexApp.services"])
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
                templateUrl:"secureIndex.html",
                controller: 'mainCtrl'
            })
            .state('customereg', {
                url:"/customereg",
                cache:false,
                templateUrl:"customReg.html",
                controller: 'RegCtrl'
            })
            .state('allManager', {
                url:"/allManager",
                cache:false,
                templateUrl:"allManager.html",
                controller: 'AllManCtrl'
            })
            .state('managerlist', {
                url:"/managerlist",
                cache:false,
                templateUrl:"managerList.html",
                controller: 'ManCusCtrl'
            })
            .state('userList', {
                url:"/userList",
                cache:false,
                templateUrl:"userList.html",
                controller: 'UserCtrl'
            })
            .state('allUsers', {
                url:"/allUsers",
                cache:false,
                templateUrl:"allUsers.html",
                controller: 'AllUserCtrl'
            })
            .state('salesreg', {
                url:"/salesreg",
                cache:false,
                templateUrl:"salesRegister.html",
                controller: 'RegCtrl'
            })
            .state('secureDetail', {
                url:'/secureDetail?sdid',
                cache:false,
                templateUrl: 'secureDetail.html',
                controller: 'SecureDetailCtrl'
            })
            .state('cusEdit', {
                url:'/cusEdit?sdid',
                cache:false,
                templateUrl: 'cusEdit.html',
                controller: 'cusEditCtrl'
            })
        ;
        //两个模块控制器不能一样，或者控制器有问题

        $urlRouterProvider.otherwise("/main");
    });
