package net.liuxuan.workflow.config.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.spring.boot.ActivitiProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-05-20
 **/
@Slf4j
@Component
public class ProcessDefinitionResourceFinder {

    private ActivitiProperties activitiProperties;

    private ResourcePatternResolver resourceLoader;

    public ProcessDefinitionResourceFinder(ActivitiProperties activitiProperties,
                                           ResourcePatternResolver resourceLoader) {
        this.activitiProperties = activitiProperties;
        this.resourceLoader = resourceLoader;
    }

    public List<Resource> discoverProcessDefinitionResources() throws IOException {
        List<Resource> resources = new ArrayList<>();
        if (activitiProperties.isCheckProcessDefinitions()) {
            for (String suffix : activitiProperties.getProcessDefinitionLocationSuffixes()) {
                String path = activitiProperties.getProcessDefinitionLocationPrefix() + suffix;
                resources.addAll(Arrays.asList(resourceLoader.getResources(path)));
            }
            if (resources.isEmpty()) {
                log.info("No process definitions were found for auto-deployment in the location `" + activitiProperties.getProcessDefinitionLocationPrefix() + "`");
            } else {
                List<String> resourcesNames = resources.stream().map(Resource::getFilename).collect(Collectors.toList());
                log.info("The following process definition files will be deployed: " + resourcesNames);
            }
        }
        return resources;
    }
}
