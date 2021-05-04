<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="root" value="${pageContext.request.contextPath }/" />
<script type="text/javascript">
  alert('글쓰기 완료');
  location.href = '${root}board/read?board_info_idx=${writeContentDTO.content_board_idx}&content_idx=${writeContentDTO.content_idx}';
</script>    