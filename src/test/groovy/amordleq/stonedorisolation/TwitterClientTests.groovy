package amordleq.stonedorisolation

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Flux
import reactor.test.StepVerifier;
import spock.lang.Specification
import twitter4j.Status;

@SpringBootTest
class TwitterClientTests extends Specification {

    @Autowired
    TwitterClient twitterClient

    def "can query for a single tweet"() {
        when:
        Flux<Status> results = twitterClient.searchForHashtag("test", 5)
        .doOnNext(s -> {println s.getText()})

        then:
        StepVerifier.create(results.take(7))
                .expectNextCount(7)
                .expectComplete()
                .verify()
    }
}
