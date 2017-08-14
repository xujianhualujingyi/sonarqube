/*
 * SonarQube
 * Copyright (C) 2009-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.process.jmvoptions;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.stream.Collectors;

public class EsJvmOptions extends JvmOptions<EsJvmOptions> {
  private static final String[] MANDATORY_OPTIONS = {
    "-XX:+UseConcMarkSweepGC",
    "-XX:CMSInitiatingOccupancyFraction=75",
    "-XX:+UseCMSInitiatingOccupancyOnly",
    "-XX:+AlwaysPreTouch",
    "-server",
    "-Xss1m",
    "-Djava.awt.headless=true",
    "-Dfile.encoding=UTF-8",
    "-Djna.nosys=true",
    "-Djdk.io.permissionsUseCanonicalPath=true",
    "-Dio.netty.noUnsafe=true",
    "-Dio.netty.noKeySetOptimization=true",
    "-Dio.netty.recycler.maxCapacityPerThread=0",
    "-Dlog4j.shutdownHookEnabled=false",
    "-Dlog4j2.disable.jmx=true",
    "-Dlog4j.skipJansi=true"
  };
  private static final String ELASTICSEARCH_JVM_OPTIONS_HEADER = "# This file has been automatically generated by SonarQube during startup.\n" +
    "# Please use the sonar.search.javaOpts in sonar.properties to specify jvm options for Elasticsearch\n" +
    "\n" +
    "# DO NOT EDIT THIS FILE\n" +
    "\n";

  public EsJvmOptions() {
    super(MANDATORY_OPTIONS);
  }

  public void writeToJvmOptionFile(File file) {
    String jvmOptions = getAll().stream().collect(Collectors.joining("\n"));
    String jvmOptionsContent = ELASTICSEARCH_JVM_OPTIONS_HEADER + jvmOptions;
    try {
      Files.write(file.toPath(), jvmOptionsContent.getBytes(Charset.forName("UTF-8")));
    } catch (IOException e) {
      throw new IllegalStateException("Cannot write Elasticsearch jvm options file", e);
    }
  }
}