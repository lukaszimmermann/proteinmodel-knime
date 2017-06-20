package org.proteinevolution.knime.nodes.analysis.crosslinkpredictor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Element;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureTools;
import org.knime.core.node.NodeLogger;
import org.proteinevolution.models.spec.pdb.PDBAtom;
import org.proteinevolution.models.spec.pdb.Residue;
import org.proteinevolution.models.structure.AtomIdentification;
import org.proteinevolution.models.structure.UnorderedAtomPair;

/*
 *  Class which is used to calculate an orthogonal grid of a 3D structure by adding atoms successively
 *   
 *  @author: lzimmermann
 */
final class Grid implements Serializable {

	private static final long serialVersionUID = 6931511384915737370L;
	private static final NodeLogger logger = NodeLogger.getLogger(Grid.class);

	private static final int margin = 5; // No. of columns of solvent at the edge of the grid

	// Grid is blocked by at least another atom which is no donor or acceptor atom
	private static final int OCCUPIED = Integer.MAX_VALUE;

	// Solvent threshold; everything below this value will be considered as solvent (allows fast resetting of the grid after the BFS has been performed)
	private int flag_threshold = Integer.MIN_VALUE + 1;


	// End of STATIC //////////////////////////////////////////////////////////////////////////////////////////

	// the actual grid
	private int[][][] grid;

	// which residues are considered as donors and acceptors in this commit
	private final Map<Residue, Set<PDBAtom>> donors;
	private final Map<Residue, Set<PDBAtom>> acceptors;

	// List all the donors and acceptors that we have encountered during addAtom
	private final List<Atom> atoms; 
	private final List<Integer> donor_acceptor;
	private int atomIdentIndex = -1;
	private final Map<UnorderedAtomPair, Integer> sasd_distances;


	// FLAGS for the donor acceptor list
	private static final int DONOR = 0;
	private static final int ACCEPTOR = 1;
	private static final int DONOR_ACCEPTOR = 2;


	// Number of cells of the grid in each dimension (triclinic)
	private final int x_dim;
	private final int y_dim;
	private final int z_dim;

	// Lower left corner of the grid
	private final double x_min;
	private final double y_min;
	private final double z_min;

	// size of the grid
	private final int size;	


	public Grid(
			final Structure structure,
			final Map<Residue, Set<PDBAtom>> donors,
			final Map<Residue, Set<PDBAtom>> acceptors) {

		logger.warn("Grid constructor called");

		this.atoms = new ArrayList<Atom>();
		this.donor_acceptor = new ArrayList<Integer>();
		this.sasd_distances = new HashMap<UnorderedAtomPair, Integer>();

		this.donors = donors;
		this.acceptors = acceptors;


		double lower_x = Double.MAX_VALUE;
		double lower_y = Double.MAX_VALUE;
		double lower_z = Double.MAX_VALUE;

		double upper_x = Double.MIN_VALUE;
		double upper_y = Double.MIN_VALUE;
		double upper_z = Double.MIN_VALUE;

		// The atoms of the structure that we are interested in for the grid
		Atom[] gridAtoms = StructureTools.getAllNonHAtomArray(structure, false);

		logger.warn("Searching grid boundaries");
		for (Atom atom : gridAtoms) {

			// Atom coordinates
			double x = atom.getX();
			double y = atom.getY();
			double z = atom.getZ();

			lower_x = x < lower_x ? x : lower_x;
			lower_y = y < lower_y ? y : lower_y;
			lower_z = z < lower_z ? z : lower_z;

			upper_x = x > upper_x ? x : upper_x;
			upper_y = y > upper_y ? y : upper_y;
			upper_z = z > upper_z ? z : upper_z;   
		}
		logger.warn("Finish grid boundaries");
		// Set grid dimensions
		this.x_dim = (int) (Math.ceil(upper_x - lower_x) + 2 * margin);
		this.y_dim = (int) (Math.ceil(upper_y - lower_y) + 2 * margin);
		this.z_dim = (int) (Math.ceil(upper_z - lower_z) + 2 * margin);

		// Set corner of grid
		this.x_min = lower_x;
		this.y_min = lower_y;
		this.z_min = lower_z;

		// Size of Grid
		this.size = this.x_dim * this.y_dim * this.z_dim;

		// Initialize Grid
		logger.warn("INIT Grid");
		this.grid = new int[this.x_dim][this.y_dim][this.z_dim];		
		for (int i = 0; i < this.x_dim  ; ++i) {

			for (int j = 0; j < this.y_dim; ++j) {

				// j == 0 or j == this_ydim-1
				for (int k = 0; k < this.z_dim; ++k) {

					this.grid[i][j][k] =
							i == 0  || i == this.x_dim - 1
							|| j == 0  || j == this.y_dim - 1 
							|| k == 0  || k == this.z_dim - 1 ? Grid.OCCUPIED : Integer.MIN_VALUE; 
				}
			}
		}

		logger.warn("Start adding atoms to grid");
		for (Atom atom : gridAtoms) {

			this.addAtom(atom);
		}
		logger.warn("Grid has been built");
	}

