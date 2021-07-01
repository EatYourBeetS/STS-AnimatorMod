package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class BarbaraPegg extends AnimatorCard
{
    public static final EYBCardData DATA = Register(BarbaraPegg.class).SetSkill(1, CardRarity.UNCOMMON);
    public static final int HP_HEAL_THRESHOLD = 30;

    public BarbaraPegg()
    {
        super(DATA);

        Initialize(0, 0, 6, 3);
        SetUpgrade(0, 0, -1);

        SetExhaust(true);
        SetSpellcaster();
        SetSynergy(Synergies.GenshinImpact);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.VFX(new RainbowCardEffect());

        int heal = Math.min(HP_HEAL_THRESHOLD, m.maxHealth - m.currentHealth);
        GameActions.Bottom.Heal(p, m, heal);
        int stacks = Math.floorDiv(heal, magicNumber);

        // NOTE: A Dark orb is being used here as a placeholder until Water orbs are implemented
        AbstractOrb waterOrb = new Dark();
        GameActions.Bottom.ChannelOrb(waterOrb);

        if (stacks > 0)
        {
            waterOrb.passiveAmount += stacks;

            if (heal >= HP_HEAL_THRESHOLD && CombatStats.TryActivateLimited(cardID))
            {
                GameActions.Bottom.StackPower(new RegenPower(player, secondaryValue));
            }
        }
    }
}