function addPic(file) {

	/*//验证图片类型
	verPicType(file);
	//验证图片大小
	verPicLength(file);
	//验证图片尺寸
	verPicSize(file);*/

	//console.log(verPicSize(file));

	//验证图片类型和图片大小
	if(!(verPicType(file) && verPicLength(file))) {
		return false;
	}

	//获取这个文件，判断上传的文件是否存在
	if(file.files && file.files[0]) {
		//读取文件的对象-->java中叫FileInputStream、FileReader
		var reader = new FileReader();
		//加载这个文件
		reader.onload = function(evt) {

			var image = new Image();
			image.onload = function() {
				console.log(image.width + "," + image.height);
				if(image.width != 724 && image.height != 464) {
					$.alert("图片像素应为：宽724*高464！");
					file.value = "";
					return false;
				} else {
					//隐藏父节点
					//找到这个增加图片的父节点li，然后把他隐藏
					console.log($(file).parents("li"));
					//css给这个标签增加一个样式
					//这个input不可以移除，如果移除，就传递不到后台了。
					//后台接收的是input元素的name
					$(file).parents("li").css("display", "none");
		
					//拼接放图片的节点
					var strs_imgs = '';
					strs_imgs += '<li class="col-xs-4 col-sm-3 col-md-2">';
					strs_imgs += '<figure>';
					//base64编码(字节码)
					//console.log(evt.target.result);
					strs_imgs += '<img src="' + evt.target.result + '" alt="图片一">';
					strs_imgs += '<figcaption>';
					strs_imgs += '<a class="btn btn-round btn-square btn-primary" href="javascript:void(0);" onclick="showImage(this);"><i class="mdi mdi-eye"></i></a>';
					strs_imgs += '<a class="btn btn-round btn-square btn-danger" href="javascript:void(0);" onclick="removeImage(this);"><i class="mdi mdi-delete"></i></a>';
					strs_imgs += '</figcaption>';
					strs_imgs += '</figure>';
					strs_imgs += '</li>';
		
					//增加添加图片的按钮
					strs_imgs += '<li class="col-xs-4 col-sm-3 col-md-2" >';
					strs_imgs += '<a class="pic-add" id="add-pic-btn" href="" title="点击上传">';
					strs_imgs += '<input type="file" class="pic-add-file" onchange="addPic(this)" id="uploadfiles" name="uploadfiles" multiple="multiple"/>';
					strs_imgs += '</a>';
					strs_imgs += '</li>';
		
					$(".lyear-uploads-pic").append(strs_imgs);
				}
			};
			image.src = evt.target.result;
			
		}
		reader.readAsDataURL(file.files[0]);
	} else {
		//打印这个文件的路径
		console.log(file.value);
	}
}

$(".ajax-post").click(function() {
	$.ajax({
		url: "../product?method=addProduct",
		type: 'POST',
		cache: false, //不缓存
		data: new FormData($('#filesForm')[0]), //将整个表单的数据封装到FormData对象中
		processData: false, //传输的数据是文件
		contentType: false, //false代表文件上传
		dataType: "json",
		beforeSend: function() {
			lightyear.loading('show');
		},
		success: function(data) {
			lightyear.loading('hide');
			console.log(data);
			resultAlert(data.code, "增加");
			if(data.code == 1){
				window.location.href = "showProduct.html";
			}
		}
	});
});

/**
 * 点击删除，删掉那张图片
 * @param {HTMLObjectElement} delBtn 删除的按钮
 */
function removeImage(delBtn) {
	//百度只能百度功能，【一定要有思路！！！！】

	//获取自己的li，而不会获取别的li
	//console.log($(delBtn).parents("li"));
	//移除掉上一个li节点
	$(delBtn).parents("li").prev().remove();
	//移除下一个li节点
	$(delBtn).parents("li").remove();
}

function showImage(showBtn) {
	//console.log($(showBtn).parents("figcaption").prev());
	//找到点击预览的这张图片对象
	var img = $(showBtn).parents("figcaption").prev();
	imgShow("#outerdiv", "#innerdiv", "#bigimg", img);
}

/**
 * 预览图片
 * @param {Object} outerdiv
 * @param {Object} innerdiv
 * @param {Object} bigimg
 * @param {Object} _this
 */
