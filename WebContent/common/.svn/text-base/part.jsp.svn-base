<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
		<div class="base-rightbar clearfix">
					<!-- <div class="base-rightbar-left">
						<div class="base-rightbar-pillar" style="height: 200px;">
							<div class="base-rightbar-quota" style="background-position: -37px 0;">
								<span>85%</span>
							</div>
						</div>
						<span class="base-rightbar-title">进度条</span>
					</div> -->
					<div class="base-rightbar-right">
						<div class="base-rightbar-pillar" style="height: ${part*2}px;">
						<c:choose>
						<c:when test="${part gt 50}">
							<div class="base-rightbar-quota" style="background-position: -37px 0;">
						</c:when><c:otherwise>
							<div class="base-rightbar-quota" style="background-position: -82px 0;">
						</c:otherwise>
						</c:choose>
						<c:if test="${part gt 50 }"></c:if>
						
							
								<span>${part}%</span>
							</div>
						</div>
						<span class="base-rightbar-title">信息完成度</span>
					</div>
</html>