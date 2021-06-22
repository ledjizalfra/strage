package it.buniva.strage.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.constant.SecurityConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;


@Component
public class JwtAuthenticationEntryPoint extends Http403ForbiddenEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException {

        ApiResponseCustom apiResponseCustom = new ApiResponseCustom(
                Instant.now(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN,
                StringUtils.EMPTY,
                SecurityConstant.FORBIDDEN_MESSAGE,
                StringUtils.EMPTY
        );


        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        // Map the ApiResponseCustom into a response so that we can see it
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, apiResponseCustom);
        outputStream.flush();
    }
}
