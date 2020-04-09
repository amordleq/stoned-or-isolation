package amordleq.stonedorisolation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.AsyncTwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
public class Config {

    @Bean
    AsyncTwitterFactory twitterFactory() {
        ConfigurationBuilder builder = new ConfigurationBuilder()
                .setTweetModeExtended(true);
        return new AsyncTwitterFactory(builder.build());
    }
}