	private int translateX(final double value) {

		return (int) Math.floor(value - this.x_min) + margin;
	}

	private int translateY(final double value) {

		return (int) Math.floor(value - this.y_min) + margin;
	}

	private int translateZ(final double value) {

		return (int) Math.floor(value - this.z_min) + margin;
	}

	/**
	 * Returns the donor residues of this grid.
	 * Makes a copy, so modifying the returned map will not change the grid
	 * 
	 * @return Copy of the donors
	 */
	public Map<Residue, Set<PDBAtom>> copyDonors() {

		return new HashMap<Residue, Set<PDBAtom>>(this.donors);
	}

	/**
	 * Returns the acceptor residues of this grid.
	 * Makes a copy, so modifying the returned map will not change the grid
	 * 
	 * @return Copy of the acceptors
	 */
	public Map<Residue, Set<PDBAtom>> copyAcceptors() {

		return new HashMap<Residue, Set<PDBAtom>>(this.acceptors);
	}

	/**
	 * Determines whether the residue_atom is acceptor in this grid
	 * 
	 * @param residue Which residue the atom belongs to
	 * @param atom Atom name
	 * @return true if the atom_name is acceptor, false otherwise
	 */
	public boolean isAcceptor(Residue residue, PDBAtom atom) {

		return this.acceptors.containsKey(residue) && this.acceptors.get(residue).contains(atom);
	}

	/**
	 * Determines whether the residue_atom is donor in this grid
	 * 
	 * @param residue Which residue the atom belongs to
	 * @param atom Atom name
	 * @return true if the atom_name is donor, false otherwise
	 */
	public boolean isDonor(Residue residue, PDBAtom atom) {

		return this.donors.containsKey(residue) && this.donors.get(residue).contains(atom);
	}

