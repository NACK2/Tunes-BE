package nack2.tunes.spotify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "spotify")
public class SpotifyController {
    private final SpotifyService spotifyService;

    @Autowired
    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    // OAuth flow: https://developer-assets.spotifycdn.com/images/documentation/web-api/auth-code-flow.png

    @GetMapping(path="login")
    public void login(HttpServletResponse response) {
        spotifyService.authorizeLogin(response);
    }

    @GetMapping(path="callback")
    public HttpEntity<String> getAuthCode(HttpServletRequest request) {
        return spotifyService.getAuthCode(request);
    }

    @PostMapping(path="token")
    public HttpEntity<String> getAccessToken(@RequestBody String authCode) {
        return spotifyService.getAccessToken(authCode);
    }
}
