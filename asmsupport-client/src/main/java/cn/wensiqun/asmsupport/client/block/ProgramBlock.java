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
package cn.wensiqun.asmsupport.client.block;

import cn.wensiqun.asmsupport.client.ClientBridge;
import cn.wensiqun.asmsupport.client.def.Param;
import cn.wensiqun.asmsupport.client.def.ParamPostern;
import cn.wensiqun.asmsupport.client.def.action.*;
import cn.wensiqun.asmsupport.client.def.param.*;
import cn.wensiqun.asmsupport.client.def.param.basic.DummyParam;
import cn.wensiqun.asmsupport.client.def.param.basic.NullParam;
import cn.wensiqun.asmsupport.client.def.param.impl.*;
import cn.wensiqun.asmsupport.client.def.var.*;
import cn.wensiqun.asmsupport.core.block.KernelProgramBlock;
import cn.wensiqun.asmsupport.core.definition.variable.IVariable;
import cn.wensiqun.asmsupport.core.definition.variable.LocalVariable;
import cn.wensiqun.asmsupport.core.utils.common.BlockTracker;
import cn.wensiqun.asmsupport.standard.action.ActionSet;
import cn.wensiqun.asmsupport.standard.def.clazz.IClass;
import cn.wensiqun.asmsupport.utils.lang.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper of {@link KernelProgramBlock}
 * 
 * @author WSQ
 *
 * @param <B>
 */
