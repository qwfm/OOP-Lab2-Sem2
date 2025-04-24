package org.example.hotelbooking.filters;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwkProvider provider;
    private final String issuer;
    private final String audience;

    public JwtFilter(
            @Value("${auth0.domain}") @NonNull String domain,
            @Value("${auth0.audience}") @NonNull String audience
    ) throws Exception {
        this.issuer = "https://" + domain + "/";
        this.audience = audience;
        URL jwksUrl = URI.create(this.issuer + ".well-known/jwks.json").toURL();
        this.provider = new JwkProviderBuilder(jwksUrl)
                .cached(10, 24, TimeUnit.HOURS)
                .build();
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            try {
                String token = header.substring(7);
                var decoded = JWT.decode(token);
                Jwk jwk = provider.get(decoded.getKeyId());
                RSAPublicKey publicKey = (RSAPublicKey) jwk.getPublicKey();
                Algorithm algorithm = Algorithm.RSA256(publicKey, null);

                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer(issuer)
                        .withAudience(audience)
                        .build();

                var jwt = verifier.verify(token);
                String email = jwt.getClaim("email").asString();

                var auth = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        Collections.emptyList()
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}
