var limit = 3;
var idsChecked = [];//存储选择了的id
var n = 0;//数组的下标
$(function() {
	$('.search-bar .dropdown-menu a').click(function() {
		var field = $(this).data('field') || '';
		$('#search-field').val(field);
		$('#search-btn').html($(this).text() + ' <span class="caret"></span>');
	});

	//查询用户数据
	$("#subBtn").click(function() {

	});
	
	//直接调用这个函数
	queryUsers(1, limit);
});
function importExcel(obj){
	
	var filename=document.getElementById('upload').value;
	console.log(filename);
	if(filename.endsWith('.xlsx')||filename.endsWith('.xls')){
			$.ajax({
		url: "../userAjax?method=importExcel",
		type: 'POST',
		cache: false, //不缓存
		data: new FormData($('#ExcelIp')[0]), //将整个表单的数据封装到FormData对象中
		processData: false, //传输的数据是文件
		contentType: false, //false代表文件上传
		dataType: "json",
		beforeSend: function() {
			lightyear.loading('show');
		},
		success: function(data) {
			lightyear.loading('hide');
			console.log(data);
			resultAlert(data.code, '导入成功');
			
			queryUsers(1,3);
		}
	});
	}
	else {
			resultAlert(-1, '请选择表格文件');
	}

}
function addUserForm() {
	var strs_add = '';
   	strs_add += '<form class="formName" id = "frm">';
    strs_add += '<div class="form-group">';
    strs_add += '<label>用户名</label>';
    strs_add += '<input type="text" name = "username" id = "username" placeholder="请输入用户名" class="name form-control" required />';
    
    strs_add += '<label>密码</label>';
    strs_add += '<input type="text" name = "password" id = "password" placeholder="请输入密码" class="name form-control" required />';
    
    strs_add += '<label>密码</label>';
    strs_add += '<img src="../images/MyImages/美女1号.jpg"/>';
    strs_add += '</div>';
    strs_add += '</form>';
	
	//弹出提示框
	$.confirm({
        title: '增加用户',
        content: strs_add,
        buttons: {
            formSubmit: {
                text: '提交',
                btnClass: 'btn-blue',
                action: function () {
                    //点击提交后会触发这里的函数
                    //做一些正则表达式的拦截
                    if($("#username").val() == '' || $("#password").val() == ''){
                    	$.alert('用户名或密码不能为空！');
                    	return false;
                    }
                    addUser();
                }
            },
            cancel: {
                text: '取消'
            },
        },
        onContentReady: function () {
            var jc = this;
            this.$content.find('form').on('submit', function (e) {
                e.preventDefault();
                jc.$$formSubmit.trigger('click');
            });
        }
    });
	
}

/**
 * 增加用户的Ajax
 */
function addUser(){
	console.log("addBtn....");
	//console.log($("#frm").serialize());
	$.ajax({
		type: "post",
		url: "../userAjax?method=addUser",
		data: $("#frm").serialize(), //直接获取表单里面所有的name
		beforeSend: function() {
			//发送url之前，启动转圈圈
			lightyear.loading('show');
		},
		dataType: "json",
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			console.log("页面请求错误！" + errorThrown);
		},
		success: function(data) {
			console.log(data);
			//判断
			if(data.code == 1) {
				//增加成功
				setTimeout(function() {
					lightyear.loading('hide');
					//成功界面显示1.5秒钟
					lightyear.notify('增加成功！', 'success', 1500, 'mdi mdi-checkbox-marked-circle');
					//重新调用查询数据的方法
					queryUsers(1, limit);
				}, 1500);
			} else if(data.code == -1) {
				setTimeout(function() {
					lightyear.loading('hide');
					lightyear.notify('用户名已存在，请重新输入！', 'danger', 3000, 'mdi mdi-close-circle');
				}, 1000);
			} else if(data.code == -2) {
				setTimeout(function() {
					lightyear.loading('hide');
					lightyear.notify('数据增加错误！', 'danger', 3000, 'mdi mdi-close-circle');
				}, 1000);
			}
	
		}
	});
}