	public void performBFS() {

		// The BFS is repeated for all localAtoms stored in the grid, we go from right to left
		for (int sourceIndex = this.atomIdentIndex; sourceIndex > -1; --sourceIndex) {

			Atom atom = this.atoms.get(sourceIndex);
			List<int[]> candidates = this.queryAtom(atom.getX(), atom.getY(), atom.getZ(), atom.getElement());



			// Grid search for each candidate
			for(int[] start : candidates) {


				// Used for performing the BFS
				Queue<Integer> x_queue = new LinkedList<Integer>();
				Queue<Integer> y_queue = new LinkedList<Integer>();
				Queue<Integer> z_queue = new LinkedList<Integer>();
				Queue<Integer> length = new LinkedList<Integer>();

				// Set initial current value of the grid search
				int current_x = start[0];
				int current_y = start[1];
				int current_z = start[2];
				int current_length = 0;

				x_queue.add(current_x);
				y_queue.add(current_y);
				z_queue.add(current_z);
				length.add(current_length);

				Set<Integer> lookingFor = new HashSet<Integer>(); 
				lookingFor.add(Grid.DONOR_ACCEPTOR);
				switch (this.donor_acceptor.get(sourceIndex)) {

				case Grid.DONOR_ACCEPTOR:

					lookingFor.add(Grid.DONOR);
					lookingFor.add(Grid.ACCEPTOR);
					break;

				case Grid.DONOR:

					lookingFor.add(Grid.ACCEPTOR);
					break;

				case Grid.ACCEPTOR:

					lookingFor.add(Grid.DONOR);
					break;
				}

				// Indices of donors that we have already found 
				// Because the distance is symmetrical, we already found all atoms above this index
				Set<Integer> found = new HashSet<Integer>();		
				for (int i = sourceIndex; i < this.atomIdentIndex + 1; ++i) {

					found.add(i);
				}

				int current_dir;

				do {
					// Get next grid point and the length
					current_x = x_queue.poll();
					current_y = y_queue.poll();
					current_z = z_queue.poll();
					current_length = length.poll();

					// Direction 1
					current_dir = this.grid[current_x-1][current_y][current_z];		
					if (current_dir < this.flag_threshold) {

						x_queue.add(current_x-1);
						y_queue.add(current_y);
						z_queue.add(current_z);
						this.grid[current_x-1][current_y][current_z] = this.flag_threshold;
						length.add(current_length+1);

						// We have the following case:
						// * Not solvent (current_dir > -1)
						// * Grid not occupied (current_dir != OCCUPIED)
						// * Associated atom has not already been found (! found.cintains(current_dir)
						// * we are looking for that kind of atom
					} else if(    current_dir > -1
							&& current_dir < sourceIndex
							&& ! found.contains(current_dir)
							&&  lookingFor.contains(this.donor_acceptor.get(current_dir))) {

						// Assemble a distance pair for this finding
						this.sasd_distances.put(
								new UnorderedAtomPair(
										new AtomIdentification(atom),
										new AtomIdentification(atoms.get(current_dir))), current_length);
						found.add(current_dir);
					}

					// Direction 2
					current_dir = this.grid[current_x+1][current_y][current_z];						
					if (current_dir < this.flag_threshold) {

						x_queue.add(current_x+1);
						y_queue.add(current_y);
						z_queue.add(current_z);
						this.grid[current_x+1][current_y][current_z] = this.flag_threshold;
						length.add(current_length+1);

					} else if(    current_dir > -1
							&& current_dir < sourceIndex
							&& ! found.contains(current_dir)
							&&  lookingFor.contains(this.donor_acceptor.get(current_dir))) {

						// Assemble a distance pair for this finding
						this.sasd_distances.put(
								new UnorderedAtomPair(
										new AtomIdentification(atom),
										new AtomIdentification(atoms.get(current_dir))), current_length);
						found.add(current_dir);
					} 

					// Direction 3
					current_dir = this.grid[current_x][current_y-1][current_z];						
					if (current_dir < this.flag_threshold) {

						x_queue.add(current_x);
						y_queue.add(current_y-1);
						z_queue.add(current_z);
						this.grid[current_x][current_y-1][current_z] = this.flag_threshold;
						length.add(current_length+1);

						// We are looking for that entry but have not found ourselved (The interesting case)
					} else if(    current_dir > -1
							&& current_dir < sourceIndex
							&& ! found.contains(current_dir)
							&&  lookingFor.contains(this.donor_acceptor.get(current_dir))) {


						// Assemble a distance pair for this finding
						this.sasd_distances.put(
								new UnorderedAtomPair(
										new AtomIdentification(atom),
										new AtomIdentification(atoms.get(current_dir))), current_length);
						found.add(current_dir);

					} 

					// Direction 4
					current_dir = this.grid[current_x][current_y+1][current_z];						
					if (current_dir < this.flag_threshold) {

						x_queue.add(current_x);
						y_queue.add(current_y+1);
						z_queue.add(current_z);
						this.grid[current_x][current_y+1][current_z] = this.flag_threshold;
						length.add(current_length+1);

						// We are looking for that entry but have not found ourselved (The interesting case)
					} else if(    current_dir > -1
							&& current_dir < sourceIndex
							&& ! found.contains(current_dir)
							&&  lookingFor.contains(this.donor_acceptor.get(current_dir))) {

						// Assemble a distance pair for this finding
						this.sasd_distances.put(
								new UnorderedAtomPair(
										new AtomIdentification(atom),
										new AtomIdentification(atoms.get(current_dir))), current_length);
						found.add(current_dir);

					} 


					// Direction 5
					current_dir = this.grid[current_x][current_y][current_z-1];						
					if (current_dir < this.flag_threshold) {

						x_queue.add(current_x);
						y_queue.add(current_y);
						z_queue.add(current_z-1);
						this.grid[current_x][current_y][current_z-1] = this.flag_threshold;
						length.add(current_length+1);

						// We are looking for that entry but have not found ourselved (The interesting case)
					} else if(    current_dir > -1
							&& current_dir < sourceIndex
							&& ! found.contains(current_dir)
							&&  lookingFor.contains(this.donor_acceptor.get(current_dir))) {

						// Assemble a distance pair for this finding
						this.sasd_distances.put(
								new UnorderedAtomPair(
										new AtomIdentification(atom),
										new AtomIdentification(atoms.get(current_dir))), current_length);
						found.add(current_dir);
					}

					// Direction 6
					current_dir = this.grid[current_x][current_y][current_z+1];						
					if (current_dir < this.flag_threshold) {

						x_queue.add(current_x);
						y_queue.add(current_y);
						z_queue.add(current_z+1);
						this.grid[current_x][current_y][current_z+1] = this.flag_threshold;
						length.add(current_length+1);

						// We are looking for that entry but have not found ourselved (The interesting case)
					} else if(    current_dir > -1
							&& current_dir < sourceIndex
							&& ! found.contains(current_dir)
							&&  lookingFor.contains(this.donor_acceptor.get(current_dir))) {

						// Assemble a distance pair for this finding
						this.sasd_distances.put(
								new UnorderedAtomPair(
										new AtomIdentification(atom),
										new AtomIdentification(atoms.get(current_dir))), current_length);
						found.add(current_dir);
					}


					// Break if the queue is empty or we already found all possible donor, acceptors
				} while( ! x_queue.isEmpty() && found.size() != this.donor_acceptor.size() && current_length < 60);

				// BFS has finished. 
				// Increase flag threshold
				this.flag_threshold++;
				if(this.flag_threshold == 0) {

					throw new RuntimeException("Grid is Exhausted. Cannot continue");
				}
			}	
		}
	}


