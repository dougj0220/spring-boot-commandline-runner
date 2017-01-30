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

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { SampleSimpleApplication.class })
public class SpringTestSampleSimpleApplicationTests {

	@Autowired
	ApplicationContext ctx;

	@Test
	public void testContextLoads() throws Exception {
		//assertThat(this.ctx).isNotNull();
		Assert.isTrue(this.ctx != null);
		//assertThat(this.ctx.containsBean("helloWorldService")).isTrue();
		Assert.isTrue(this.ctx.containsBean("helloWorldService"));
		//assertThat(this.ctx.containsBean("sampleSimpleApplication")).isTrue();
		Assert.isTrue(this.ctx.containsBean("sampleSimpleApplication"));
	}

}
