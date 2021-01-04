package com.example.pipeline_compiler;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.tools.JavaFileObject;

public class Utils {

    static String getPackageName(ProcessingEnvironment processingEnv, Element classElement) {
        PackageElement packageOf = processingEnv.getElementUtils().getPackageOf(classElement);
        Name qualifiedName = packageOf.getQualifiedName();
        return qualifiedName.toString();
    }

    static boolean generateSourceFile(Filer mFiler, StringBuffer source, String fileName) {
        Writer writer = null;
        try {
            JavaFileObject sourceFile = mFiler.createSourceFile(fileName);
            writer = sourceFile.openWriter();
            writer.write(source.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