	public Map<UnorderedAtomPair, Integer> copyDistances() {

		return new HashMap<UnorderedAtomPair, Integer>(this.sasd_distances);
	}

	public int getXDim() {

		return this.x_dim;
	}

	public int getYDim() {

		return this.y_dim;
	}

	public int getZDim() {

		return this.z_dim;
	}

	public int getSize() {

		return this.size;
	}

	/**
	 * Occupies (set cell value to OCCUPIED) all cells in the grid that lie within 
	 * the VDW radius of an encountered atom
	 * @param x x coordinate of the atom
	 * @param y y coordinate of the atom
	 * @param z z coordinate of the atom
	 * @param atom 
	 * @return Number of cells were occupied by this call
	 */
	private void occupyVDW(final double x, final double y, final double z, Element element) {

		// Space to block is determined by the VDW radius of the atom
		double radius = element.getVDWRadius();
		double radius2 = radius*radius;

		for (double x_iter = 0; x_iter < radius; x_iter++) {

			int coord_x_1 = this.translateX(x + x_iter);
			int coord_x_2 = this.translateX(x - x_iter);

			double x_radius = Math.sqrt(radius2 - x_iter*x_iter);
			double x_radius2 = x_radius*x_radius;

			for (double y_iter = 0; y_iter < x_radius; y_iter++) {

				int coord_y_1 = this.translateY(y + y_iter);
				int coord_y_2 = this.translateY(y - y_iter);

				double y_radius = Math.sqrt(x_radius2 - y_iter*y_iter);

				for (double z_iter = 0; z_iter < y_radius; z_iter++) {

					int coord_z_1 = this.translateZ(z + z_iter);
					int coord_z_2 = this.translateZ(z - z_iter);

					this.grid[coord_x_1][coord_y_1][coord_z_1] = Grid.OCCUPIED;
					this.grid[coord_x_1][coord_y_1][coord_z_2] = Grid.OCCUPIED;
					this.grid[coord_x_1][coord_y_2][coord_z_1] = Grid.OCCUPIED;
					this.grid[coord_x_1][coord_y_2][coord_z_2] = Grid.OCCUPIED;
					this.grid[coord_x_2][coord_y_1][coord_z_1] = Grid.OCCUPIED;
					this.grid[coord_x_2][coord_y_1][coord_z_2] = Grid.OCCUPIED;
					this.grid[coord_x_2][coord_y_2][coord_z_1] = Grid.OCCUPIED;
					this.grid[coord_x_2][coord_y_2][coord_z_2] = Grid.OCCUPIED;
				}
				// Update cells for z_iter = y_radius
				int coord_z_1 = this.translateZ(z + y_radius);
				int coord_z_2 = this.translateZ(z - y_radius);

				this.grid[coord_x_1][coord_y_1][coord_z_1] = Grid.OCCUPIED;
				this.grid[coord_x_1][coord_y_1][coord_z_2] = Grid.OCCUPIED;
				this.grid[coord_x_1][coord_y_2][coord_z_1] = Grid.OCCUPIED;
				this.grid[coord_x_1][coord_y_2][coord_z_2] = Grid.OCCUPIED;
				this.grid[coord_x_2][coord_y_1][coord_z_1] = Grid.OCCUPIED;
				this.grid[coord_x_2][coord_y_1][coord_z_2] = Grid.OCCUPIED;
				this.grid[coord_x_2][coord_y_2][coord_z_1] = Grid.OCCUPIED;
				this.grid[coord_x_2][coord_y_2][coord_z_2] = Grid.OCCUPIED;
			}

			// Update for y_iter = x_radius
			// Then z_iter = 0
			int coord_y_1 = this.translateY(y + x_radius);
			int coord_y_2 = this.translateY(y - x_radius);
			int coord_z = this.translateZ(z);

			this.grid[coord_x_1][coord_y_1][coord_z] = Grid.OCCUPIED;
			this.grid[coord_x_1][coord_y_2][coord_z] = Grid.OCCUPIED;
			this.grid[coord_x_2][coord_y_1][coord_z] = Grid.OCCUPIED;
			this.grid[coord_x_2][coord_y_2][coord_z] = Grid.OCCUPIED;
		}

		// Update for x_iter = radius
		// So y_iter = 0
		// So z_iter = 0
		int coord_y = this.translateY(y);
		int coord_z = this.translateZ(z);

		this.grid[this.translateX(x + radius)][coord_y][coord_z] = Grid.OCCUPIED;
		this.grid[this.translateX(x - radius)][coord_y][coord_z] = Grid.OCCUPIED;
	}


