
package com.easy.apt.processor;


import com.easy.apt.annotation.sp.Preferences;
import com.easy.apt.processor.sp.FinderGenerator;
import com.easy.apt.processor.sp.PreferenceGenerator;
import com.google.auto.service.AutoService;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;


@AutoService(Processor.class)
public class TreasureProcessor extends AbstractProcessor {

    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        mFiler = env.getFiler();
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(Preferences.class.getCanonicalName());
        return supportTypes;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            PreferenceGenerator generator = new PreferenceGenerator(mFiler);
            Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(Preferences.class);
            for (Element element : set) {
                if (element instanceof TypeElement) {
                    generator.setElement((TypeElement) element);
                    generator.generate();
                }
            }
            FinderGenerator finderGenerator = new FinderGenerator(mFiler, set);
            finderGenerator.generate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
