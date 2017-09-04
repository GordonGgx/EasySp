package com.ggx;

import com.ggx.annotation.BooleanKey;
import com.ggx.annotation.FloatKey;
import com.ggx.annotation.IntKey;
import com.ggx.annotation.LongKey;
import com.ggx.annotation.SharePreference;
import com.ggx.annotation.StringKey;
import com.ggx.annotation.Transient;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;

import static javax.lang.model.type.TypeKind.DECLARED;

/**
 * @author jerry.Guan
 *         created by 2017/9/2
 */
@SupportedAnnotationTypes({"com.ggx.annotation.SharePreference"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
public class BindCompiler1 extends AbstractProcessor{
    private static final ClassName CONTEXT=ClassName.get("android.content","Context");
    private static final ClassName INJECT=ClassName.get("com.ggx.sharepreference","Inject");
    private static final ClassName SP_MANAGER=ClassName.get("com.ggx.sharepreference","SpManager");
    private static final ClassName SP_MANAGER_EDITOR=ClassName.get("android.content.SharedPreferences","Editor");

    private static final String stringType="java.lang.String";
    private static final String floatType="java.lang.Float";
    private static final String longType="java.lang.Long";
    private static final String booleanType="java.lang.Boolean";
    private static final String intType="java.lang.Integer";

    private Filer filer;
    private Elements elementsUtil;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer=processingEnv.getFiler();
        elementsUtil=processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //get with @SharePreference marks Classes
        Set<? extends TypeElement> elements= ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(SharePreference.class));
        for (TypeElement element:elements){
            try {
                JavaFile file=getInjectClass(element,roundEnv);
                file.writeTo(filer);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * create a Inject class
     * <code>
     * public class Person$$Inject extends Inject<Person> {
     *   public Person$$Inject(Context context) {
     *      super(manager);
     *   }
     *
     *   @Override
     *   public Person read(Person person, SpManager manager) {
     *      return null;
     *   }
     *
     *   @Override
     *   public void editor(Person person, SharedPreferences.Editor editor) {
     *
     *   }
     * }
     * </code>
     * @param typeElement
     */
    private JavaFile getInjectClass(TypeElement typeElement,RoundEnvironment roundEnv) throws ClassNotFoundException {
        String className=typeElement.getSimpleName().toString();

        TypeSpec.Builder injectType=TypeSpec.classBuilder(className+"$$InjectSp")
                .addModifiers(Modifier.PUBLIC)
                .superclass(ParameterizedTypeName.get(INJECT,ClassName.get(typeElement.asType())));
        SharePreference sp=typeElement.getAnnotation(SharePreference.class);
        String spName=sp.name();
        if(isEmpty(spName)){
            spName=typeElement.asType().toString();
        }
        //add constructor
        MethodSpec.Builder constructor=MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CONTEXT,"context")
                .addStatement("super($N.getManager(context,$S))","com.ggx.sharepreference.SpManager",spName);
        injectType.addMethod(constructor.build());

        MethodSpec.Builder readMethod=MethodSpec.methodBuilder("read")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(typeElement.asType()))
                .addParameter(ClassName.get(typeElement.asType()),"obj")
                .addParameter(SP_MANAGER,"manager");
        MethodSpec.Builder editorMethod=MethodSpec.methodBuilder("editor")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(ClassName.get(typeElement.asType()),"obj")
                .addParameter(SP_MANAGER_EDITOR,"editor");

        //get all elements with current class
        for (Element e:typeElement.getEnclosedElements()){
            if (!(e instanceof VariableElement)){
                continue;
            }
            VariableElement element= (VariableElement) e;
            if(element.getAnnotation(Transient.class)!=null){
                continue;
            }
            //defalut all fields must be persistent
            String simpleName=element.getSimpleName().toString();
            System.out.println(element.asType().getKind()== DECLARED);
            switch (element.asType().getKind()){
                case DECLARED:
                    String type=element.asType().toString();
                    switch (type){
                        case stringType:
                            readMethod.addCode(stringCode(element));
                            editorMethod.addCode(stringEditor(element));
                            break;
                        case floatType:
                            readMethod.addCode(floatCode(element));
                            editorMethod.addCode(floatEditor(element));
                            break;
                        case intType:
                            readMethod.addCode(intCode(element));
                            editorMethod.addCode(intEditor(element));
                            break;
                        case longType:
                            readMethod.addCode(longCode(element));
                            editorMethod.addCode(longEditor(element));
                            break;
                        case booleanType:
                            readMethod.addCode(booleanCode(element));
                            editorMethod.addCode(booleanEditor(element,true));
                            break;
                    }
                    break;
                case INT:
                    readMethod.addCode(intCode(element));
                    editorMethod.addCode(intEditor(element));
                    break;
                case FLOAT:
                    readMethod.addCode(floatCode(element));
                    editorMethod.addCode(floatEditor(element));
                    break;
                case LONG:
                    readMethod.addCode(longCode(element));
                    editorMethod.addCode(longEditor(element));
                    break;
                case BOOLEAN:
                    readMethod.addCode(booleanCode(element));
                    editorMethod.addCode(booleanEditor(element,false));
                    break;
            }
        }
        readMethod.addStatement("return obj");
        injectType.addMethod(readMethod.build());
        injectType.addMethod(editorMethod.build());

        String packageName=elementsUtil.getPackageOf(typeElement).getQualifiedName().toString();
        return JavaFile.builder(packageName,injectType.build())
                .build();
    }

    private CodeBlock intCode(Element element){
        String simpleName=element.getSimpleName().toString();
        String name;
        int value=0;
        IntKey intKey=element.getAnnotation(IntKey.class);
        if(intKey!=null){
            name=intKey.name();
            value=intKey.value();
            if(isEmpty(name)){
                name=simpleName;
            }
        }else {
            name=simpleName;
        }
        if(element.getModifiers().contains(Modifier.PRIVATE)){
            return CodeBlock.of("obj.set$N(manager.getInt($S,$L));\n",
                    captureName(simpleName),name,value);
        }else {
            return CodeBlock.of("obj.$N=manager.getInt($S,$L);\n",
                    simpleName,name,value);
        }
    }

    private CodeBlock floatCode(Element element){
        String simpleName=element.getSimpleName().toString();
        String name;
        float value=0f;
        FloatKey key=element.getAnnotation(FloatKey.class);
        if(key!=null){
            name=key.name();
            value=key.value();
            if(isEmpty(name)){
                name=simpleName;
            }
        }else {
            name=simpleName;
        }
        if(element.getModifiers().contains(Modifier.PRIVATE)){
            return CodeBlock.of("obj.set$N(manager.getFloat($S,$Lf));\n",
                    captureName(simpleName),name,value);
        }else {
            return CodeBlock.of("obj.$N=manager.getFloat($S,$Lf);\n",
                    simpleName,name,value);
        }
    }

    private CodeBlock longCode(Element element){
        String simpleName=element.getSimpleName().toString();
        String name;
        long value=0;
        LongKey key=element.getAnnotation(LongKey.class);
        if(key!=null){
            name=key.name();
            value=key.value();
            if(isEmpty(name)){
                name=simpleName;
            }
        }else {
            name=simpleName;
        }
        if(element.getModifiers().contains(Modifier.PRIVATE)){
            return CodeBlock.of("obj.set$N(manager.getLong($S,$L));\n",
                    captureName(simpleName),name,value);
        }else {
            return CodeBlock.of("obj.$N=manager.getLong($S,$L);\n",
                    simpleName,name,value);
        }
    }

    private CodeBlock booleanCode(Element element){
        String simpleName=element.getSimpleName().toString();
        String name;
        boolean value=false;
        BooleanKey key=element.getAnnotation(BooleanKey.class);
        if(key!=null){
            name=key.name();
            value=key.value();
            if(isEmpty(name)){
                name=simpleName;
            }
        }else {
            name=simpleName;
        }
        if(element.getModifiers().contains(Modifier.PRIVATE)){
            return CodeBlock.of("obj.set$N(manager.getBoolean($S,"+value+"));\n",
                    captureName(simpleName),name);
        }else {
            return CodeBlock.of("obj.$N=manager.getBoolean($S,"+value+");\n",
                    simpleName,name);
        }
    }

    private CodeBlock stringCode(Element element){
        String simpleName=element.getSimpleName().toString();
        String name;
        String value=null;
        StringKey key=element.getAnnotation(StringKey.class);
        if(key!=null){
            name=key.name();
            value=key.value();
            if(isEmpty(name)){
                name=simpleName;
            }
        }else {
            name=simpleName;
        }
        if(element.getModifiers().contains(Modifier.PRIVATE)){
            return CodeBlock.of("obj.set$N(manager.getString($S,$S));\n",
                    captureName(simpleName),name,isEmpty(value)?null:value);
        }else {
            return CodeBlock.of("obj.$N=manager.getString($S,$S);\n",
                    simpleName,name,isEmpty(value)?null:value);
        }
    }


    private CodeBlock intEditor(Element element){
        String simpleName=element.getSimpleName().toString();
        String name;
        IntKey intKey=element.getAnnotation(IntKey.class);
        if(intKey!=null){
            name=intKey.name();
            if(isEmpty(name)){
                name=simpleName;
            }
        }else {
            name=simpleName;
        }
        if(element.getModifiers().contains(Modifier.PRIVATE)){
            return CodeBlock.of("editor.putInt($S,obj.get$N());\n",
                    name,captureName(simpleName));
        }else {
            return CodeBlock.of("editor.putInt($S,obj.$N);\n",
                    name,simpleName);
        }
    }

    private CodeBlock floatEditor(Element element){
        String simpleName=element.getSimpleName().toString();
        String name;
        FloatKey key=element.getAnnotation(FloatKey.class);
        if(key!=null){
            name=key.name();
            if(isEmpty(name)){
                name=simpleName;
            }
        }else {
            name=simpleName;
        }
        if(element.getModifiers().contains(Modifier.PRIVATE)){
            return CodeBlock.of("editor.putFloat($S,obj.get$N());\n",
                    name,captureName(simpleName));
        }else {
            return CodeBlock.of("editor.putFloat($S,obj.$N);\n",
                    name,simpleName);
        }
    }

    private CodeBlock longEditor(Element element){
        String simpleName=element.getSimpleName().toString();
        String name;
        LongKey key=element.getAnnotation(LongKey.class);
        if(key!=null){
            name=key.name();
            if(isEmpty(name)){
                name=simpleName;
            }
        }else {
            name=simpleName;
        }
        if(element.getModifiers().contains(Modifier.PRIVATE)){
            return CodeBlock.of("editor.putLong($S,obj.get$N());\n",
                    name,captureName(simpleName));
        }else {
            return CodeBlock.of("editor.putLong($S,obj.$N);\n",
                    name,simpleName);
        }
    }

    private CodeBlock booleanEditor(Element element,boolean declared){
        String simpleName=element.getSimpleName().toString();
        String name;
        BooleanKey key=element.getAnnotation(BooleanKey.class);
        if(key!=null){
            name=key.name();
            if(isEmpty(name)){
                name=simpleName;
            }
        }else {
            name=simpleName;
        }
        if(element.getModifiers().contains(Modifier.PRIVATE)){
            if(declared){
                return CodeBlock.of("editor.putBoolean($S,obj.get$N());\n",
                        name,captureName(simpleName));
            }else {
                return CodeBlock.of("editor.putBoolean($S,obj.is$N());\n",
                        name,captureName(simpleName));
            }
        }else {
            return CodeBlock.of("editor.putBoolean($S,obj.$N);\n",
                    name,simpleName);
        }
    }

    private CodeBlock stringEditor(Element element){
        String simpleName=element.getSimpleName().toString();
        String name;
        StringKey key=element.getAnnotation(StringKey.class);
        if(key!=null){
            name=key.name();
            if(isEmpty(name)){
                name=simpleName;
            }
        }else {
            name=simpleName;
        }

        if(element.getModifiers().contains(Modifier.PRIVATE)){
            return CodeBlock.of("editor.putString($S,obj.get$N());\n",
                    name,captureName(simpleName));
        }else {
            return CodeBlock.of("editor.putString($S,obj.$N);\n",
                    name,simpleName);
        }
    }

    private static String captureName(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);

    }
    private static boolean isEmpty(String msg){
        return msg == null || msg.trim().length() == 0;
    }
}