	/**
	 * Adds an atom to the grid 
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param residue
	 * @param atom
	 */
	public void addAtom(final Atom atom) {

		AtomIdentification atomIdentification = new AtomIdentification(atom);
		double x = atom.getX();
		double y = atom.getY();
		double z = atom.getZ();		
		PDBAtom pdbatom = atomIdentification.getAtom();

		// Ignore hydrogen
		if (pdbatom.element.equals(Element.H)) {
			return;
		}

		Residue residue = atomIdentification.getResidue();

		boolean isDonor = this.donors.containsKey(residue) && this.donors.get(residue).contains(pdbatom);
		boolean isAcceptor = this.acceptors.containsKey(residue) && this.acceptors.get(residue).contains(pdbatom);

		// occupy the vdW volume of the atom (Might be inlined)
		this.occupyVDW(x, y, z, pdbatom.element);

		if (isDonor || isAcceptor) {

			if (this.atoms.contains(atom)) {

				throw new IllegalArgumentException("Atom has already been inserted.");
			}

			this.donor_acceptor.add(isDonor && isAcceptor ? Grid.DONOR_ACCEPTOR : (isDonor ? Grid.DONOR : Grid.ACCEPTOR));
			this.atoms.add(atom);
			this.atomIdentIndex++;
			// Could also be inlined
			this.makeAccessible(x, y, z, atomIdentification.getAtom().element.getVDWRadius());
		}
	}


