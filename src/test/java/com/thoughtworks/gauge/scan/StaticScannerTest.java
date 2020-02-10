// Copyright 2015 ThoughtWorks, Inc.

// This file is part of Gauge-Java.

// This program is free software.
//
// It is dual-licensed under:
// 1) the GNU General Public License as published by the Free Software Foundation,
// either version 3 of the License, or (at your option) any later version;
// or
// 2) the Eclipse Public License v1.0.
//
// You can redistribute it and/or modify it under the terms of either license.
// We would then provide copied of each license in a separate .txt file with the name of the license as the title of the file.

package com.thoughtworks.gauge.scan;

import com.google.common.base.Charsets;
import com.thoughtworks.gauge.Util;
import com.thoughtworks.gauge.registry.StepRegistry;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Enclosed.class)
public class StaticScannerTest {

    private static final String STEP_TEXT = "new step";
    private static final String JAVA_IMPL_FILE = Util.workingDir() + File.separator + String.format("src%stest%sresources%stest%sfiles%sfoo.java", File.separator, File.separator, File.separator, File.separator, File.separator);
    private static final String KOTLIN_IMPL_FILE = Util.workingDir() + File.separator + String.format("src%stest%sresources%stest%sfiles%sfoo.kt", File.separator, File.separator, File.separator, File.separator, File.separator);

    public static class Java {
        @Test
        public void ShouldAddStepsFromFile() {
            StaticScanner staticScanner = new StaticScanner();
            String contents = staticScanner.readFile(JAVA_IMPL_FILE, Charsets.UTF_8);
            staticScanner.addStepsFromFileContents(JAVA_IMPL_FILE, contents);
            StepRegistry registry = staticScanner.getRegistry();
            assertTrue(registry.contains(STEP_TEXT));
        }

        @Test
        public void ShouldAddStepsWithConcatenatedAnnotationStep() {
            String contents = "public class StepTest {\n" +
                    "   @Step(\"This is \" + \"a step\")\n" +
                    "       public void newstep() {\n" +
                    "       assertThat(2).isEqualTo(2);\n" +
                    "   }\n" +
                    "}";
            StaticScanner staticScanner = new StaticScanner();
            staticScanner.addStepsFromFileContents(JAVA_IMPL_FILE, contents);
            StepRegistry registry = staticScanner.getRegistry();
            assertTrue(registry.contains("This is a step"));
        }

        @Test
        public void ShouldAddStepsAliases() {
            String contents = "public class StepTest {\n" +
                    "   @Step({\"This is \" + \"a step\", \"new step\"})\n" +
                    "       public void newstep() {\n" +
                    "       assertThat(2).isEqualTo(2);\n" +
                    "   }\n" +
                    "}";
            StaticScanner staticScanner = new StaticScanner();
            staticScanner.addStepsFromFileContents(JAVA_IMPL_FILE, contents);
            StepRegistry registry = staticScanner.getRegistry();
            assertTrue(registry.contains("This is a step"));
            assertTrue(registry.contains("new step"));
            assertTrue(registry.get("This is a step").getAliases().contains("new step"));
            assertTrue(registry.get("new step").getAliases().contains("This is a step"));
        }

        @Test
        public void ShouldRemoveStepsFromFile() {
            StaticScanner staticScanner = new StaticScanner();
            String contents = staticScanner.readFile(JAVA_IMPL_FILE, Charsets.UTF_8);
            staticScanner.addStepsFromFileContents(JAVA_IMPL_FILE, contents);
            StepRegistry registry = staticScanner.getRegistry();

            assertTrue(registry.contains(STEP_TEXT));
            registry.removeSteps(JAVA_IMPL_FILE);
            assertFalse(registry.contains(STEP_TEXT));
        }

