package jwt.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class User {

    @Id
    private Long id;

    private String name;

    private String username;

    private String password;
}
