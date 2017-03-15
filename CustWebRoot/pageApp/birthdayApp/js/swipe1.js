/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 17-3-13
 * Time: 上午10:43
 * To change this template use File | Settings | File Templates.
 */
var screenWidth=window.innerWidth;
var screenHeight = window.innerHeight;

$("#containerid").css("height",screenHeight+"px");
$("#containerid").css("width",screenWidth+"px");

$("#slide1").css("width",screenWidth+"px");
$("#slide1").css("height",screenHeight+"px");

$("#leftbottomimg").css("width",screenWidth*0.6+'px');
$("#rightbottomimg").css("width",screenWidth*0.6+'px');

$('#rotate').css("height",screenHeight*0.8*0.05+'px');

$('#rotatebottom').css("height",screenHeight*0.8*0.6+'px');
$('#rotatebottom').css("margin-top",screenHeight*0.8*0.1+'px');


$("#mainDiv").css("width",screenWidth*0.8+'px');
$("#mainDiv").css("margin-left",screenWidth*0.1+'px');
$("#mainDiv").css("margin-right",screenWidth*0.1+'px');
$("#mainDiv").css("height",screenHeight*0.8+'px');


$("#font6").css("width",screenWidth*0.8*0.8+'px');
$("#font6").css("margin-left",screenWidth*0.8*0.1+'px');
$("#font6").css("margin-right",screenWidth*0.8*0.1+'px');
$("#font6").css("margin-top",screenHeight*0.05+'px');

$("#topDiv").css("width",screenWidth*0.8+'px');
$("#topDiv").css("height",screenHeight*0.8*0.06+'px');
$("#topDiv").css("margin-top",screenHeight*0.15+'px');
$("#topDiv").css("margin-left",screenWidth*0.1+'px');
$("#topDiv").css("margin-right",screenWidth*0.1+'px');