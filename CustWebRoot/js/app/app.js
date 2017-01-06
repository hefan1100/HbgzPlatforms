angular.module("indexApp", ["ionic","ui.router","indexApp.controllers","indexApp.services"])
angular.module('myApp', ['ionic-citypicker'])
    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('tabs', {
                url:"/tab",
                abstract:true,
                templateUrl:"tabs.html"
            })
            .state('tabs.main', {
                url:"/main",
                cache: false,
                views:{
                    'main-tab':{
                        templateUrl:"main.html",
                        controller: 'mainCtrl'
                    }
                }
            })
//            .state('checkList', {
//                url:"/tab/main/checkList/:doIt",
//                cache: false,
//                templateUrl:"checkList.html",
//                controller: 'checkListCtrl'
//            })
            .state('tabs.news', {
                url:"/news",
                cache: false,
                views:{
                    'news-tab':{
                        templateUrl:"news.html",
                        controller: 'newsCtrl'
                    }
                }
            })
            .state('tabs.mailList', {
                url:"/mailList",
                views:{
                    'mailList-tab':{
                        templateUrl:"mailList.html",
                        controller: 'mailListCtrl'
                    }
                }
            })
//            .state('queryBidInfoDetail', {
//                url:"/tab/main/queryBidInfoDetail/:domain/:demdId",
//                cache: false,
//                templateUrl:"queryBidInfoDetail.html",
//                controller: 'queryDetailCtrl'
//            })
//            .state('queryBidMoneyDetail', {
//                url:"/tab/main/queryBidMoneyDetail/:domain/:demdId",
//                cache: false,
//                templateUrl:"queryBidMoneyDetail.html",
//                controller: 'queryDetailCtrl'
//            })
//            .state('queryCttInfoDetail', {
//                url:"/tab/main/queryCttInfoDetail/:domain/:demdId",
//                cache: false,
//                templateUrl:"queryCttInfoDetail.html",
//                controller: 'queryDetailCtrl'
//            })
//            .state('queryIncRecDetail', {
//                url:"/tab/main/queryIncRecDetail/:domain/:demdId",
//                cache: false,
//                templateUrl:"queryIncRecDetail.html",
//                controller: 'queryDetailCtrl'
//            })
//            .state('queryInvAppDetail', {
//                url:"/tab/main/queryInvAppDetail/:domain/:demdId",
//                cache: false,
//                templateUrl:"queryInvAppDetail.html",
//                controller: 'queryDetailCtrl'
//            })
//            .state('queryPayAppDetail', {
//                url:"/tab/main/queryPayAppDetail/:domain/:demdId",
//                cache: false,
//                templateUrl:"queryPayAppDetail.html",
//                controller: 'queryDetailCtrl'
//            })
//            .state('queryPurcConDetail', {
//                url:"/tab/main/queryPurcConDetail/:domain/:demdId",
//                cache: false,
//                templateUrl:"queryPurcConDetail.html",
//                controller: 'queryDetailCtrl'
//            })
//            .state('queryStoCkDetail', {
//                url:"/tab/main/queryStoCkDetail/:domain/:demdId",
//                cache: false,
//                templateUrl:"queryStoCkDetail.html",
//                controller: 'queryDetailCtrl'
//            })
        ;
        $urlRouterProvider.otherwise("/tab/main");
    });
