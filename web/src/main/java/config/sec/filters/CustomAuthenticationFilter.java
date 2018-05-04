package config.sec.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static config.sec.SecurityConstants.EXPIRATION_TIME;
import static config.sec.SecurityConstants.HEADER_STRING;
import static config.sec.SecurityConstants.SECRET;
import static config.sec.SecurityConstants.TOKEN_PREFIX;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager manager;

    public CustomAuthenticationFilter(AuthenticationManager manager) {
        this.manager = manager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//            Client creds = new ObjectMapper()
//                    .readValue(request.getInputStream(), Client.class);
            String username = request.getParameter("username");
            String pass = request.getParameter("password");

            return manager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            pass,
                            new ArrayList<>())
            );

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {

        User client = (User) auth.getPrincipal();
        Claims claims = Jwts.claims();
        claims.put("roles", client.getAuthorities());
        String token = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .setClaims(claims)
                .setSubject(client.getUsername())
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        // it allows cors request js code getting access to this header
        response.addHeader("access-control-expose-headers", HEADER_STRING);
//        Cookie cookie = new Cookie(HEADER_STRING, token);
//        cookie.setMaxAge(36000);
//        response.addCookie(cookie);
    }
}
