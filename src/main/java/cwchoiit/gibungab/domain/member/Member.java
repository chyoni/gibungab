package cwchoiit.gibungab.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "members",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_members_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_members_social", columnNames = {"social_provider", "social_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_provider", nullable = false, length = 20)
    private SocialProvider socialProvider;

    @Column(name = "social_id", nullable = false)
    private String socialId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public static Member of(String email, String nickname, String profileImageUrl,
                            SocialProvider socialProvider, String socialId) {
        Member member = new Member();
        member.email = email;
        member.nickname = nickname;
        member.profileImageUrl = profileImageUrl;
        member.socialProvider = socialProvider;
        member.socialId = socialId;
        member.createdAt = LocalDateTime.now();
        member.updatedAt = LocalDateTime.now();
        return member;
    }

    public void updateProfile(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
