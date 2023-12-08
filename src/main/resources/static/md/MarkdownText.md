


<br><br>


### < DIEx01 >   
(com.exam.di01)   
HelloBean1, HelloBean2 : sayHello()   
MainEx : 객체 생성 / 사용 / 소멸    

(com.exam.di02)   
Hello : Interface, 인터페이스, sayHello()   
HelloBean1, HelloBean2 : implements Hello   
MainEx : Hello hello = new HelloBean1(), HelloBean2()   

(com.exam.di03)   
context.xml :    
HelloBean1, HelloBean2 : 생성자 추가   
MainEx : GenericXmlApplicationContext - XML로부터 객체 설정 정보를 가져온다   

(com.exam.di04)   
context.xml :    
Hello : Interface, 인터페이스, sayHello()   
HelloBean1, HelloBean2 : implements Hello   
MainEx : Hello hello = new HelloBean1(), HelloBean2()   

(com.exam.di05)   
context.xml : scope="singleton", scope="prototype"   
HelloBean : sayHello()   
MainEx : Singleton, Prototype   

(com.exam.di06)   
context.xml : <constructor-arg><value>매개변수 값</value></constructor-arg>   
HelloBean : 생성자 매개변수 Override   
MainEx : Singleton, Prototype   


<br><br>


### < DIEx02 >   
(com.exam.di01)   
context.xml : 사용자 설정 매개변수   
BoardTO : 생성자()   
WriteAction, ViewAction : execute()   
MainEx : viewAction.execute()   

(com.exam.di02)   
context.xml : 사용자 설정 매개변수 TO   
BoardTO : seq, subject, lombok - @AllArgsConstructor   
ListAction : execute()   
MainEx : listAction.execute()   

(com.exam.di03)   
Lombok Dependency - https://mvnrepository.com/artifact/org.projectlombok/lombok   
context.xml : Setter 설정   
BoardTO : lombok - Getter, Setter   
WriteAction : lombok - @AllArgsConstructor, execute()   
MainEx : Print Getter   

(com.exam.di04)   
context.xml : namespace p 설정   
BoardTO : lombok - Getter, Setter   
WriteAction : lombok - @AllArgsConstructor, execute()   
MainEx : Print Getter   

(com.exam.di05)   
context.xml : namespace p 설정   
BoardListTO : ArrayList<String> userLists, ArrayList<BoardTO> boardLists;   
BoardTO : lombok - Getter, Setter   
WriteAction : lombok - @AllArgsConstructor, execute()   
MainEx01 : ArrayList<TO> Setter and Getter   
MainEx02 : MainEx01 + context.xml (Bean Configuration File)   

BoardMapTO : HashMap<String, String> userMaps, HashMap<String, BoardTO> boardMaps   
MainEx03 : HashMap + Bean Configuration File   

<br>

(com.exam.di06)   
Maven - MariaDB 3.2.0 https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client/3.2.0   
com.exam.di06.model1   
	BoardDAO   
	BoardTO   
com.exam.di06.model2   
	Action   
	ListAction : dao.boardLists()   
com.exam.di06   
	MainEx01 : 기본 JDBC   
	MainEx02 : context.xml 사용   


<br><br>


### < DIEx03 >   
(com.exam.di01)   
MainEx01 : 다형성 호출   
MainEx02 : Default scope : singleton   
(./model1)   
Hello : Interface   
HelloBean1, HelloBean2 : print(name)   
(./config)   
BeanConfig : @Bean, 다형성 + Option(name, scope)   

(com.exam.di02)   
MainEx : new AnnotationConfigApplicationContext( BeanConfig.class )   
(./model)   
Hello : Interface   
HelloBean : sayHello()   
(./config)   
BeanConfig : Annotation @Bean   

(com.exam.di03)   
MainEx : Annotation   
(./model1)   
BoardTO : seq, subject   
WriteAction : 생성자()   
(./config)   
BeanConfig : @Bean WriteAction   

