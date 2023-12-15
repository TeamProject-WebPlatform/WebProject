package platform.game.model.Authorization;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    SUPERADMIN("ROLE_SUPERADMIN,ROLE_ADMIN"),
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    GUESET("ROLE_GUEST");
    
    private String value;
}
