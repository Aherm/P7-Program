package streaming;

import twitter4j.conf.ConfigurationBuilder;

public class Oauth {
	
	public ConfigurationBuilder createConfigBuilder(){
	 ConfigurationBuilder cb = new ConfigurationBuilder();

	cb.setDebugEnabled(true)
	  .setOAuthConsumerKey("4A2SYP3nEDHJvobAtdGBW8leO")
	  .setOAuthConsumerSecret("wJ8Pr1eII8XgFMgjav9sW143IYhwyvLO75dNcnW8XZZyNCygR8")
	  .setOAuthAccessToken("3619116801-YML3CKo04vM5JMt37eTYKsWwb8tWPbJN01RB2aa")
	  .setOAuthAccessTokenSecret("zKp63ebQbiIyAFG1tdz3YC1JLfNQCy5UVqf14HQ5zMy41");
	  
	return cb;
	}
}
