package cn.anony.config;

import cn.anony.annotations.ElementVersion;
import com.google.auto.service.AutoService;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * @author Cheng Yufei
 * @create 2024-02-17 16:25
 *
 * @AutoService @AutoService(Processor.class) :向javac注册自定义的注解处理器，
 *  * 这样，在javac编译时，才会调用到我们这个自定义的注解处理器方法。@AutoService这里主要是用来生成
 *  * META-INF/services/javax.annotation.processing.Processor文件的。如果不加上这个注解，那么，你需要自己进行手动配置进行注册(创建 resources/META-INF/services/javax.annotation.processing.Processor 文件)
 *
 **/
@AutoService(Processor.class)
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"cn.anony.annotations.ElementVersion"})
public class ElementAnnoConfig extends AbstractProcessor {

    /**
     * Messager接口提供注解处理器用来报告错误消息、警告和其他通知的方式
     */
    private Messager messager;

    private JavacTrees javacTrees;

    /**
     * 需要将${JAVA_HOME}/lib/tools.jar 添加到项目的classpath,IDE默认不加载这个依赖
     * TreeMaker创建语法树节点的所有方法，创建时会为创建出来的JCTree设置pos字段，
     * 所以必须用上下文相关的TreeMaker对象来创建语法树节点，而不能直接new语法树节点。
     */
    private TreeMaker treeMaker;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.javacTrees = JavacTrees.instance(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ElementVersion.class);
        for (Element element : elements) {
            ElementKind kind = element.getKind();
            messager.printMessage(Diagnostic.Kind.NOTE,">>>Element king"+kind.name());
            if (kind.isField()) {
                // JCVariableDecl为字段/变量定义语法树节点
                JCTree.JCVariableDecl variableDecl = (JCTree.JCVariableDecl) javacTrees.getTree(element);
                // 字段赋值
                variableDecl.init = treeMaker.Literal(getVersion());
                messager.printMessage(Diagnostic.Kind.NOTE,">>> 插入式注解赋值成功");
            }
        }
        return false;
    }

   /*
     也可通过注解实现：@SupportedAnnotationTypes({"cn.anony.annotations.ElementVersion"})
   @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> set = new HashSet<>();
        set.add(ElementVersion.class.getName()); // 支持解析的注解
        return set;
    }*/


    private String getVersion(){

        // 逻辑xxxx
        return " 缠绕所有对你的眷恋  ";
    }
}