/**
 * 	获取需要修改用户的信息
 * @param {User} user
 */
function updateUserForm(user){
	//从后台重新取数据
	/*$.ajax({
		type: "get",
		url: "../userAjax?method=queryUser",
		data: {
			id:id
		}, 
		beforeSend: function() {
			//发送url之前，启动转圈圈
		},
		dataType: "json",
		success: function(data) {
			console.log(data);
			//这种方式获取的数据是实时的数据
		}
	});*/
	console.log(user);
	var strs_update = '';
	strs_update += '<form class="formName" id = "frm">';
	strs_update += '<div class="form-group">';
	strs_update += '<label>用户名</label>';
	strs_update += '<input type="text" value="'+user.username+'" name = "username" id = "username" placeholder="请输入用户名" class="name form-control" required />';
	
	strs_update += '<label>密码</label>';
	strs_update += '<input type="text" value="'+user.password+'" name = "password" id = "password" placeholder="请输入密码" class="name form-control" required />';
	strs_update += '</div>';
	strs_update += '</form>';
	
	//弹出提示框
	$.confirm({
	    title: '修改用户',
	    content: strs_update,
	    buttons: {
	        formSubmit: {
	            text: '提交',
	            btnClass: 'btn-blue',
	            action: function () {
	                //点击提交后会触发这里的函数
	                //做一些正则表达式的拦截
	                if($("#username").val() == '' || $("#password").val() == ''){
	                	$.alert('用户名或密码不能为空！');
	                	return false;
	                }
	                updateUser(id);
	            }
	        },
	        cancel: {
	            text: '取消'
	        },
	    },
	    onContentReady: function () {
	        var jc = this;
	        this.$content.find('form').on('submit', function (e) {
	            e.preventDefault();
	            jc.$$formSubmit.trigger('click');
	        });
	    }
	});
}

/**
 * 	修改用户的id
 * @param {Number} id
 */
function updateUser(id){
	/*$.ajax({
		type: "post",
		url: "../userAjax?method=updateUser",
		data: $("#frm").serialize()+"&id="+id, //直接获取表单里面所有的name
		beforeSend: function() {
			//发送url之前，启动转圈圈
			lightyear.loading('show');
		},
		dataType: "json",
		success: function(data) {
			console.log(data);
			//判断
			resultAlert(data.code, '修改');
		}
	});*/
	//直接提交post请求
	$.post("../userAjax?method=updateUser", $("#frm").serialize()+"&id="+id, function(data){
		console.log(data);
		resultAlert(data.code, '修改');
	},'json');
}

/**
 * 	删除用户
 * @param {Object} id
 */
