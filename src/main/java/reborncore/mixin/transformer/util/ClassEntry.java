/*
 * Copyright (c) 2017 modmuss50 and Gigabit101
 *
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/*******************************************************************************
 * Copyright (c) 2015 Jeff Martin.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public
 * License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Contributors:
 * Jeff Martin - initial API and implementation
 ******************************************************************************/

package reborncore.mixin.transformer.util;

import com.google.common.collect.Lists;

import java.util.List;

public class ClassEntry implements Entry {

	private String name;

	public ClassEntry(String className) {
		if (className == null) {
			throw new IllegalArgumentException("Class name cannot be null!");
		}
		if (className.indexOf('.') >= 0) {
			throw new IllegalArgumentException("Class name must be in JVM format. ie, path/to/package/class$inner : " + className);
		}

		this.name = className;

		if (isInnerClass() && getInnermostClassName().indexOf('/') >= 0) {
			throw new IllegalArgumentException("Inner class must not have a package: " + className);
		}
	}

	public ClassEntry(ClassEntry other) {
		this.name = other.name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getClassName() {
		return this.name;
	}

	@Override
	public ClassEntry getClassEntry() {
		return this;
	}

	@Override
	public ClassEntry cloneToNewClass(ClassEntry classEntry) {
		return classEntry;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof ClassEntry && equals((ClassEntry) other);
	}

	public boolean equals(ClassEntry other) {
		return other != null && this.name.equals(other.name);
	}

	@Override
	public String toString() {
		return this.name;
	}

	public boolean isInnerClass() {
		return this.name.lastIndexOf('$') >= 0;
	}

	public List<String> getClassChainNames() {
		return Lists.newArrayList(this.name.split("\\$"));
	}

	public List<ClassEntry> getClassChain() {
		List<ClassEntry> entries = Lists.newArrayList();
		StringBuilder buf = new StringBuilder();
		for (String name : getClassChainNames()) {
			if (buf.length() > 0) {
				buf.append("$");
			}
			buf.append(name);
			entries.add(new ClassEntry(buf.toString()));
		}
		return entries;
	}

	public String getOutermostClassName() {
		if (isInnerClass()) {
			return this.name.substring(0, this.name.indexOf('$'));
		}
		return this.name;
	}

	public ClassEntry getOutermostClassEntry() {
		return new ClassEntry(getOutermostClassName());
	}

	public String getOuterClassName() {
		if (!isInnerClass()) {
			throw new Error("This is not an inner class!");
		}
		return this.name.substring(0, this.name.lastIndexOf('$'));
	}

	public ClassEntry getOuterClassEntry() {
		return new ClassEntry(getOuterClassName());
	}

	public String getInnermostClassName() {
		if (!isInnerClass()) {
			throw new Error("This is not an inner class!");
		}
		return this.name.substring(this.name.lastIndexOf('$') + 1);
	}

	public boolean isInDefaultPackage() {
		return this.name.indexOf('/') < 0;
	}

	public String getPackageName() {
		int pos = this.name.lastIndexOf('/');
		if (pos > 0) {
			return this.name.substring(0, pos);
		}
		return null;
	}

	public String getSimpleName() {
		int pos = this.name.lastIndexOf('/');
		if (pos > 0) {
			return this.name.substring(pos + 1);
		}
		return this.name;
	}

	public ClassEntry buildClassEntry(List<ClassEntry> classChain) {
		assert (classChain.contains(this));
		StringBuilder buf = new StringBuilder();
		for (ClassEntry chainEntry : classChain) {
			if (buf.length() == 0) {
				buf.append(chainEntry.getName());
			} else {
				buf.append("$");
				buf.append(chainEntry.isInnerClass() ? chainEntry.getInnermostClassName() : chainEntry.getSimpleName());
			}

			if (chainEntry == this) {
				break;
			}
		}
		return new ClassEntry(buf.toString());
	}
}
