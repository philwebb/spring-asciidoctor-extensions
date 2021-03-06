/*
 * Copyright 2014-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.asciidoctor.springboot.aj15;

import java.util.List;

import io.spring.asciidoctor.springboot.ConfigurationPropertyValidator;
import io.spring.asciidoctor.springboot.Logger;
import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.ast.Document;
import org.asciidoctor.ast.ListNode;
import org.asciidoctor.extension.Treeprocessor;

/**
 * {@link Treeprocessor} that validates configuration properties found in blocks with the
 * {@code configprops} attribute.
 *
 * @author Andy Wilkinson
 */
class ConfigurationPropertiesTreeprocessor extends Treeprocessor {

	private final ConfigurationPropertyValidator validator;

	ConfigurationPropertiesTreeprocessor(Logger logger) {
		this.validator = new ConfigurationPropertyValidator(logger);
	}

	@Override
	public Document process(Document document) {
		process((AbstractBlock) document);
		return document;
	}

	private void process(AbstractBlock block) {
		if (block.getAttributes().containsValue("configprops")) {
			this.validator.validateProperties(block.getContent(), (String) block.getAttr("language"));
		}
		List<AbstractBlock> children = getChildren(block);
		if (children != null) {
			for (AbstractBlock child : children) {
				process(child);
			}
		}
	}

	private List<AbstractBlock> getChildren(AbstractBlock block) {
		if (block instanceof ListNode) {
			return null;
		}
		return block.getBlocks();
	}

}