	private void setIfNotOccupied(int x_index, int y_index, int z_index) {			
		if (this.grid[x_index][y_index][z_index] != OCCUPIED) {
			this.grid[x_index][y_index][z_index] = this.atomIdentIndex;
		}
	}


	private void makeAccessible(double x, double y, double z, double vdwRadius) {

		double radius = vdwRadius + 1;
		double radius2 = radius*radius;

		for (double x_iter = 0; x_iter < radius; x_iter++) {

			int coord_x_1 = this.translateX(x + x_iter);
			int coord_x_2 = this.translateX(x - x_iter);

			double x_radius = Math.sqrt(radius2 - x_iter*x_iter);
			double x_radius2 = x_radius*x_radius;

			for (double y_iter = 0; y_iter < x_radius; y_iter++) {

				int coord_y_1 = this.translateY(y + y_iter);
				int coord_y_2 = this.translateY(y - y_iter);

				double y_radius = Math.sqrt(x_radius2 - y_iter*y_iter);

				// Update cells or z_iter = y_radius
				int coord_z_1 = this.translateZ(z + y_radius);
				int coord_z_2 = this.translateZ(z - y_radius);

				this.setIfNotOccupied(coord_x_1, coord_y_1, coord_z_1);
				this.setIfNotOccupied(coord_x_1, coord_y_1, coord_z_2);
				this.setIfNotOccupied(coord_x_1, coord_y_2, coord_z_1);
				this.setIfNotOccupied(coord_x_1, coord_y_2, coord_z_2);
				this.setIfNotOccupied(coord_x_2, coord_y_1, coord_z_1);
				this.setIfNotOccupied(coord_x_2, coord_y_1, coord_z_2);
				this.setIfNotOccupied(coord_x_2, coord_y_2, coord_z_1);
				this.setIfNotOccupied(coord_x_2, coord_y_2, coord_z_2);
			}

			// Update for y_iter = x_radius
			// Then z_iter = 0
			int coord_y_1 = this.translateY(y + x_radius);
			int coord_y_2 = this.translateY(y - x_radius);
			int coord_z = this.translateZ(z);

			this.setIfNotOccupied(coord_x_1, coord_y_1, coord_z);
			this.setIfNotOccupied(coord_x_1, coord_y_2, coord_z);
			this.setIfNotOccupied(coord_x_2, coord_y_1, coord_z);
			this.setIfNotOccupied(coord_x_2, coord_y_2, coord_z);
		}

		// Update for x_iter = radius
		// So y_iter = 0
		// So z_iter = 0
		int coord_y = this.translateY(y);
		int coord_z = this.translateZ(z);

		this.setIfNotOccupied(this.translateX(x + radius), coord_y, coord_z);
		this.setIfNotOccupied(this.translateX(x - radius), coord_y, coord_z);
	}

