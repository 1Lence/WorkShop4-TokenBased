package org.example.workshop4tokenbased.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class JweFilter extends OncePerRequestFilter {
    private final JweService jweService;
    private final UserDetailServiceIml userDetailServiceIml;
    private final BannedTokenRepository bannedTokenRepository;


    public void toBlackList(HttpServletRequest req) {
        String token = getTokenFromRequest(req);

        if (token != null) {
            BannedToken bannedToken = BannedToken
                    .builder()
                    .token(token)
                    .build();

            bannedTokenRepository.save(bannedToken);
        }
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);

        if (token != null) {
            if (bannedTokenRepository.findByToken(token).isPresent()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token banned or logout");
                return;
            }

            if (jweService.validateJweToken(token)) {
                setCustomUserDetailsToSecurityContextHolder(token);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    //TODO: Выброс ошибки
    private void setCustomUserDetailsToSecurityContextHolder(String token) {
        try {
            String login = jweService.getLoginFromToken(token);
            if (login != null) {
                CustomUserDetails customUserDetails = userDetailServiceIml.loadUserByUsername(login);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        customUserDetails, null, customUserDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("Could not extract login from valid token");
            }
        } catch (Exception e) {
            log.error("Error setting user details", e);
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}