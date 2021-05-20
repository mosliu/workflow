package net.liuxuan.workflow.controller;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-05-20
 **/
@RestController
@RequestMapping("/leave")
public class LeaveController {
    //org.activiti.engine.RuntimeService
    @Autowired
    private RuntimeService runtimeService;//流程相关

    //org.activiti.engine.TaskService
    @Autowired
    private TaskService taskService;//任务相关

    //org.activiti.engine.HistoryService
    @Autowired
    private HistoryService historyService;//历史记录相关

    /**
     * 开始流程
     *
     * @param jobNumber
     * @return
     * @author 悟纤
     */
    @RequestMapping(value = "/start")
    public String start(String jobNumber) {
        //http://localhost:8884/leave/start?jobNumber=USER1123
        //设置流程的发起者
        Authentication.setAuthenticatedUserId(jobNumber);

        // bpmn中定义process的id。
        String instanceKey = "leaveProcess";
        System.out.println("开启请假流程...");

        // 设置流程参数，开启流程
        Map<String, Object> variables = new HashMap<String, Object>();
        //设置参数，这里的key就是上面配置的assignee的${jobNumber}，会进行赋值。
        variables.put("jobNumber", jobNumber);

        //使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
        // 流程开启成功之后，获取到ProcessInstance信息。
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(instanceKey, variables);

        System.out.println("流程实例ID:" + instance.getId());
        System.out.println("流程定义ID:" + instance.getProcessDefinitionId());


        //验证是否启动成功
        //通过查询正在运行的流程实例来判断
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        //根据流程实例ID来查询
        List<ProcessInstance> runningList = processInstanceQuery.processInstanceId(instance.getProcessInstanceId()).list();
        System.out.println("根据流程ID查询条数:" + runningList.size());

        // 返回流程ID
        return instance.getId();
    }

    /**
     * <p>查看任务</p>
     *
     * @author 悟纤
     */
    @RequestMapping(value = "/showTask")
    public List<Map<String, String>> showTask(String jobNumber) {
        /*
         * 获取请求参数
         */
        TaskQuery taskQuery = taskService.createTaskQuery();

        List<Task> taskList = null;
        if (jobNumber == null) {
            //获取所有人的所有任务.
            taskList = taskQuery.list();
        } else {
            //获取分配人的任务.
            taskList = taskQuery.taskAssignee(jobNumber).list();
        }

        if (taskList == null || taskList.size() == 0) {
            System.out.println("查询任务列表为空！");
            return null;
        }


        /*
         * 查询所有任务，并封装
         */
        List<Map<String, String>> resultList = new ArrayList<>();
        for (Task task : taskList) {
            Map<String, String> map = new HashMap<>();
            map.put("taskId", task.getId());
            map.put("name", task.getName());
            map.put("createTime", task.getCreateTime().toString());
            map.put("assignee", task.getAssignee());
            map.put("instanceId", task.getProcessInstanceId());
            map.put("executionId", task.getExecutionId());
            map.put("definitionId", task.getProcessDefinitionId());
            resultList.add(map);
        }


        /*
         * 返回结果
         */
        return resultList;
    }

    /**
     * 员工提交申请
     *
     * @return String 申请受理结果
     * @author 悟纤
     */
    @RequestMapping(value = "/employeeApply")
    public String employeeApply(HttpServletRequest request) {
        System.out.println("--> 提交申请单信息");
        /*
         * 获取请求参数
         */
        String taskId = request.getParameter("taskId"); // 任务ID
        //String jobNumber = request.getParameter("jobNumber"); // 工号
        String deptJobNumber = request.getParameter("deptJobNumber"); //上级
        String leaveDays = request.getParameter("leaveDays"); // 请假天数
        String leaveReason = request.getParameter("leaveReason"); // 请假原因


        /*
         *  查询任务
         */
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            System.out.println("任务ID:" + taskId + "查询到任务为空！");
            return "fail";
        }


        /*
         * 参数传递并提交申请
         */
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("days", leaveDays);
        variables.put("date", new Date());
        variables.put("reason", leaveReason);
        variables.put("deptJobNumber", deptJobNumber);
        taskService.complete(task.getId(), variables);
        System.out.println("执行【员工申请】环节，流程推动到【部门审核】环节");

        /*
         * 返回成功
         */
        return "success";
    }

    /**
     * <p>部门经理审核</p>
     *
     * @return String 受理结果
     * @author 悟纤
     */
    @ResponseBody
    @RequestMapping(value = "/deptManagerAudit")
    public String deptManagerAudit(HttpServletRequest request) {
        /*
         * 获取请求参数
         */
        //任务id
        String taskId = request.getParameter("taskId");
        //审批意见
        String auditOpinion = request.getParameter("auditOpinion");
        //审批结果：同意1；不同意0
        String audit = request.getParameter("audit");

        if (StringUtils.isBlank(taskId)) return "fail";



        /*
         * 查找任务
         */
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            System.out.println("审核任务ID:" + taskId + "查询到任务为空！");
            return "fail";
        }


        /*
         * 设置局部变量参数，完成任务
         */
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("audit", audit);
        map.put("auditOpinion", auditOpinion);
        taskService.complete(taskId, map);

        return "success";
    }
    @RequestMapping("/historyList")
    public List<Map<String,Object>> historyList(String jobNumber){

        List<HistoricProcessInstance> historicProcessInstances =historyService // 历史任务Service
                .createHistoricProcessInstanceQuery() // 创建历史活动实例查询
                .processDefinitionKey("leaveProcess")//processDefinitionKey
                .finished().startedBy(jobNumber)
                .orderByProcessInstanceEndTime().desc()
                .list();

        List<Map<String,Object>> list = new ArrayList<>();
        for(HistoricProcessInstance hpi:historicProcessInstances){
            Map<String, Object> map = new HashMap<>();
            map.put("startUserId", hpi.getStartUserId());
            map.put("startTime", hpi.getStartTime());
            map.put("endTime", hpi.getEndTime());
            list.add(map);

            //查询审批结果：
            Map<String, Object> variableMap = new HashMap<>();
            List<HistoricVariableInstance> varInstanceList = historyService.createHistoricVariableInstanceQuery().processInstanceId(hpi.getId()).list();
            for(HistoricVariableInstance hvi:varInstanceList){
                variableMap.put(hvi.getVariableName(),hvi.getValue());
            }
            map.put("variables", variableMap);
            list.add(map);
        }
        return list;
    }
}