        @Test
        public void ShouldReloadStepsFromFile() {
            StaticScanner staticScanner = new StaticScanner();
            String contents = staticScanner.readFile(JAVA_IMPL_FILE, Charsets.UTF_8);
            staticScanner.addStepsFromFileContents(JAVA_IMPL_FILE, contents);
            StepRegistry registry = staticScanner.getRegistry();
            registry.get(STEP_TEXT).setHasAlias(true);

            assertTrue(registry.contains(STEP_TEXT));
            assertTrue(registry.get(STEP_TEXT).getHasAlias());

            staticScanner.reloadSteps(JAVA_IMPL_FILE, contents);

            assertTrue(registry.contains(STEP_TEXT));
            assertFalse(registry.get(STEP_TEXT).getHasAlias());
        }
    }

    public static class Kotlin {
        @Test
        public void ShouldAddStepsFromFile() {
            StaticScanner staticScanner = new StaticScanner();
            String contents = staticScanner.readFile(KOTLIN_IMPL_FILE, Charsets.UTF_8);
            staticScanner.addStepsFromFileContents(KOTLIN_IMPL_FILE, contents);
            StepRegistry registry = staticScanner.getRegistry();
            assertTrue(registry.contains(STEP_TEXT));
        }

        @Test
        public void ShouldAddStepsWithConcatenatedAnnotationStep() {
            String contents = "class StepTest {\n" +
                    "   @Step(\"This is \" + \"a step\")\n" +
                    "       fun newstep() {\n" +
                    "       assertThat(2).isEqualTo(2)\n" +
                    "   }\n" +
                    "}";
            StaticScanner staticScanner = new StaticScanner();
            staticScanner.addStepsFromFileContents(KOTLIN_IMPL_FILE, contents);
            StepRegistry registry = staticScanner.getRegistry();
            assertTrue(registry.contains("This is a step"));
        }

        @Test
        public void ShouldAddStepsAliases() {
            String contents = "class StepTest {\n" +
                    "   @Step(\"This is \" + \"a step\", \"new step\")\n" +
                    "       fun newstep() {\n" +
                    "       assertThat(2).isEqualTo(2)\n" +
                    "   }\n" +
                    "}";
            StaticScanner staticScanner = new StaticScanner();
            staticScanner.addStepsFromFileContents(KOTLIN_IMPL_FILE, contents);
            StepRegistry registry = staticScanner.getRegistry();
            assertTrue(registry.contains("This is a step"));
            assertTrue(registry.contains("new step"));
            assertTrue(registry.get("This is a step").getAliases().contains("new step"));
            assertTrue(registry.get("new step").getAliases().contains("This is a step"));
        }

        @Test
        public void ShouldRemoveStepsFromFile() {
            StaticScanner staticScanner = new StaticScanner();
            String contents = staticScanner.readFile(KOTLIN_IMPL_FILE, Charsets.UTF_8);
            staticScanner.addStepsFromFileContents(KOTLIN_IMPL_FILE, contents);
            StepRegistry registry = staticScanner.getRegistry();

            assertTrue(registry.contains(STEP_TEXT));
            registry.removeSteps(KOTLIN_IMPL_FILE);
            assertFalse(registry.contains(STEP_TEXT));
        }

        @Test
        public void ShouldReloadStepsFromFile() {
            StaticScanner staticScanner = new StaticScanner();
            String contents = staticScanner.readFile(KOTLIN_IMPL_FILE, Charsets.UTF_8);
            staticScanner.addStepsFromFileContents(KOTLIN_IMPL_FILE, contents);
            StepRegistry registry = staticScanner.getRegistry();
            registry.get(STEP_TEXT).setHasAlias(true);

            assertTrue(registry.contains(STEP_TEXT));
            assertTrue(registry.get(STEP_TEXT).getHasAlias());

            staticScanner.reloadSteps(KOTLIN_IMPL_FILE, contents);

            assertTrue(registry.contains(STEP_TEXT));
            assertFalse(registry.get(STEP_TEXT).getHasAlias());
        }
    }
}