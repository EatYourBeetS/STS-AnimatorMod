package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class BarbaraPegg extends AnimatorCard {
    public static final EYBCardData DATA = Register(BarbaraPegg.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.ALL).SetColor(CardColor.COLORLESS);
    public static final int HP_HEAL_THRESHOLD = 30;

    public BarbaraPegg() {
        super(DATA);

        Initialize(0, 0, 8, 3);
        SetUpgrade(0, 0, -2);

        SetExhaust(true);
        SetSpellcaster();
        SetSynergy(Synergies.GenshinImpact);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.VFX(new RainbowCardEffect());

        // Fully heal ALL enemies and calculate the number of stacks
        int totalHeal = 0;

        ArrayList<AbstractMonster> enemies = GameUtilities.GetEnemies(true);
        for (AbstractMonster mo : enemies)
        {
            int heal = mo.maxHealth - mo.currentHealth;
            GameActions.Bottom.Heal(p, mo, heal);
            totalHeal += heal;
        }

        int stacks = Math.floorDiv(totalHeal, magicNumber);

        // Channel an orb and increase its power for each stack
        // NOTE: An Aether orb is being used here as a placeholder until Water orbs are implemented
        AbstractOrb waterOrb = new Aether();
        GameActions.Bottom.ChannelOrb(waterOrb);

        if (stacks > 0)
        {
            waterOrb.passiveAmount += stacks;

            // Limited effect when healing at least 30 HP in total
            if (totalHeal >= HP_HEAL_THRESHOLD && CombatStats.TryActivateLimited(cardID))
            {
                GameActions.Bottom.StackPower(new RegenPower(player, secondaryValue));
            }
        }
    }
}