package me.radopeti.rssapi;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jsoup.Jsoup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpResponse.BodyHandlers;

@RestController
@RequestMapping("/feed")
public class FeedController {

    @CrossOrigin
    @GetMapping()
    public ResponseEntity<FeedResponse> getFeed(@RequestParam("url") String url) throws IOException, FeedException, URISyntaxException, InterruptedException {
        //String url = "https://danaepp.com/feed";
        final HttpResponse<InputStream> response =
                HttpClient.newHttpClient()
                        .send(
                                newBuilder().GET().uri(new URL(url).toURI()).build(),
                                BodyHandlers.ofInputStream()
                        );
        SyndFeed feed = new SyndFeedInput().build(new XmlReader(response.body()));

        final FeedResponse feedResponse = new FeedResponse(Jsoup.parse(feed.getEntries().get(0).getContents().get(0).getValue()).text());

        return ResponseEntity.ok(feedResponse);
    }

    record FeedResponse(String message) {}
}
