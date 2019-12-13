package com.bdilab.util;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.OutputStream;
import java.io.StringReader;

/**
 * @ClassName XMLUtil
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/10/30 15:31
 * @Version 1.0
 */
public class XMLUtil {
    private static DocumentBuilderFactory factory;
    private static DocumentBuilder builder;
    static {
        factory = DocumentBuilderFactory.newInstance();
        try{
            builder = factory.newDocumentBuilder();
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }

    }
//    //读取HDFS指定路径上的xml文件
//    public static Document readHDFS(String uri) throws Exception {
//        InputStream in = HDFSUtil.downloadFile(uri);
//
//        if (in==null){
//            System.out.println("the path:"+uri+" is null");
//            return null;
//        }
//        return builder.parse(in);
//    }
    //读取本地xml文件
    public static Document readLocalFile(String file)throws Exception{
        return builder.parse(new File(file));
    }
    //读取xml字符串
    public static Document readXMLString(String xml) throws Exception {
        InputSource inputSource = new InputSource(new StringReader(xml));
        return builder.parse(inputSource);
    }
    //上传xml文件
    public static void upLoadXMLFile(String xml, OutputStream out)throws Exception{
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer tf = factory.newTransformer();
        tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
        tf.setOutputProperty(OutputKeys.INDENT,"yes");
        tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4");
        Document dom = readXMLString(xml);
        tf.transform(new DOMSource(dom),new StreamResult(out));
    }

    public static void main(String[] args)throws Exception{
//        Element action = WorkflowNodes.actionNode("myAction","end",WorkflowNodes.mapreduceNode());
//        Document dom = WorkflowNodes.workflowNode("myWorkFlow",new ArrayList<Element>() {{add(action);}});
        //upLoadXMLFile("<workflow-app xmlns=\"uri:oozie:workflow:0.2\" name=\"java-main-wf\"><start to=\"java-node\"/><action name=\"java-node\"><java><job-tracker>${jobTracker}</job-tracker><name-node>${nameNode}</name-node><configuration><property><name>mapred.job.queue.name</name><value>${queueName}</value></property></configuration><main-class>org.apache.oozie.example.DemoJavaMain</main-class><arg>Hello</arg><arg>Oozie!</arg></java><ok to=\"end\"/><error to=\"fail\"/></action><kill name=\"fail\"><message>Java failed, error message[${wf:errorMessage(wf:lastErrorNode())}]</message></kill><end name=\"end\"/></workflow-app>\n",new FileOutputStream("C://workflow.xml"));
    }
}
