package nack2.tunes.spotify;

import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
public class SpotifyService {
    public SpotifyService() {
    }

    @Value("${spotify.id}")
    private String clientId;

    @Value("${spotify.secret}")
    private String clientSecret;

    @Value("${api.base-url}")
    private String API_BASE_URL;

    private final String state = UUID.randomUUID().toString();

    public String getState() {
        return this.state;
    }

    public String getAuthUrl() {
        String redirectUrl = "http://localhost:5173/";
        String scope = "app-remote-control";

        String authUrl = "https://accounts.spotify.com/authorize?" +
                "response_type=code" +
                "&client_id=" + this.clientId +
                "&scope=" + scope +
                "&redirect_uri=" + redirectUrl+
                "&state=" + this.state;

        return authUrl;
    }

    public String getAuthCode(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        if (code == null || code.isEmpty()) {
            throw new Exception("Missing authorization code is in query parameters");
        } else if (state == null || state.isEmpty()) {
            throw new Exception("Missing state in query parameters");
        } else if (!this.state.equals(state)) {
            throw new Exception("Incorrect state in query parameters");
        }

        return code;
    }

    public String getAccessToken(Map<String, String> authCode) throws Exception {
        String url = "https://accounts.spotify.com/api/token";
        String redirectUrl = API_BASE_URL + "/spotify/callback";

        String credentials = this.clientId + ":" + this.clientSecret;
        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(base64Credentials);
        String body = "grant_type=authorization_code&code="+ authCode.get("authCode") + "&redirect_uri=" + redirectUrl;

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.postForObject(url, entity, String.class);
            return new JSONObject(result).getString("access_token");
        } catch (Exception e) {
            throw new Exception("Invalid authorization code");
        }
    }
}
