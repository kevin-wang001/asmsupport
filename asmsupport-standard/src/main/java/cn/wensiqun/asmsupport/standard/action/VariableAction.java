/**    
 *  Asmsupport is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cn.wensiqun.asmsupport.standard.action;

import cn.wensiqun.asmsupport.standard.def.IParam;
import cn.wensiqun.asmsupport.standard.def.clazz.AClass;
import cn.wensiqun.asmsupport.standard.def.var.IVar;

/**
 * 
 * All variable operations. such as assign, create new variable.
 * 
 * @author sqwen
 *
 * @param <_P> The parameterized generic type.
 * @param <_V>      The Local Variable generic type
 */
public interface VariableAction<_P extends IParam, _V extends IVar> {

    /**
     * 
     * 在程序块中获取this关键字. 需要注意的是，和java代码一样，这个变量只能够在非静态方法或者程序块中调用，否则会抛异常。
     * 
     * @return {@link _LocVar}
     * @see #super_()
     */
    public _V this_();
    
    /**
     * Get global variable of current class according the passed name. 
     * this method is equivalence to : 
     * <pre>
     *     _this().getGlobalVariable(name);
     * <pre>
     * @param name
     * @return {@link _LocVar}
     */
    public _V this_(String name);
    
    /**
     * 获取父类super关键字。 需要注意的是，和java代码一样，这个变量只能够在非静态方法或者程序块中调用，否则会抛异常。
     * 
     * @return {@link _LocVar}
     * @see #this_()
     */
    public _V super_();
    
    /**
     * Create a local variable with anonymous
     * @param type
     * @param para
     * @return
     */
    public _V var(Class<?> type, _P para);

    /**
     * Create a local variable with anonymous, this method equivalent to following code :
     * <p>
     * var("", type, true, para)
     * </p>
     * @param type
     * @param para
     * @return
     */
    public _V var(AClass type, _P para);

    /**
     * Create a local variable
     * @param name
     * @param type
     * @param para
     * @return
     */
    public _V var(String name, Class<?> type, _P para);
    
    /**
     * Create a local variable, this method equivalent to following code :
     * <p>
     * var(name, type, false, para)
     * </p>
     * @param name
     * @param type
     * @param para
     * @return
     */
    public _V var(String name, AClass type, _P para);
    

    /**
     * Get field from current object. the method is same to call following :
     * 
     * <pre>
     * this_().field(name);
     * </pre>
     * 
     * @param name
     * @return
     */
    public _V field(String name);
    
	/**
	 * assign a value to a variable. for exampel:
	 * java code:<br/>
	 * <pre>
	 * i = 10;
	 * </pre>
	 * asmsupport code:<br/>
	 * <pre>
	 * assign(i, getValue(10));
	 * </pre>
	 * @param variable
	 * @param val
	 * @return
	 */
	_P assign(_V variable, _P val);
	
}