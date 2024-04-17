package finchainstorage.businesslayer.security;

import finchainstorage.businesslayer.dto.EmployeeDTO;
import finchainstorage.businesslayer.dto.EmployeeDTODetails;
import finchainstorage.businesslayer.inmemory.SessionRegistry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SessionFilter extends OncePerRequestFilter {

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private EmployeeDTODetails employeeDTODetails;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        final String authzHeader = request.getHeader("Authorization");

        //Check is the request contains a session id
        if (authzHeader == null || !authzHeader.startsWith("Bearer ")) {
            //responseError(response);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            filterChain.doFilter(request, response);
            return;
        }
        final String sessionId = authzHeader.substring(7);

        final String employeeName = sessionRegistry.getEmployeeNameBySessionId(sessionId);

        System.out.println("Employee Name from session: " + employeeName);

        if (employeeName != null && SecurityContextHolder.getContext().getAuthentication() == null ) {

            EmployeeDTO employee = employeeDTODetails.loadUserByUsername(employeeName); //Hardcoded for now until the database is implemented
            if (sessionRegistry.checkSession(sessionId)) {
                authenticate(employee, request);
            }
        }

        filterChain.doFilter(request, response);

    }
    private void authenticate(EmployeeDTO employee, HttpServletRequest request) {

        UsernamePasswordAuthenticationToken authentication = new
                UsernamePasswordAuthenticationToken(employee, null, employee.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    /*private void responseError(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Unauthorized - Session ID missing or invalid.\"}");
        response.getWriter().flush();
    }*/
}
