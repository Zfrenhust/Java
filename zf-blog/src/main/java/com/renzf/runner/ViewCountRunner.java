package com.renzf.runner;

import com.renzf.domain.entity.Article;
import com.renzf.mapper.ArticleMapper;
import com.renzf.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
//        //查询博客信息 id和ViewCount
//        List<Article> articles = articleMapper.selectList(null);
//        Map<String, Integer> viewCountMap = articles.stream()
//                .collect(Collectors.toMap(new Function<Article, String>() {
//                    @Override
//                    public String apply(Article article) {
//                        return article.getId().toString();
//                    }
//                }, new Function<Article, Integer>() {
//                    @Override
//                    public Integer apply(Article article) {
//                        return article.getViewCount().intValue();//为了递增转化为Long类型
//                    }
//                }));
//        //存储到Redis中
//        redisCache.setCacheMap("article:viewCount",viewCountMap);
        //查询博客信息  id  viewCount
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> {
                    return article.getViewCount().intValue();//
                }));
        //存储到redis中
        redisCache.setCacheMap("article:viewCount",viewCountMap);

    }
}
