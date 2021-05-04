package com.tjoeun.spring.config;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class ConfigSpring extends AbstractAnnotationConfigDispatcherServletInitializer{

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {RootAppContext.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] {ServletAppContext.class};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] {"/"};
	}
	
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		
		return new Filter[] {encodingFilter};
	}
	
	@Override
	protected void customizeRegistration(Dynamic registration) {
		super.customizeRegistration(registration);
		
		// MiltipartConfigElement 매개변수(인자값:argument)
	  	// 1) 클라이언트가 보낸 file data를 저장하는 임시폴더의 경로
	  	//     ㄴ null 로 지정하면 tomcat server가 설정해 놓은
	  	//        임시폴더로 자동으로 설정됨
	  	// 2) 업로드하는 파일 데이터의 최대 용량
	  	//     ㄴ 용량 계산 : 1024 - 1Kbyte
	  	//                    1024 * 1024 - 1Mbyte
	  	//                    50*1024*1024 - 50Mbyte
	  	//                      ㄴ 52428800
	  	// 3) 파일 데이터 용량을 포함한
	  	//    전체 요청 정보의 최대 용량
	  	// 4) 파일의 임계값 : 0 - 자동으로 처리됨
		
		MultipartConfigElement config = new MultipartConfigElement(null, 50*1024*1024, 200*1024*1024, 0);
		
		registration.setMultipartConfig(config);
	}
}
