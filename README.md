# Backend for Tunes Media Player

## Local Development
- JDK version 23
- Apache Maven version 3.9.9

### To obtain Spotify credentials (needed for spotify API):
- Go to [Spotify's developer dashboard](https://developer.spotify.com/dashboard) and sign in
- Click "Create app"
- Under "Redirect URIs" enter: http://localhost:8080/api/v1/spotify/callback (Name whatever you want for other required fields)
- Check off "Web APIs" (at the bottom under "Which API/SDKs are you planning to use?")
- Once created, click on your new Spotify app's settings (should be in top right)
- Copy your `Client ID` and `Client secret`

### Setting your Client ID and Client secret as local environment variables
- Notes: We are storing our client credentials in our local envs, and then our Tunes application will access them through our local envs; this will keep them secure
  
Windows:
- Open command prompt and enter the following:

  ```
  setx SPOTIFY_ID {your client id here}
  setx SPOTIFY_SECRET {your client secret here}
  ```

- Type `echo $SPOTIFY_ID` and `echo $SPOTIFY_SECRET` to confirm credentials haves been saved correctly
- Now that you've set your environment variables, the Tunes application should be able to access your credentials and Spotify APIs will be called with your Spotify Account (under `application.properties`)
- (If unable to still run application, a computer restart may be required to save environment variables properly)

Mac:
- todo

### To run locally:
- ```mvn clean install```
- Run application

### Flow of Spotify OAuth Authorization Endpoints: 
- [Visualization of Spotify API Authorization](https://developer-assets.spotifycdn.com/images/documentation/web-api/auth-code-flow.png)
  1) Call GET `/api/v1/spotify/login` will direct user to Spotify and ask them to authorize
  2) Once authorized, user will be redirected to `/api/v1/spotify/callback` with an authentication code in the uri query parameter (e.g. `/api/v1/spotify/callback?code={whatevercodehere`)
  3) GET `api/v1/spotify/callback` will automatically return the auth code (no need to call it) from it's the query parameter after user is redirected here from step 1
  4) Call POST `api/v1/spotify/token` with auth code (received in step 3) in the body, should receive access token
  5) Authorization done! Use the access token for all futuer Spotify API calls
