package org.proteinevolution.externaltools.tools;

import java.io.File;
import java.io.IOException;

import org.proteinevolution.externaltools.base.CommandLine;
import org.proteinevolution.models.interfaces.Writeable;

public class PsiBlast extends ExternalToolInvocation<Writeable[], File[]>  {

	public PsiBlast(File executable) throws IOException {
		super(executable);
		// TODO Auto-generated constructor stub
	}


	
	
	
	
	
	
	
	@Override
	protected File[] getResult(CommandLine cmd) {
		
		
		
		return null;
	}

	@Override
	protected void setCmd(CommandLine cmd) throws IOException {
		
		
	}
}

