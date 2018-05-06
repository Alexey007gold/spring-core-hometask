package ua.epam.spring.hometask.configuration;

import org.springframework.ws.transport.http.support.AbstractAnnotationConfigMessageDispatcherServletInitializer;

public class SoapInitializer extends
        AbstractAnnotationConfigMessageDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[0];
    }

    @Override
    protected String getServletName() {
        return "soap";
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{SoapConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/ws/*"};
    }

}