	private List<int[]> queryAtom(
			final double x,
			final double y,
			final double z,
			final Element element) {

		List<int[]> result = new ArrayList<int[]>();

		double radius = element.getVDWRadius() + 1;
		double radius2 = radius*radius;

		for (double x_iter = 0; x_iter < radius; x_iter++) {

			int coord_x_1 = this.translateX(x + x_iter);
			int coord_x_2 = this.translateX(x - x_iter);

			double x_radius = Math.sqrt(radius2 - x_iter*x_iter);
			double x_radius2 = x_radius*x_radius; 

			for (double y_iter = 0; y_iter < x_radius; y_iter++) {

				int coord_y_1 = this.translateY(y + y_iter);
				int coord_y_2 = this.translateY(y - y_iter);

				double y_radius = Math.sqrt(x_radius2 - y_iter*y_iter);

				// Update cells or z_iter = y_radius
				int coord_z_1 = this.translateZ(z + y_radius);
				int coord_z_2 = this.translateZ(z - y_radius);


				if (this.grid[coord_x_1][coord_y_1][coord_z_1] != Grid.OCCUPIED) {	
					result.add(new int[] {coord_x_1, coord_y_1, coord_z_1});
				}
				if (this.grid[coord_x_1][coord_y_1][coord_z_2] != Grid.OCCUPIED) {	
					result.add(new int[]  {coord_x_1, coord_y_1, coord_z_2 });
				}
				if (this.grid[coord_x_1][coord_y_2][coord_z_1] != Grid.OCCUPIED) {	
					result.add(new int[] {coord_x_1, coord_y_2, coord_z_1});
				}
				if (this.grid[coord_x_1][coord_y_2][coord_z_2] != Grid.OCCUPIED) {	
					result.add(new int[] {coord_x_1, coord_y_2, coord_z_2 });
				}
				if (this.grid[coord_x_2][coord_y_1][coord_z_1] != Grid.OCCUPIED) {	
					result.add(new int[] {coord_x_2, coord_y_1, coord_z_1});
				}
				if (this.grid[coord_x_2][coord_y_1][coord_z_2] != Grid.OCCUPIED) {	
					result.add(new int[] {coord_x_2, coord_y_1, coord_z_2});
				}
				if (this.grid[coord_x_2][coord_y_2][coord_z_1] != Grid.OCCUPIED) {	
					result.add(new int[] {coord_x_2, coord_y_2, coord_z_1}) ;
				}
				if (this.grid[coord_x_2][coord_y_2][coord_z_2] != Grid.OCCUPIED) {	
					result.add(new int[] {coord_x_2, coord_y_2, coord_z_2});
				}
			}

			// Update for y_iter = x_radius
			// Then z_iter = 0
			int coord_y_1 = this.translateY(y + x_radius);
			int coord_y_2 = this.translateY(y - x_radius);
			int coord_z = this.translateZ(z);

			if (this.grid[coord_x_1][coord_y_1][coord_z] != Grid.OCCUPIED) {	
				result.add(new int[] {coord_x_1, coord_y_1, coord_z});
			}
			if (this.grid[coord_x_1][coord_y_2][coord_z] != Grid.OCCUPIED) {	
				result.add(new int[] {coord_x_1, coord_y_2, coord_z});
			}
			if (this.grid[coord_x_2][coord_y_1][coord_z] != Grid.OCCUPIED) {	
				result.add(new int[] {coord_x_2, coord_y_1, coord_z});
			}
			if (this.grid[coord_x_2][coord_y_2][coord_z] != Grid.OCCUPIED) {	
				result.add(new int[] {coord_x_2, coord_y_2, coord_z});
			}
		}

		// Update for x_iter = radius
		// So y_iter = 0
		// So z_iter = 0

		int coord_x_1 = this.translateX(x + radius);
		int coord_x_2 = this.translateX(x - radius);
		int coord_y = this.translateY(y);
		int coord_z = this.translateZ(z);

		if (this.grid[coord_x_1][coord_y][coord_z] != Grid.OCCUPIED) {	
			result.add(new int[] {coord_x_1, coord_y, coord_z});
		}
		if (this.grid[coord_x_2][coord_y][coord_z] != Grid.OCCUPIED) {	
			result.add(new int[] {coord_x_2, coord_y, coord_z});
		}
		return result;
	}


