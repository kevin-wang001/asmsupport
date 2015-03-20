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
package cn.wensiqun.asmsupport.core.asm.adapter;

import cn.wensiqun.asmsupport.core.block.ProgramBlockInternal;
import cn.wensiqun.asmsupport.core.operator.asmdirect.VisitInsn;
import cn.wensiqun.asmsupport.core.operator.numerical.OperatorFactory;

public class VisitInsnAdapter implements VisitXInsnAdapter {

	private int opcode;
	
	public VisitInsnAdapter(int opcode) {
		this.opcode = opcode;
	}

	@Override
	public void newVisitXInsnOperator(ProgramBlockInternal block) {
		OperatorFactory.newOperator(VisitInsn.class, 
				new Class[]{ProgramBlockInternal.class, int.class}, 
				block, opcode);
		//new VisitInsn(block, opcode);
	}

}
