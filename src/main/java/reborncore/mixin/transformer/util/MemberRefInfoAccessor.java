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

import java.lang.reflect.Field;

public class MemberRefInfoAccessor {

	private static Class<?> clazz;
	private static Field classIndex;
	private static Field nameAndTypeIndex;

	private Object item;

	public MemberRefInfoAccessor(Object item) {
		this.item = item;
	}

	public int getClassIndex() {
		try {
			return (Integer) classIndex.get(this.item);
		} catch (Exception ex) {
			throw new Error(ex);
		}
	}

	public void setClassIndex(int val) {
		try {
			classIndex.set(this.item, val);
		} catch (Exception ex) {
			throw new Error(ex);
		}
	}

	public int getNameAndTypeIndex() {
		try {
			return (Integer) nameAndTypeIndex.get(this.item);
		} catch (Exception ex) {
			throw new Error(ex);
		}
	}

	public void setNameAndTypeIndex(int val) {
		try {
			nameAndTypeIndex.set(this.item, val);
		} catch (Exception ex) {
			throw new Error(ex);
		}
	}

	public static boolean isType(ConstInfoAccessor accessor) {
		return clazz.isAssignableFrom(accessor.getItem().getClass());
	}

	static {
		try {
			clazz = Class.forName("javassist.bytecode.MemberrefInfo");
			classIndex = clazz.getDeclaredField("classIndex");
			classIndex.setAccessible(true);
			nameAndTypeIndex = clazz.getDeclaredField("nameAndTypeIndex");
			nameAndTypeIndex.setAccessible(true);
		} catch (Exception ex) {
			throw new Error(ex);
		}
	}
}
