<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="zh">
<head>
<meta charset="utf-8">
  <body>
  	<!-- 
  		文件上传：
  		1.提交方式只能是post
  		2.enctype：
  		上传文件：multipart/form-data
  		普通文字：application/x-www-form-urlencoded
  	-->
    <form action="upload?method=fileUpload" enctype="multipart/form-data" method="post">
        <table>
            <tr>
                <th>用户名：</th>
                <td>
                    <input type="text" name="username"/><br>
                    <input type="radio" name="sex" value="男" checked="checked"/>男
                    <input type="radio" name="sex" value="女"/>女<br>
                    <input type="checkbox" name="ins" value="足球"/>足球
                    <input type="checkbox" name="ins" value="篮球"/>篮球
                    <input type="checkbox" name="ins" value="rap"/>rap
                </td>
            </tr>
            <tr>
                <th>上传文件1：</th>
                <td>
                    <input type="file" name="file1"/>
                </td>
            </tr>
            <tr>
                <th>上传文件2：</th>
                <td>
                    <input type="file" name="file2"/>
                </td>
            </tr>
            <tr>
                <th>上传文件3多选：</th>
                <td>
                    <input type="file" name="file3" multiple="multiple"/>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="submit" value="上传" />
                </td>
            </tr>
        </table>
    </form>
  </body>
</html>
