package org.proteinevolution.models.knime.alignment.view;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public enum ColorMap {

	TAYLOR {
		
		@Override
		protected void initialize() {
			
			this.map.put('A', new Color( 204 , 255 , 0 ));
			this.map.put('V', new Color( 153 , 255 , 0 ));
			this.map.put('I', new Color( 102 , 255 , 0 ));
			this.map.put('M', new Color( 0 , 255 , 0 ));
			this.map.put('P', new Color( 255 , 204 , 0 ));
			this.map.put('G', new Color( 255 , 153 , 0 ));
			this.map.put('C', new Color( 255 , 255 , 0 ));
			this.map.put('D', new Color( 255 , 0 , 0 ));
			this.map.put('E', new Color( 255 , 0 , 102 ));
			this.map.put('F', new Color( 0 , 255 , 102 ));
			this.map.put('W', new Color( 0 , 204 , 255 ));
			this.map.put('Y', new Color( 0 , 255 , 204 ));
			this.map.put('S', new Color( 255 , 51 , 0 ));
			this.map.put('T', new Color( 255 , 102 , 0 ));
			this.map.put('N', new Color( 204 , 0 , 255 ));
			this.map.put('Q', new Color( 255 , 0 , 204 ));
			this.map.put('K', new Color( 102 , 0 , 255 ));
			this.map.put('H', new Color( 0 , 102 , 255 ));
			this.map.put('R', new Color( 0 , 0 , 255 ));
			this.map.put('L', new Color( 51 , 255 , 0 ));
		}
	},
	
	ZENECA {
		
		@Override
		protected void initialize() {

			this.map .put('A', new Color( 255 , 255 , 255 ));
			this.map .put('C', new Color( 255 , 255 , 136 ));
			this.map .put('D', new Color( 255 , 136 , 136 ));
			this.map .put('E', new Color( 255 , 136 , 136 ));
			this.map .put('F', new Color( 255 , 136 , 255 ));
			this.map .put('G', new Color( 255 , 170 , 136 ));		
			this.map .put('H', new Color( 136 , 255 , 255 ));
			this.map .put('I', new Color( 255 , 255 , 68 ));
			this.map .put('K', new Color( 136 , 255 , 255 ));
			this.map .put('L', new Color( 255 , 90 , 255 ));
			this.map .put('M', new Color( 255 , 255 , 255 ));
			this.map .put('N', new Color( 136 , 255 , 136 ));
			this.map .put('P', new Color( 255 , 170 , 136 ));
			this.map .put('Q', new Color( 136 , 255 , 136 ));
			this.map .put('R', new Color( 136 , 255 , 255 ));
			this.map .put('S', new Color( 136 , 255 , 136 ));
			this.map .put('T', new Color( 136 , 255 , 136 ));
			this.map .put('V', new Color( 0 , 255 , 255 ));
			this.map .put('W', new Color( 255 , 136 , 255 ));
			this.map .put('Y', new Color( 255 , 136 , 255 ));
		}
	};
	
	protected final Map<Character, Color> map = new HashMap<Character, Color>();
	public static final Color def = new Color( 187 , 187 , 187 );
	
	// Initializes the color map
	protected abstract void initialize();
	
	public Color getColor(final char c) {
		
		return this.map.containsKey(c) ? this.map.get(c) : ColorMap.def;
	}
	
	private ColorMap() {
		
		this.initialize();
	}	
}
