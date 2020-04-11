$(function() {
	//点击立即登陆，触发点击事件
	$("#subBtn").click(function() {
		//使用正则表达式拦截参数是否填写
		if($("#username").val() == '' || $("#password").val() == '') {
			lightyear.notify('用户名或者密码不能为空！', 'danger', 3000, 'mdi mdi-close-circle');
			return false;
		}

		$.ajax({
			type: "post",
			url: "../userAjax",
			async: true,
			data: {
				method: "login",
				username: $("#username").val(),
				password: $("#password").val()
			},
			beforeSend: function() {
				//发送url之前，启动转圈圈的效果
				lightyear.loading('show');
			},
			dataType: "json",
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				console.log("页面请求错误！" + errorThrown);
			},
			success: function(data) {
				console.log(data);
				if(data.code == 1) {
					//1.5秒之后隐藏这个圈圈界面
					setTimeout(function() {
						lightyear.loading('hide');
						//成功界面显示1.5秒钟
						lightyear.notify('登陆成功，页面即将自动跳转！', 'success', 1500, 'mdi mdi-checkbox-marked-circle');
						//设置登陆和注册按钮不可再点击，避免显示提示信息时用户胡乱按键
						$("#subBtn").prop("disabled", "disabled");
						//直接跳转到首页。让弹窗显示1.5秒之后再调用页面
						setTimeout('window.location.href = "lyear_pages_doc.html";', 1500);
					}, 1500);
				} else {
					setTimeout(function() {
						lightyear.loading('hide');
						lightyear.notify('登陆失败！用户名或密码错误！', 'danger', 3000, 'mdi mdi-close-circle');
						//刷新这个登陆界面
						//setTimeout('window.location.reload();', 2000);
						//不刷新页面，清空用户名和密码更快
						$("#username").val('');
						$("#password").val('');
					}, 1500);

				}
			}
		});
	});
});