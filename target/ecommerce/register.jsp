<%@ include file="includes/header.jsp" %>
<div class="row justify-content-center">
    <div class="col-md-4">
        <h2>Register</h2>
        <% if(request.getAttribute("error") != null) { %>
            <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
        <% } %>
        <form action="register" method="post">
            <div class="mb-3">
                <label class="form-label">Username</label>
                <input type="text" name="username" class="form-control" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Password</label>
                <input type="password" name="password" class="form-control" required>
            </div>
            <button type="submit" class="btn btn-success w-100">Register</button>
        </form>
        <p class="mt-2">Already have an account? <a href="login">Login</a></p>
    </div>
</div>
<%@ include file="includes/footer.jsp" %>