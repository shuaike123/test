var limit = 3;

$(function(){
	queryProducts(1, limit);
})


/**
 * 显示所有商品数据
 */
function queryProducts(pn, limit){
	$.ajax({
		type: "get",
		url: "../product",
		async: true,
		data: {
			method: "showProducts",
			pn: pn,
			limit: limit
		},
		dataType: "json",
		success: function(data) {
			console.log(data);
			//开始遍历
			var strs_data = '';
			var pm = data.pm; //分页对象数据
			$(data.products).each(function(indx, product) {
				strs_data += '<tr>';
				strs_data += '<td>';
				strs_data += '<label class="lyear-checkbox checkbox-primary">';
				strs_data += '<input type="checkbox" name="ids[]" onclick="selectCheckbox(this);" value="'+product.id+'"';
				strs_data += '><span></span>';
				strs_data += '</label>';
				strs_data += '</td>';
				strs_data += '<td style="vertical-align:middle;text-align: center;" >' + product.id + '</td>';
				strs_data += '<td style="vertical-align:middle;text-align: center;">' + product.pro_name + '</td>';
				strs_data += '<td style="vertical-align:middle;text-align: center;">' + product.pro_price + '</td>';
				strs_data += '<td style="vertical-align:middle;text-align: center;">' + product.pro_stock + '</td>';
				var path = product.pics[0].pic_address;
				//为了以防万一
				path = path.replace(/\\/g,"/");
				strs_data += '<td style="vertical-align:middle;text-align: center;" width="240px"><img onclick="showImage(this)" src="../product?method=showImage&path='+path+'" height="100px"/></td>';
				strs_data += '<td style="vertical-align:middle;text-align: center;">';
				strs_data += '<div class="btn-group">';
				//将这个对象转化成json字符串传递过去
				var productStr = JSON.stringify(product).replace(/\"/g,"'");
				strs_data += '<a class="btn btn-xs btn-default" href="javascript:updateUserForm('+productStr+');" title="编辑" data-toggle="tooltip"><i class="mdi mdi-pencil"></i></a>';
				strs_data += '<a class="btn btn-xs btn-default" href="javascript:deleteProduct('+product.id+');" title="删除" data-toggle="tooltip"><i class="mdi mdi-window-close"></i></a>';
				strs_data += '</div>';
				strs_data += '</td>';
				strs_data += '</tr>';
			});
			//将整个html代码加载到页面中
			$(".table-bordered tbody").html(strs_data);

			//加载分页
			var strs_pages = '';

			//上一页
			if(pm.pn <= 1) {
				strs_pages += '<li class="disabled"><span>«</span></li>';
			} else {
				strs_pages += '<li><a href = "javascript:queryProducts(' + (pm.pn - 1) + ',' + limit + ')"><span>«</span></a></li>';
			}

			//中间部分
			for(var i = 1; i <= pm.pages; i++) {
				if(pm.pn == i) {
					//代表这一页正好是这个数字
					strs_pages += '<li class="active"><span>' + i + '</span></li>';
				} else {
					strs_pages += '<li><a href="javascript:queryProducts(' + i + ',' + limit + ')">' + i + '</a></li>';
				}
			}

			//strs_pages += '<li class="disabled"><span>...</span></li>';
			//strs_pages += '<li><a href="#!">14452</a></li>';
			//strs_pages += '<li><a href="#!">14453</a></li>';
			//最后一页
			if(pm.pn >= pm.pages) {
				strs_pages += '<li class="disabled"><span>»</span></li>';
			} else {
				strs_pages += '<li><a href="javascript:queryProducts(' + (pm.pn + 1) + ',' + limit + ')">»</a></li>';
			}

			//将这个分页的html字符串写到页面中
			$(".pagination").html(strs_pages);
		}
	});
}


function showImage(img) {
	imgShow("#outerdiv", "#innerdiv", "#bigimg", $(img));
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
function addProduct(){
	window.location.href="addProduct.html"
}
function deleteProduct(id){
	$.confirm({
        title: '警告',
        content: '确认删除吗？删除后数据不可恢复！',
        type: 'orange',
        typeAnimated: false,
        buttons: {
            omg: {
                text: '确定',
                btnClass: 'btn-orange',
                action : function(){
                	$.getJSON("../product?method=deleteProduct&id="+id,function(data){
						console.log(data);
						//判断
						resultAlert(data.code, '删除');
					});
                }
            },
            close: {
                text: '关闭',
            }
        }
    });
	
}
function resultAlert(code, type, specify){
	lightyear.loading('show');
	if(code == 1) {
		setTimeout(function() {
			lightyear.loading('hide');
			lightyear.notify(type+'成功！', 'success', 1500, 'mdi mdi-checkbox-marked-circle');
			queryUsers(1, limit);
		}, 1500);
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
function updateProduct(product){
	//window.location.href="updatepage.html?id="
	
}
