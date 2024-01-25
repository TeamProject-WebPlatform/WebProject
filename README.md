현재 프로필 이미지 링크가 aws 배포용 링크로 되어 있음


프로필 이미지 경로 : ProfileController 212행, 306행 Path, upload 수정 필요함


컨트롤러에서 경로 수정 후 templates안의 html 페이지에서 profileimage 경로 수정 필요


security/WebConfig.java 파일 -> 외부 폴더 접근용 파일이니 필요없으면 삭제해도 됨
