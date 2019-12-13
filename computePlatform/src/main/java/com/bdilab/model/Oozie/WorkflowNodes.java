package com.bdilab.model.Oozie;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName WorkflowNodes
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/10/30 21:57
 * @Version 1.0
 */
public class WorkflowNodes {
    private static Document dom;
    static {
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            dom = builder.newDocument();
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }
    }
    public static Document getDom(){
        return dom;
    }
    public static Element startNode(String actionName){
        Element start = dom.createElement("start");
        start.setAttribute("to",actionName);
        return start;
    }
    public static Element endNode(){
        Element end = dom.createElement("end");
        end.setAttribute("name","end");
        return end;
    }
    public static Element killNode(){
        Element kill = dom.createElement("kill");
        Element message = dom.createElement("message");
        kill.setAttribute("name","fail");
        message.setTextContent("job failed,error message[${wf:errorMessage(wf:lastErrorNode())}]");
        kill.appendChild(message);
        return kill;
    }
    public static Element forkNode(String forkName,List<String> items){
        Element fork = dom.createElement("fork");
        fork.setAttribute("name",forkName);
        for (String item:items){
            Element path = dom.createElement("path");
            path.setAttribute("start",item);
            fork.appendChild(path);
        }
        return fork;
    }
    public static Element joinNode(String joinName,String nextNode){
        Element join = dom.createElement("join");
        join.setAttribute("name",joinName);
        join.setAttribute("to",nextNode);
        return join;
    }
    public static Element actionNode(String actionName,String okNode,String errorNode,Element action){
        Element act = dom.createElement("action");
        Element ok = dom.createElement("ok");
        Element error = dom.createElement("error");
        act.setAttribute("name",actionName);
        ok.setAttribute("to",okNode);
        error.setAttribute("to",errorNode);
        act.appendChild(action);
        act.appendChild(ok);
        act.appendChild(error);
        return act;
    }
    public static Element workflowNode(String wfname){
        Element workflow = dom.createElement("workflow-app");
        workflow.setAttribute("xmlns","uri:oozie:workflow:0.2");
        workflow.setAttribute("name",wfname);
        return workflow;
    }
    public static Element jobTrackerNode(){
        Element jobtracker = dom.createElement("job-tracker");
        jobtracker.setTextContent("${jobTracker}");
        return jobtracker;
    }
    public static Element nameNode(){
        Element namenode = dom.createElement("name-node");
        namenode.setTextContent("${nameNode}");
        return namenode;
    }
    public static Element prepareNode(){
        Element prepare = dom.createElement("prepare");
        Element delete = dom.createElement("delete");
        delete.setAttribute("path","${nameNode}/user/${wf:user()}/${examplesRoot}/output-data/${outputDir}");
        Element mkdir = dom.createElement("mkdir");
        mkdir.setAttribute("path","${nameNode}/user/${wf:user()}/${examplesRoot}/output-data");
        prepare.appendChild(delete);
        prepare.appendChild(mkdir);
        return prepare;
    }
    public static Element confNode(Map<String,String> properties){
        Element configuration = dom.createElement("configuration");
        for (String key:properties.keySet()){
            Element property = dom.createElement("property");
            Element name = dom.createElement("name");
            Element value = dom.createElement("value");
            name.setTextContent(key);
            value.setTextContent(properties.get(key));
            property.appendChild(name);
            property.appendChild(value);
            configuration.appendChild(property);
        }
        return configuration;
    }
    public static Element streamingNode(Map<String,List<String>> streamingConf){
        Element streaming = dom.createElement("streaming");
        Element mapper = dom.createElement("mapper");
        mapper.setTextContent(streamingConf.get("mapper").get(0));
        streaming.appendChild(mapper);
        Element reducer = dom.createElement("reducer");
        reducer.setTextContent(streamingConf.get("reducer").get(0));
        streaming.appendChild(reducer);
        Element recordReader = dom.createElement("record-reader");
        recordReader.setTextContent(streamingConf.get("record-reader").get(0));
        streaming.appendChild(recordReader);
        for (String s:streamingConf.get("record-reader-mapping")){
            Element mapping = dom.createElement("record-reader-mapping");
            mapping.setTextContent(s);
            streaming.appendChild(mapping);
        }
        for (String s:streamingConf.get("env")){
            Element env = dom.createElement("env");
            env.setTextContent(s);
            streaming.appendChild(env);
        }
        return streaming;
    }
    public static Element pipesNode(Map<String,String> pipesConf){
        Element pipes = dom.createElement("pipes");
        Element map = dom.createElement("map");
        map.setTextContent(pipesConf.get("map"));
        pipes.appendChild(map);
        Element reduce = dom.createElement("reduce");
        reduce.setTextContent(pipesConf.get("reduce"));
        pipes.appendChild(reduce);
        Element inputformat = dom.createElement("inputformat");
        inputformat.setTextContent(pipesConf.get("inputformat"));
        pipes.appendChild(inputformat);
        Element partitioner = dom.createElement("partitioner");
        partitioner.setTextContent(pipesConf.get("partitioner"));
        pipes.appendChild(partitioner);
        Element writer = dom.createElement("writer");
        writer.setTextContent(pipesConf.get("writer"));
        pipes.appendChild(writer);
        Element program = dom.createElement("program");
        program.setTextContent(pipesConf.get("program"));
        pipes.appendChild(program);
        return pipes;
    }
    public static Element mapreduceNode(Map<String,List<String>> streamingConf,Map<String,String> pipesConf,
                                        Map<String,String> conf,List<String> files,List<String> archives){
        Element mapreduce = dom.createElement("map-reduce");
        mapreduce.appendChild(jobTrackerNode());
        mapreduce.appendChild(nameNode());
        mapreduce.appendChild(prepareNode());
        if (streamingConf!=null){
            mapreduce.appendChild(streamingNode(streamingConf));
        }
        if (pipesConf!=null){
            mapreduce.appendChild(pipesNode(pipesConf));
        }
        if (conf!=null){
            mapreduce.appendChild(confNode(conf));
        }
        if (files!=null){
            for (String f:files){
                Element file = dom.createElement("file");
                file.setTextContent(f);
                mapreduce.appendChild(file);
            }
        }
        if (archives!=null){
            for (String f:archives){
                Element archive = dom.createElement("archive");
                archive.setTextContent(f);
                mapreduce.appendChild(archive);
            }
        }
        return mapreduce;
    }
    public static Element hiveNode(Map<String,String> conf,String scriptName,List<String> params){
        Element hive = dom.createElement("hive");
        hive.setAttribute("xmlns","uri:oozie:hive-action:0.2");
        hive.appendChild(jobTrackerNode());
        hive.appendChild(nameNode());
        hive.appendChild(prepareNode());
        if (conf!=null){
            hive.appendChild(confNode(conf));
        }
        if (scriptName!=null){
            Element script = dom.createElement("script");
            script.setTextContent(scriptName);
            hive.appendChild(script);
        }
        if (params!=null){
            for (String p:params){
                Element param = dom.createElement("param");
                param.setTextContent(p);
                hive.appendChild(param);
            }
        }
        return hive;
    }
    public static Element sqoopNode(Map<String,String> conf,String sqoopCommand,List<String> files){
        Element sqoop = dom.createElement("sqoop");
        sqoop.setAttribute("xmlns","uri:oozie:sqoop-action:0.2");
        sqoop.appendChild(jobTrackerNode());
        sqoop.appendChild(nameNode());
        sqoop.appendChild(prepareNode());
        if (conf!=null){
            sqoop.appendChild(confNode(conf));
        }
        if (sqoopCommand!=null){
            Element command = dom.createElement("commend");
            command.setTextContent(sqoopCommand);
            sqoop.appendChild(command);
        }
        if (files!=null){
            for (String f:files){
                Element file = dom.createElement("file");
                file.setTextContent(f);
                sqoop.appendChild(file);
            }
        }
        return sqoop;
    }
    public static Element pigNode(String jobXMLFile,Map<String,String> conf,String scriptFile,
                                  List<String> params,List<String> arguments,List<String> files,
                                  List<String> archives){
        Element pig = dom.createElement("pig");
        pig.appendChild(jobTrackerNode());
        pig.appendChild(nameNode());
        pig.appendChild(prepareNode());
        if (jobXMLFile!=null){
            Element jobxml = dom.createElement("job-xml");
            jobxml.setTextContent(jobXMLFile);
            pig.appendChild(jobxml);
        }
        if (conf!=null){
            pig.appendChild(confNode(conf));
        }
        if (scriptFile!=null){
            Element script = dom.createElement("script");
            script.setTextContent(scriptFile);
            pig.appendChild(script);
        }
        if (params!=null){
            for (String p:params){
                Element param = dom.createElement("param");
                param.setTextContent(p);
                pig.appendChild(param);
            }
        }
        if (arguments!=null){
            for (String f:arguments){
                Element argument = dom.createElement("argument");
                argument.setTextContent(f);
                pig.appendChild(argument);
            }
        }
        if (files!=null){
            for (String f:files){
                Element file = dom.createElement("file");
                file.setTextContent(f);
                pig.appendChild(file);
            }
        }
        if (archives!=null){
            for (String f:archives){
                Element archive = dom.createElement("archive");
                archive.setTextContent(f);
                pig.appendChild(archive);
            }
        }
        return pig;
    }
    public static Element fsNode(String deletePath,String mkdirPath,String moveSourcePath,String moveTargetPath,
                                 String chmodPath,String permissions,String touchzPath){
        Element fs = dom.createElement("fs");
        if (deletePath!=null){
            Element delete = dom.createElement("delete");
            delete.setAttribute("path",deletePath);
            fs.appendChild(delete);
        }
        if (mkdirPath!=null){
            Element mkdir = dom.createElement("mkdir");
            mkdir.setAttribute("path",mkdirPath);
            fs.appendChild(mkdir);
        }
        if (moveSourcePath!=null && moveTargetPath!=null){
            Element move = dom.createElement("move");
            move.setAttribute("source",moveSourcePath);
            move.setAttribute("target",moveTargetPath);
            fs.appendChild(move);
        }
        if (chmodPath!=null && permissions!=null){
            Element chmod = dom.createElement("chmod");
            chmod.setAttribute("path",chmodPath);
            chmod.setAttribute("permissions",permissions);
            chmod.setAttribute("dir-files","false");
            fs.appendChild(chmod);
        }
        if (touchzPath!=null){
            Element touchz = dom.createElement("touchz");
            touchz.setAttribute("path",touchzPath);
            fs.appendChild(touchz);
        }
        return fs;
    }
    public static Element sshNode(String hostName,String shellCommand,List<String> args){
        Element ssh = dom.createElement("ssh");
        if (hostName!=null){
            Element host = dom.createElement("host");
            host.setTextContent(hostName);
            ssh.appendChild(host);
        }
        if (shellCommand!=null){
            Element command = dom.createElement("command");
            command.setTextContent(shellCommand);
            ssh.appendChild(command);
        }
        if (args!=null){
            for (String f:args){
                Element argument = dom.createElement("args");
                argument.setTextContent(f);
                ssh.appendChild(argument);
            }
        }
        Element capture = dom.createElement("capture-output");
        ssh.appendChild(capture);
        return ssh;
    }
    public static Element javaNode(String jobXml,Map<String,String> conf,String mainClass,String javaOpts,
                                   List<String> args,List<String> files,List<String> archives){
        Element java = dom.createElement("java");
        java.appendChild(jobTrackerNode());
        java.appendChild(nameNode());
        java.appendChild(prepareNode());
        if (jobXml!=null){
            Element jobxml = dom.createElement("job-xml");
            jobxml.setTextContent(jobXml);
            java.appendChild(jobxml);
        }
        if (conf!=null){
            java.appendChild(confNode(conf));
        }
        if (mainClass!=null){
            Element mainclass = dom.createElement("main-class");
            mainclass.setTextContent(mainClass);
            java.appendChild(mainclass);
        }
        if (javaOpts!=null){
            Element javaopts = dom.createElement("java-opts");
            javaopts.setTextContent(javaOpts);
            java.appendChild(javaopts);
        }
        if (args!=null){
            for (String f:args){
                Element argument = dom.createElement("arg");
                argument.setTextContent(f);
                java.appendChild(argument);
            }
        }
        if (files!=null){
            for (String f:files){
                Element file = dom.createElement("file");
                file.setTextContent(f);
                java.appendChild(file);
            }
        }
        if (archives!=null){
            for (String f:archives){
                Element archive = dom.createElement("archive");
                archive.setTextContent(f);
                java.appendChild(archive);
            }
        }
        return java;
    }
    public static Element subWorkflowNode(String appPath,Map<String,String> conf){
        Element subWorkflow = dom.createElement("sub-workflow");
        if (appPath!=null){
            Element apppath = dom.createElement("app-path");
            apppath.setTextContent(appPath);
            subWorkflow.appendChild(apppath);
        }
        Element propConf = dom.createElement("propagate-configuration");
        subWorkflow.appendChild(propConf);
        if (conf!=null){
            subWorkflow.appendChild(confNode(conf));
        }
        return subWorkflow;
    }
    public static Element shellNode(Map<String,String> conf,String shellCommand,String argumentValue){
        Element shell = dom.createElement("shell");
        shell.setAttribute("xmlns","uri:oozie:shell-action:0.2");
        shell.appendChild(jobTrackerNode());
        shell.appendChild(nameNode());
        shell.appendChild(prepareNode());
        if (conf!=null){
            shell.appendChild(confNode(conf));
        }
        if (shellCommand!=null){
            Element exec = dom.createElement("exec");
            exec.setTextContent(shellCommand);
            shell.appendChild(exec);
        }
        if (argumentValue!=null){
            Element argument = dom.createElement("argument");
            argument.setTextContent(argumentValue);
            shell.appendChild(argument);
        }
        Element capture = dom.createElement("capture-output");
        shell.appendChild(capture);
        return shell;
    }
}
