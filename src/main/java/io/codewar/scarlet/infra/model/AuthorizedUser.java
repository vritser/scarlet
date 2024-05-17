package io.codewar.scarlet.infra.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * AuthorizedUser
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AuthorizedUser {
    private Long id;
    private String name;
}
