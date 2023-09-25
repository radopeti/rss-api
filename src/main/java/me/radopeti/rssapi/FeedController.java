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
import java.net.URL;

@RestController
@RequestMapping("/feed")
public class FeedController {

    @CrossOrigin
    @GetMapping()
    public ResponseEntity<FeedResponse> getFeed(@RequestParam("url") String url) throws IOException, FeedException {
        //String url = "https://danaepp.com/feed";
        SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(url)));
        final FeedResponse feedResponse = new FeedResponse(Jsoup.parse(feed.getEntries().get(0).getContents().get(0).getValue()).text());

        return ResponseEntity.ok(feedResponse);
    }

    record FeedResponse(String message) {}
}
