package fr.fadigoStore.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue
    private Integer id;

    @NotEmpty(message = "This field is necessary")
    @NotBlank(message = "This field is necessary")
    private String firstname;

    @NotEmpty(message = "This field is necessary")
    @NotBlank(message = "This field is necessary")
    private String lastname;

    @NotEmpty(message = "This field is necessary")
    @NotBlank(message = "This field is necessary")
    @Email(message = "Email address is recommended")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "This field is necessary")
    @NotBlank(message = "This field is necessary")
    @Size(min = 8, message = "The size is 8 Characters for password")
    private String password;

    private Boolean isAccountLocked;
    private Boolean isEnable;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;



    @Override
    public String getName() {
        return this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(
                    role.getName()
                )).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isAccountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnable;
    }

    public String getFullname() {
        return this.firstname+" "+this.lastname;
    }
}
