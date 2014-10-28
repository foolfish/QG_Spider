<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:include page="/common/domain.jsp" flush="true"/> 
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<body>

 				<tr>
                  <td style="width:20px;">id</td>
                  <td style="width:20px;">email</td>
                  <td style="width:20px;">
                  	类型
                  </td>
                  <td style="width:20px;">申请时间</td>
                   <td style="width:20px;">真实姓名</td>
                   <td style="width:20px;">职业</td>
                   <td style="width:20px;">联系方式</td>
                </tr>
                <br>	
  			<c:if test="${not empty applyEmailList}">
			   <c:forEach var="applyEmail" items="${applyEmailList}" varStatus="status"> 
                <tr>
                  <td style="width:20px;">${applyEmail.id}</td>
                  <td style="width:20px;">${applyEmail.email}</td>
                  <td style="width:20px;">
                  	<c:set var="ctype" value="${applyEmail.ctype}"/>
                  	<c:choose><c:when test="${ctype eq 1 }">客户</c:when><c:otherwise>小微</c:otherwise></c:choose>
                  </td>
                  <td style="width:20px;"><fmt:formatDate value="${applyEmail.applyTime}"  type="both"/></td>
                   <td style="width:20px;">${applyEmail.realname}</td>
                   <td style="width:20px;">${applyEmail.job}</td>
                   <td style="width:20px;">${applyEmail.teleno}</td>
                </tr>
                <br>
                </c:forEach>
                </c:if>
</body>
</html>