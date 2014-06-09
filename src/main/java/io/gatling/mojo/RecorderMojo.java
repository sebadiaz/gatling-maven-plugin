/**
 * Copyright 2011-2014 eBusiness Information, Groupe Excilys (www.ebusinessinformation.fr)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gatling.mojo;

import io.gatling.recorder.GatlingRecorder;
import io.gatling.recorder.config.RecorderPropertiesBuilder;
import io.gatling.recorder.controller.RecorderController;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import scala.Option;
import scala.io.Codec;
import scala.reflect.io.File;

import javax.swing.*;
import java.util.Properties;

/**
 * Mojo to run Gatling Recorder.
 *
 * @goal recorder
 * @phase integration-test
 * @description Gatling Recorder Plugin
 * @requiresDependencyResolution test
 */
public class RecorderMojo extends AbstractMojo {
    private static final String GATLING_HOME = "GATLING_HOME";

    /**
     * Local recorder port.
     *
     * @parameter property="gatling.recorder.localPort" default-value="0"
     * @description Local port used by Gatling Proxy for HTTP
     */
    private int localPort;

    /**
     * Proxy host
     *
     * @parameter property="gatling.recorder.proxyHost"
     * @description Outgoing proxy host
     */
    private String proxyHost;

    /**
     * Proxy port.
     *
     * @parameter property="gatling.recorder.proxyPort" default-value="0"
     * @description Outgoing proxy port for HTTP
     */
    private int proxyPort;

    /**
     * Proxy SSL port.
     *
     * @parameter property="gatling.recorder.proxySSLPort" default-value="0"
     * @description Outgoing proxy port for HTTPS
     */
    private int proxySSLPort;

    /**
     * Output folder.
     *
     * @parameter property="gatling.recorder.outputFolder" default-value="${basedir}/src/test/scala"
     * @description Uses <folderName> as the folder where generated simulations will be stored
     */
    private String outputFolder;

    /**
     * Request bodies folder.
     *
     * @parameter property="gatling.recorder.requestBodiesFolder" default-value="${basedir}/src/test/resources/request-bodies"
     * @description Uses <folderName> as the folder where request bodies are stored
     */
    private String requestBodiesFolder;

    /**
     * Class name.
     *
     * @parameter property="gatling.recorder.className"
     * @description Sets the name of the generated class
     */
    private String className;

    /**
     * Package.
     *
     * @parameter property="gatling.recorder.package"
     * @description Sets the package of the generated class
     */
    private String packageName;

    /**
     * Encoding.
     *
     * @parameter property="gatling.recorder.encoding"
     * @description Sets the encoding used in the recorder
     */
    private String encoding;

    /**
     * Follow redirect.
     *
     * @parameter property="gatling.recorder.followRedirect" default-value="false"
     * @description Sets the follow redirect option
     */
    private boolean followRedirect;

    /**
     * Config file
     *
     * @parameter property="gatling.recorder.recorderConfigFile" default-value="${basedir}/recorder.conf"
     * @description The config file to load recorder settings from
     */
    private String configFile;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        RecorderPropertiesBuilder props = new RecorderPropertiesBuilder();
        if (localPort != 0) props.localPort(localPort);
        if (proxyHost != null) props.proxyHost(proxyHost);
        if (proxyPort != 0) props.proxyPort(proxyPort);
        if (proxySSLPort != 0) props.proxyPort(proxySSLPort);
        props.simulationOutputFolder(outputFolder);
        props.requestBodiesFolder(requestBodiesFolder);
        if (className != null) props.simulationClassName(className);
        if (packageName != null) props.simulationPackage(packageName);
        if (encoding != null) props.encoding(encoding);
        if (followRedirect) props.followRedirect(followRedirect);
        RecorderController.apply(props.build(), Option.apply(new File(new java.io.File(configFile), Codec.UTF8())));
        while (JFrame.getFrames().length > 0) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                return;
            }
        }
    }
}
