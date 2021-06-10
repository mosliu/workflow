package net.liuxuan.security.config;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.security.entrypoint.JwtAccessDeniedHandler;
import net.liuxuan.security.entrypoint.JwtAuthenticationEntryPoint;
import net.liuxuan.security.jwt.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.sql.DataSource;
import java.util.Collections;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-05-20
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private MyAuthenticationProvider myAuthenticationProvider;

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;


    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;


    // 这里记住一定要重新父类的对象，不然在注入 AuthenticationManager时会找不到，
    // 默认spring security 没有给我们注入到容器中
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Autowired
//    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
//    }


    @Override
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(myAuthenticationProvider);
//        auth.userDetailsService(myUserDetailsService());
    }

//    @Bean
//    public UserDetailsService myUserDetailsService() {
////        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//        // 初始化用户数据，记得先建表，初始化执行一次
//        //addGroupAndRoles(jdbcUserDetailsManager);
//        //        return jdbcUserDetailsManager;
//
//        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//
//        String[][] usersGroupsAndRoles = {
//                {"user", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
//                {"team-leader", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
//                {"hr", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
//                {"other", "password", "ROLE_ACTIVITI_USER", "GROUP_otherTeam"},
//                {"admin", "password", "ROLE_ACTIVITI_ADMIN"},
//        };
//
//        for (String[] user : usersGroupsAndRoles) {
//            List<String> authoritiesStrings = Arrays.asList(Arrays.copyOfRange(user, 2, user.length));
//            log.info("> Registering new user: " + user[0] + " with the following Authorities[" + authoritiesStrings + "]");
//            inMemoryUserDetailsManager.createUser(new User(user[0], passwordEncoder().encode(user[1]),
//                    authoritiesStrings.stream().map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList())));
//        }
//        return inMemoryUserDetailsManager;
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //未授权处理
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 这块是配置跨域请求的 //让Spring security放行所有preflight request
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .antMatchers("/wuzz/test4","/code/*").permitAll() //不需要保护的资源，可以多个
//                .antMatchers("/wuzz/**").authenticated()// 需要认证得资源，可以多个
                .antMatchers("/druid/**", "/actuator/**", "/auth/**", "/verify/code/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers(HttpMethod.POST, "/user/login").permitAll()
//                .antMatchers(HttpMethod.POST, "/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()
//                .httpBasic()
                .formLogin().loginPage("http://localhost:8088/#/login")//自定义登陆地址
                .loginProcessingUrl("/user/login") //登录处理地址
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/index")
//                .successForwardUrl("/index")
                .successHandler(myAuthenticationSuccessHandler) // 登陆成功处理器
                .failureHandler(myAuthenticationFailureHandler) // 登陆失败处理器
                .permitAll()
                .and()

                .addFilterBefore(new JwtLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
//                .userDetailsService(myUserDetailService)//设置userDetailsService，处理用户信息
        ;
        http.headers().cacheControl(); //禁用缓存
        http.csrf().disable(); //禁用csrf校验
    }

    //忽略的uri
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//                .antMatchers( "/api/**", "/resources/**", "/static/**", "/public/**", "/webui/**", "/h2-console/**"
//                        , "/configuration/**",  "/swagger-resources/**", "/api-docs", "/api-docs/**", "/v2/api-docs/**"
//                        ,  "/**/*.css", "/**/*.js","/**/*.ftl", "/**/*.png ", "/**/*.jpg", "/**/*.gif ", "/**/*.svg", "/**/*.ico", "/**/*.ttf", "/**/*.woff");
//    }
    // 这块是配置跨域请求的
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowCredentials(true); // sessionid 多次访问一致  //4,允许凭证
//        List<String> allowedOriginPatterns = new ArrayList<>();
//        allowedOriginPatterns.add("*");
//        cors.setAllowedOriginPatterns(allowedOriginPatterns);
        cors.setAllowedOriginPatterns(Collections.singletonList("*"));
//        cors.addAllowedOrigin("*");// 1允许任何域名使用
        cors.addAllowedHeader(CorsConfiguration.ALL);// 2允许任何头
        cors.addAllowedMethod(CorsConfiguration.ALL); // 3允许任何方法（post、get等）
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", cors);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean registration(JwtAuthenticationTokenFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

}
