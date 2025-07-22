package org.example.workshop4tokenbased.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.workshop4tokenbased.dto.CustomUserDetails;
import org.example.workshop4tokenbased.entity.BannedToken;
import org.example.workshop4tokenbased.repository.BannedTokenRepository;
import org.example.workshop4tokenbased.security.UserDetailServiceIml;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailServiceIml userDetailServiceIml;
    private final BannedTokenRepository bannedTokenRepository;


    public void toBlackList(HttpServletRequest req) {
        String token = getTokenFromRequest(req);
        BannedToken bannedToken = BannedToken.builder().token(token).build();

        bannedTokenRepository.save(bannedToken);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);


        if (bannedTokenRepository.findByToken(token).isPresent()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token banned or logout");
            return;
        }

        if(token != null && jwtService.validateJwtToken(token)) {
            setCustomUserDetailsToSecurityContextHolder(token);
        }

        filterChain.doFilter(request, response);
    }

    private void setCustomUserDetailsToSecurityContextHolder(String token) {
        String login = jwtService.getLoginFromToken(token);
        CustomUserDetails customUserDetails = userDetailServiceIml.loadUserByUsername(login);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetails,
                null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}