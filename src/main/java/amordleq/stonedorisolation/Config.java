package amordleq.stonedorisolation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.ConfigurationContext;

@Configuration
public class Config {

    @Bean
    Twitter twitter() {
        ConfigurationBuilder builder = new ConfigurationBuilder()
                .setTweetModeExtended(true);
        return new TwitterFactory(builder.build()).getInstance();
    }
}
