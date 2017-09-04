//package com.ggx;
//
//import com.ggx.annotation.BooleanKey;
//import com.ggx.annotation.FloatKey;
//import com.ggx.annotation.IntKey;
//import com.ggx.annotation.LongKey;
//import com.ggx.annotation.SharePreference;
//import com.ggx.annotation.StringKey;
//import com.google.auto.service.AutoService;
//import com.squareup.javapoet.ClassName;
//import com.squareup.javapoet.JavaFile;
//import com.squareup.javapoet.MethodSpec;
//import com.squareup.javapoet.ParameterizedTypeName;
//import com.squareup.javapoet.TypeSpec;
//
//import java.io.IOException;
//import java.util.Set;
//
//import javax.annotation.processing.AbstractProcessor;
//import javax.annotation.processing.Filer;
//import javax.annotation.processing.ProcessingEnvironment;
//import javax.annotation.processing.Processor;
//import javax.annotation.processing.RoundEnvironment;
//import javax.annotation.processing.SupportedAnnotationTypes;
//import javax.annotation.processing.SupportedSourceVersion;
//import javax.lang.model.SourceVersion;
//import javax.lang.model.element.Element;
//import javax.lang.model.element.Modifier;
//import javax.lang.model.element.TypeElement;
//import javax.lang.model.element.VariableElement;
//import javax.lang.model.util.ElementFilter;
//import javax.lang.model.util.Elements;
//
///**
// * @author jerry.Guan
// *         created by 2017/9/2
// */
//@SupportedAnnotationTypes({"com.ggx.annotation.SharePreference"})
//@SupportedSourceVersion(SourceVersion.RELEASE_7)
//@AutoService(Processor.class)
//public class BindCompiler extends AbstractProcessor{
//    private static final ClassName CONTEXT=ClassName.get("android.content","Context");
//    private static final ClassName INJECT=ClassName.get("com.ggx.sharepreference","Inject");
//    private static final ClassName SP_MANAGER=ClassName.get("com.ggx.sharepreference","SpManager");
//    private static final ClassName SP_MANAGER_EDITOR=ClassName.get("android.content.SharedPreferences","Editor");
//
//    private Filer filer;
//    private Elements elementsUtil;
//
//    @Override
//    public synchronized void init(ProcessingEnvironment processingEnv) {
//        super.init(processingEnv);
//        filer=processingEnv.getFiler();
//        elementsUtil=processingEnv.getElementUtils();
//    }
//
////    @Override
////    public Set<String> getSupportedAnnotationTypes() {
////        Set<String> set= new LinkedHashSet<>();
////        set.add(SharePreference.class.getCanonicalName());
////        return set;
////    }
//
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        //get with @SharePreference marks Classes
//        Set<? extends TypeElement> elements= ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(SharePreference.class));
//        for (TypeElement element:elements){
//            try {
//                JavaFile file=getInjectClass(element,roundEnv);
//                file.writeTo(filer);
//            } catch (ClassNotFoundException | IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return true;
//    }
//
//    /**
//     * create a Inject class
//     * <code>
//     * public class Person$$Inject extends Inject<Person> {
//     *   public Person$$Inject(Context context) {
//     *      super(manager);
//     *   }
//     *
//     *   @Override
//     *   public Person read(Person person, SpManager manager) {
//     *      return null;
//     *   }
//     *
//     *   @Override
//     *   public void editor(Person person, SharedPreferences.Editor editor) {
//     *
//     *   }
//     * }
//     * </code>
//     * @param typeElement
//     */
//    private JavaFile getInjectClass(TypeElement typeElement,RoundEnvironment roundEnv) throws ClassNotFoundException {
//        String className=typeElement.getSimpleName().toString();
//
//        TypeSpec.Builder injectType=TypeSpec.classBuilder(className+"$$InjectSp")
//                .addModifiers(Modifier.PUBLIC)
//                .superclass(ParameterizedTypeName.get(INJECT,ClassName.get(typeElement.asType())));
//        SharePreference sp=typeElement.getAnnotation(SharePreference.class);
//        String spName=sp.name();
//        if(isEmpty(spName)){
//            spName=typeElement.asType().toString();
//        }
//        //get all elements with current class
////        for (Element e:typeElement.getEnclosedElements()){
////            System.out.println(e.getSimpleName());
////        }
//        //add constructor
//        MethodSpec.Builder constructor=MethodSpec.constructorBuilder()
//                .addModifiers(Modifier.PUBLIC)
//                .addParameter(CONTEXT,"context")
//                .addStatement("super($N.getManager(context,$S))","com.ggx.sharepreference.SpManager",spName);
//        injectType.addMethod(constructor.build());
//        injectType.addMethod(getReadMethod(typeElement,roundEnv));
//        injectType.addMethod(getEditorMathod(typeElement,roundEnv));
//
//        String packageName=elementsUtil.getPackageOf(typeElement).getQualifiedName().toString();
//        return JavaFile.builder(packageName,injectType.build())
//                .build();
//    }
//
//    private static String captureName(String name) {
//        char[] cs=name.toCharArray();
//        cs[0]-=32;
//        return String.valueOf(cs);
//
//    }
//    private static boolean isEmpty(String msg){
//        return msg == null || msg.trim().length() == 0;
//    }
//
//    private MethodSpec getReadMethod(TypeElement typeElement,RoundEnvironment roundEnv){
//        MethodSpec.Builder readMethod=MethodSpec.methodBuilder("read")
//                .addModifiers(Modifier.PUBLIC)
//                .returns(ClassName.get(typeElement.asType()))
//                .addParameter(ClassName.get(typeElement.asType()),"obj")
//                .addParameter(SP_MANAGER,"manager");
//        Set<VariableElement> intFields=ElementFilter.fieldsIn(roundEnv.getElementsAnnotatedWith(IntKey.class));
//        for (VariableElement element:intFields){
//            IntKey key=element.getAnnotation(IntKey.class);
//            String name=key.name();
//            int value=key.value();
//            if(!typeElement.asType().equals(element.getEnclosingElement().asType())){
//                continue;
//            }
//            String simpleName=element.getSimpleName().toString();
//            String spKey;
//            if(isEmpty(name)){
//                spKey=simpleName;
//            }else {
//                spKey=name;
//            }
//            if(element.getModifiers().contains(Modifier.PRIVATE)){
//                readMethod.addStatement("obj.set$N(manager.getInt($S,$L))",
//                        captureName(simpleName),spKey,value);
//            }else {
//                readMethod.addStatement("obj.$N=manager.getInt($S,$L)",
//                        simpleName,spKey,value);
//            }
//        }
//        Set<VariableElement> stringFiles=ElementFilter.fieldsIn(roundEnv.getElementsAnnotatedWith(StringKey.class));
//        for (VariableElement element:stringFiles){
//            StringKey key=element.getAnnotation(StringKey.class);
//            String name=key.name();
//            String value=key.value();
//            if(!typeElement.asType().equals(element.getEnclosingElement().asType())){
//                continue;
//            }
//            String simpleName=element.getSimpleName().toString();
//            String spKey;
//            if(isEmpty(name)){
//                spKey=simpleName;
//            }else {
//                spKey=name;
//            }
//            if(element.getModifiers().contains(Modifier.PRIVATE)){
//                readMethod.addStatement("obj.set$N(manager.getString($S,$S))",
//                        captureName(simpleName),spKey,isEmpty(value)?null:value);
//            }else {
//                readMethod.addStatement("obj.$N=manager.getString($S,$S)",
//                        simpleName,spKey,isEmpty(value)?null:value);
//            }
//        }
//        Set<VariableElement> longFields=ElementFilter.fieldsIn(roundEnv.getElementsAnnotatedWith(LongKey.class));
//        for (VariableElement element:longFields){
//            LongKey key=element.getAnnotation(LongKey.class);
//            String name=key.name();
//            long value=key.value();
//            if(!typeElement.asType().equals(element.getEnclosingElement().asType())){
//                continue;
//            }
//            String simpleName=element.getSimpleName().toString();
//            String spKey;
//            if(isEmpty(name)){
//                spKey=simpleName;
//            }else {
//                spKey=name;
//            }
//            if(element.getModifiers().contains(Modifier.PRIVATE)){
//                readMethod.addStatement("obj.set$N(manager.getLong($S,$L))",
//                        captureName(simpleName),spKey,value);
//            }else {
//                readMethod.addStatement("obj.$N=manager.getLong($S,$L)",
//                        simpleName,spKey,value);
//            }
//        }
//        Set<VariableElement> floatFields=ElementFilter.fieldsIn(roundEnv.getElementsAnnotatedWith(FloatKey.class));
//        for (VariableElement element:floatFields){
//            FloatKey key=element.getAnnotation(FloatKey.class);
//            String name=key.name();
//            float value=key.value();
//            if(!typeElement.asType().equals(element.getEnclosingElement().asType())){
//                continue;
//            }
//            String simpleName=element.getSimpleName().toString();
//            String spKey;
//            if(isEmpty(name)){
//                spKey=simpleName;
//            }else {
//                spKey=name;
//            }
//            if(element.getModifiers().contains(Modifier.PRIVATE)){
//                readMethod.addStatement("obj.set$N(manager.getFloat($S,$Lf))",
//                        captureName(simpleName),spKey,value);
//            }else {
//                readMethod.addStatement("obj.$N=manager.getFloat($S,$Lf)",
//                        simpleName,spKey,value);
//            }
//        }
//
//        Set<VariableElement> booleanField=ElementFilter.fieldsIn(roundEnv.getElementsAnnotatedWith(BooleanKey.class));
//        for (VariableElement element:booleanField){
//            BooleanKey key=element.getAnnotation(BooleanKey.class);
//            String name=key.name();
//            boolean value=key.value();
//            if(!typeElement.asType().equals(element.getEnclosingElement().asType())){
//                continue;
//            }
//            String simpleName=element.getSimpleName().toString();
//            String spKey;
//            if(isEmpty(name)){
//                spKey=simpleName;
//            }else {
//                spKey=name;
//            }
//            if(element.getModifiers().contains(Modifier.PRIVATE)){
//                readMethod.addStatement("obj.set$N(manager.getBoolean($S,"+value+"))",
//                        captureName(simpleName),spKey);
//            }else {
//                readMethod.addStatement("obj.$N=manager.getBoolean($S,"+value+")",
//                        simpleName,spKey);
//            }
//        }
//
//        readMethod.addStatement("return obj");
//        return readMethod.build();
//    }
//
//    private MethodSpec getEditorMathod(TypeElement typeElement,RoundEnvironment roundEnv){
//
//        MethodSpec.Builder editorMethod=MethodSpec.methodBuilder("editor")
//                .addModifiers(Modifier.PUBLIC)
//                .returns(void.class)
//                .addParameter(ClassName.get(typeElement.asType()),"obj")
//                .addParameter(SP_MANAGER_EDITOR,"editor");
//        Set<VariableElement> intFields=ElementFilter.fieldsIn(roundEnv.getElementsAnnotatedWith(IntKey.class));
//        for (VariableElement element:intFields){
//            IntKey key=element.getAnnotation(IntKey.class);
//            String name=key.name();
//            if(!typeElement.asType().equals(element.getEnclosingElement().asType())){
//                continue;
//            }
//            String simpleName=element.getSimpleName().toString();
//            String spKey;
//            if(isEmpty(name)){
//                spKey=simpleName;
//            }else {
//                spKey=name;
//            }
//            if(element.getModifiers().contains(Modifier.PRIVATE)){
//                editorMethod.addStatement("editor.putInt($S,obj.get$N())",
//                        spKey,captureName(simpleName));
//            }else {
//                editorMethod.addStatement("editor.putInt($S,obj.$N)",
//                        spKey,simpleName);
//            }
//        }
//        Set<VariableElement> stringFiles=ElementFilter.fieldsIn(roundEnv.getElementsAnnotatedWith(StringKey.class));
//        for (VariableElement element:stringFiles){
//            StringKey key=element.getAnnotation(StringKey.class);
//            String name=key.name();
//            if(!typeElement.asType().equals(element.getEnclosingElement().asType())){
//                continue;
//            }
//            String simpleName=element.getSimpleName().toString();
//            String spKey;
//            if(isEmpty(name)){
//                spKey=simpleName;
//            }else {
//                spKey=name;
//            }
//            if(element.getModifiers().contains(Modifier.PRIVATE)){
//                editorMethod.addStatement("editor.putString($S,obj.get$N())",
//                        spKey,captureName(simpleName));
//            }else {
//                editorMethod.addStatement("editor.putString($S,obj.$N)",
//                        spKey,simpleName);
//            }
//        }
//        Set<VariableElement> longFields=ElementFilter.fieldsIn(roundEnv.getElementsAnnotatedWith(LongKey.class));
//        for (VariableElement element:longFields){
//            LongKey key=element.getAnnotation(LongKey.class);
//            String name=key.name();
//            if(!typeElement.asType().equals(element.getEnclosingElement().asType())){
//                continue;
//            }
//            String simpleName=element.getSimpleName().toString();
//            String spKey;
//            if(isEmpty(name)){
//                spKey=simpleName;
//            }else {
//                spKey=name;
//            }
//            if(element.getModifiers().contains(Modifier.PRIVATE)){
//                editorMethod.addStatement("editor.putLong($S,obj.get$N())",
//                        spKey,captureName(simpleName));
//            }else {
//                editorMethod.addStatement("editor.putLong($S,obj.$N)",
//                        spKey,simpleName);
//            }
//        }
//        Set<VariableElement> floatFields=ElementFilter.fieldsIn(roundEnv.getElementsAnnotatedWith(FloatKey.class));
//        for (VariableElement element:floatFields){
//            FloatKey key=element.getAnnotation(FloatKey.class);
//            String name=key.name();
//            if(!typeElement.asType().equals(element.getEnclosingElement().asType())){
//                continue;
//            }
//            String simpleName=element.getSimpleName().toString();
//            String spKey;
//            if(isEmpty(name)){
//                spKey=simpleName;
//            }else {
//                spKey=name;
//            }
//            if(element.getModifiers().contains(Modifier.PRIVATE)){
//                editorMethod.addStatement("editor.putFloat($S,obj.get$N())",
//                        spKey,captureName(simpleName));
//            }else {
//                editorMethod.addStatement("editor.putFloat($S,obj.$N)",
//                        spKey,simpleName);
//            }
//        }
//
//        Set<VariableElement> booleanField=ElementFilter.fieldsIn(roundEnv.getElementsAnnotatedWith(BooleanKey.class));
//        for (VariableElement element:booleanField){
//            BooleanKey key=element.getAnnotation(BooleanKey.class);
//            String name=key.name();
//            if(!typeElement.asType().equals(element.getEnclosingElement().asType())){
//                continue;
//            }
//            String simpleName=element.getSimpleName().toString();
//            String spKey;
//            if(isEmpty(name)){
//                spKey=simpleName;
//            }else {
//                spKey=name;
//            }
//            if(element.getModifiers().contains(Modifier.PRIVATE)){
//                editorMethod.addStatement("editor.putBoolean($S,obj.is$N())",
//                        spKey,captureName(simpleName));
//            }else {
//                editorMethod.addStatement("editor.putBoolean($S,obj.$N)",
//                        spKey,simpleName);
//            }
//        }
//
//        return editorMethod.build();
//    }
//}
