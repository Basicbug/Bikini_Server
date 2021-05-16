package com.basicbug.bikini.filter;

import static com.basicbug.bikini.model.CommonConstants.REQUEST_TOKEN_HEADER_NAME;

import com.basicbug.bikini.util.JwtTokenProvider;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
public class JwtAuthorizationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthorizationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        String token = ((HttpServletRequest) servletRequest).getHeader(REQUEST_TOKEN_HEADER_NAME);

        if (jwtTokenProvider.isValidToken(token)) {

        }
    }

//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//        throws IOException, ServletException {
//
//        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if (header == null || !header.startsWith(CommonConstants.TOKEN_PREFIX)) {
//            UserPrincipal userPrincipal = new UserPrincipal(new User("", RandomString.make(10), AuthConstants.UNKNOWN_USER));
//            Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        }
//
//        chain.doFilter(request, response);
//    }
}
