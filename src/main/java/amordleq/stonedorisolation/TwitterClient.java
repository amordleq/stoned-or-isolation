package amordleq.stonedorisolation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import twitter4j.*;

@Component
public class TwitterClient {

    private AsyncTwitterFactory twitterFactory;

    private int pageSize;

    public TwitterClient(AsyncTwitterFactory twitterFactory, @Value("${stonedorisolation.pageSize}") int pageSize) {
        this.twitterFactory = twitterFactory;
        this.pageSize = pageSize;
    }

    public Flux<Status> searchForStonedThoughts() {
        return searchForHashtag("stonedthoughts", pageSize);
    }

    public Flux<Status> searchForIsolationThoughts() {
        return searchForHashtag("isolationthoughts", pageSize);
    }

    public Flux<Status> searchForHashtag(String hashtag, int pageSize) {
        Query query = new Query("#" + hashtag).count(pageSize);

        query.lang("en");
        return Flux.from(search(query))
                .expand(queryResult -> {
                    if (!queryResult.hasNext()) {
                        return Flux.empty();
                    } else {
                        return Flux.from(search(queryResult.nextQuery()));
                    }
                })
                .flatMap(queryResult -> Flux.fromIterable(queryResult.getTweets()));
    }

    private Mono<QueryResult> search(Query query) {
        AsyncTwitter twitter = twitterFactory.getInstance();
        return Mono.create(sink -> {
            twitter.addListener(new TwitterAdapter(){
                @Override
                public void searched(QueryResult queryResult) {
                    sink.success(queryResult);
                }
            });
            twitter.search(query);
        });
    }
}
