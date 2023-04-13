package com.example.demo.mapper;

import com.example.demo.model.ArticleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {

    public List<ArticleInfo> getList(@Param("psize") Integer psize,@Param("offset") int offset);

    public List<ArticleInfo> getMyList(@Param("uid") Integer uid);

    public ArticleInfo getDetail(@Param("aid") Integer aid);

    public int update(@Param("aid") Integer aid,
                      @Param("uid") Integer uid,
                      @Param("title") String title,
                      @Param("content") String content);

    public int getTotalCount();

    public int add(@Param("uid") Integer uid,@Param("title") String title,
                   @Param("content") String content);
}
