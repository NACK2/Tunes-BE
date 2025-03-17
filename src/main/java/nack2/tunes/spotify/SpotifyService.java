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

    private String state;

    public String getAuthUrl() {
        String redirectUrl = API_BASE_URL + "/spotify/callback";
        String scope = "app-remote-control";
        this.state = UUID.randomUUID().toString();

        String authUrl = "https://accounts.spotify.com/authorize?" +
                "response_type=code" +
                "&client_id=" + clientId +
                "&scope=" + scope +
                "&redirect_uri=" + redirectUrl+
                "&state=" + state;

        return authUrl;
    }

    public String getAuthCode(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        if (code == null || code.isEmpty()) {
            throw new Exception("Authorization code is missing");
        } else if (!this.state.equals(state)) {
            throw new Exception("Invalid state");
        }

        return code;
    }

    public String getAccessToken(String authCode) {
        String url = "https://accounts.spotify.com/api/token";
        String redirectUrl = API_BASE_URL + "/spotify/callback";

        String credentials = this.clientId + ":" + this.clientSecret;
        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(base64Credentials);

        String body = "grant_type=authorization_code&code="+ authCode + "&redirect_uri=" + redirectUrl;

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(url, entity, String.class);

        return new JSONObject(result).getString("access_token");
    }
}