public class ProgramBlock<B extends KernelProgramBlock> implements ActionSet<
Param, Var, FieldVar,
IF, While, DoWhile, ForEach, Try, Sync> {

	private ClientBridge<B> clientBridge;
	
	ProgramBlock<? extends KernelProgramBlock> parent;
	
	LocVar[] locVars; 
	
	Map<String, LocVar> locVarMap = new HashMap<>();
	
	final static LocVar[] EMPTY_LOCAL_VARS = new LocVar[0];
	
	public LocVar[] getMethodArguments() {
		if(locVars == null) {
			LocalVariable[] localVariables = getGenerateTimeBlock().getMethodArguments();
			if(ArrayUtils.isNotEmpty(localVariables)) {
				locVars = new LocVar[localVariables.length];
				for(int i = 0; i<locVars.length; i++) {
					locVars[i] = new LocVar(this.getClientBridge(), localVariables[i]);
				}
			} else {
				locVars = EMPTY_LOCAL_VARS;
			}
		}
		return locVars;
	}

	/**
	 * get current method owner.
	 * 
	 * @return
	 */
	public IClass getMethodOwner() {
		return getGenerateTimeBlock().getMethodDeclaringClass();
	}

	@Override
	public This this_() {
		return new This(getClientBridge(), getGenerateTimeBlock().this_());
	}

	@Override
	public FieldVar this_(String name) {
		return new FieldVar(getClientBridge(), getGenerateTimeBlock().this_(name));
	}

	@Override
	public Super super_() {
		return new Super(getClientBridge(), getGenerateTimeBlock().super_());
	}

	@Override
	public LocVar var(String name, Class<?> type, Param para) {
		LocVar var = new LocVar(getClientBridge(), getGenerateTimeBlock().var(name, type, para.getTarget()));
		locVarMap.put(name, var);
		return var;
	}

	@Override
	public LocVar var(Class<?> type, Param para) {
		return new LocVar(getClientBridge(), getGenerateTimeBlock().var(type, para.getTarget()));
	}

	@Override
	public LocVar var(String name, IClass type, Param para) {
		LocVar var = new LocVar(getClientBridge(), getGenerateTimeBlock().var(name, type, para.getTarget()));
		locVarMap.put(name, var);
		return var;
	}

	@Override
	public LocVar var(IClass type, Param para) {
		return new LocVar(getClientBridge(), getGenerateTimeBlock().var(type, para.getTarget()));
	}

	@Override
	public FieldVar field(String name) {
		return new FieldVar(getClientBridge(), getGenerateTimeBlock().field(name));
	}

	@Override
	public UncertainParam assign(Var variable, Param val) {
		return new UncertainParamImpl(getClientBridge(), getGenerateTimeBlock().assign((IVariable) ParamPostern.getTarget(variable), val.getTarget()));
	}

	@Override
	public UncertainParam call(Param objRef, String methodName, Param... arguments) {
		return new UncertainParamImpl(getClientBridge(), getGenerateTimeBlock().getBlockTracker().track().call(objRef.getTarget(), methodName, ParamPostern.getTarget(arguments)));
	}

	@Override
	public UncertainParam call(String methodName, Param... args) {
		return new UncertainParamImpl(getClientBridge(), getGenerateTimeBlock().call(methodName, ParamPostern.getTarget(args)));
	}

	@Override
	public UncertainParam call(IClass owner, String methodName, Param... arguments) {
		return new UncertainParamImpl(getClientBridge(), getGenerateTimeBlock().call(owner, methodName, ParamPostern.getTarget(arguments)));
	}
    
	@Override
    public UncertainParam call(Class<?> owner, String methodName, Param... arguments) {
    	return new UncertainParamImpl(getClientBridge(), getGenerateTimeBlock().call(owner, methodName, ParamPostern.getTarget(arguments)));
    }

	@Override
	public UncertainParam new_(Class<?> owner, Param... arguments) {
		return new UncertainParamImpl(getClientBridge(), getGenerateTimeBlock().new_(owner, ParamPostern.getTarget(arguments)));
	}

	@Override
	public UncertainParam new_(IClass owner, Param... arguments) {
		return new UncertainParamImpl(getClientBridge(), getGenerateTimeBlock().new_(owner, ParamPostern.getTarget(arguments)));
	}

	@Override
	public UncertainParam callOrig() {
		return new UncertainParamImpl(getClientBridge(), getGenerateTimeBlock().callOrig());
	}

	@Override
	public ArrayParam makeArray(IClass aClass, Param... dimensions) {
		return new ArrayParamImpl(getClientBridge(), getGenerateTimeBlock().makeArray(aClass, ParamPostern.getTarget(dimensions)));
	}

	@Override
	public ArrayParam makeArray(Class<?> arraytype, Param... dimensions) {
		return new ArrayParamImpl(getClientBridge(), getGenerateTimeBlock().makeArray(arraytype, ParamPostern.getTarget(dimensions)));
	}

	@Override
	public ArrayParam newarray(IClass aClass, Object arrayObject) {
		return new ArrayParamImpl(getClientBridge(), getGenerateTimeBlock().newarray(aClass, ParamPostern.getTarget(arrayObject)));
	}

	/**
	 * The second parameter must be a array and element type of array is {@link Param} type
	 */
	@Override
	public ArrayParam newarray(Class<?> type, Object arrayObject) {
		return new ArrayParamImpl(getClientBridge(), getGenerateTimeBlock().newarray(type, ParamPostern.getTarget(arrayObject)));
	}

	@Override
	public UncertainParam arrayLoad(Param arrayReference, Param pardim, Param... parDims) {
		return new UncertainParamImpl(getClientBridge(), getGenerateTimeBlock().arrayLoad(arrayReference.getTarget(), pardim.getTarget(), ParamPostern.getTarget(parDims)));
	}
	
	@Override
	public UncertainParam arrayStore(Param arrayReference, Param value, Param dim, Param... dims) {
		return new UncertainParamImpl(getClientBridge(), getGenerateTimeBlock().arrayStore(arrayReference.getTarget(),
				value.getTarget(), dim.getTarget(), ParamPostern.getTarget(dims)));
	}

	@Override
	public NumParam arrayLength(Param arrayReference, Param... dims) {
	    Param[] operands = ParamPostern.unionParam(arrayReference, dims);
		return new NumParamImpl(getClientBridge(), new ArrayLengthAction(getClientBridge(), operands.length - 1), operands);
	}
	
	@Override
	public NumParam add(Param leftFactor, Param rightFactor) {
	    return new NumParamImpl(getClientBridge(), new AddAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public NumParam sub(Param leftFactor, Param rightFactor) {
        return new NumParamImpl(getClientBridge(), new SubAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public NumParam mul(Param leftFactor, Param rightFactor) {
        return new NumParamImpl(getClientBridge(), new MulAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public NumParam div(Param leftFactor, Param rightFactor) {
        return new NumParamImpl(getClientBridge(), new DivAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public NumParam mod(Param leftFactor, Param rightFactor) {
        return new NumParamImpl(getClientBridge(), new ModAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public NumParam reverse(Param factor) {
		return new NumParamImpl(getClientBridge(), new ReverseAction(getClientBridge()), factor);
	}

	@Override
	public NumParam band(Param leftFactor, Param rightFactor) {
		return new NumParamImpl(getClientBridge(), new BandAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public NumParam bor(Param leftFactor, Param rightFactor) {
        return new NumParamImpl(getClientBridge(), new BorAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public NumParam bxor(Param leftFactor, Param rightFactor) {
        return new NumParamImpl(getClientBridge(), new BxorAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public NumParam shl(Param leftFactor, Param rightFactor) {
		return new NumParamImpl(getClientBridge(), new ShiftLeftAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public NumParam shr(Param leftFactor, Param rightFactor) {
        return new NumParamImpl(getClientBridge(), new ShiftRightAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public NumParam ushr(Param leftFactor, Param rightFactor) {
        return new NumParamImpl(getClientBridge(), new UnsignedShiftRightAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public NumParam predec(Param crement) {
		return new NumParamImpl(getClientBridge(),
				new DummyParam(getClientBridge(), getGenerateTimeBlock().predec(crement.getTarget())));
	}

	@Override
	public NumParam postdec(Param crement) {
		return new NumParamImpl(getClientBridge(),
				new DummyParam(getClientBridge(), getGenerateTimeBlock().postdec(crement.getTarget())));
	}

	@Override
	public NumParam preinc(Param crement) {
		return new NumParamImpl(getClientBridge(),
				new DummyParam(getClientBridge(), getGenerateTimeBlock().preinc(crement.getTarget())));
	}

	@Override
	public NumParam postinc(Param crement) {
		return new NumParamImpl(getClientBridge(),
				new DummyParam(getClientBridge(), getGenerateTimeBlock().postinc(crement.getTarget())));
	}

	@Override
	public BoolParam gt(Param leftFactor, Param rightFactor) {
		return new BoolParamImpl(getClientBridge(), new GreaterThanAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public BoolParam ge(Param leftFactor, Param rightFactor) {
        return new BoolParamImpl(getClientBridge(), new GreaterEqualAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public BoolParam lt(Param leftFactor, Param rightFactor) {
        return new BoolParamImpl(getClientBridge(), new LessThanAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public BoolParam le(Param leftFactor, Param rightFactor) {
        return new BoolParamImpl(getClientBridge(), new LessEqualAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public BoolParam eq(Param leftFactor, Param rightFactor) {
        return new BoolParamImpl(getClientBridge(), new EqualAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public BoolParam ne(Param leftFactor, Param rightFactor) {
        return new BoolParamImpl(getClientBridge(), new NotEqualAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public BoolParam logicalAnd(Param leftFactor, Param rightFactor) {
		return new BoolParamImpl(getClientBridge(), new LogicAndAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public BoolParam logicalOr(Param leftFactor, Param rightFactor) {
        return new BoolParamImpl(getClientBridge(), new LogicOrAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public BoolParam logicalXor(Param leftFactor, Param rightFactor) {
        return new BoolParamImpl(getClientBridge(), new LogicXorAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public BoolParam and(Param leftFactor, Param rightFactor, Param... otherFactor) {
        return new BoolParamImpl(getClientBridge(), new AndAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public BoolParam or(Param leftFactor, Param rightFactor, Param... otherFactor) {
        return new BoolParamImpl(getClientBridge(), new OrAction(getClientBridge()), leftFactor, rightFactor);
	}

	@Override
	public BoolParam no(Param factor) {
        return new BoolParamImpl(getClientBridge(), new NotAction(getClientBridge()), factor);
	}

	@Override
	public UncertainParam checkcast(Param cc, IClass to) {
		return new UncertainParamImpl(getClientBridge(), getGenerateTimeBlock().checkcast(cc.getTarget(), to));
	}

	@Override
	public UncertainParam checkcast(Param cc, Class<?> to) {
		return new UncertainParamImpl(getClientBridge(), getGenerateTimeBlock().checkcast(cc.getTarget(), to));
	}

	@Override
	public NumParam neg(Param factor) {
		return new NumParamImpl(getClientBridge(), new NegAction(getClientBridge()), factor);
	}

	@Override
	public DummyParam ternary(Param exp1, Param exp2, Param exp3) {
		return new DummyParam(getClientBridge(), getGenerateTimeBlock().ternary(exp1.getTarget(), exp2.getTarget(), exp3.getTarget()));
	}

	@Override
	public UncertainParam stradd(Param par1, Param... pars) {
		return new UncertainParamImpl(getClientBridge(), getGenerateTimeBlock().stradd(par1.getTarget(), ParamPostern.getTarget(pars)));
	}

	@Override
	public BoolParam instanceof_(Param obj, IClass type) {
		return new BoolParamImpl(getClientBridge(), new InstanceofAction(getClientBridge(), type), obj);
	}

	@Override
	public BoolParam instanceof_(Param obj, Class<?> type) {
        return new BoolParamImpl(getClientBridge(), new InstanceofAction(getClientBridge(), getType(type)), obj);
	}

	@Override
	public void break_() {
		getGenerateTimeBlock().break_();
	}

	@Override
	public void continue_() {
		getGenerateTimeBlock().continue_();
	}

	@Override
	public void throw_(Param exception) {
		getGenerateTimeBlock().throw_(exception.getTarget());
	}

	@Override
	public void return_() {
		getGenerateTimeBlock().return_();
	}

	@Override
	public void return_(Param param) {
		getGenerateTimeBlock().return_(param.getTarget());
	}

	@Override
	public IF if_(IF ifBlock) {
	    ifBlock.parent = this;
		getGenerateTimeBlock().if_(ifBlock.getDelegate());
		return ifBlock;
	}

	@Override
	public While while_(While whileLoop) {
	    whileLoop.parent = this;
		getGenerateTimeBlock().while_(whileLoop.getDelegate());
		return whileLoop;
	}

	@Override
	public DoWhile dowhile(DoWhile doWhile) {
	    doWhile.parent = this;
		getGenerateTimeBlock().dowhile(doWhile.getDelegate());
		return doWhile;
	}

	@Override
	public ForEach for_(ForEach forEach) {
	    forEach.parent = this;
		getGenerateTimeBlock().for_(forEach.getDelegate());
		return forEach;
	}

	@Override
	public Try try_(Try tryClient) {
	    tryClient.parent = this;
		getGenerateTimeBlock().try_(tryClient.getDelegate());
		return tryClient;
	}

	@Override
	public Sync sync(Sync sync) {
	    sync.parent = this;
		getGenerateTimeBlock().sync(sync.getDelegate());
		return sync;
	}

	@Override
	public NumParam val(Integer val) {
		return new NumParamImpl(getClientBridge(), new DummyParam(getClientBridge(), getGenerateTimeBlock().val(val)));
	}

	@Override
	public NumParam val(Short val) {
		return new NumParamImpl(getClientBridge(), new DummyParam(getClientBridge(), getGenerateTimeBlock().val(val)));
	}

	@Override
	public NumParam val(Byte val) {
		return new NumParamImpl(getClientBridge(), new DummyParam(getClientBridge(), getGenerateTimeBlock().val(val)));
	}

	@Override
	public DummyParam val(Boolean val) {
		return new DummyParam(getClientBridge(), getGenerateTimeBlock().val(val));
	}

	@Override
	public NumParam val(Long val) {
		return new NumParamImpl(getClientBridge(), new DummyParam(getClientBridge(), getGenerateTimeBlock().val(val)));
	}

	@Override
	public NumParam val(Double val) {
		return new NumParamImpl(getClientBridge(), new DummyParam(getClientBridge(), getGenerateTimeBlock().val(val)));
	}

	@Override
	public NumParam val(Character val) {
		return new NumParamImpl(getClientBridge(), new DummyParam(getClientBridge(), getGenerateTimeBlock().val(val)));
	}

	@Override
	public NumParam val(Float val) {
		return new NumParamImpl(getClientBridge(), new DummyParam(getClientBridge(), getGenerateTimeBlock().val(val)));
	}

	@Override
	public ObjectParam val(IClass val) {
		return new ObjectParamImpl(getClientBridge(), getGenerateTimeBlock().val(val));
	}

	@Override
	public ObjectParam val(Class<?> val) {
		return new ObjectParamImpl(getClientBridge(), getGenerateTimeBlock().val(val));
	}

	@Override
	public ObjectParam val(String val) {
		return new ObjectParamImpl(getClientBridge(), getGenerateTimeBlock().val(val));
	}

	@Override
	public NullParam null_(IClass type) {
		return new NullParam(getClientBridge(), getGenerateTimeBlock().null_(type));
	}

	@Override
	public NullParam null_(Class<?> type) {
		return new NullParam(getClientBridge(), getGenerateTimeBlock().null_(type));
	}

	@Override
	public IClass getType(Class<?> cls) {
		return getGenerateTimeBlock().getType(cls);
	}

	@Override
	public IClass getType(String cls) {
		return getGenerateTimeBlock().getType(cls);
	}

	@Override
	public IClass getArrayType(Class<?> cls, int dim) {
		return getGenerateTimeBlock().getArrayType(cls, dim);
	}

	@Override
	public IClass getArrayType(IClass rootComponent, int dim) {
		return getGenerateTimeBlock().getArrayType(rootComponent, dim);
	}

    public LocVar getLocVar(String name) {
    	ProgramBlock<? extends KernelProgramBlock> block = this;
    	LocVar var;
    	do {
    		var = block.locVarMap.get(name);
    		block = block.parent;
    	} while (var == null && block != null);
    	return var;
    }
	
    LocVar[] internalVar2ClientVar(LocalVariable... pars) {
        if(pars == null) {
            return null;
        }
        LocVar[] paras = new LocVar[pars.length];
        for(int i=0; i<pars.length; i++) {
            paras[i] = new LocVar(getClientBridge(), pars[i]);
        }
        return paras;
    }

	private B getGenerateTimeBlock() {
		return (B) getClientBridge().track();
	}


	public BlockTracker getClientBridge() {
		return clientBridge;
	}

    /**
     * Get the delegate
     * @return
     */
    public B getDelegate() {
        return clientBridge.getDelegate();
    }

    void setKernelBlock(B kernelBlock) {
        this.clientBridge = new ClientBridge<>(kernelBlock);
    }

}
