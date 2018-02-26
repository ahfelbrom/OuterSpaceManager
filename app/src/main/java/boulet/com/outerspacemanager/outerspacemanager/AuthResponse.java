package boulet.com.outerspacemanager.outerspacemanager;

/**
 * Created by bouleta on 26/02/2018.
 */
public class AuthResponse {
    private String token;
    private String expires;

    public String getExpires() {
        return expires;
    }

    public String getToken() {
        return token;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AuthResponse(String token, String expires){
        this.token = token;
        this.expires = expires;
    }
}