function imgShow(outerdiv, innerdiv, bigimg, _this) {
	var src = _this.attr("src"); //获取当前点击的pimg元素中的src属性  
	$(bigimg).attr("src", src); //设置#bigimg元素的src属性  

	/*获取当前点击图片的真实大小，并显示弹出层及大图*/
	$("<img/>").attr("src", src).load(function() {
		var windowW = $(window).width(); //获取当前窗口宽度  
		var windowH = $(window).height(); //获取当前窗口高度  
		var realWidth = this.width; //获取图片真实宽度  
		var realHeight = this.height; //获取图片真实高度  
		var imgWidth, imgHeight;
		var scale = 0.8; //缩放尺寸，当图片真实宽度和高度大于窗口宽度和高度时进行缩放  

		if(realHeight > windowH * scale) { //判断图片高度  
			imgHeight = windowH * scale; //如大于窗口高度，图片高度进行缩放  
			imgWidth = imgHeight / realHeight * realWidth; //等比例缩放宽度  
			if(imgWidth > windowW * scale) { //如宽度扔大于窗口宽度  
				imgWidth = windowW * scale; //再对宽度进行缩放  
			}
		} else if(realWidth > windowW * scale) { //如图片高度合适，判断图片宽度  
			imgWidth = windowW * scale; //如大于窗口宽度，图片宽度进行缩放  
			imgHeight = imgWidth / realWidth * realHeight; //等比例缩放高度  
		} else { //如果图片真实高度和宽度都符合要求，高宽不变  
			imgWidth = realWidth;
			imgHeight = realHeight;
		}
		$(bigimg).css("width", imgWidth); //以最终的宽度对图片缩放  

		var w = (windowW - imgWidth) / 2; //计算图片与窗口左边距  
		var h = (windowH - imgHeight) / 2; //计算图片与窗口上边距  
		$(innerdiv).css({
			"top": h,
			"left": w
		}); //设置#innerdiv的top和left属性  
		$(outerdiv).fadeIn("fast"); //淡入显示#outerdiv及.pimg  
	});

	$(outerdiv).click(function() { //再次点击淡出消失弹出层  
		$(this).fadeOut("fast");
	});
}

//图片类型验证
function verPicType(file) {
	var fileTypes = [".jpg", ".png", ".bmp"];
	var filePath = file.value;
	//当括号里面的值为0、空字符、false 、null 、undefined的时候就相当于false
	console.log("filePath1=" + filePath);
	if(filePath) {
		var isNext = false;
		var fileEnd = filePath.substring(filePath.indexOf("."));
		for(var i = 0; i < fileTypes.length; i++) {
			if(fileTypes[i] == fileEnd) {
				isNext = true;
				break;
			}
		}
		if(!isNext) {
			$.alert('不接受此文件类型，只支持['+fileTypes+"]类型的文件");
			file.value = "";
			return false;
		}
		return true;
	} else {
		return false;
	}
}

//图片大小验证
function verPicLength(file) {
	var fileSize = 0;
	var fileMaxSize = 1024; //1M
	var filePath = file.value;
	console.log("filePath2=" + filePath);
	if(filePath) {
		fileSize = file.files[0].size;
		console.log("fileSize2=" + fileSize);
		var size = fileSize / 1024;
		if(size > fileMaxSize) {
			$.alert("文件大小不能大于1M！");
			file.value = "";
			return false;
		} else if(size <= 0) {
			$.alert("文件大小不能为0M！");
			file.value = "";
			return false;
		}
		return true;
	} else {
		return false;
	}
}

//图片尺寸验证
function verPicSize(file) {
	var filePath = file.value;
	if(filePath) {
		//读取图片数据
		var filePic = file.files[0];
		var reader = new FileReader();
		reader.onload = function(e) {
			var data = e.target.result;
			//加载图片获取图片真实宽度和高度
			var image = new Image();
			image.onload = function() {
				var width = image.width;
				var height = image.height;
				console.log("width=" + width + ",height=" + height);
				if(width != 500 && height != 312) {
					$.alert("文件尺寸应为：720*1280！");
					file.value = "";
				}
			};
			image.src = data;
		};
		reader.readAsDataURL(filePic);
	} else {
		return false;
	}
}


/**
 * 	
 * @param {Object} code 标识符
 * @param {Object} type 内容
 */
function resultAlert(code, type, specify){
	lightyear.loading('show');
	if(code == 1) {
		setTimeout(function() {
			lightyear.loading('hide');
			lightyear.notify(type+'成功！', 'success', 1000, 'mdi mdi-checkbox-marked-circle');
		}, 1000);
	} else if(code == -1) {
		setTimeout(function() {
			lightyear.loading('hide');
			lightyear.notify(type+'失败！', 'danger', 3000, 'mdi mdi-close-circle');
		}, 1000);
	} else if(code == -2){
		setTimeout(function() {
			lightyear.loading('hide');
			lightyear.notify(specify, 'danger', 3000, 'mdi mdi-close-circle');
		}, 1000);
	}
}


