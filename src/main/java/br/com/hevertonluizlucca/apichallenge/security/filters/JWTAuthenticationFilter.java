package br.com.hevertonluizlucca.apichallenge.security.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;

import br.com.hevertonluizlucca.apichallenge.security.utils.JwtTokenUtil;
import io.jsonwebtoken.Jwts;

public class JWTAuthenticationFilter extends GenericFilterBean {
	
	private static final String AUTH_HEADER = "Authorization";
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain
        ) throws IOException, ServletException {

        Authentication authentication = null;
        
        String token = ((HttpServletRequest) request).getHeader(AUTH_HEADER);
        if (token != null && Jwts.parser().isSigned(token)) {
        	UserDetails userDetails = userDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));
        	authentication  = userDetails != null ? new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()) : null;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }


}
