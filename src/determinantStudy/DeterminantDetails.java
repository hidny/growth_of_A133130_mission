package determinantStudy;

import java.util.ArrayList;

public class DeterminantDetails {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public int cycles[];
	public boolean detSignPos;
	
	public DeterminantDetails(int[] cycles, boolean detSignPos) {
		super();
		this.cycles = cycles;
		this.detSignPos = detSignPos;
	}
	
	public boolean cyclesMatches(int cycles[]) {
		boolean sameCycles = true;
		
		//Lengths shoulld match:
		if(this.cycles.length != cycles.length) {
			return false;
		}
		
		for(int i=0; i < this.cycles.length; i++) {
			if(this.cycles[i] != cycles[i]) {
				sameCycles = false;
			}
		}
		return sameCycles;
	}
	
	public boolean isValid(int cycles[] , boolean detSignPos) {
		boolean sameCycles = cyclesMatches(cycles);
		
		if( !sameCycles) {
			return true;
		}
		
		if(this.detSignPos == detSignPos) {
			return true;
		} else {
			return false;
		}
	}
	

	
	public static int getNumElements(int cycles[]) {
		int ret = 0;
		for(int i=0; i<cycles.length; i++) {
			ret += i * cycles[i];
		}
		return ret;
	}
	
	// Return -1 if invalid.
	// Return num Valid if valid.
	public static int validateAllSum(ArrayList<DeterminantDetails> details, int cycles[], boolean detSignPos) {
		
		int numValid = 0;
		
		for(int i=0; i<details.size(); i++) {
			if(details.get(i).isValid(cycles, detSignPos) == false) {
				numValid = -1;
				break;
			}
			
			if(details.get(i).cyclesMatches(cycles)) {
				numValid++;
			}
			
			if(getNumElements(details.get(i).cycles) != getNumElements(cycles)) {
				System.out.println("Error in validate all: the number of elements don't match!");
			}
		}
		
		return numValid;
	}
}
