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


    // ???????????????????????????????????????????????????????????? AuthenticationManager??????????????????
    // ??????spring security ?????????????????????????????????
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
//        // ???????????????????????????????????????????????????????????????
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
                //???????????????
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // ?????????????????????????????? //???Spring security????????????preflight request
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .antMatchers("/wuzz/test4","/code/*").permitAll() //???????????????????????????????????????
//                .antMatchers("/wuzz/**").authenticated()// ????????????????????????????????????
                .antMatchers("/druid/**", "/actuator/**", "/auth/**", "/verify/code/**").permitAll()
                .antMatchers("/leave/**").permitAll()
                .antMatchers("/favicon.ico","/uploads/**").permitAll()
                .antMatchers(HttpMethod.POST, "/user/login").permitAll()
//                .antMatchers(HttpMethod.POST, "/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()
//                .httpBasic()
                .formLogin().loginPage("http://localhost:8088/#/login")//?????????????????????
                .loginProcessingUrl("/user/login") //??????????????????
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/index")
//                .successForwardUrl("/index")
                .successHandler(myAuthenticationSuccessHandler) // ?????????????????????
                .failureHandler(myAuthenticationFailureHandler) // ?????????????????????
                .permitAll()
                .and()

                .addFilterBefore(new JwtLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
//                .userDetailsService(myUserDetailService)//??????userDetailsService?????????????????????
        ;
        http.headers().cacheControl(); //????????????
        http.csrf().disable(); //??????csrf??????
    }

    //?????????uri
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//                .antMatchers( "/api/**", "/resources/**", "/static/**", "/public/**", "/webui/**", "/h2-console/**"
//                        , "/configuration/**",  "/swagger-resources/**", "/api-docs", "/api-docs/**", "/v2/api-docs/**"
//                        ,  "/**/*.css", "/**/*.js","/**/*.ftl", "/**/*.png ", "/**/*.jpg", "/**/*.gif ", "/**/*.svg", "/**/*.ico", "/**/*.ttf", "/**/*.woff");
//    }
    // ??????????????????????????????
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowCredentials(true); // sessionid ??????????????????  //4,????????????
//        List<String> allowedOriginPatterns = new ArrayList<>();
//        allowedOriginPatterns.add("*");
//        cors.setAllowedOriginPatterns(allowedOriginPatterns);
        cors.setAllowedOriginPatterns(Collections.singletonList("*"));
//        cors.addAllowedOrigin("*");// 1????????????????????????
        cors.addAllowedHeader(CorsConfiguration.ALL);// 2???????????????
        cors.addAllowedMethod(CorsConfiguration.ALL); // 3?????????????????????post???get??????
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