(com.exam.di04)   
DIEx02/src/com/exam/di06 + Annotation   
(./model1)   
BoardDAO : boardList2()   
BoardTO : seq, subject, writer, mail, password, content, hit, wip, wdate, wgap   
(./model2)   
Action : Interface   
ListAction : execute()   
(./config)   
BeanConfig : @Bean ListAction   

(com.exam.lifecycle)   
MainEx : Bean Lifecycle 빈 라이프사이클 https://blog.naver.com/edy5016/221280377077   
(./model)   
Action : Interface   
WriteAction : implements Action, InitializingBean, DisposableBean, ApplicationContextAware, BeanNameAware, BeanClassLoaderAware, BeanFactoryAware   
(./config)   
BeanConfig : @Bean( initMethod = "init_method", destroyMethod = "destroy_method" )   
CustomBeanPostProcessor : postProcessAfterInitialization(), postProcessBeforeInitialization()   

(com.exam.di05)   
MainEx : 다중 Config.class 가져오기   
(./model)   
Hello : Interface   
HelloBean1, HelloBean2 : Print Hello + name   
(./config)   
BeanConfig : @Import( { BeanConfig1.class, BeanConfig2.class } )   
BeanConfig1, BeanConfig2 : @Bean HelloBean1, HelloBean2   

(com.exam.di06)   
MainEx : call getDAO()
(./model)   
BoardDAO : Print Log   
WriteAction : @Autowired, getDAO()   
(./config)   
BeanConfig : @Configuration   

(com.exam.di06_fz)   
실습코드   


<br><br>


### < AOPEx01 >   
(com.exam.aop01)   
MainEx : getBean( "writeAction", Action.class ), execute() 전처리/후처리   
**<span style="color:red">Context.xml : AOP 설정</span>**   
(./model)   
Action : Interface   
WriteAction : constructor, setWriter(), execute()   
(./advice)   
BasicAdvice1, BasicAdvice2 : 전처리, 후처리 LOG   

(com.exam.aop02)   
AOPEx01/pom.xml : <dependency> AspectJ => com.exam.aop02.advice 의 implements 불필요   
Context.xml : AOP 환경 설정
(./model)   
Action : Interface   
WriteAction : constructor, execute()   
(./advice)   
BasicAdvice1, BasicAdvice2 : Print LOG   

(com.exam.aop03)   
MainEx : Call Action   
Context.xml : Annotation 자동 검사 <aop:aspectj-autoproxy />   
(./model)   
Action : Interface   
WriteAction : constructor, execute()   
(./advice)   
BasicAdvice1, BasicAdvice2 : Print LOG   
BasicAdvice3 : 전처리, 후처리만 실행 @Before( "execution(* execute())" ), @After( "execution(* execute())" )   
(./config)   
BeanConfig : @Bean WriteAction   


<br><br>


### < BoardModel2Ex >   
URL 방식 Board Project   


<br><br>


### < WebEx01 >   
** Spring MVC + URL 방식 Maven Project **   
pom.xml : artifactId => mvc01   
web.xml : Spring5 239p DispatcherServlet 설정   
servlet-context.xml : <bean> *.do -> .jsp   

index : href="./list1.do"   
list1, list2 : Print Hello   


<br><br>


### **< WebEx02 >**   
< WebEx01 > 복습, artifactId = mvc02   
servlet-context.xml : property name="prefix", "suffix" 옵션   
(com.exam.mvc.model)
ListAction1, ListAction2 : return new ModelAndView( "listview1" );   


<br><br>


### < GugudanEx01 > & < GugudanModel2Ex02 >   
http://localhost:8080/gugudan/gugudan.do	GugudanAction	/WEB-INF/views/gugudan.jsp(form)   
http://localhost:8080/gugudan/gugudan_ok.do	GugudanOkAction	/WEB-INF/views/gugudan_ok.jsp(form)   

