//package net.liuxuan.workflow.config;
//
//import com.google.common.collect.ImmutableList;
//import org.activiti.api.runtime.shared.identity.UserGroupManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//
///**
// * @author Liuxuan
// * @version v1.0.0
// * @description Tools for xx use
// * @date 2021-05-19
// **/
//
//@Configuration
//public class ActivitiSpringIdentityAutoConfiguration {
//    @Bean
//    public UserGroupManager userGroupManager() {
//        return new UserGroupManager() {
//            @Override
//            public List<String> getUserGroups(String s) {
//                return ImmutableList.of("Specify user group");
//            }
//
//            @Override
//            public List<String> getUserRoles(String s) {
//                return null;
//            }
//
//            @Override
//            public List<String> getGroups() {
//                return null;
//            }
//
//            @Override
//            public List<String> getUsers() {
//                return null;
//            }
//        };
//    }
//}
