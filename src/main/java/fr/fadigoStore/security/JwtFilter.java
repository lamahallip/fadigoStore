package fr.fadigoStore.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain) throws ServletException, IOException {

          if(request.getContextPath().contains("api/register")) {
              filterChain.doFilter(request, response);
              return;
          }

          String authHeader =  request.getHeader(HttpHeaders.AUTHORIZATION);

          if(!authHeader.startsWith("Barear ")) {
              filterChain.doFilter(request, response);
              return;
          }

          String jwt = authHeader.substring(7);
          String username = jwtService.extractUsername(jwt);

          if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

              UserDetails userDetails = userDetailsService.loadUserByUsername(username);

              if(jwtService.isTokenValid(jwt, userDetails)) {

                  UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                          userDetails, null, userDetails.getAuthorities()
                  );

                  authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                  SecurityContextHolder.getContext().setAuthentication(authToken);
              }
          }

          filterChain.doFilter(request, response);
    }
}
