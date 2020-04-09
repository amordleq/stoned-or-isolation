package amordleq.stonedorisolation;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Collections;

@Component
public class QuizEntryGenerator {

    TwitterClient twitterClient;

    public QuizEntryGenerator(TwitterClient twitterClient) {
        this.twitterClient = twitterClient;
    }

    Flux<QuizEntry> generateEntries() {
        Flux<QuizEntry> stonedEntries = twitterClient.searchForStonedThoughts().map(QuizEntry::new);
        Flux<QuizEntry> isolationEntries = twitterClient.searchForIsolationThoughts().map(QuizEntry::new);

        return stonedEntries.mergeWith(isolationEntries)
                .buffer(50)
                .flatMap(buffer -> {
                    Collections.shuffle(buffer);
                    return Flux.fromIterable(buffer);
                });
    }
}
