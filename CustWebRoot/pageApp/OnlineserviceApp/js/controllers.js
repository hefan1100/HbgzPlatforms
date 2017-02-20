angular.module("indexApp.controllers",[])
    .controller('mainCtrl', function ($scope,$rootScope,$ionicHistory,$http, $ionicPopup, $state,$cacheFactory,$ionicSlideBoxDelegate) {
        $scope.screenHeight=document.body.clientHeight; //网页可见高度
        $scope.screenWidth=window.screen.width;
        //集合大小
        $scope.listStyle= {
            "height":$scope.screenHeight*0.8+'px',
            "width":'80%',
            "display":"inline"
        };
        //间隙大小
        $scope.itemStyle= {
            "height":$scope.screenHeight*0.4+'px',
            "width":'100%'
        };

        $scope.goChats=function(){
            $state.go('chats');
        };

        $scope.goPhone=function(){
            $state.go('phone');
        };

    })
    .controller('YxlistCtrl', function ($scope,$rootScope,$ionicHistory,$http, $ionicPopup, $state,$cacheFactory,$ionicSlideBoxDelegate,IndexFactory) {
        $scope.searchtext='';
   //     $scope.chats=[obj,obj,obj,obj];
        $scope.objitem=new Object();
        $scope.objitem.s='sfsfdsfsfsdfdsfds';
   //     $scope.objitem.r='sdfsfsfddsfsfsfsdfsdsfs';
    //    var recordsarray=[objitem,objitem,objitem];
        $scope.records = [];
        $scope.send=function(){
      //      alert('input:'+$scope.searchtext);
            $scope.doReply();
        };

        $scope.doReply=function(){
            IndexFactory.getChatsReply($scope.searchtext).then(function(response){
         //       $scope.articlelist=response;
                var array= $scope.records;
        //        array.push($scope.objitem);
                $scope.objitem.s=response.se;
                $scope.objitem.r=response.re;
                array.push($scope.objitem);
                $scope.records=array;
            });
        };
    })
    .controller('chatsCtrl', function ($scope,$rootScope,$ionicHistory,$http, $ionicPopup, $state,$cacheFactory,$ionicSlideBoxDelegate,IndexFactory,$ionicScrollDelegate,$timeout) {
        $scope.availubleHeight=screen.availHeight;
        $scope.availwidth=screen.availWidth;
       $scope.sendText='';
        $scope.constantText=[{
          text: "[1]寄递服务",
            digital:1
        },{
            text: "[2]邮件到哪里了，什么时候可以到",
            digital:2
        },{
            text: "[3]投诉与建议",
            digital:3
        }
        ];
       $scope.send=function(){
           if($scope.sendText==null||$scope.sendText=='')
                return;
           var correspond=false;
           correspond=$scope.correspondByDigital($scope.sendText);
           if(correspond==true) {
               var d=parseInt($scope.sendText);
               if(d==3)
               {
                   $scope.addHtml(null);
                   $timeout(function(){
                      $state.go("advise");
                   },500);
               }
               else {
                   IndexFactory.getChatsReply($scope.sendText).then(function (response) {
                       $scope.addHtml(response.reply);
                       $scope.sendText="";
                   });
               }
           }
           else{
               $scope.addHtml(null);
               $scope.sendText="";
           }

       };

        $scope.addHtml=function(reply)
        {
            var htmlcode = $scope.createHtml($scope.sendText, reply);
            var s = $('#listid').html();
            s += htmlcode;
            $('#listid').html(s);
            $ionicScrollDelegate.scrollBottom(1000);
        }

        $scope.sendMessage=function(typeid){
            if(typeid==1)
            {
                $scope.sendText="1";
                $scope.send();
            }
            else if(typeid==2)
            {
                $scope.sendText="2";
                $scope.send();
            }
            else if(typeid==3)
            {
                $scope.sendText="3";
                $scope.send();
            }
        }

       $scope.titleStyle={
            "height":$scope.availubleHeight*0.2+'px',
            "width": '100%',
            "text-align":"center"
       }

       $scope.titleitemStyle={
           "width": "100%",
           "height":$scope.availubleHeight*0.05+'px',
           "border": "none",
           "background-color":"white"
       };

        $scope.titleitemgStyle={
            "width": "100%",
            "height":$scope.availubleHeight*0.05+'px',
            "color":"#8CBC42",
            "border": "none",
            "background-color":"white"
        };



       $scope.createHtml=function(send,reply)
        {
            var html="<ion-item style='border: none;min-height:100px;max-height:360px;padding: 0px;background-color: #EEEEEE;'>";
            html+="<div class='list' style='background-color: #EEEEEE;'>";
            html+="<div class='item' style='width: 100%; border: none;background-color: #EEEEEE;'>";
            html+="<div style='min-height: 50px;padding: 20px;line-height:150%;max-width:33%;min-width:5%;float: right;background-image: url(img/dialog_box@2x.png);background-size: 100% 100%;background-repeat: no-repeat;white-space: normal;font-size:1em;'>";
            html+=send;
            html+="</div>";
            html+="</div>";
            if(reply!=null) {
                html += "<div class='item' style='width: 100%;border: none;background-color: #EEEEEE;'>";
                html += "<div style='min-height: 50px;min-width: 5%;line-height:150%;max-width:50%;float: left;background-image: url(img/dialog_kf@2x.png);background-size: 100% 100%;background-repeat: no-repeat;white-space: normal;padding: 20px;font-size:1em;'>";
                html += reply;
                html += "</div>";
                html += "</div>";
            }
            html+="</div>";
            html+="</ion-item>";
            return html;
        }



        $scope.correspondByDigital=function(mess)
        {
            var iscorrespond=false;
            var textobj=$scope.constantText;
            if(mess.length>1)
                return false;
            else {
                var id=null;
                try {
                    id= parseInt(mess);
                }catch (e)
                {
                    return false;
                }
                if(id!=null) {
                    for (var i = 0; i < textobj.length; i++) {
                        if(textobj[i].digital==id)
                       {
                           iscorrespond=true;
                           break;
                       }
                    }
                }
                return iscorrespond;
            }
            return iscorrespond;
        }

        $scope.itemStyle={
            "height":$scope.availubleHeight*0.1+'px',
            "width":"100%",
            "border":"none"
        };

        $scope.itemButtonStyle={
            "height":$scope.availubleHeight*0.1+'px',
            "width":"100%",
            "border":"none",
            "background-color":"red",
            "text-align": "center"
        };

        $scope.itemContentStyle={
            "height":$scope.availubleHeight*0.3+'px',
            "width":"100%",
            "border":"none"
        };
    })

    .controller('adviseCtrl', function ($timeout,$ionicHistory,$scope,$rootScope,$ionicHistory,$http, $ionicPopup, $state,$cacheFactory,$ionicSlideBoxDelegate,IndexFactory,$ionicScrollDelegate) {
        $scope.availubleHeight=screen.availHeight;
        $scope.availwidth=screen.availWidth;
        $scope.width=window.screen.width;
        $scope.modelstyle={
            "width":$scope.width*0.8+"px",
            "margin-left": $scope.width*0.1+"px",
            "margin-right":$scope.width*0.1+"px"
        };

        var pageH = window.innerHeight;


        $scope.submitInfo=function(){
             var phonereg =  /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,2,3,5-9]))\\d{8}$/;
             var isphone=testPhone();
             if(!isphone)
                 $scope.showAlert("请您输入正确的手机号");
             else
             {
                  if(($scope.name==null)||($scope.name==''))
                  {
                      $scope.showAlert("请您输入您的姓名");
                      return;
                  }
                     if(($scope.sex==null)||($scope.sex==''))
                     {
                         $scope.showAlert("请您输入您的性别");
                         return;
                     }
                 if(($scope.advisecontent==null)||($scope.advisecontent==''))
                 {
                     $scope.showAlert("请您输入您的提交内容");
                     return;
                 }
                 if(($scope.phonenumber==null)||($scope.phonenumber==''))
                 {
                     $scope.showAlert("请您输入您的联系方式");
                     return;
                 }
                 else
                 {
                     var params={
                         name:$scope.name,
                         sex:$scope.sex,
                         advisecontent:$scope.advisecontent,
                         ordernumber:$scope.ordernumber,
                         phonenumber:$scope.phonenumber
                     };
                     IndexFactory.submitForAdvise(params).then(function (response) {
                         var popup=$scope.showAlert(response.message);
                         $timeout(function(){
                             popup.close();
                         },1500);
                         //$cordovaToast.showShortBottom(response.message);
                         if(response.code=='success')
                            $state.go("main");
                     });
                 }
             }
         };
         $scope.name=""; //名字
         $scope.sex="";  //性别
         $scope.advisecontent=""; //提交内容
         $scope.ordernumber="";   //订单号
         $scope.phonenumber="";   //电话号码
        $scope.form = $scope;
        $scope.showConfirm = function(title) {
            var confirmPopup = $ionicPopup.confirm({
                title: title
            });
            confirmPopup.then(function(res) {

            });
            return  confirmPopup;
        };
        $scope.inputStyle={
            "height": $scope.availubleHeight*0.1+'px',
            "width":"100%"
        };
          $scope.itemContentStyle={
            "height": $scope.availubleHeight*0.25+'px',
            "width":"100%",
            "vertical-align":"top"
        };
        $scope.itemtextareaStyle={
            "height": '100%',
            "vertical-align":"top"
        };
        function testPhone() {
            var myreg=/^1[3|4|5|8][0-9]\d{4,8}$/;
            return myreg.test($scope.phonenumber);
        }

        $scope.showAlert = function(title) {
            var alertPopup = $ionicPopup.alert({
                title: title
            });
            alertPopup.then(function(res) {

            });
            return alertPopup;
        };
        $scope.allStyle={
            "width":"100%",
            "height":"100%",
            "background-color":"#ffffff"
        };

        $scope.clickSex=function(){
            $scope.openPopup();
        };

        $scope.openPopup=function() {

            var myPopup = $ionicPopup.show({
                title: '请选择您的性别',
                scope: $scope,
                buttons: [
                    {text: '<b>女</b>',
                        onTap: function (e) {
                           $scope.sex="女";
                        }},
                    {
                        text: '<b>男</b>',
                        type: 'button-positive',
                        onTap: function (e) {
                            $scope.sex="男";
                        }
                    },
                ]
            });
            myPopup.then(function (res) {
                console.log('Tapped!', res);
            });
            $timeout(function() {
                myPopup.close(); // 3秒后关闭弹窗
            }, 3000);
        }

    })
    .controller('phoneCtrl', function ($timeout,$ionicHistory,$scope,$rootScope,$ionicHistory,$http, $ionicPopup, $state,$cacheFactory,$ionicSlideBoxDelegate,IndexFactory,$ionicScrollDelegate) {
        $scope.availHeight=screen.availHeight;
        $scope.availWidth=screen.availWidth;
        $scope.titleStyle={
            "height":$scope.availHeight*0.2+'px',
            "width": '100%',
            "text-align":"center"
        };

        $scope.msgStyle={
            "width": "100%",
            "margin-top": "30px",
            "padding-left": "4%",
            "padding-right": "5%",
            "min-height": $scope.availHeight*0.3+'px',
            "max-height": $scope.availHeight*0.5+'px'
        };

        $scope.pitemStyle={
            "width":"100%",
            "padding-left":"5%",
            "padding-right":"5%",
            "border": "none"

        };
    })
;