	/**
	 * Returns the percentage of the surface shell that is accessible of the atom
	 * @param x
	 * @param y
	 * @param z
	 * @param residue
	 * @param atom
	 * @param grid_values
	 * @return
	 */
	public double queryAtomAccessibility(
			final double x,
			final double y,
			final double z,
			final Residue residue,
			final Element element) {

		double radius = element.getVDWRadius() + 1;
		double radius2 = radius*radius;

		double numerator = 0;
		double denominator = 0;

		for (double x_iter = 0; x_iter < radius; x_iter++) {

			int coord_x_1 = this.translateX(x + x_iter);
			int coord_x_2 = this.translateX(x - x_iter);

			double x_radius = Math.sqrt(radius2 - x_iter*x_iter);
			double x_radius2 = x_radius*x_radius;

			for (double y_iter = 0; y_iter < x_radius; y_iter++) {

				int coord_y_1 = this.translateY(y + y_iter);
				int coord_y_2 = this.translateY(y - y_iter);

				double y_radius = Math.sqrt(x_radius2 - y_iter*y_iter);

				// Update cells or z_iter = y_radius
				int coord_z_1 = this.translateZ(z + y_radius);
				int coord_z_2 = this.translateZ(z - y_radius);

				if (this.grid[coord_x_1][coord_y_1][coord_z_1] != Grid.OCCUPIED) {	
					numerator++;
				}
				if (this.grid[coord_x_1][coord_y_1][coord_z_2] != Grid.OCCUPIED) {	
					numerator++;
				}
				if (this.grid[coord_x_1][coord_y_2][coord_z_1] != Grid.OCCUPIED) {	
					numerator++;
				}
				if (this.grid[coord_x_1][coord_y_2][coord_z_2] != Grid.OCCUPIED) {	
					numerator++;
				}
				if (this.grid[coord_x_2][coord_y_1][coord_z_1] != Grid.OCCUPIED) {	
					numerator++;
				}
				if (this.grid[coord_x_2][coord_y_1][coord_z_2] != Grid.OCCUPIED) {	
					numerator++;
				}
				if (this.grid[coord_x_2][coord_y_2][coord_z_1] != Grid.OCCUPIED) {	
					numerator++;
				}
				if (this.grid[coord_x_2][coord_y_2][coord_z_2] != Grid.OCCUPIED) {	
					numerator++;
				}
				denominator += 8;
			}

			// Update for y_iter = x_radius
			// Then z_iter = 0
			int coord_y_1 = this.translateY(y + x_radius);
			int coord_y_2 = this.translateY(y - x_radius);
			int coord_z = this.translateZ(z);

			if (this.grid[coord_x_1][coord_y_1][coord_z] != Grid.OCCUPIED) {	
				numerator++;
			}
			if (this.grid[coord_x_1][coord_y_2][coord_z] != Grid.OCCUPIED) {	
				numerator++;
			}
			if (this.grid[coord_x_2][coord_y_1][coord_z] != Grid.OCCUPIED) {	
				numerator++;
			}
			if (this.grid[coord_x_2][coord_y_2][coord_z] != Grid.OCCUPIED) {	
				numerator++;
			}
			denominator += 4;
		}

		// Update for x_iter = radius
		// So y_iter = 0
		// So z_iter = 0
		int coord_y = this.translateY(y);
		int coord_z = this.translateZ(z);

		if (this.grid[this.translateX(x + radius)][coord_y][coord_z] != Grid.OCCUPIED) {	
			numerator++;
		}
		if (this.grid[this.translateX(x - radius)][coord_y][coord_z] != Grid.OCCUPIED) {	
			numerator++;
		}
		denominator += 2;

		return ((double) numerator) / ((double) denominator);
	}
}
