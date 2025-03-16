# Backend for Tunes Media Player

## Local Development
### To obtain Spotify credentials (needed for spotify API):
- Sign in to Spotify and go to [developer dashboard](https://developer.spotify.com/dashboard)
- Click "Create app"
- In "Redirect URIs" enter: http://localhost:8080/api/v1/spotify/callback
- Once created, click on your new Spotify app's settings (should be in top right)
- Copy your `Client ID` and `Client secret`

### Setting your Client ID and Client secret as local environment variables
Windows:
- Open command prompt and enter the following:

  ```
  setx SPOTIFY_ID {your client id here}
  setx SPOTIFY_SECRET {your client secret here}
  ```

- Type `echo $SPOTIFY_ID` and `echo $SPOTIFY_SECRET` to confirm credentials have been saved correctly
- Now that you've set your environment variables, the Tunes application should be able to access your credentials and Spotify APIs will be called with your Spotify Account (under `application.properties`)
- (If unable to still run application, a computer restart may be required to save environment variables properly)
