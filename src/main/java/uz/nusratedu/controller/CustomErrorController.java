package uz.nusratedu.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Object status = request.getAttribute("jakarta.servlet.error.status_code");
        Object error = request.getAttribute("jakarta.servlet.error.exception");

        if (error != null) {
            String errorMessage = error.toString();

            // Check if this is an SSL handshake error
            if (errorMessage.contains("Invalid character found in method name") &&
                    errorMessage.contains("0x160x03")) {

                log.warn("SSL handshake attempted on non-SSL port from {}",
                        request.getRemoteAddr());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "error", "HTTPS_NOT_SUPPORTED",
                                "message", "This endpoint does not support HTTPS. Please use HTTP.",
                                "timestamp", System.currentTimeMillis(),
                                "path", request.getRequestURI()
                        ));
            }
        }

        // Default error response
        int statusCode = status != null ? (int) status : HttpStatus.INTERNAL_SERVER_ERROR.value();
        String errorMsg = HttpStatus.valueOf(statusCode).getReasonPhrase();

        return ResponseEntity.status(statusCode)
                .body(Map.of(
                        "error", errorMsg,
                        "status", statusCode,
                        "timestamp", System.currentTimeMillis(),
                        "path", request.getRequestURI()
                ));
    }
}