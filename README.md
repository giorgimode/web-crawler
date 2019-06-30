# web-crawler

This app 
1. accepts user input
2. queries it in google
3. extracts url links from google results
4. retrieves the content of each url
5. in the retrieved content extracts the javascript libraries
6. counts the frequency of each library and prints top 5

Possible improvements:
* For crawling the web, [jsoup library](https://jsoup.org/) would make life much easier.  
* Sort/limit operations inside the stream can be expensive (WebCrawler::retrieveLibraries). 
Stream can be parallelized(via .parallel()) to query/parse each link separately. 
Sorting/aggregation logic can be done on the results outside the stream. 
This approach needs to be benchmarked, as using _.parallel()_ is not always the optimal solution

## Usage

Package: `mvn clean package`

Run: `java -jar target\web-crawler-1.0-SNAPSHOT.jar`

_\<Enter Search String\>_    


## Benchmark results
Different search words were used in `WebCrawlBenchmarkAgentTest` to benchmark speed for using _limit()_ and _.parallelized()_ inside the stream.
It seems best combination is to avoid using _.parallelized()_ and _limit()_ completely, retrieve the map of libraries sorted by 
frequency and get first N elements outside the stream.  