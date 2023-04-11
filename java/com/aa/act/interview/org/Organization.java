package com.aa.act.interview.org;

import javax.swing.text.html.Option;
import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

public abstract class Organization {

	private Position root;
	private int identifier = 0;
	
	public Organization() {
		root = createOrganization();
	}
	
	protected abstract Position createOrganization();
	
	/**
	 * hire the given person as an employee in the position that has that title
	 * 
	 * @param person
	 * @param title
	 * @return the newly filled position or empty if no position has that title
	 */
	public Optional<Position> hire(Name person, String title) {
		//your code here
		// if position is CEO
		if(root.getTitle().equals(title)){
			// if position is not filled then fill the position
			return hire(person, root);
		}

		// iterating through all positions via bfs graph
		Queue<Position> q = new ArrayDeque<>();
		q.offer(root);
		while(!q.isEmpty()){
			Position boss = q.poll();
			for(Position pos : boss.getDirectReports()){
				if(pos.getTitle().equals(title)){
					return hire(person, pos);
				}
				q.offer(pos);
			}
		}

		identifier -= 1;
		return Optional.empty();
	}

	@Override
	public String toString() {
		return printOrganization(root, "");
	}

	//region PRIVATE METHODS
	private String printOrganization(Position pos, String prefix) {
		StringBuffer sb = new StringBuffer(prefix + "+-" + pos.toString() + "\n");
		for(Position p : pos.getDirectReports()) {
			sb.append(printOrganization(p, prefix + "\t"));
		}
		return sb.toString();
	}

	private Optional<Position> hire(Name person, Position pos){
		// generating unique identifier for each employee getting hired
		identifier += 1;
		Employee emp = new Employee(identifier, person);
		if(!pos.isFilled()) {
			pos.setEmployee(Optional.of(emp));
			return Optional.of(pos);
		}

		identifier -= 1;
		return Optional.empty();
	}

	//endregion
}
