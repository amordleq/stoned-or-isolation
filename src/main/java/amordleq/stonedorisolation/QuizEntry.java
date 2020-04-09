package amordleq.stonedorisolation;

import lombok.*;
import twitter4j.Status;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class QuizEntry {

    String quizText;
    String fullText;
    ThoughtType type;

    enum ThoughtType {
        STONED, ISOLATION, BOTH, UNKNOWN
    }

    public QuizEntry(Status status) {
        fullText = status.getText();
        quizText = removeHashtags(fullText);

        boolean isolation = fullText.toLowerCase().contains("#isolationthoughts");
        boolean stoned = fullText.toLowerCase().contains("#stonedthoughts");
        if (isolation && stoned) {
            type = ThoughtType.BOTH;
        } else if (isolation) {
            type =  ThoughtType.ISOLATION;
        } else if (stoned) {
            type = ThoughtType.STONED;
        } else {
            type = ThoughtType.UNKNOWN;
        }
    }

    private String removeHashtags(String text) {
        return text.replaceAll("#\\S*", "<removed>");
    }
}
