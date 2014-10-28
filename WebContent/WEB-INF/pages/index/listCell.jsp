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
                
                  <td style="width:50px;">手机号</td>
                  <td style="width:50px;">申请时间</td>
                  
                </tr>
                <br>	
  			<c:if test="${not empty cellList}">
			   <c:forEach var="cell" items="${cellList}" varStatus="status"> 
                <tr>
                  <td style="width:20px;">${cell.baby}</td>
                  <td style="width:20px;"><fmt:formatDate value="${cell.cTime}"  type="both"/></td>
                </tr>
                <br>
                </c:forEach>
                </c:if>
</body>
</html>