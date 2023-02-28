package com.example.demo.controller;


import com.example.demo.common.AjaxResult;
import com.example.demo.common.Constant;
import com.example.demo.common.SessionUtil;
import com.example.demo.model.ArticleInfo;
import com.example.demo.model.UserInfo;
import com.example.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/art")
public class ArticleController {

    @Autowired
    private ArticleService articleService;



    @RequestMapping("/mylist")
    public List<ArticleInfo> initMyList(HttpServletRequest request){
        UserInfo userInfo = SessionUtil.getLoginUser(request);
        if(userInfo != null){
            return articleService.getMyList(userInfo.getId());
        }
        return null;
    }

    @RequestMapping("/detail")
    public Object getDetail(Integer aid){
        if(aid!=null&& aid >0){
            return AjaxResult.success(articleService.getDetail(aid));
        }
        return AjaxResult.fail(-1,"查询失败");
    }

    @RequestMapping("/detailbyid")
    public Object getDetailById(Integer aid,HttpServletRequest request){
        if(aid!=null&& aid >0){
            ArticleInfo articleInfo = articleService.getDetail(aid);

            UserInfo userInfo = SessionUtil.getLoginUser(request);
            if(userInfo != null && articleInfo != null && userInfo.getId()==articleInfo.getUid()){
                return AjaxResult.success(articleInfo);
            }
        }
        return AjaxResult.fail(-1,"查询失败");
    }

    @RequestMapping("/update")
    public int update(Integer aid,String title,String content,HttpServletRequest request){
        //todo:非空校验
        UserInfo userInfo = SessionUtil.getLoginUser(request);
        if(userInfo!= null && userInfo.getId()>0){
            return articleService.update(aid,userInfo.getId(),title,content);
        }
        return 0;
    }

    @RequestMapping("/list")
    public List<ArticleInfo> getList(Integer pindex,Integer psize){
        if(pindex == null || psize == null){
            return null;
        }
        int offset = (pindex-1)*psize;
        return articleService.getList(psize,offset);
    }

    @RequestMapping("/totalpage")
    public Integer getTotalCount(Integer psize){
        if(psize!=null){
            int totalCount = articleService.getTotalCount();
            return (int) Math.ceil(totalCount*1.0/psize);
        }

        return null;
    }

}