GugudanOkAction : modelAndview.addObject( "result", sbHtml.toString() );   


<br><br>


### < ZipcodeEx01 >   
추가 라이브러리   
1. mariadb-java-client   
2. lombok   

우편번호 검색 프로젝트를 Spring MVC 로 변경하기   


<br><br>


### <BoardModel2Ex > -> < BoardEx01 > -> < BoardModel2Ex01 >   
model2 구조의 프로젝트를 Spring MVC Project로 변경하기   
servlet-context.xml 다수 연결   


<br><br>


### < WebEx03 >   
servlet-context.xml : xmlns:context, Controller 파일 자동 스캔   
(com.exam.mvc.controller)   
ConfigController : @Controller, @RequestMapping   


<br><br>


### < WebEx04 >   
servlet-context.xml : xmlns:context, Controller 파일 자동 스캔   
(com.exam.mvc.controller)   
ConfigController : GET, POST 별 개별 호출 함수 설정   


<br><br>


### < WebEx04 >   
(com.exam.mvc.controller)   
ConfigController : Controller 방법 1 ~ 6   


<br><br>


### < ZipcodeEx02 >   
< WebEx04 > 응용   
(com.exam.mvc.controller)   
ZipcodeController : ZipcodeAction + ZipcodeOkAction   
ConfigServlet : servlet-context.xml -> Annotation 방식   

web.xml : ConfigServlet 등록, <param-name>contextClass</param-name>   


<br><br>


### < BoardModel2Ex02 >   
< BoardModel2Ex01 > + Annotation Spring MVC   
(com.exam.mvc.controller)   
BoardController :   
 - 가상 디렉토리 생성 @RequestMapping( "/board" ) => http://localhost:8080/BoardModel2Ex02/list.do -> http://localhost:8080/BoardModel2Ex02/board/list.do   
 - return "String", /*return ModelAndView*/   


<br><br>


### < WebEx022 >   
공유 데이터 설정   
(com.exam.mvc.share)   
ShareClass : private String shareData1;, Getter/Setter   

root-context.xml : name=shareClass -> ShareClass 등록   
web.xml : contextConfigLocation, ContextLoaderListener 추가   

ListAction1, 2 : private ShareClass shareClass;   
servlet-context.xml : <property name="shareClass" ref="shareClass" />   


<br><br>


### < ShareClassEx01 >   
<WebEx022 > 공유데이터 ShareClass + Annotation   


<br><br>


### < UploadEx01 >   
이미지 전송 및 저장   


<br><br>


### **< PDSBoardEx01 >**   
< PDSModel2Ex01 > File Upload + Annotation   


<br><br>


### **< UploadEx02 >**   
pom.xml dependency   
commons-fileupload https://mvnrepository.com/artifact/commons-fileupload/commons-fileupload/1.5   
commons-io https://mvnrepository.com/artifact/commons-io/commons-io/2.15.0   


<br><br>


### **< JDBCEx01 >**   
pom.xml dependency   
<artifactId>spring-jdbc</artifactId>   

root-context.xml : JDBC 설정   


<br><br>


### **< JDBCEx02 >**   
JdbcTemplate   
(com.exam.mvc.model)   
DeptDAO : 자동 Getter, Setter, 1행1열/1행다열/다행다열   
DeptTO : Using Lombok   
DeptMapper : implements RowMapper<DeptTO>   


<br><br>


### **< JDBCEx03 >**   
DML(INSERT, UPDATE, DELETE), DDL(CREATE TABLE)   


<br><br>


### **< ZipcodeEx03 >**   
< ZipcodeEx02 > + JDBC   


<br><br>


### < MyBatisEx01 >   
maven - jdbc driver, Spring-jdbc, lombok, mybatis, mybatis-spring   
(src/main.resources/mapper)   
mapper.xml : SQL 등록   


<br><br>