function deleteUser(id){
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
                	$.getJSON("../userAjax?method=deleteUser&id="+id,function(data){
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

function deleteAll(){
	if(idsChecked.length == 0){
		$.alert("请选择要删除的数据!!!");
		return false;
	}
	$.confirm({
        title: '警告',
        content: '确认删除选中项吗？删除后数据不可恢复！删除数据为：'+idsChecked,
        type: 'orange',
        typeAnimated: false,
        buttons: {
            omg: {
                text: '确定',
                btnClass: 'btn-orange',
                action : function(){
                	$.post("../userAjax?method=deleteAll", {ids:idsChecked},function(data){
						console.log(data);
						resultAlert(data.code, '批量删除');
					},"json");
                }
            },
            close: {
                text: '关闭',
            }
        }
    });
}



function queryUsers(pn, limit) {
	$.ajax({
		type: "get",
		url: "../userAjax",
		async: true,
		data: {
			method: "showUsers",
			pn: pn,
			limit: limit
		},
		beforeSend: function() {
			//发送url之前，启动转圈圈
		},
		dataType: "json",
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			console.log("页面请求错误！" + errorThrown);
		},
		success: function(data) {
			console.log(data);
			//开始遍历
			var strs_data = '';
			var pm = data.pm; //分页对象数据
			$(data.users).each(function(indx, user) {
				strs_data += '<tr>';
				strs_data += '<td>';
				strs_data += '<label class="lyear-checkbox checkbox-primary">';
				strs_data += '<input type="checkbox" name="ids[]" onclick="selectCheckbox(this);" value="'+user.id+'"';
				//console.log(idsChecked);
				//console.log($.inArray(user.id.toString(),idsChecked));
				//这个方法返回的是下标
				if($.inArray(user.id.toString(), idsChecked) >= 0 
					|| $("#chAll").prop("checked")){
					strs_data += ' checked ';
				}
				strs_data += '><span></span>';
				strs_data += '</label>';
				strs_data += '</td>';
				strs_data += '<td>' + user.id + '</td>';
				strs_data += '<td>' + user.username + '</td>';
				strs_data += '<td>' + user.password + '</td>';
				strs_data += '<td>';
				strs_data += '<div class="btn-group">';
				//将这个对象转化成json字符串传递过去
				var userStr = JSON.stringify(user).replace(/\"/g,"'");
				//console.log(userStr);
				strs_data += '<a class="btn btn-xs btn-default" href="javascript:updateUserForm('+userStr+');" title="编辑" data-toggle="tooltip"><i class="mdi mdi-pencil"></i></a>';
				strs_data += '<a class="btn btn-xs btn-default" href="javascript:deleteUser('+user.id+');" title="删除" data-toggle="tooltip"><i class="mdi mdi-window-close"></i></a>';
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
				strs_pages += '<li><a href = "javascript:queryUsers(' + (pm.pn - 1) + ',' + limit + ')"><span>«</span></a></li>';
			}

			//中间部分
			for(var i = 1; i <= pm.pages; i++) {
				if(pm.pn == i) {
					//代表这一页正好是这个数字
					strs_pages += '<li class="active"><span>' + i + '</span></li>';
				} else {
					strs_pages += '<li><a href="javascript:queryUsers(' + i + ',' + limit + ')">' + i + '</a></li>';
				}
			}

			//strs_pages += '<li class="disabled"><span>...</span></li>';
			//strs_pages += '<li><a href="#!">14452</a></li>';
			//strs_pages += '<li><a href="#!">14453</a></li>';
			//最后一页
			if(pm.pn >= pm.pages) {
				strs_pages += '<li class="disabled"><span>»</span></li>';
			} else {
				strs_pages += '<li><a href="javascript:queryUsers(' + (pm.pn + 1) + ',' + limit + ')">»</a></li>';
			}

			//将这个分页的html字符串写到页面中
			$(".pagination").html(strs_pages);
		}
	});
}

/**
 * 选择对应的多选框
 */
function selectCheckbox(checkboxObj){
	var chkValue = checkboxObj.value;
	console.log(chkValue);
	if(checkboxObj.checked){
		//选中，加入到这个数组
		idsChecked[n] = chkValue;
		n++;//数组前进1
	} else {
		//取消选中，从这个数组中移除
		idsChecked.splice($.inArray(chkValue, idsChecked), 1);
		n--;
	}
	console.log(idsChecked);
}

/**
 * 	全选或者取消全选
 * @param {Object} obj
 */
function checkAll(obj){
	var ids = $("input[name='ids[]']");
	if(obj.checked){
		//如果这个主选框被选中了，就全选
		$(ids).each(function(indx, chObj){
			chObj.checked = true;
		});
		//只能加当前页
		//idsChecked[indx] = chObj.value;
		//查询所有的id
		$.getJSON("../userAjax?method=getIds",function(data){
			console.log(data);
			$(data.ids).each(function(indx, id){
				idsChecked[indx] = id[0].toString();
				n++;
			});
		});
	} else {
		//清空整个数组内容
		idsChecked = [];
		n = 0;
		$(ids).each(function(indx, chObj){
			chObj.checked = false;
		});
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
