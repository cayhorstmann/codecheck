package com.horstmann.codecheck;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class CodeCheckRunner extends BlockJUnit4ClassRunner {
	
	public CodeCheckRunner(final Class<?> klass) throws InitializationError  {
	      super(klass);
	}
	
	@Override
    protected List<FrameworkMethod> computeTestMethods() {
      // First, get the base list of tests
      final List<FrameworkMethod> allMethods = getTestClass()
            .getAnnotatedMethods(Test.class);
      if (allMethods == null || allMethods.size() == 0)
         return allMethods;

      // Filter the list down
      final List<FrameworkMethod> filteredMethods = new ArrayList<FrameworkMethod>(allMethods.size());
      for (final FrameworkMethod method : allMethods) {
         final Point customAnnointtation = method.getAnnotation(Point.class);
         if (customAnnointtation != null) {
            // Add to accepted test methods, if matching criteria met
            // For example `if(currentOs.equals(customAnnotation.OS()))`
            filteredMethods.add(method);
         } else {
            // If test method doesnt have the custom annotation, either add it to
            // the accepted methods, or not, depending on what the 'default' behavior
            // should be
            filteredMethods.add(method);
         }
      }

      return filteredMethods;
   }
}
