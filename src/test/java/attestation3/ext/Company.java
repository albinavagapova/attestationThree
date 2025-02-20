package attestation3.ext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Company(int id, String name, String description, Boolean isActive) {
}
