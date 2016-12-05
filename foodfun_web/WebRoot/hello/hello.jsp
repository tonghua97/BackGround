<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>jsp</title>
	<!--jquery文件-->
        <script src="js/jquery.min.js" type="text/javascript"></script>
        <!--jcrop文件-->
        <script src="js/jquery.Jcrop.js" type="text/javascript"></script>
        <link rel="stylesheet" href="css/jquery.Jcrop.css" type="text/css" />
        <!--异步上传文件-->
		<script src="js/ajaxfileupload.js" type="text/javascript"></script>
</head>
<body>
	<form>
		<input type="file" name="img_poster" id="img_poster" onchange="ajaxFileUpload()"/>
		
		<div class="cutDiv">
			<p id="cutInfo"></p>
			<img id="img1" alt="" src="" />
			<input type="hidden" id="x1" name="x1" value="0" />
			<input type="hidden" id="y1" name="y1" value="0" />
			<input type="hidden" id="x2" name="x2" value="0" />
			<input type="hidden" id="y2" name="y2" value="0" />
			<input type="hidden" id="cw" name="cw" value="0" />
			<input type="hidden" id="ch" name="ch" value="0" />
			<input type="hidden" id="imgName" name="imgName" value="" />
			<input type="hidden" id="imgType" name="imgType" value="" />
			<input id="cutimg" type="button" value="裁剪上传" />
			<input id="cancelimg" type="button" value="取消" />
		</div>
		
		
		<script type="text/javascript">
			var _jcropApi, _boundx, _boundy, _ratio=1;//定义比例、长宽等
			$(document).ready(function() {   
				//取消按钮事件   
				$("#cancelimg").click(function() {
					$("#img_hotLine").attr('value','');
					$(".img-group").show();
					$("#cutDiv").hide();

					if (_jcropApi) {
						_jcropApi.destroy();
					}
				$("#img1").hide();
			  });
				//裁剪上传按钮事件
				$("#cutimg").click(function() {
					ajaxFileCut();
				});        
			 
				
			});
			//当裁剪框变动时，将左上角相对图片的X坐标与Y坐标，宽度以及高度放到<input type="hidden">中    (上传到服务器上裁剪会用到)
			function showCoords(c) {
					$('#x1').val(c.x * _ratio);
					$('#y1').val(c.y * _ratio);
					$('#x2').val(c.x2 * _ratio);
					$('#y2').val(c.y2 * _ratio);
					$('#cw').val(c.w * _ratio);
					$('#ch').val(c.h * _ratio);
					$("#cutInfo").text("已选择宽*高：" + (parseInt($('#x2').val()) - parseInt($('#x1').val())) + "*" + (parseInt($('#y2').val()) - parseInt($('#y1').val())));
				}
			//ajax文件上传
			function ajaxFileUpload() {
				$.ajaxFileUpload({
						url: '/hello/upload', //用于文件上传的服务器端请求地址
						secureuri: false, //一般设置为false
						fileElementId: 'img_poster', //文件上传空间的id属性
						dataType: 'json', //返回值类型 一般设置为json
						success: function (data, status){  //服务器成功响应处理函数
							var result = eval('(' + data + ')' );
							$("#cutDiv").show();//显示裁剪框						
							$("#img1").attr("src", result.fileUrl).show(); //显示图片
							$("#imgName").val(result.fileName);
							//同时启动裁剪操作，触发裁剪框显示，让用户选择图片区域
							$('#img1').Jcrop({
								aspectRatio: 2 / 1,  //设置比例
								bgOpacity: .4,     
								fadeTime: 10,
								boxWidth: 500,
								boxHeight: 500,
								onChange: showCoords,   //当裁剪框变动时执行的函数
								onSelect: showCoords    //当选择完成时执行的函数);
							}); 
						},error: function (data, status, e)//服务器响应失败处理函数
						{
							alert("系统错误：请检查图片尺寸及大小是否符合要求");
						}
					});	
					return false;
					
				}
			//AJAX上传图片裁剪
			function ajaxFileCut() {
				var args ='imgName=' + $("#imgName").val() 
									+ '&x1='+parseInt($("#x1").val()) 
									+ "&y1="+parseInt($("#y1").val())
									+ "&cw="+parseInt($("#cw").val())
									+ "&ch="+parseInt($("#ch").val());
			   $.ajax({
				  type: "post",
				  url: "/hello/ajaxImageCut?"+args,
				  datatype: "json",
				  success: function (data) {
					 alert(data.msg);
				  },
				  error: function (data, status, e)//服务器响应失败处理函数
				  {
					 alert(e);
				  }
			   });
			   return false;
			}
		</script>
</body>
</html>