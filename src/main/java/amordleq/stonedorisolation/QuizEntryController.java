package amordleq.stonedorisolation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class QuizEntryController {

    @Value("${stonedorisolation.maximumTotalResults}")
    int maximumNumberOfTotalResults;

    @Autowired
    private QuizEntryGenerator quizEntryGenerator;

    @RequestMapping(path = "/entries", produces = "application/stream+json;charset=UTF-8")
    @ResponseBody
    public Flux<QuizEntry> entries() {
        Flux<QuizEntry> entries = quizEntryGenerator.generateEntries();
        if (maximumNumberOfTotalResults > 0) {
            return entries.take(maximumNumberOfTotalResults);
        } else {
            return entries;
        }
    }
}
