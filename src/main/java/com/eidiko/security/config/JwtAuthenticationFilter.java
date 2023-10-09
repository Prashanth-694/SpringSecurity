package com.eidiko.security.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.eidiko.security.util.JwtHelper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtHelper jwtHelper;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("inside doFilterInternal()");
		String userName="";
		String token="";
		String requestHeader=request.getHeader("Authorization");
		log.info("Requestheader :{} "+requestHeader);
		
		if(requestHeader!=null && requestHeader.startsWith("Bearer")) {
			
			//fetch required details from token
			token=requestHeader.substring(7);
			
			try {

                userName = this.jwtHelper.getUsernameFromToken(token);

            } catch (IllegalArgumentException e) {
                logger.info("Illegal Argument while fetching the username !!");
                e.printStackTrace();
            } catch (ExpiredJwtException e) {
                logger.info("Given jwt token is expired !!");
                e.printStackTrace();
            } catch (MalformedJwtException e) {
                logger.info("Some changed has done in token !! Invalid Token");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();

            }
			log.info("User name : "+userName);
			//checking token has proper username and already any user is authenticated in security context
			if(!userName.isEmpty() && userName!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
				
				//fetch user details from given username
				
				UserDetails userDetails=userDetailsService.loadUserByUsername(userName);
				boolean validateToken=jwtHelper.validateToken(token, userDetails);
				
				if(validateToken) {
					UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				log.info("User Authorities : "+SecurityContextHolder.getContext().getAuthentication().getAuthorities());
				}else {
					log.info("Validations failed");
				}
			}else {
				log.info("Invalid User^");
			 }
			} else {
            logger.info("Invalid Header Value !! ");
        }
		filterChain.doFilter(request, response);
	}

}
