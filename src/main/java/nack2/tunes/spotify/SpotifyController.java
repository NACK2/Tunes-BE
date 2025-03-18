package nack2.tunes.spotify;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping(path = "spotify")
public class SpotifyController {
    private final SpotifyService spotifyService;

    @Autowired
    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    // OAuth flow: https://developer-assets.spotifycdn.com/images/documentation/web-api/auth-code-flow.png

    @GetMapping(path="auth")
    public ResponseEntity<Map<String, String>> getAuthUrl() {
        Map<String, String> result = new HashMap<>();
        result.put("path", "/api/v1/spotify/auth");
        String authUrl = spotifyService.getAuthUrl();
        result.put("authUrl", authUrl);
        result.put("status", HttpStatus.OK.toString());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // GET /callback shouldn't be called by client, client will be redirected
    // here automatically after granting authorization at GET /auth
    @GetMapping(path="callback")
    public HttpEntity<Map<String, String>> getAuthCode(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        result.put("path", "/api/v1/spotify/callback");

        try {
            String authCode = spotifyService.getAuthCode(request);
            result.put("authCode", authCode);
            result.put("status", HttpStatus.OK.toString());
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch(Exception e) {
            result.put("error", e.getMessage());
            result.put("status", HttpStatus.BAD_REQUEST.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }


    @PostMapping(path="token")
    public ResponseEntity<Map<String, String>> getAccessToken(@RequestBody Map<String, String> authCode) {
        Map<String, String> result = new HashMap<>();
        result.put("path", "/api/v1/spotify/token");

        if (!authCode.containsKey("authCode")) {
            result.put("error", "Missing authCode in body");
            result.put("status", HttpStatus.BAD_REQUEST.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        try {
            String accessToken = spotifyService.getAccessToken(authCode);
            result.put("accessToken", accessToken);
            result.put("status", HttpStatus.OK.toString());
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            result.put("error", e.getMessage());
            result.put("status", HttpStatus.BAD_REQUEST.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }
}
