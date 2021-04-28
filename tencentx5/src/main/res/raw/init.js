var objs = document.getElementsByTagName("img");
var timeOutEvent=0;
var imageUrl="";
 console.log("enterjs");
for(var i=0; i<objs.length; i++) {
  	var img=objs[i];
  	//检测类型是不是我们定义的mobile,jsInterface为变量名
  	 if(typeof jsInterface!="undefined"){
  	jsInterface.addImageUrl(img.src);//收集网页中图片的url
  	}
  	 //清除之前设置的，防止重复设置
  	 img.removeEventListener('touchstart',touchstart)
     img.removeEventListener('touchmove',touchmove)
     img.removeEventListener('touchend',touchend)

     //注册事件
    img.addEventListener('touchstart',touchstart)
	img.addEventListener('touchmove',touchmove)
    img.addEventListener('touchend',touchend)

}
function touchstart(e){
       console.log("touchstart");
      timeOutEvent = setTimeout("longPress()",1000);
     imageUrl=this.src;
}

function touchmove(e){
       console.log("touchmove");
    clearTimeout(timeOutEvent);
     timeOutEvent = 0;
}

function touchend(e){
       console.log("touchend");
   clearTimeout(timeOutEvent);
          if(timeOutEvent!=0){
               //展示图片
                if(typeof jsInterface!="undefined"){
              jsInterface.showImage(imageUrl);
               }
           }
            return false;
}

function longPress(){
    timeOutEvent = 0;
   if(typeof jsInterface!="undefined"){
       jsInterface.scanCode(imageUrl);
    }
}


