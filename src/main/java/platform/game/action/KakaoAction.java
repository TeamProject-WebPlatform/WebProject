package platform.game.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import platform.game.model.TO.MemberTO;

public class KakaoAction {

    public static String getKakaoToken(String access_token){

        //System.out.println("getKakaoToken(String access_token) 호출");
        RestTemplate restTemplate = new RestTemplate();
        
        //헤더 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        //헤더와 바디를 하나로 합침
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        //http 요청 - post방식
        ResponseEntity<String> response = restTemplate.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.POST,
            kakaoProfileRequest,
            String.class
        );

        //Json으로 불러온거 저장하기
        ObjectMapper objectMapper = new ObjectMapper();
        //KakaoProfileTO kakaoProfileTO = null;
        Map<String, Object> responseMap = null;
        try {
            //kakaoProfileTO = objectMapper.readValue(response.getBody(), KakaoProfileTO.class);
            responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
        } catch (JsonMappingException e) {
            System.out.println("카카오톡 로그인 에러1 : " + e.getMessage());
        } catch (JsonProcessingException e){
            System.out.println("카카오톡 로그인 에러2 : " + e.getMessage());
        }

        //System.out.println(kakaoProfileTO.getKakaoAccount().getEmail());
        String email = "";
        if (responseMap != null) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) responseMap.get("kakao_account");
            if (kakaoAccount != null) {
                email = kakaoAccount.get("email").toString();
                //System.out.println("엑션 email : " + kakaoAccount.get("email"));
            }
        }
        return email;
    }
}
