package amordleq.stonedorisolation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import twitter4j.*;

@Component
public class TwitterClient {

    private Twitter twitter;
    private int pageSize;

    public TwitterClient(Twitter twitter, @Value("${stonedorisolation.pageSize}") int pageSize) {
        this.twitter = twitter;
        this.pageSize = pageSize;
    }

    public Flux<Status> searchForStonedThoughts() {
        return searchForHashtag("stonedthoughts", pageSize);
    }

    public Flux<Status> searchForIsolationThoughts() {
        return searchForHashtag("isolationthoughts", pageSize);
    }

    public Flux<Status> searchForHashtag(String hashtag, int pageSize) {
        //FIXME:  this should really use the async version
        Query query = new Query("#" + hashtag).count(pageSize);

        query.lang("en");
        QueryResult result = searchSafely(query);

        return Flux.just(result)
                .expand(queryResult -> {
                    if (!queryResult.hasNext()) {
                        return Flux.empty();
                    } else {
                        return Flux.just(searchSafely(queryResult.nextQuery()));
                    }
                })
                .flatMap(queryResult -> Flux.fromIterable(queryResult.getTweets()));
    }

    private QueryResult searchSafely(Query query) {
        try {
            return twitter.search(query);
        } catch (TwitterException e) {
            throw Exceptions.propagate(e);
        }
    }
}
