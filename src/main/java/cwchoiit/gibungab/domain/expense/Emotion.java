package cwchoiit.gibungab.domain.expense;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Emotion {

    @Column(name = "satisfaction_score", nullable = false)
    private int satisfactionScore;

    @Column(name = "emotion_memo", length = 500)
    private String memo;

    public static Emotion of(int satisfactionScore, String memo) {
        if (satisfactionScore < 1 || satisfactionScore > 5) {
            throw new IllegalArgumentException("만족도는 1~5 사이여야 합니다.");
        }
        Emotion emotion = new Emotion();
        emotion.satisfactionScore = satisfactionScore;
        emotion.memo = memo;
        return emotion;
    }
}
