package attestation3.ext;

public record AuthResponse(String userToken, String role, String displayName, String login) {
}
