package com.tjoeun.spring.config;

import javax.annotation.Resource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tjoeun.spring.dto.UserDTO;
import com.tjoeun.spring.interceptor.CheckLoginInterceptor;
import com.tjoeun.spring.interceptor.CheckWriteInterceptor;
import com.tjoeun.spring.interceptor.TopMenuInterceptor;
import com.tjoeun.spring.mapper.BoardMapper;
import com.tjoeun.spring.mapper.TopMenuMapper;
import com.tjoeun.spring.mapper.UserMapper;
import com.tjoeun.spring.service.BoardService;
import com.tjoeun.spring.service.TopMenuService;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.tjoeun.spring.controller", 
							   "com.tjoeun.spring.dao",
							   "com.tjoeun.spring.service"})

@PropertySource("/WEB-INF/properties/db.properties")
public class ServletAppContext implements WebMvcConfigurer{
	
	@Value("${db.classname}")
	private String db_classname;
	
	@Value("${db.url}")
	private String db_url;
	
	@Value("${db.username}")
	private String db_username;
	
	@Value("${db.password}")
	private String db_password;
	
	// 주입받는 객체들은 Singleton으로 생성되어있음
	// 주입 받을 때마다 객체가 생성되는 것이 아니고
	// 이미 생성되어있는 객체의 주소만 주입받게 됨
	// 한 프로젝트에서 필요한 곳 어느 곳에서나
	// 주입 받아도 생성되어있는 한 객체의 주소를 주입받게 됨
	@Autowired
	private TopMenuService topMenuService;
	
	@Autowired
	private BoardService boardService;
	
	@Resource(name="loginUserDTO")
	private UserDTO loginUserDTO;
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registy) {
		WebMvcConfigurer.super.configureViewResolvers(registy);
		registy.jsp("/WEB-INF/view/",".jsp");
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);
		registry.addResourceHandler("/**").addResourceLocations("/resources/");
	}
	
	// 데이터 베이스에 접속 정보를 관리하는 Bean
	@Bean
	public BasicDataSource dataSource() {
		BasicDataSource source = new BasicDataSource();
		source.setDriverClassName(db_classname);
		source.setUrl(db_url);
		source.setUsername(db_username);
		source.setPassword(db_password);
		return source;
	}
	
	// 쿼리문과 접속정보 관리 객체
	@Bean
	public SqlSessionFactory factory(BasicDataSource source) throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(source);
		SqlSessionFactory factory = factoryBean.getObject();
		return factory;
	}
	
	// 쿼리문 실행 객체(Mapper 관리)
	// Mapper 들록하기 : BoardMapper
	@Bean
	public MapperFactoryBean<BoardMapper> getBoardMapper(SqlSessionFactory factory) throws Exception {
		MapperFactoryBean<BoardMapper> factoryBean = new MapperFactoryBean<BoardMapper> (BoardMapper.class);
		factoryBean.setSqlSessionFactory(factory);
		return factoryBean;
	}
	
	// 쿼리문 실행 객체 (Mapper 관리)
	  // Mapper 등록하기 : TopMenuMapper
		@Bean
		public MapperFactoryBean<TopMenuMapper> getTopMenuMapper(SqlSessionFactory factory) throws Exception{
			MapperFactoryBean<TopMenuMapper> factoryBean = new MapperFactoryBean<TopMenuMapper>(TopMenuMapper.class);
			factoryBean.setSqlSessionFactory(factory);
			return factoryBean;
		}
		
	// 쿼리문 실행 객체 (Mapper 관리)
		// Mapper 등록 : UserMapper
		@Bean
		public MapperFactoryBean<UserMapper> getUserMapper(SqlSessionFactory factory) throws Exception {
			MapperFactoryBean<UserMapper> factoryBean = new MapperFactoryBean<UserMapper>(UserMapper.class);
			factoryBean.setSqlSessionFactory(factory);
			return factoryBean;
		}
	
	// Interceptor 등록하기
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		WebMvcConfigurer.super.addInterceptors(registry);
		
		TopMenuInterceptor topMenuInterceptor = new TopMenuInterceptor(topMenuService, loginUserDTO);
		CheckLoginInterceptor checkLoginInterceptor = new CheckLoginInterceptor(loginUserDTO);
		CheckWriteInterceptor checkWriteInterceptor = new CheckWriteInterceptor(loginUserDTO, boardService);
		
		InterceptorRegistration reg1 = registry.addInterceptor(topMenuInterceptor);
		InterceptorRegistration reg2 = registry.addInterceptor(checkLoginInterceptor);
		InterceptorRegistration reg3 = registry.addInterceptor(checkWriteInterceptor);
		
		// /** : 모든 요청의 주소에 대해서 interceptor가 동작함
		// 이렇게 패턴을 정해줄수가 있다.
		reg1.addPathPatterns("/**");
		reg2.addPathPatterns("/user/modify", "/user/logout", "/board/*");
		reg2.excludePathPatterns("/board/main");// 예외로 두는 경로 패턴
		reg3.addPathPatterns("/board/modify","/board/delete");
		
	}
	
	
	// properties 폴더 안에 있는 
	// properties 파일들이 충돌되지 않도록 개별적으로 관리해주는 Bean
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcePlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	//메시지 등록하기
	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource res = new ReloadableResourceBundleMessageSource();
		res.setBasename("/WEB-INF/properties/error_message");
		return res;
	}
	
	/*
	  enctype="multipart/form-data
  	  를 설정했을 때 data가 정상적으로 전송되게 함
  	  영상 등등 멀티 미디어 전송 가능 Bean 등록
	*/
	@Bean
	public StandardServletMultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}
}
