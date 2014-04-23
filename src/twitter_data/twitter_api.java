package twitter_data;

import java.util.List;

import twitter4j.GeoQuery;
import twitter4j.Place;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class twitter_api {
	
	public static String CONSUMER_KEY = "Enter consumer key here";
	public static String CONSUMER_SECRET ="Enter consumer secret  here";
	public static String ACCESS_TOKEN="Enter acess token here";
	public static String ACCESS_SECRET="Enter key here";
	public static int max_tweet_search_count=10;//-1=> unlimited
	public static int max_user_search_count=10;//-1=>unlimited
	
	public static Twitter authentication()
	{
		//create a configuration 
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(CONSUMER_KEY)
		  .setOAuthConsumerSecret(CONSUMER_SECRET)
		  .setOAuthAccessToken(ACCESS_TOKEN)
		  .setOAuthAccessTokenSecret(ACCESS_SECRET);
		
		//load these configuration to twitter
		TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
		return twitter;
	}
	
	public static void users_timeline() throws TwitterException
	{
		Twitter twitter=authentication();		
        List<Status> statuses = twitter.getHomeTimeline();
	    System.out.println("Showing home timeline.");
	    for (Status status : statuses)
	    {
	    	System.out.println("\n");
	        System.out.println(status.getUser().getName() + ":" +
	                           status.getText());
	    }
	}
	
	public static void search_tweets(String name) 
	{
		Twitter twitter=authentication();	
		Query query = new Query(name);
		if(max_tweet_search_count>=100)
			query.setCount(100);
		else if(max_tweet_search_count>=0)//for no<100 & no >0
			query.setCount(max_tweet_search_count);
		else//for unlimited tweets search
			query.setCount(100);
		
		QueryResult result=null;
		try {
			result = twitter.search(query);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Access level : "+result.getAccessLevel());
		int search_count=0;
		do{
			List<Status> tweets = result.getTweets();	
			for(Status tweet: tweets)
			{
				if(search_count==max_tweet_search_count)
					break;
				search_count++;
				System.out.println("\n#################################\n");
				System.out.println("@" + tweet.getUser().getScreenName() +" : "+ tweet.getLang()+ " : " + tweet.getText() );
				//System.out.println("Tweet: "+tweet.getText());
			}
			query=result.nextQuery();
			if(query!=null)
			{
				try {
					result=twitter.search(query);
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}while(query!=null);		 
		System.out.println("total size : "+search_count);	
		 
	}
	public static void search_users(String user_name)// throws TwitterException
	{
		Twitter twitter=authentication();			
		ResponseList<User> users=null;
		int page=1;
		int user_count=0;
		boolean flag=false;
		do{
			try {
				
				users=twitter.searchUsers(user_name,page);
				for(User u:users)
				{
					if(user_count==max_user_search_count)
					{
						flag=true;
						break;
					}
					user_count++;
					System.out.println("#################################\n");
					System.out.println(u.getId()+"\n"+u.getName()+"\n"+u.getDescription()+"\n"+u.getLocation());
					
				}
				page++;
			} catch (TwitterException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
			
		}while(!flag);
		System.out.println("Number of users matching the given name : "+user_count);
	}

	public static void main(String[] args)
	{
		//search_users("ajith rao");
		search_tweets("from:Kiran");
		//to search tweets from a particluar user : search_tweets("from:kiran");
		
	}
}
