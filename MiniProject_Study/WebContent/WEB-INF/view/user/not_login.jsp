<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="root" value="${pageContext.request.contextPath }/" />
<script>
  alert('로그인하셔야 사용하실 수 있습니다');
  location.href = '${root}user/login';
</script>