package ie.gmit.sw.enemy;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.*;

	/*
	 * Creates a new fight object whenever the player meets an enemy
	 * The fight uses fuzzy logic using the enemies strength and player's weapon condition
	 * to evaluate the outcome of the fight
	 */

public class Fight {
    private static final String FCL_FILE_NAME = "fcl/FightEvaluator.fcl";
	
	public Fight() {
		super();
	}
	
	public int evaluateFight(Enemy e, int weaponDurability) {
        FIS fis = FIS.load(FCL_FILE_NAME,true);

        FunctionBlock functionBlock = fis.getFunctionBlock("fightEvaluator");
        fis.setVariable("enemy_strength", e.getStrength());
        fis.setVariable("weapon_durability", weaponDurability);
        
        Variable damageTaken = functionBlock.getVariable("damage_taken");
        fis.evaluate();
        Variable enemyStrength = functionBlock.getVariable("enemy_strength");
        enemyStrength.getLinguisticTerms();
        e.kill();
        int d = (int)damageTaken.getLatestDefuzzifiedValue();
		return d;
	}
}
