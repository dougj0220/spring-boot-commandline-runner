/*
 * Copyright 2012-2016 the original author or authors.
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
package sample.simple;

import com.tii.php.entity.Medication;
import com.tii.php.entity.MedicationPackage;
import com.tii.php.service.MedicationService;
import java.io.File;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import sample.simple.service.HelloWorldService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.Assert;

@SpringBootApplication
@ComponentScan({ "com.tii.php", "sample.simple" })
public class SampleSimpleApplication implements CommandLineRunner {

	// Simple example shows how a command line spring application can execute an
	// injected bean service. Also demonstrates how you can use @Value to inject
	// command line args ('--name=whatever') or application properties
	@Autowired
	private HelloWorldService helloWorldService;

	@Autowired
	private MedicationService medicationService;

	@Override
	public void run(String... args) {
		System.out.println(this.helloWorldService.getHelloMessage());
		if (args.length > 0 && args[0].equals("exitcode")) {
			throw new ExitException();
		}

		try {
			bulkInsertMedicationPackages();
			System.out.println("medication packages load completed successfully!!");
			bulkInsertMedication();
			System.out.println("medications load completed successfully!!");
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SampleSimpleApplication.class, args).close();
	}

	private void bulkInsertMedicationPackages() throws Exception {
		List<MedicationPackage> packagesList = new LinkedList<>();
		MedicationPackage mp = null;
		File tempFile = File.createTempFile("temp", Long.toString(System.nanoTime()));
		InputStream in = this.getClass().getResourceAsStream("/package.txt");
		FileUtils.copyInputStreamToFile(in, tempFile);
		String file = FileUtils.readFileToString(tempFile);
		String[] packages = file.split("\\R");
		Assert.isTrue(packages.length == 197756);

		for (int i = 1; i < packages.length; i++) {
			String[] data = packages[i].split("\\t");
			mp = new MedicationPackage(data[0], data[1], data[2], data[3]);
			packagesList.add(mp);
		}
		List<MedicationPackage> storedRows = medicationService.bulkCreateMedicationPackage(packagesList);
		Assert.isTrue(storedRows.size() == packagesList.size());
		tempFile.delete();
	}

	private void bulkInsertMedication() throws Exception {
		List<Medication> medicationsList = new LinkedList<>();
		Medication m = null;
		File tempFile = File.createTempFile("temp2", Long.toString(System.nanoTime()));
		InputStream in = this.getClass().getResourceAsStream("/product.txt");
		FileUtils.copyInputStreamToFile(in, tempFile);
		String file = FileUtils.readFileToString(tempFile);
		String[] medications = file.split("\\R");
		Assert.isTrue(medications.length == 105750);

		for (int i = 1; i < medications.length; i++) {
			String[] data = medications[i].split("\\t");
			int stopper = 18;
			if (data.length < 18) {
				stopper = data.length;
				// System.out.println("Data row has length: " + data.length);
			}
			m = new Medication();
			m.setProductId(data[0]);
			m.setProductNdcCode(data[1]);
			m.setProductTypeName(data[2]);
			m.setProprietaryName(data[3]);
			m.setProprietaryNameSuffix(data[4]);
			m.setNonProprietaryName(data[5]);
			m.setDosageFormName(data[6]);
			m.setRouteName(data[7]);
			m.setStartMarketingDate(data[8]);
			m.setEndMarketingDate(data[9]);
			m.setMarketingCategoryName(data[10]);
			m.setApplicationNumber(data[11]);
			m.setLabelerName((stopper >= 13) ? data[12] : null);
			m.setSubstanceName((stopper >= 14) ? data[13] : null);
			m.setActiveNumeratorStrength((stopper >= 15) ? data[14] : null);
			m.setActiveIngredUnit((stopper >= 16) ? data[15] : null);
			m.setPharmClasses((stopper >= 17) ? data[16] : null);
			m.setDeaSchedule((stopper >= 18) ? data[17] : null);
			medicationsList.add(m);
		}

		List<Medication> storedRows = medicationService.bulkCreateMedication(medicationsList);
		Assert.isTrue(storedRows.size() == medicationsList.size());
		tempFile.delete();
	}

}