### < BoardModel2Ex04 >   
BoardModel2 + MyBatis   


<br><br>


### < MyBatisEx02 >   
< MyBatisEx01 > + Annotation   


<br><br>


### < boot01 > JFrame 확인   
### < boot02 > JDBC 확인   


<br><br>


### < boot03 >   
(src/main/resources)application.properties : MariaDB 연결 환경설정   
HikariDataSource https://github.com/brettwooldridge/HikariCP   


<br><br>


### < boot04 >   
Zipcode 검색기   


<br><br>


### < boot05 >   
(src.main.resources.mappers)   
mapper.xml : select문 등록   


<br><br>


### < ZipcodeEx04 >   
Zipcode 검색기   
Spring Boot + MyBatis + Annotation Mapper.java   


<br><br>


### < boot06 >   
(src/main/java)   
DatabaseConfiguration : mapper Session 환경 설정   


<br><br>


### < web01 >   
pom.xml : JSP dependency 추가   
application.properties : JSP 폴더 경로, 파일 형식 설정   
(src/main/resources)   
(static) : 고정된 파일(html)만 가능, .jsp 파일 불가능   


<br><br>


### < web02 >   
form.do -> form.jsp   
form_ok.do -> form_ok.jsp   
(com.example.config)   
ConfigController : URL Listener 설정   


<br><br>


### < gugudan01 >   
< GugudanModel2Ex02 > + Spring Boot + Controller   


<br><br>


### < jdbc01 >   
pom.xml : JSP 파일 환경 설정   
application.properties : JSP + MariaDB 설정   


<br><br>


### < board01 >   
< BoardModel2Ex03 > + Spring Boot + JDBC API(JdbcTemplate)   


<br><br>


### < mybatis01 >   
< boot05 > + .jsp 연결   


<br><br>


### < upload01 >   
파일업로드   
중복 이름 파일 처리 -> 파일명.확장자 -> 파일명_타임스탬프.확장자   
파일별 디렉토리 분류 -> 이미지(/images), 기타(uploads)   
ConfigController : 다중 파일 처리   
FileRenamePolicy : 파일이름_타임스탬프.확장자   

form.jsp : 다중 파일 선택 옵션 multiple="multiple"   


<br><br>


### < mail01 >   
보안 -> 앱 비밀번호 : xxxx xxxx xxxx xxxx   


<br><br>


### < board02 >   
AlbumBoard + JDBCTemplate + Mail   
+++   
--list 행 SKIP--   
view 이전글, 다음글   
write_ok 중복파일 이름 설정   
BoardDAO() boardReplyOk() ???   


<br><br>


### < board022 >   
AlbumBoard + ModelAndView + JDBCTemplate + Mail + JQuery(ajax)   
+++   
--list 행 SKIP--   
--view 이전글, 다음글--   
--write_ok 중복파일 이름 설정--   
--BoardDAO() boardReplyOk()--  


<br><br>


### < board03 >   
AlbumBoard + ModelAndView + MyBatis + Mail + JQuery(ajax)    


<br><br>


### < thymeleaf01 >   
pom.xml : Thymeleaf 추가   
application.properties : Thymeleaf 설정   

(static/templates)   
참고 https://www.thymeleaf.org/doc/tutorials/3.1/usingthymeleaf.html   
view2 : Thymeleaf 문법 적용   
view3 : 삼항연산자, if/unless   
view4 : request 받기   
view5 : TO 데이터 받기   
view6 : ArrayList<TO> Table 출력   


<br><br>


### < thymeleafBoard01 >   
board01 + Thymeleaf(HTML)   
list, view, write, write_ok 구현    
board_write1_ok.html은 코드 오류 아님!   


<br><br>


### < thymeleafBoard02 >   
/target/   
Project -> Run As -> Maven Build -> Goals : package ->   
thymeleafBoard02-0.0.1-SNAPSHOT.jar 생성   


<br><br>


### <  >   















<br>

