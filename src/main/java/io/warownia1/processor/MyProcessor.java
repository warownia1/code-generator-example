package io.warownia1.processor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import com.google.auto.service.AutoService;

@SupportedAnnotationTypes("io.warownia1.processor.AutoMain")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class MyProcessor extends AbstractProcessor {

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    // TODO Auto-generated method stub
    for (TypeElement annotation : annotations) {
      Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
      for (Element element : annotatedElements) {
        try {
          writeAutoMain((ExecutableElement) element);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
    return true;
  }

  private void writeAutoMain(ExecutableElement element) throws IOException {
    String qualClassName = ((TypeElement) element.getEnclosingElement())
        .getQualifiedName().toString();
    String packageName = qualClassName.substring(0, qualClassName.lastIndexOf('.'));
    String className = qualClassName.substring(packageName.length() + 1);
    String generatedClassName = className + "_AutoMain_" + element.getSimpleName();
    JavaFileObject builderFile = processingEnv.getFiler()
        .createSourceFile(packageName + "." + generatedClassName);
    try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
      out.format("package %1$s;%n", packageName);
      out.format("public class %1$s {%n", generatedClassName);
      out.println("public static void main(String[] args) {");
      out.format("%1$s object = new %1$s(args);", className);
      out.format("object.%1$s();", element.getSimpleName());
      out.println("}}");
    }
  }
}
