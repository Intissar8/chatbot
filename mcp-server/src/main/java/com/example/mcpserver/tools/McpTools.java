package com.example.mcpserver.tools;

import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class McpTools {
    @McpTool(name = "getEmployee",description = "Get information about a given employee")//we use the description to explain to the LLM what's the tool for
    public Employee getEmployee(@McpArg(description = "the employee name") String name){
        return new Employee(name,12300,4);
    }

    @McpTool(description = "Get All Employees")
    public List<Employee> getAllEmployees(){
        return List.of(new Employee("Hassan",12300,4),
                new Employee("Mohamed",34000,1),
                new Employee("Imane",23000,10)
        );
    }
}

record Employee(String name, double salary,int seniority) {}

