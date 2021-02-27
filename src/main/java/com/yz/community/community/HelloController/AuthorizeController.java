package com.yz.community.community.HelloController;

import com.yz.community.community.Provider.GitHubProvider;
import com.yz.community.community.dto.AccessTokenDto;
import com.yz.community.community.dto.GitHubUsedrDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * create by yuanze
 */
@Controller
public class AuthorizeController {
    @Autowired
    private GitHubProvider gitHubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request){
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setCode(code);
        accessTokenDto.setState(state);
        accessTokenDto.setRedirect_uri(redirectUri);
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setClient_secret(clientSecret);
        String accessToken = gitHubProvider.getAccessToken(accessTokenDto);
        GitHubUsedrDto user = gitHubProvider.getUser(accessToken);
        if(user != null){
            //登录成功
            request.getSession().setAttribute("user",user);
            return "redirect:/";
        }else{
            //登录失败
            return "redirect:/";
        }
    }
}
