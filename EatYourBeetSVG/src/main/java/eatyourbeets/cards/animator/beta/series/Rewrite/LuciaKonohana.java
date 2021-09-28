package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class LuciaKonohana extends AnimatorCard
{
    public static final EYBCardData DATA = Register(LuciaKonohana.class).SetSkill(-1, CardRarity.UNCOMMON, EYBCardTarget.Normal).SetSeriesFromClassPackage();
    public static final EYBCardTooltip CommonDebuffs = new EYBCardTooltip(DATA.Strings.EXTENDED_DESCRIPTION[1], DATA.Strings.EXTENDED_DESCRIPTION[2]);

    public LuciaKonohana()
    {
        super(DATA);

        Initialize(0, 0, 4, 2);
        SetUpgrade(0, 0, 1);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Light(1, 0, 0);

        SetExhaust(true);
    }

    @Override
    public void initializeDescription()
    {
        super.initializeDescription();

        if (cardText != null)
        {
            tooltips.add(CommonDebuffs);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int stacks = GameUtilities.UseXCostEnergy(this);

        for (int i=0; i<stacks; i++)
        {
            if (i == 0)
            {
                GameActions.Bottom.VFX(new PotionBounceEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY), 0.3f);
            }
            else
            {
                GameActions.Bottom.VFX(new PotionBounceEffect(m.hb.cX, m.hb.cY, m.hb.cX, m.hb.cY), 0.4f);
            }
            GameActions.Bottom.ApplyPoison(p, m, magicNumber);
            GameActions.Bottom.ApplyWeak(p, m, 1);
        }

        if (stacks >= secondaryValue)
        {
            if (CombatStats.TryActivateLimited(cardID))
            {
                GameActions.Bottom.ApplyPower(p, m, new LuciaKonohanaPower(m, 1));
            }
        }
    }

    public static class LuciaKonohanaPower extends AnimatorPower
    {
        public LuciaKonohanaPower(AbstractCreature owner, int amount)
        {
            super(owner, LuciaKonohana.DATA);

            this.amount = amount;
            this.type = PowerType.DEBUFF;

            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0);
        }

        @Override
        public void onDeath()
        {
            final AbstractCreature corpse = this.owner;
            int powAmount;

            for (AbstractPower debuff : corpse.powers)
            {
                for (PowerHelper commonDebuffHelper : GameUtilities.GetCommonDebuffs()) {
                    if (commonDebuffHelper.ID.equals(debuff.ID)) {
                        powAmount = GameUtilities.GetPowerAmount(corpse, debuff.ID);
                        GameActions.Bottom.ApplyPower(TargetHelper.Enemies(), commonDebuffHelper, powAmount);
                    }
                }
            }
        }
    }
}