<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
    <form action="/auth/loginProc" method="post">
        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" name="username" class="form-control" placeholder="Enter Username" id="username">
        </div>
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" name="password" class="form-control" placeholder="Enter Password" id="password">
        </div>
        <button id="btn_login" class="btn btn-primary">Login</button>
        <a href="https://kauth.kakao.com/oauth/authorize?client_id=f6c79072a0eed466ef0cead34c306912&redirect_uri=http://localhost:8000/auth/kakao/callback&response_type=code"><img src="/image/kakao_login_btn.png" height="40px"/></a>
    </form>
</div>



<%@ include file="../layout/footer.jsp"%>