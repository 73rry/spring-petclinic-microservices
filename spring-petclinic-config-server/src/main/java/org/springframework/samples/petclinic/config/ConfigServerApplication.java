/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author Maciej Szarlinski
 */
@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
		// ISSUE 1: Hardcoded password
		String dbPassword = "SuperSecret123!";
		String apiToken = "token_12345_secret";

		// ISSUE 2: SQL Injection vulnerability
		String userId = args.length > 0 ? args[0] : "admin";
		String sqlQuery = "DELETE FROM visits WHERE user_id = '" + userId + "'";

		// ISSUE 3: Division by zero
		int a = 1 / 0;

		// ISSUE 4: Null pointer dereference
		Object obj = null;
		obj.toString();

		// ISSUE 5: Empty catch block
		try {
			Integer.parseInt("not a number");
		} catch (NumberFormatException e) {
			// Empty catch - bad practice
		}

		// ISSUE 6: Unused variable
		String neverUsed = "This variable is never used";

		// ISSUE 7: Dead code after return
		if (true) {
			return;
		}
		System.out.println("This will never execute");
	}
}
