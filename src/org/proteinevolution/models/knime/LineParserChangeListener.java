package org.proteinevolution.models.knime;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;

import org.knime.core.node.NodeLogger;
import org.proteinevolution.nodes.input.pdb.xwalk.XWalkNodeModel;


public class LineParserChangeListener extends SettingsModelStringChangeListener {

	
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(LineParserChangeListener.class);
	
	private final List<LineParser> parsers = new ArrayList<LineParser>();
	
	
	public void addParser(LineParser parser) {
	
		this.parsers.add(parser);
	}
	public void removeParser(LineParser parser) {
		
		this.parsers.remove(parser);
	}
	public void removeParser(int index) {
		
		this.parsers.remove(index);
	}
	
	
	
	
	@Override
	public void stateChanged(ChangeEvent e) {
		
		super.stateChanged(e);
		
			
		try(BufferedReader bufferedReader = new BufferedReader(new FileReader(this.lastValue))) {
		
			String line;
			
			while((line = bufferedReader.readLine()) != null) {
				
				line = line.trim();				
				for(LineParser parser : this.parsers ){
					
					parser.parse(line);
				}
			}
			// Finish all parser
			for (LineParser parser : this.parsers) {
				parser.finish();
			}
		
		} catch (FileNotFoundException ex) {
			
			
		} catch (IOException ex) {
			
		}
	
	}	
}
