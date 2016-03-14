package com.ewerk.gradle.plugins.tasks

import com.ewerk.gradle.plugins.Jaxb2Plugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Task that does the actual generation stuff. Declares the Ant task and then runs it for all
 * configured {@link XjcTaskConfig} objects.
 *
 * @author holgerstolzenberg
 * @since 1.0.0
 */
class GenerateJaxb2Classes extends DefaultTask {
    private Set<XjcTaskConfig> xjcConfigs

    GenerateJaxb2Classes() {
        this.group = Jaxb2Plugin.TASK_GROUP
        xjcConfigs = project.extensions.jaxb2.xjc
        for (XjcTaskConfig theConfig : this.xjcConfigs) {
            this.outputs += theConfig.generatedSourcesDir
        }
    }

    @SuppressWarnings("GroovyUnusedDeclaration")
    @TaskAction
    def antXjc() {
        this.inputs += project.configurations.jaxb2.asPath

        ant.taskdef(
                name: 'xjc',
                classname: project.extensions.jaxb2.taskName,
                classpath: project.configurations.jaxb2.asPath)


        for (XjcTaskConfig theConfig : this.xjcConfigs) {
            ant.xjc(destdir: theConfig.generatedSourcesDir,
                    package: theConfig.basePackage,
                    schema: theConfig.schema)
        }
    }
}
