<%@ include file="includes/header.jsp" %>
<div class="row justify-content-center">
    <div class="col-md-4">
        <h2>Login</h2>
        <% if(request.getParameter("registered") != null) { %>
            <div class="alert alert-success">Registration successful! Please login.</div>
        <% } %>
        <% if(request.getAttribute("error") != null) { %>
            <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
        <% } %>
        <form action="login" method="post">
            <div class="mb-3">
                <label class="form-label">Username</label>
                <input type="text" name="username" class="form-control" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Password</label>
                <input type="password" name="password" class="form-control" required>
            </div>
            <button type="submit" class="btn btn-primary w-100">Login</button>
        </form>
        <p class="mt-2">Don't have an account? <a href="register">Register</a></p>
    </div>
</div>
<%@ include file="includes/footer.jsp" %>