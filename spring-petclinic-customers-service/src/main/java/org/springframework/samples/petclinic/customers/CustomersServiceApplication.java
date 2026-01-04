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
package org.springframework.samples.petclinic.customers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Maciej Szarlinski
 */
@EnableDiscoveryClient
@SpringBootApplication
public class CustomersServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomersServiceApplication.class, args);

		// ============================================
		// MOCK CRITICAL ISSUES FOR QUALITY GATE TESTING
		// Uncomment the code below to trigger SonarQube critical issues
		// ============================================

		// CRITICAL ISSUE 1: Hardcoded credentials (Security Hotspot - Critical)
		String password = "admin123"; // SonarQube will flag this as hardcoded credential
		String dbPassword = "P@ssw0rd123!"; // Another hardcoded credential

		// CRITICAL ISSUE 2: SQL Injection vulnerability pattern
		String username = args.length > 0 ? args[0] : "guest";
		String query = "SELECT * FROM users WHERE username = '" + username + "'"; // SQL Injection risk

		// CRITICAL ISSUE 3: Empty catch block (Code Smell - Critical)
		try {
			int result = 10 / 0;
		} catch (Exception e) {
			// Empty catch block - SonarQube critical issue
		}

		// CRITICAL ISSUE 4: Null pointer dereference
		String nullString = null;
		int length = nullString.length(); // Guaranteed NullPointerException

		// To pass quality gate: Comment out all the code above from "MOCK CRITICAL
		// ISSUES" to here
		// ============================================
	}
}
